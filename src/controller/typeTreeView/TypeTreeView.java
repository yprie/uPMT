/*****************************************************************************
 * TypeTreeView.java
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
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.swing.GroupLayout.Alignment;

import application.Main;
import controller.MomentExpVBox;
import controller.TypeClassRepresentationController;
import controller.command.RemoveTypeCommand;
import controller.controller.Observable;
import controller.controller.Observer;
import controller.controller.TypeController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Cell;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.StageStyle;
import model.Classe;
import model.DescriptionEntretien;
import model.Dossier;
import model.MomentExperience;
import model.Propriete;
import model.Schema;
import model.Type;
import utils.UndoCollector;

public class TypeTreeView extends TreeCell<TypeController>{

	private Main main;
	private TextField textField;
	private ContextMenu addMenu = new ContextMenu();
	private TypeTreeViewController controller;
		
	public TypeTreeView(Main main) {
		this.main = main;
	}
	
	// method used to add a class to the current cell
	public void addClass(int classNumber){
		TreeItem<TypeController> newTypeController = new TreeItem<TypeController>();
        Type nt = new Classe("Classe "+classNumber);
        TypeController tc;
        if(getItem().getType().getClass().equals(Schema.class)){
        	tc = new TypeController(nt, getTreeItem().getValue().getType());
        }else{
        	tc = new TypeController(nt, getTreeItem().getParent().getValue().getType());
        }
        newTypeController.setValue(tc);
        getTreeItem().getChildren().add(newTypeController);
        getItem().getType().getTypes().add(nt);
        this.getTreeItem().setExpanded(true);
	}
	
	// method used to add a folder to the current cell
	public void addFolder(int folderNumber){
		TreeItem<TypeController> newTypeController = new TreeItem<TypeController>();
        Type nt = new Dossier("Dossier "+folderNumber);
        TypeController tc;
        if(getItem().getType().getClass().equals(Schema.class)){
        	tc = new TypeController(nt, getTreeItem().getValue().getType());
        }else{
        	tc = new TypeController(nt, getTreeItem().getParent().getValue().getType());
        }
        tc = new TypeController(nt, getTreeItem().getValue().getType());
        newTypeController.setValue(tc);
        getTreeItem().getChildren().add(newTypeController);
        getItem().getType().getTypes().add(nt);
        this.getTreeItem().setExpanded(true);
	}

	// method used to add a property to the current cell
	public void addProperty(Type nt){
		TreeItem<TypeController> newType = new TreeItem<TypeController>();
		TypeController tc = new TypeController(nt, getTreeItem().getParent().getValue().getType());
        newType.setValue(tc);
        getTreeItem().getChildren().add(newType);
        this.getTreeItem().setExpanded(true);	
	}
	
	private Propriete dupProperty(Propriete p){
		Propriete newp = new Propriete(p.getName());
		return newp;
	}
	
	public TypeTreeViewController getController() {
		return this.controller;
	}
	
	//Override StartEdit to avoir graphical bug
	@Override
	public void startEdit() {} 
	
    private String getString() {
        return getItem() == null ? "" : getItem().getType().getName();
    }
    
    // if cell selected -> show options
    // otherwise stay lite on visuals
    @Override
    public void updateSelected(boolean selected) {
    	super.updateSelected(selected);
    	if (selected) {
        	this.controller.showButtons();
		}
    	else{
    		this.controller.hideButtons();
    	}
    }
    	
	@Override
	public void updateItem(TypeController elem, boolean empty) {
		
		super.updateItem(elem, empty);
		if(empty || elem == null) {
	        setText(null);
	        setGraphic(null);
		}else{
			 if (isEditing()) {
                 if (textField != null) {
                     textField.setText(getString());
                 }
                 setText(null);
                 setGraphic(textField);
			 }
			 else
			 {
            	 // Linking to the visual representation (XML)
    			 try {
    				 FXMLLoader loader = new FXMLLoader();
    			     TypeTreeViewController controller =  new TypeTreeViewControllerRoot(elem,this,main);
    				 loader.setLocation(getClass().getResource("/view/typeTreeView/TypeTreeView.fxml"));

    				 // loading view (XML) with fitting type
    				 if(elem.getType().isProperty()){
        			     loader.setLocation(getClass().getResource("/view/typeTreeView/TypeTreeViewProperty.fxml"));
        			     controller =  new TypeTreeViewControllerProperty(elem,this, main);
    				 }
    				 if(elem.getType().isClass()){
        			     loader.setLocation(getClass().getResource("/view/typeTreeView/TypeTreeViewClass.fxml"));
        			     controller =  new TypeTreeViewControllerClass(elem,this, main);
    				 }
    				 if(elem.getType().isFolder()){
        			     loader.setLocation(getClass().getResource("/view/typeTreeView/TypeTreeViewFolder.fxml"));
        			     controller =  new TypeTreeViewControllerFolder(elem,this, main);
    				 }
    				 
    			     this.controller = controller;
    			     loader.setController(controller);
    			     BorderPane elementPane = (BorderPane) loader.load();
    			     this.setGraphic(elementPane);
    			     this.controller.hideButtons();
    			 }
    			 catch (IOException e) {
    				 e.printStackTrace();
    			 }

             }			
			// allow Dragging of the type
			this.setOnDragDetected(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					
					if(main.getCurrentDescription() != null){
						if(elem.getType().isClass()){
							Dragboard db = ((TypeTreeView)event.getSource()).startDragAndDrop(TransferMode.ANY);
							ClipboardContent content = new ClipboardContent();
				            content.putString("ajoutType");
				            content.putRtf(elem.getType().getName());
				            db.setContent(content);
						}

					}
				}
			});	
		}
	}
}
