/*****************************************************************************
 * TypePropertyRepresentation.java
 *****************************************************************************
 * Copyright © 2017 uPMT
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston MA 02110-1301, USA.
 *****************************************************************************/

package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

import application.Main;
import controller.command.ChangeExtractMomentCommand;
import controller.command.ChangePropertyValueCommand;
import controller.controller.ChangePropertyValueController;
import controller.controller.Observable;
import controller.controller.Observer;
import controller.controller.PropertyExtractController;
import controller.controller.RenamePropertyController;
import controller.controller.TypeController;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Record;
import model.Descripteme;
import model.Property;
import utils.ResourceLoader;
import utils.UndoCollector;

public class TypePropertyRepresentation extends HBox implements Initializable, Observer{
	
	private @FXML Label propertyValue;
	private @FXML Label propertyName;
	private @FXML Button hasExtractImageProperties;
	private @FXML HBox propertyPane;
	private Main main;
	private Property property;
	private TreeItem<TypeController> propertyTypeTreeItem;
	
	private MomentExpVBox momentBox;
	
	private TypeController propertyController;
	
	private Tooltip extractTooltip;
	
	private PropertyExtractController propertyExtractController;
	
	public TypePropertyRepresentation(Property t, MomentExpVBox m,  TreeItem<TypeController> propertyTypeTreeItem, Main main) {
		momentBox = m;
		this.property = t;
		this.propertyTypeTreeItem = propertyTypeTreeItem;
		this.main = main;
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/TypePropertyRepresentation.fxml"));
        fxmlLoader.setController(this);
        try {
        	fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        this.propertyExtractController = new PropertyExtractController(property);
        propertyExtractController.addObserver(this);
        propertyExtractController.addObserver(main.getMainViewController());
        
        extractTooltip = new Tooltip();
        extractTooltip.setWrapText(true);
		extractTooltip.setMaxWidth(500);
        hasExtractImageProperties.setOnMouseEntered(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent event) {
		    	hasExtractImageProperties.setCursor(Cursor.HAND);
		    	javafx.geometry.Point2D p = hasExtractImageProperties.localToScreen(hasExtractImageProperties.getLayoutBounds().getMaxX(), hasExtractImageProperties.getLayoutBounds().getMaxY()); 
		    	extractTooltip.setOpacity(1);
		    	extractTooltip.show(hasExtractImageProperties, p.getX(), p.getY());
	    	
		    }
		});
        
        
        hasExtractImageProperties.setOnMouseExited(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent event) {
		    	hasExtractImageProperties.setCursor(Cursor.DEFAULT);
		    	extractTooltip.setOpacity(0);
		    	extractTooltip.hide();
		    }
		});
    	this.hideExtractIcon();
    	
    	hasExtractImageProperties.setOnDragOver(new EventHandler<DragEvent>() {
        	public void handle(DragEvent event) {
        		if(event.getDragboard().getString().equals("dragDescripteme")) {
	        		dragExtractIcon();
	        		event.acceptTransferModes(TransferMode.ANY);
        		}
        		event.consume();
        	}
        });
    	hasExtractImageProperties.setOnDragExited(new EventHandler<DragEvent>() {
        	public void handle(DragEvent event) {
        		if(property.getDescriptemes().isEmpty())
        			hideExtractIcon();
        		else
        			showExtractIcon(property.getDescriptemes());
        		event.consume();
        	}
        });
    	hasExtractImageProperties.setOnDragDropped(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				LinkedList<Descripteme> newDescriptemes = new LinkedList<Descripteme>();
	        	for(Descripteme d : property.getDescriptemes()) {
	        		newDescriptemes.add(new Descripteme(d.getTexte()));
	    		}
	        	newDescriptemes.add(new Descripteme((String)event.getDragboard().getContent(DataFormat.HTML)));
				ChangeExtractMomentCommand cmd = new ChangeExtractMomentCommand(
						propertyExtractController,
						property.getDescriptemes(),
						newDescriptemes,
						main
	        			);
				cmd.execute();
				UndoCollector.INSTANCE.add(cmd);
				event.consume();
			}
        });
        
        LinkToTreeProperty();
        if(this.propertiesHaveDescriptem()) 
        	this.showExtractIcon(property.getDescriptemes());
        else this.hideExtractIcon();
        
	}
	
	private boolean propertiesHaveDescriptem() {
		return !property.getDescriptemes().isEmpty();
	}

	@FXML
	private void pickPropertyExtract() {
		Stage promptWindow = new Stage(StageStyle.UTILITY);
		promptWindow.setTitle(main._langBundle.getString("select_extract"));
		//promptWindow.setAlwaysOnTop(true);
		promptWindow.initModality(Modality.APPLICATION_MODAL);
		try {
			main.setCurrentMoment(momentBox);
			momentBox.setCurrentProperty(property);

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/DescriptemeViewer.fxml"));
			loader.setController(new DescriptemeViewerController(main, promptWindow, this.property, propertyExtractController));
			loader.setResources(main._langBundle);
			BorderPane layout = (BorderPane) loader.load();
			Scene launchingScene = new Scene(layout);
			promptWindow.setScene(launchingScene);
			promptWindow.show();

		} catch (IOException e) {
			// TODO Exit Program
			e.printStackTrace();
		}
	}
	
	private void LinkToTreeProperty() {
		for(TreeItem<TypeController> t : propertyTypeTreeItem.getChildren()) {
			if(t.getValue().getType().getName().equals(property.getName())) {
				this.propertyController = t.getValue();
				t.getValue().getRenamePropertyController().addObserver(this);
				t.getValue().getChangePropertyValueController().addObserver(this);
				t.getValue().getChangePropertyValueController().updateModel(property);
				break;
			}
		}		
	}

	public Property getProperty(){
		return this.property;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.propertyName.setText(property.getName());
		if(this.property.getValue() != null){
			this.propertyValue.setText(property.getValue());
		}else{
			this.propertyValue.setText("____");
		}
		setLabelChangeName(propertyPane,this);
	}
	
	public void setPropertyName(String pn){
		this.propertyName.setText(pn);
	}
	
	public void setValue(String value) {
		propertyValue.setText(value);
		property.setValue(value);
	}
	
	private void setLabelChangeName(HBox propertyPane2, TypePropertyRepresentation tpr){
		
		
		propertyValue.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				
				if(arg0.getClickCount() == 2){
					TextField t = new TextField(propertyValue.getText());
					t.setMaxWidth(70);
					t.setMinWidth(10);
					
					ChangeListener<Boolean> listener = new ChangeListener<Boolean>() {
						 @Override
						    public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
						    {
						        if (!newPropertyValue)
						        {
						        	ChangePropertyValueCommand cmd = new ChangePropertyValueCommand(
						        			tpr,
						        			property.getValue(), 
						        			t.getText(),
						        			main);
						        	cmd.execute();
						        	UndoCollector.INSTANCE.add(cmd);
						        	propertyPane2.getChildren().remove(2);
						        	propertyPane2.getChildren().add(propertyValue);
									//propertyPane2.setRight(propertyValue);
						        }
						    }
					};
					t.setOnKeyPressed(new EventHandler<KeyEvent>() {

						@Override
						public void handle(KeyEvent event) {
							if(event.getCode() == KeyCode.ENTER){
								t.setText(t.getText());
								propertyPane2.getChildren().remove(2);
					        	propertyPane2.getChildren().add(propertyValue);
								//propertyPane2.setRight(propertyValue);
							}
							if(event.getCode() == KeyCode.ESCAPE){
								propertyPane2.getChildren().remove(2);
					        	propertyPane2.getChildren().add(propertyValue);
					        	//propertyPane2.setRight(propertyValue);
							}
						}
					});
					t.focusedProperty().addListener(listener);
					Platform.runLater(()->t.requestFocus());
					Platform.runLater(()->t.selectAll());
					propertyPane2.getChildren().remove(2);
		        	propertyPane2.getChildren().add(t);
					//propertyPane2.setRight(t);
				}
			}
		});
	}

	@Override
	public void updateVue(Observable obs, Object value) {
		if(obs.getClass().equals(RenamePropertyController.class)) {
			setPropertyName((String) value);
			property.setName((String) value);
		}
		if(obs.getClass().equals(ChangePropertyValueController.class)) {
			ChangePropertyValueController controller = (ChangePropertyValueController) obs;
			// Both Property representations are based on same Object ( inspector based on the momentItself )
			// if identical --> update 
			if(controller.getProperty().hashCode() == this.property.hashCode()) {
				setValue((String) value);
			}
		}
		if(obs.getClass().equals(PropertyExtractController.class)) {
			if(value != null) {
				//System.out.println("Have Value :"+(String) value);
				if(!((LinkedList<Descripteme>) value).isEmpty())
					this.showExtractIcon((LinkedList<Descripteme>) value);
				else  this.hideExtractIcon();

			}else {
			//System.out.println("Haven't Value");
				this.hideExtractIcon();
			}
		}
	}
	
	public void showExtractIcon(LinkedList<Descripteme> tooltips){
		this.hasExtractImageProperties.getStyleClass().clear();
		this.hasExtractImageProperties.getStyleClass().add("button");
		this.hasExtractImageProperties.getStyleClass().add("buttonMomentView");
		
		String tooltip = "";
		for(int i=0; i<tooltips.size();i++) {
			tooltip+="Descripteme "+(i+1)+": "+tooltips.get(i).getTexte();
			if(i!=tooltips.size()-1) tooltip+="\n";
		}
		
		extractTooltip.setText(tooltip);
		extractTooltip.setOpacity(0);
    	extractTooltip.hide();
	}
	
	public void hideExtractIcon(){
		this.hasExtractImageProperties.getStyleClass().clear();
		this.hasExtractImageProperties.getStyleClass().add("button");
		this.hasExtractImageProperties.getStyleClass().add("buttonMomentViewDisabled");
		
		extractTooltip.setText(main._langBundle.getString("no_descripteme"));
		extractTooltip.setOpacity(0);
    	extractTooltip.hide();
	}
	
	public void dragExtractIcon(){
		this.hasExtractImageProperties.getStyleClass().clear();
		this.hasExtractImageProperties.getStyleClass().add("button");
		this.hasExtractImageProperties.getStyleClass().add("buttonMomentViewDrag");
		extractTooltip.setOpacity(0);
    	extractTooltip.hide();
	}
	
	public TypeController getPropertyController() {
		return propertyController;
	}
	
	public Main getMain() {
		return this.main;
	}
}