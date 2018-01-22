/*****************************************************************************
 * TypeTreeViewControllerRoot.java
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
import controller.controller.TypeController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import model.Dossier;
import model.Type;
import utils.Utils;

public class TypeTreeViewControllerRoot extends TypeTreeViewController {
	
	private @FXML Button addFolder;
	private @FXML Button addClass;
	private @FXML ImageView rootIcon;
	private Main main;
	
	private static int classNumber = 1;
	private static int folderNumber = 1;
	
	public TypeTreeViewControllerRoot(TypeController type, TypeTreeView typeTreeView,Main main) {
		super(type,typeTreeView);
		this.main = main;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nomType.setText(type.getType().getName());
		if(Main.activateBetaDesign)
			this.nomType.setTextFill(Color.WHITE);
		File image = new File("./img/schema.gif");
		Image icon = new Image(image.toURI().toString());
		this.rootIcon.setImage(icon);
		
		File imageRename = new File("./img/rename.png");
		Node iconRename = new ImageView(new Image(imageRename.toURI().toString()));
		this.rename.setGraphic(iconRename);
		
		File imageAddClass = new File("./img/addclass.gif");
		Node iconAddClass = new ImageView(new Image(imageAddClass.toURI().toString()));
		this.addClass.setGraphic(iconAddClass);
		
		File imageAddFold = new File("./img/newfolder.gif");
		Node iconAddFold = new ImageView(new Image(imageAddFold.toURI().toString()));
		this.addFolder.setGraphic(iconAddFold);
		
		Tooltip renameTip = new Tooltip("Renommer le schéma");
		rename.setTooltip(renameTip);
		
		Tooltip addClassTip = new Tooltip("Ajouter une Classe à la racine");
		addClass.setTooltip(addClassTip);
		
		Tooltip addFolderTip = new Tooltip("Ajouter un Dossier à la racine");
		addFolder.setTooltip(addFolderTip);
		
	}

	@Override
	public void rename() {
		rename.setDisable(true);
		textField.setText(type.getType().getName());
		textField.setMaxWidth(120);
		textField.requestFocus();
		textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			 @Override
			    public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
			    {
			        if (!newPropertyValue)
			        {
			        	nomType.setText(textField.getText());
						type.getType().setName(textField.getText());
						main.getCurrentProject().getSchemaProjet().setName(textField.getText());
						typePane.setLeft(nomType);
						rename.setDisable(false);
			        }
			    }
		});
		textField.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.ENTER){
					nomType.setText(textField.getText());
					type.getType().setName(textField.getText());
					typePane.setLeft(nomType);
					rename.setDisable(true);
					main.getCurrentProject().getSchemaProjet().setName(textField.getText());
				}
				if(event.getCode() == KeyCode.ESCAPE){
					typePane.setLeft(nomType);
					rename.setDisable(true);
				}
			}
		});
		typePane.setLeft(textField);
		Platform.runLater(()->textField.requestFocus());
		Platform.runLater(()->textField.selectAll());
	}
	
	@FXML
	public void addFolder(){
		tree.addFolder(folderNumber);
	}
	
	@FXML
	public void addClass(){
		tree.addClass(classNumber);
	}
	
	@Override
	public void hideButtons(){
		this.addClass.setVisible(false);
		this.addFolder.setVisible(false);
		this.rename.setVisible(false);
	}

	@Override
	public void showButtons() {
		this.addClass.setVisible(true);
		this.addFolder.setVisible(true);
		this.rename.setVisible(true);

	}

}
