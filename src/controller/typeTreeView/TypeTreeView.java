/*****************************************************************************
 * TypeTreeView.java
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
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.swing.GroupLayout.Alignment;

import application.Main;
import controller.MomentExpVBox;
import controller.TypeCategoryRepresentationController;
import controller.command.MoveTypeCommand;
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
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import model.Category;
import model.DescriptionInterview;
import model.Folder;
import model.MomentExperience;
import model.Property;
import model.Schema;
import model.Type;
import utils.MainViewTransformations;
import utils.Serializer;
import utils.UndoCollector;

public class TypeTreeView extends TreeCell<TypeController>{

	private Main main;
	private TextField textField;
	private ContextMenu addMenu = new ContextMenu();
	private TypeTreeViewController controller;
	public static DataFormat pnm = new DataFormat("controller.typeTreeView.TypeTreeView.parentname");
	public static DataFormat nm = new DataFormat("controller.typeTreeView.TypeTreeView.name");
	public static DataFormat ty = new DataFormat("controller.typeTreeView.TypeTreeView.type");
	public static DataFormat TYPE = new DataFormat("model.Type");
	public static DataFormat TYPE_PARENT = new DataFormat("model.Type.parent");
	
		
	public TypeTreeView(Main main) {
		this.main = main;
	}
	
	// method used to add a class to the current cell
	public TreeItem<TypeController> addClass(int classNumber){
		TreeItem<TypeController> newTypeController = new TreeItem<TypeController>();
        Category nt = new Category("Category "+classNumber);
        TypeController tc;
        /*if(getItem().getType().getClass().equals(Schema.class)){
        	tc = new TypeController(nt, getTreeItem().getValue().getType());
        }else{
        	tc = new TypeController(nt, getTreeItem().getParent().getValue().getType());
        }*/
        tc = new TypeController(nt, getTreeItem().getValue().getType());
        System.out.println("Le parent de la categorie est "+getTreeItem().getValue().getType());
        newTypeController.setValue(tc);
        getTreeItem().getChildren().add(newTypeController);
        ((Folder)getItem().getType()).addCategory(nt);
        this.getTreeItem().setExpanded(true);
        return newTypeController;
	}
	
	// method used to add a folder to the current cell
	public TreeItem<TypeController> addFolder(int folderNumber){
		TreeItem<TypeController> newTypeController = new TreeItem<TypeController>();
        Folder nt = new Folder("Folder "+folderNumber);
        TypeController tc;
        /*if(getItem().getType().getClass().equals(Schema.class)){
        	tc = new TypeController(nt, getTreeItem().getValue().getType());
        }else{
        	tc = new TypeController(nt, getTreeItem().getParent().getValue().getType());
        	System.out.println("Le parent est "+getTreeItem().getParent().getValue().getType().getName());
        }*/
        System.out.println("Le parent du dossier est "+getTreeItem().getValue().getType());
        tc = new TypeController(nt, getTreeItem().getValue().getType());
        //tc = new TypeController(nt, getTreeItem().getValue().getType());
        newTypeController.setValue(tc);
        getTreeItem().getChildren().add(newTypeController);
        if(getItem().getType().isSchema()) {
        	((Schema)getItem().getType()).addFolder(nt);
        }
        else
        	((Folder)getItem().getType()).addFolder(nt);
        System.out.println("2. Le parent est "+getItem().getType().getName());
        this.getTreeItem().setExpanded(true);
        return newTypeController;
	}

	// method used to add a property to the current cell
	public void addProperty(Property nt){
		TreeItem<TypeController> newType = new TreeItem<TypeController>();
		//TypeController tc = new TypeController(nt, getTreeItem().getParent().getValue().getType());
		TypeController tc = new TypeController(nt, getTreeItem().getValue().getType());
		System.out.println("Le parent de la propriete est "+getTreeItem().getValue().getType());
		newType.setValue(tc);
        getTreeItem().getChildren().add(newType);
        this.getTreeItem().setExpanded(true);
        //System.out.println("On ajoute une propriete !");
        //System.out.println("On a essay� de la renommer");
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
        	this.controller.isSelected();
		}
    	else{
    		this.controller.isUnselected();
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
    				 if(elem.getType().isCategory()){
        			     loader.setLocation(getClass().getResource("/view/typeTreeView/TypeTreeViewCategory.fxml"));
        			     controller =  new TypeTreeViewControllerCategory(elem,this, main);
    				 }
    				 if(elem.getType().isFolder()){
        			     loader.setLocation(getClass().getResource("/view/typeTreeView/TypeTreeViewFolder.fxml"));
        			     controller =  new TypeTreeViewControllerFolder(elem,this, main);
    				 }
    				 
    			     this.controller = controller;
    			     elem.setTypeTreeViewController(this.controller);
    			     loader.setController(controller);
    			     BorderPane elementPane = (BorderPane) loader.load();
    			     this.setGraphic(elementPane);
    			     this.controller.showButtons(false);
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
						if(!elem.getType().isSchema() && !elem.getType().isProperty()){
							Dragboard db = startDragAndDrop(TransferMode.ANY);
							ClipboardContent content = new ClipboardContent();
							
				            if(elem.getType().isCategory())
				            	content.putString("ajoutType");
				            else 
				            	content.putString("moveType");
				            content.putRtf(elem.getType().getName());
				            content.put(TypeTreeView.nm, elem.getType().getName());
				            content.put(TypeTreeView.pnm, elem.getParent().getName());
				            content.put(TypeTreeView.ty, elem.getType().typeToString());
				            try {
								content.put(TypeTreeView.TYPE, Serializer.serialize(elem.getType()));
								content.put(TypeTreeView.TYPE_PARENT, Serializer.serialize(elem.getParent()));
							}
							catch(IOException e) {
								e.printStackTrace();
							}
				            db.setContent(content);
				            TypeTreeView.this.main.droppingTmp = TypeTreeView.this;
				            System.out.println("Drag detected on : "+TypeTreeView.this.getController().getType().getName());
				            
						}

					}
				}
			});
			
			this.setOnDragOver(new EventHandler<DragEvent>() {
				@Override
				public void handle(DragEvent event) {
					
					boolean cond = false;
					Type me = elem.getType();
					Type meParent = elem.getParent();
					Type you = MainViewTransformations.getTypeByName((String)event.getDragboard().getContent(TypeTreeView.nm), (String)event.getDragboard().getContent(TypeTreeView.pnm), main);
					String typeDrop = you.typeToString();
					//System.out.println("Drop by "+you.getName()+" on "+me.getName());
					if(!me.isProperty() && you!=me) {
						if(event.getDragboard().getString().equals("ajoutType") || event.getDragboard().getString().equals("moveType")){
							if(me.isCategory() && typeDrop.equals("Property")) {
								cond=true;
							}
							else if(me.isFolder() &&  (  typeDrop.equals("Category") || (typeDrop.equals("Folder")) ) ) {
								cond=true;
							}
							else if(me.isSchema() && typeDrop.equals("Folder")) {
								cond=true;
							}
						}
					}
					
					if(cond && (MainViewTransformations.isParentOf(you, me) || MainViewTransformations.isDirectParentOf(me, you))) cond=false;
			    	if(cond) {
			    		event.acceptTransferModes(TransferMode.ANY);
			    		controller.getTypePane().setBackground(new Background(new BackgroundFill(Color.gray(0.8), CornerRadii.EMPTY, Insets.EMPTY)));
			    	}
				    event.consume();
				}
			});
			
			this.setOnDragDropped(new EventHandler<DragEvent>() {
				@Override
				public void handle(DragEvent event) {
					if(event.getDragboard().getString().equals("moveType") || event.getDragboard().getString().equals("ajoutType")) {
						MoveTypeCommand cmd = new MoveTypeCommand(event, TypeTreeView.this.main.droppingTmp, TypeTreeView.this,TypeTreeView.this.main);
						cmd.execute();
						UndoCollector.INSTANCE.add(cmd);
					}
					controller.getTypePane().setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
					System.out.println(event.getDragboard().getString()+" - Dropped on : "+TypeTreeView.this.getController().getType().getName());
				}
			});
			
			this.setOnDragExited(new EventHandler<DragEvent>() {
				@Override
				public void handle(DragEvent event) {
					controller.getTypePane().setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
		    	}
			});
		}
	}
}
