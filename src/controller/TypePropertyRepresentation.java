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
import java.util.ResourceBundle;

import application.Main;
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
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Enregistrement;
import model.Propriete;
import utils.UndoCollector;

public class TypePropertyRepresentation extends BorderPane implements Initializable, Observer{
	
	private @FXML Label propertyValue;
	private @FXML Label propertyName;
	private @FXML ImageView hasExtractImageProperties;
	private @FXML BorderPane propertyPane;
	private Main main;
	private Propriete property;
	private TreeItem<TypeController> propertyTypeTreeItem;
	
	private TypeController propertyController;
	
	private Tooltip extractTooltip;
	
	private PropertyExtractController propertyExtractController;
	
	public TypePropertyRepresentation(Propriete t,  TreeItem<TypeController> propertyTypeTreeItem, Main main) {
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
        
        propertyName.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent mouseEvent) {
		        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
		            if(mouseEvent.getClickCount() == 2){
		            	pickPropertyExtract();
		            }
		        }
		    }
		});
        extractTooltip = new Tooltip();
        extractTooltip.setWrapText(true);
		extractTooltip.setMaxWidth(500);
        hasExtractImageProperties.setOnMouseEntered(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent event) {
		    	if(propertiesHaveDescriptem()) {
			    	extractTooltip.setText(property.getDescripteme().getTexte());
			        javafx.geometry.Point2D p = hasExtractImageProperties.localToScreen(hasExtractImageProperties.getLayoutBounds().getMaxX(), hasExtractImageProperties.getLayoutBounds().getMaxY()); 
			        extractTooltip.show(hasExtractImageProperties, p.getX(), p.getY());
		    	}
		    }
		});
        
        
        hasExtractImageProperties.setOnMouseExited(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent event) {
		    	if(propertiesHaveDescriptem())
		    		extractTooltip.hide();
		    }
		});
        
        

        propertyName.setOnMouseEntered(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent event) {
		    	if(!propertiesHaveDescriptem()) {
		    		extractTooltip.setText("Double-click to add an extract.");
			    	javafx.geometry.Point2D p = propertyName.localToScreen(propertyName.getLayoutBounds().getMaxX(), propertyName.getLayoutBounds().getMaxY()); 
			        extractTooltip.show(propertyName, p.getX(), p.getY());
		    	}
		    }
		});
        propertyName.setOnMouseExited(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent event) {
		    	if(!propertiesHaveDescriptem())
		    		extractTooltip.hide();
		    }
		});
        
        LinkToTreeProperty();
        if(this.propertiesHaveDescriptem()) {
        	this.showExtractIcon(property.getDescripteme().getTexte());
        	//System.out.println("Propriete "+property.getName()+": "+property.getDescripteme().getTexte());
        }
        else{
        	//System.out.println("Hide icon: Propriete "+property.getName()+": "+property.getDescripteme().getTexte());
        	this.hideExtractIcon();
        }
	}
	
	private boolean propertiesHaveDescriptem() {
		if(property.getDescripteme().getTexte()==null) {
    		return false;
    	}
    	else if(property.getDescripteme().getTexte().length()==0) {
    		return false;
    	}
    	else return true;
	}

	private void pickPropertyExtract() {
		Stage promptWindow = new Stage();
		promptWindow.setTitle("Selection de l'extrait");
		try {
			main.getCurrentMoment().setCurrentProperty(property);
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/SelectDescriptemePart.fxml"));
			loader.setController(new SelectDescriptemePartController(
					main,
					promptWindow,
					new TextArea(),
					propertyExtractController));
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

	public Propriete getProperty(){
		return this.property;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.propertyName.setText(property.getName());
		if(this.property.getValeur() != null){
			this.propertyValue.setText(property.getValeur());
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
		property.setValeur(value);
	}
	
	private void setLabelChangeName(BorderPane box, TypePropertyRepresentation tpr){
		
		
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
						        			property.getValeur(), 
						        			t.getText(),
						        			main);
						        	cmd.execute();
						        	UndoCollector.INSTANCE.add(cmd);
									box.setRight(propertyValue);
						        }
						    }
					};
					t.setOnKeyPressed(new EventHandler<KeyEvent>() {

						@Override
						public void handle(KeyEvent event) {
							if(event.getCode() == KeyCode.ENTER){
								t.setText(t.getText());
								box.setRight(propertyValue);
							}
							if(event.getCode() == KeyCode.ESCAPE){
								box.setRight(propertyValue);
							}
						}
					});
					t.focusedProperty().addListener(listener);
					Platform.runLater(()->t.requestFocus());
					Platform.runLater(()->t.selectAll());
					box.setRight(t);
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
				System.out.println("Have Value");
				this.showExtractIcon((String) value);

			}else {
				System.out.println("Haven't Value");
				this.hideExtractIcon();
			}
		}
	}
	
	public void showExtractIcon(String tooltip){
		File image = new File("./img/hasExtractIcon.gif");
		Image icon = new Image(image.toURI().toString());
		this.hasExtractImageProperties.setManaged(true);
		this.hasExtractImageProperties.setVisible(true);
		this.hasExtractImageProperties.setImage(icon);
		extractTooltip.setText(tooltip);
		extractTooltip.setOpacity(1);
	}
	
	
	public void hideExtractIcon(){
		this.hasExtractImageProperties.setImage(null);
		this.hasExtractImageProperties.setVisible(false);
		this.hasExtractImageProperties.setManaged(false);
		//extractTooltip.setOpacity(0);
	}
	
	public TypeController getPropertyController() {
		return propertyController;
	}
	
	public Main getMain() {
		return this.main;
	}
}