/*****************************************************************************
 * TypeTreeViewControllerClass.java
 *****************************************************************************
 * Copyright 锟 2017 uPMT
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

package controller.typeTreeView;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import controller.command.AddPropertyToClassCommand;
import controller.command.ChangeColorCategoryCommand;
import controller.command.RemoveCategoryFromParentCommand;
import controller.command.RenameCategorySchemeCommand;
import controller.controller.ChangeColorCategorySchemeController;
import controller.controller.Observable;
import controller.controller.Observer;
import controller.controller.RenameCategorySchemeController;
import controller.controller.TypeController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import model.Category;
import model.Property;
import utils.MainViewTransformations;
import utils.ResourceLoader;
import utils.UndoCollector;
import utils.Utils;

public class TypeTreeViewControllerCategory extends TypeTreeViewController implements Observer{
	
	private @FXML ColorPicker couleurType;
	private @FXML ImageView classIcon;
	
	
	public static int propertiesNumber = 1;
	
	public TypeTreeViewControllerCategory(TypeController type, TypeTreeView typeTreeView, Main m) {
		super(type,typeTreeView, m);
		type.getClassNameController().addObserver(this);
		type.getClassColorController().addObserver(this);
	}
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nomType.setText(type.getType().getName());
		couleurType.setStyle("-fx-color-label-visible: false ;");
		setColor();
		
		Image icon = ResourceLoader.loadImage("category.png");
		this.classIcon.setImage(icon);

		
		treeviewMenuAction.getItems().clear();
        MenuItem menu1 = new MenuItem(main._langBundle.getString("rename"));
        menu1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				rename();
			}
        });
        MenuItem menu2 = new MenuItem(main._langBundle.getString("delete"));
        menu2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				deleteClass();
			}
        });
        MenuItem menu3 = new MenuItem(main._langBundle.getString("add_property"));
        menu3.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				addProperty();
			}
        });
        treeviewMenuAction.getItems().addAll(menu1, menu2, menu3);
        nomType.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(isSelected) rename();
			}
        });      
	}
	

	@Override
	public void rename() {
		super.rename();
		ChangeListener<Boolean>	 listener = new ChangeListener<Boolean>() {
			 @Override
			    public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
			    {
			        if (!newPropertyValue){
			        	if(type.getType().isCategory()){
			        		String oldName = new String(type.getType().getName());
			        		if(!oldName.equals(textField.getText())) {
				        		if(MainViewTransformations.getCategory(textField.getText(), main.getCurrentProject().getSchema())==null) {
				        			
				        			RenameCategorySchemeCommand cmd = new RenameCategorySchemeCommand(
											type.getClassNameController(), 
											oldName, 
											textField.getText(),
											main);
									cmd.execute();
									UndoCollector.INSTANCE.add(cmd);
				        		}
								else {
									Alert alert = new Alert(AlertType.INFORMATION);
									alert.setTitle(main._langBundle.getString("invalid_name"));
									alert.setHeaderText(null);
									alert.setContentText(main._langBundle.getString("category_name_invalid"));
									alert.show();
								}
			        		}
						}		        	
						typePane.setLeft(nomType);
						textField.focusedProperty().removeListener(this);
			        }
			    }
		};
		
		textField.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.ENTER){
					
					if(type.getType().isProperty()){
						textField.setText(textField.getText());
					}
					typePane.setLeft(nomType);
				}
				if(event.getCode() == KeyCode.ESCAPE){
					typePane.setLeft(nomType);
				}
			}
		});
		textField.focusedProperty().addListener(listener);
		typePane.setLeft(textField);
		Platform.runLater(()->textField.requestFocus());
		Platform.runLater(()->textField.selectAll());
	}
	
	@FXML
	public void pickColor(){
		Color colorPicked = couleurType.getValue();
		String colorConverted = Utils.toRGBCode(colorPicked);
		ChangeColorCategoryCommand cmd = new ChangeColorCategoryCommand(
				type.getClassColorController(),
				this.type.getType().getColor(),
				colorConverted,
				main);
		cmd.execute();
		UndoCollector.INSTANCE.add(cmd);		
	}
	
	@FXML
	public void addProperty(){
		propertiesNumber++;
		Property nt = new Property("Propriete " + propertiesNumber);
		AddPropertyToClassCommand cmd = new AddPropertyToClassCommand(type, nt, tree.getTreeItem(), this.main);
		cmd.execute();
		UndoCollector.INSTANCE.add(cmd);
	}
		
	@FXML
	public void deleteClass(){
		RemoveCategoryFromParentCommand cmd = new RemoveCategoryFromParentCommand(
				tree.getTreeItem().getValue(), 
				(Category) type.getType(),
				tree.getTreeItem().getParent(),
				main);
		cmd.execute();
		UndoCollector.INSTANCE.add(cmd);
	}
	
	public void setColor(){
		if (type.getType().getColor() != null) {
			couleurType.setValue(Color.valueOf(type.getType().getColor()));
			if(!Main.activateBetaDesign)
				this.nomType.setTextFill(Color.web(type.getType().getColor()));
			else
				this.nomType.setTextFill(Main.colorSelectedCell);
		}
	}
	
	@Override
	public void showButtons(boolean visibility) {
		this.couleurType.setVisible(visibility);
		this.treeviewMenuAction.setVisible(visibility);
	}
	
	@Override
	public void isSelected() {
		new java.util.Timer().schedule(new java.util.TimerTask() {
		            @Override
		            public void run() {
		            	isSelected = true;
		            }},500);
		
		showButtons(true);
	}
	
	@Override
	public void isUnselected() {
		isSelected = false;
		showButtons(false);
	}

	@Override
	public void updateVue(Observable obs, Object value) {
		if(obs.getClass().equals(RenameCategorySchemeController.class)) {
			this.nomType.setText((String) value);
		}
		if(obs.getClass().equals(ChangeColorCategorySchemeController.class)) {
			
			if(!Main.activateBetaDesign)
				this.nomType.setTextFill(Color.web((String) value));
			else
				this.nomType.setTextFill(Color.BLACK);
			
			couleurType.setValue(Color.valueOf((String) value));
		}
	}
}
