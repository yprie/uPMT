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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import controller.command.ChangePropertyValueCommand;
import controller.controller.ChangePropertyValueController;
import controller.controller.Observable;
import controller.controller.Observer;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import model.Propriete;
import utils.UndoCollector;

public class TypePropertyRepresentation extends BorderPane implements Initializable, Observer{
	
	private @FXML Label propertyValue;
	private @FXML Label propertyName;
	private @FXML BorderPane propertyPane;
	private Main main;
	private Propriete property;
	private TreeItem<TypeController> propertyTypeTreeItem;
	
	private TypeController propertyController;
	
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
        LinkToTreeProperty();
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
						        	ChangePropertyValueCommand cmd = new ChangePropertyValueCommand(tpr, property.getValeur(), t.getText());
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
			System.out.println("HEHRE");
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
	}
	
	public TypeController getPropertyController() {
		return propertyController;
	}
	
	public Main getMain() {
		return this.main;
	}
}