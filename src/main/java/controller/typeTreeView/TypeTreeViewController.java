/*****************************************************************************
 * TypeTreeViewController.java
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

package controller.typeTreeView;

import java.net.URL;
import java.util.ResourceBundle;


import SchemaTree.Cell.Models.ITypeAdapter;
import application.Main;
import controller.controller.TypeController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class TypeTreeViewController implements Initializable{
	
	protected @FXML Label nomType;
	protected TypeController type;
	protected TypeTreeView tree;
	protected @FXML BorderPane typePane;
	protected @FXML MenuButton treeviewMenuAction;
	
	protected Main main;
	protected boolean isSelected = false;
	protected TextField textField;
	
	public TypeTreeViewController(TypeController type,TypeTreeView tree, Main main) {
		
		this.main = main;
		this.type = type;
		this.tree = tree;
		this.textField = new TextField();
	}
	
	public BorderPane getTypePane() {
		return typePane;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.nomType.setText(type.getType().getName());
		
		treeviewMenuAction.getItems().clear();
        MenuItem menu1 = new MenuItem(main._langBundle.getString("rename"));
        menu1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				rename();
			}
        });
        treeviewMenuAction.getItems().addAll(menu1);
        
        
	}

	public void rename() {
		textField.setText(type.getType().getName());
		textField.setMaxWidth(150);
		textField.requestFocus();
		//System.out.println("On renomme");
	}
	
	protected void showButtons(boolean visibility) {}
	protected void isSelected() {}
	protected void isUnselected() {}
	
	public ITypeAdapter getType() {
		return type.getType();
	}
	
	public ITypeAdapter getParent() {
		return type.getParent();
	}

}