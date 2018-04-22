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
import javafx.scene.paint.Color;
import model.Folder;
import model.Schema;
import model.Type;
import utils.ResourceLoader;
import utils.UndoCollector;

public class TypeTreeViewControllerFolder extends TypeTreeViewController {
	
	private @FXML Button deleteFolder;
	private @FXML Button addClass;
	private @FXML Button addFolder;
	private @FXML ImageView folderIcon;
	
	private Main main;
	private TypeTreeViewControllerFolder ref;
	
	public TypeTreeViewControllerFolder(TypeController type, TypeTreeView typeTreeView, Main m) {
		super(type,typeTreeView);
		ref = this;
		main = m;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nomType.setText(type.getType().getName());
		if(Main.activateBetaDesign)
			this.nomType.setTextFill(Color.BLACK);
		Image icon = ResourceLoader.loadImage("folder.png");
		this.folderIcon.setImage(icon);
		
		Node iconRename = new ImageView(ResourceLoader.loadImage("rename.png"));
		this.rename.setGraphic(iconRename);
		
		Node iconAddClass = new ImageView(ResourceLoader.loadImage("addclass.gif"));
		this.addClass.setGraphic(iconAddClass);
		
		Node iconAddFold = new ImageView(ResourceLoader.loadImage("newfolder.gif"));
		this.addFolder.setGraphic(iconAddFold);
		
		Node iconDelete = new ImageView(ResourceLoader.loadImage("delete.gif"));
		this.deleteFolder.setGraphic(iconDelete);
		
		Tooltip deleteFolerTip = new Tooltip("Delete the folder");
		deleteFolder.setTooltip(deleteFolerTip);
		Tooltip addClassTip = new Tooltip("Add a category to the folder");
		addClass.setTooltip(addClassTip);
		Tooltip addFolderTip = new Tooltip("Add a subfolder to the folder");
		addFolder.setTooltip(addFolderTip);
		Tooltip renameTip = new Tooltip("Rename the folder");
		rename.setTooltip(renameTip);
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
	
	@FXML
	public void addClass(){
		tree.addClass(++TypeTreeViewControllerClass.propertiesNumber);
	}
	
	@FXML
	public void addFolder(){
		tree.addFolder(++TypeTreeViewControllerClass.propertiesNumber);
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
		this.deleteFolder.setVisible(false);
		this.addClass.setVisible(false);
		this.addFolder.setVisible(false);
		this.rename.setVisible(false);
	}

	public void showButtons() {
		this.deleteFolder.setVisible(true);
		this.addClass.setVisible(true);
		this.addFolder.setVisible(true);
		this.rename.setVisible(true);
	}

}
