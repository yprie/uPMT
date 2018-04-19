/*****************************************************************************
 * TypeTreeViewControllerProperty.java
 *****************************************************************************
 * Copyright � 2017 uPMT
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

import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.Main;
import controller.MomentExpVBox;
import controller.command.RemovePropertyFromClassCommand;
import controller.command.RenameClassSchemeCommand;
import controller.command.RenamePropertySchemeCommand;
import controller.controller.Observable;
import controller.controller.Observer;
import controller.controller.RemovePropertySchemeController;
import controller.controller.RenameClassSchemeController;
import controller.controller.RenamePropertyController;
import controller.controller.TypeController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import model.Category;
import model.Property;
import model.Type;
import utils.ResourceLoader;
import utils.UndoCollector;
import utils.Utils;

public class TypeTreeViewControllerProperty extends TypeTreeViewController implements Observer{
	
	private @FXML Button deleteProperty;
	private @FXML ImageView propertyIcon;
	private Main main;
	
	public TypeTreeViewControllerProperty(TypeController type, TypeTreeView typeTreeView, Main m) {
		super(type,typeTreeView);
		type.getRenamePropertyController().addObserver(this);
		main = m;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nomType.setText(type.getType().getName());
		/*if(Main.activateBetaDesign)
			this.nomType.setTextFill(Color.BLACK);*/
		this.setLabelChangeName(main,this);  //double click

		Image icon = ResourceLoader.loadImage("property.png");
		this.propertyIcon.setImage(icon);
		
		Node iconRename = new ImageView(ResourceLoader.loadImage("rename.png"));
		this.rename.setGraphic(iconRename);
		
		Node iconDelete = new ImageView(ResourceLoader.loadImage("delete.gif"));
		this.deleteProperty.setGraphic(iconDelete);
		
		Tooltip deletePropertyTip = new Tooltip("Suppression de la propriet�");
		deleteProperty.setTooltip(deletePropertyTip);
		Tooltip renameTip = new Tooltip("Renommer la propriet�");
		rename.setTooltip(renameTip);
		initNumber();
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
							for(Property propriete : ((Category)type.getParent()).getProperties()) {
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
					rename.setDisable(false);
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
					rename.setDisable(false);
				}
				if(event.getCode() == KeyCode.ESCAPE){
					typePane.setLeft(nomType);
					rename.setDisable(true);
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
		    if(lastNumberInt > TypeTreeViewControllerClass.propertiesNumber) {
		    	TypeTreeViewControllerClass.propertiesNumber = lastNumberInt;
		    }
		}
	}
	
	@FXML
	public void deleteProperty(){
		RemovePropertyFromClassCommand cmd = new RemovePropertyFromClassCommand(
				tree.getTreeItem().getParent().getValue(), 
				(Property) type.getType(),
				tree.getTreeItem().getParent(),
				main);
		cmd.execute();
		UndoCollector.INSTANCE.add(cmd);
	}
	
	@Override
	public void hideButtons(){
		this.deleteProperty.setVisible(false);
		this.rename.setVisible(false);
	}

	@Override
	public void showButtons() {
		this.deleteProperty.setVisible(true);
		this.rename.setVisible(true);
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
