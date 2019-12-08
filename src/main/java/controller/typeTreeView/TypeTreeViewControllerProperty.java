/*****************************************************************************
 * TypeTreeViewControllerProperty.java
 *****************************************************************************
 * Copyright 锟� 2017 uPMT
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import SchemaTree.Cell.Models.IPropertyAdapter;
import application.Main;
import controller.command.RemovePropertyFromCategoryCommand;
import controller.command.RenamePropertySchemeCommand;
import controller.controller.Observable;
import controller.controller.Observer;
import controller.controller.RenamePropertyController;
import controller.controller.TypeController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
import utils.ResourceLoader;
import utils.UndoCollector;

public class TypeTreeViewControllerProperty extends TypeTreeViewController implements Observer{
	
	private @FXML ImageView propertyIcon;

	public TypeTreeViewControllerProperty(TypeController type, TypeTreeView typeTreeView, Main m) {
		super(type,typeTreeView, m);
		type.getRenamePropertyController().addObserver(this);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nomType.setText(type.getType().getName());
		this.nomType.setTextFill(Color.BLACK);
		this.setLabelChangeName(main,this);  //double click

		
		Image icon = ResourceLoader.loadImage("property.png");
		this.propertyIcon.setImage(icon);
		
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
				deleteProperty();
			}
        });
        
        treeviewMenuAction.getItems().addAll(menu1, menu2);
		initNumber();
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
					if(type.getType().isProperty()){
						String oldName = new String(type.getType().getName());
						if(!oldName.equals(textField.getText())) {
							boolean hasName = false;
							for(IPropertyAdapter propriete : ((Category)type.getParent()).getProperties()) {
								if(propriete.getName().equals(textField.getText())) {
									hasName = true;
									break;
								}
							}	
						
							if(!hasName) {
								RenamePropertySchemeCommand cmd = new RenamePropertySchemeCommand(
										type.getRenamePropertyController(), 
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
								alert.setContentText(main._langBundle.getString("properties_name_invalid"));
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
	
	private void initNumber() {
		Pattern lastIntPattern = Pattern.compile("[^0-9]+([0-9]+)$");
		String input = type.getType().getName();
		Matcher matcher = lastIntPattern.matcher(input);
		if (matcher.find()) {
		    String someNumberStr = matcher.group(1);
		    int lastNumberInt = Integer.parseInt(someNumberStr);
		    if(lastNumberInt > TypeTreeViewControllerCategory.propertiesNumber) {
		    	TypeTreeViewControllerCategory.propertiesNumber = lastNumberInt;
		    }
		}
	}
	
	@FXML
	public void deleteProperty(){
		RemovePropertyFromCategoryCommand cmd = new RemovePropertyFromCategoryCommand(
				tree.getTreeItem().getParent().getValue(), 
				(Property) type.getType(),
				tree.getTreeItem().getParent(),
				main);
		cmd.execute();
		UndoCollector.INSTANCE.add(cmd);
	}
	
	@Override
	public void showButtons(boolean visibility) {
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
		if(obs.getClass().equals(RenamePropertyController.class)) {
			this.nomType.setText((String) value);
		}
	}
	
	public void setLabelChangeName(Main main,TypeTreeViewControllerProperty thiss){
		nomType.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				if(arg0.getClickCount() == 2){
					//System.out.println("DoubleClick");
					rename();
				}
			}
		});
	}

}
