/*****************************************************************************
 * TypeTreeViewControllerFolder.java
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

import java.io.File;
import java.net.URL;
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
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
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
		File image = new File("./img/folder.png");
		Image icon = new Image(image.toURI().toString());
		this.folderIcon.setImage(icon);
		
		File imageRename = new File("./img/rename.png");
		Node iconRename = new ImageView(new Image(imageRename.toURI().toString()));
		this.rename.setGraphic(iconRename);
		
		File imageAddClass = new File("./img/addclass.gif");
		Node iconAddClass = new ImageView(new Image(imageAddClass.toURI().toString()));
		this.addClass.setGraphic(iconAddClass);
		
		File imageAddFold = new File("./img/newfolder.gif");
		Node iconAddFold = new ImageView(new Image(imageAddFold.toURI().toString()));
		this.addFolder.setGraphic(iconAddFold);
		
		File delete = new File("./img/delete.gif");
		Node iconDelete = new ImageView(new Image(delete.toURI().toString()));
		this.deleteFolder.setGraphic(iconDelete);
		
		Tooltip deleteFolerTip = new Tooltip("Suppression du dossier");
		deleteFolder.setTooltip(deleteFolerTip);
		Tooltip addClassTip = new Tooltip("Ajout d'une Classe au dossier");
		addClass.setTooltip(addClassTip);
		Tooltip addFolderTip = new Tooltip("Ajout d'un sous-dossier au dossier");
		addFolder.setTooltip(addFolderTip);
		Tooltip renameTip = new Tooltip("Renommer le dossier");
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
			        if (!newPropertyValue)
			        {
						if(type.getType().isFolder()){
							RenameFolderSchemeCommand cmd = new RenameFolderSchemeCommand(
									ref, 
									type.getType().getName(), 
									textField.getText(),
									main);
							cmd.execute();
							UndoCollector.INSTANCE.add(cmd);
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
