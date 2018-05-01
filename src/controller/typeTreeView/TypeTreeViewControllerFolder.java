/*****************************************************************************
 * TypeTreeViewControllerFolder.java
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

import application.Main;
import controller.command.RemoveFolderSchemeCommand;
import controller.command.RenameFolderSchemeCommand;
import controller.command.RenamePropertySchemeCommand;
import controller.controller.TypeController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import model.Folder;
import model.Schema;
import model.Type;
import utils.ResourceLoader;
import utils.UndoCollector;

public class TypeTreeViewControllerFolder extends TypeTreeViewController {
	
	private @FXML ImageView folderIcon;
	
	private TypeTreeViewControllerFolder ref;
	
	public TypeTreeViewControllerFolder(TypeController type, TypeTreeView typeTreeView, Main m) {
		super(type,typeTreeView, m);
		ref = this;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nomType.setText(type.getType().getName());
		this.nomType.setTextFill(Color.BLACK);
		Image icon = ResourceLoader.loadImage("folder.png");
		this.folderIcon.setImage(icon);
		
		
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
				deleteFolder();
			}
        });
        MenuItem menu3 = new MenuItem(main._langBundle.getString("add_category"));
        menu3.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				addClass();
			}
        });
        MenuItem menu4 = new MenuItem(main._langBundle.getString("add_folder"));
        menu4.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				addFolder();
			}
        });
        treeviewMenuAction.getItems().addAll(menu1, menu2, menu3, menu4);
	}
	
	public void setName(String newName) {
		this.type.getType().setName(newName);
		this.nomType.setText(newName);
	}
	
	@Override
	public void rename() {
		super.rename();
		ChangeListener<Boolean>	 listener = new ChangeListener<Boolean>() {
			@Override
		    public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
		    {
		        if (!newPropertyValue){
					if(type.getType().isFolder()){
						String oldName = new String(type.getType().getName());
						if(!oldName.equals(textField.getText())) {
							boolean hasName = false;
							LinkedList<Folder> folders;
							if(type.getParent().isSchema())
								folders = ((Schema)type.getParent()).getFolders();
							else
								folders = ((Folder)type.getParent()).getFolders();
							for(Type folder : folders) {
								if(folder.getName().equals(textField.getText())) {
									hasName = true;
									break;
								}	
							}
							if(!hasName) {
								RenameFolderSchemeCommand cmd = new RenameFolderSchemeCommand(
										ref, 
										type.getType().getName(), 
										textField.getText(),
										main);
								cmd.execute();
								UndoCollector.INSTANCE.add(cmd);
							}
							else {
								Alert alert = new Alert(AlertType.INFORMATION);
								alert.setTitle(main._langBundle.getString("invalid_name"));
								alert.setHeaderText(null);
								alert.setContentText(main._langBundle.getString("folder_name_invalid"));
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
	public void addClass(){
		tree.addClass(++TypeTreeViewControllerCategory.propertiesNumber);
	}
	
	@FXML
	public void addFolder(){
		tree.addFolder(++TypeTreeViewControllerCategory.propertiesNumber);
	}
	
	@FXML
	public void deleteFolder(){
		RemoveFolderSchemeCommand cmd = new RemoveFolderSchemeCommand(this.tree.getTreeItem(), main);
		cmd.execute();
		UndoCollector.INSTANCE.add(cmd);
	}
	
	@FXML
	public void deleteType(){}
	
	@Override
	public void hideButtons(){
		this.treeviewMenuAction.setVisible(false);
	}

	public void showButtons() {
		this.treeviewMenuAction.setVisible(true);
	}

}
