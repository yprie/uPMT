/*****************************************************************************
 * TypeClassRepresentationController.java
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

package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.ResourceBundle;

import SchemaTree.Cell.Models.IPropertyAdapter;
import application.Main;
import controller.controller.AddCategorySchemeController;
import controller.controller.AddPropertySchemeController;
import controller.controller.AddPropertySchemeWithValueController;
import controller.controller.ChangeColorCategorySchemeController;
import controller.controller.Observable;
import controller.controller.Observer;
import controller.controller.RemoveCategorySchemeController;
import controller.controller.RemovePropertySchemeController;
import controller.controller.RenameCategorySchemeController;
import controller.controller.TypeController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.Category;
import model.Property;

public class TypeCategoryRepresentationController extends BorderPane implements Initializable, Observer{
	
	private @FXML Label className;
	private @FXML VBox properties;
	private Category classe;
	
	private MomentExpVBox moment;
	private Main main;
	
	private TypeController treeClass;
	private TreeItem<TypeController> treeClassTreeItem;
	
	private Deque<Property> stack = new ArrayDeque<Property>();
	
	public TypeCategoryRepresentationController(Category classe,MomentExpVBox moment,Main main) {
		
		this.classe = classe;
        this.moment = moment;
        this.main = main;
        
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/TypeCategoryRepresentation.fxml"));
        fxmlLoader.setController(this);
        try {
        	fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        LinkToTreeClass(this.main.getTreeViewSchema().getRoot());
        
		this.className.setText(classe.getName());	
		if(classe.getColor() != null){
			setColor(classe.getColor());
		}
		loadProperties();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {}
	
	private void LinkToTreeClass(TreeItem<TypeController> tree){
		
		if(tree.getValue() != null && tree.getValue().getType().getName().equals(this.classe.getName())) {
			this.treeClass = tree.getValue();
			this.treeClassTreeItem = tree;
			try {
				tree.getValue().getClassNameController().addObserver(this);
			}catch(Exception e) {}
			try {
				tree.getValue().getClassColorController().addObserver(this);
			}catch(Exception e) {}
			try {
				tree.getValue().getAddPropertySchemeController().addObserver(this);
			}catch(Exception e) {}	
			try {
				tree.getValue().getRemovePropertySchemeController().addObserver(this);
			}catch(Exception e) {}
			try {
				tree.getValue().GetAddPropertySchemeWithValueController().addObserver(this);
			}catch(Exception e) {}
			try {
				tree.getValue().getAddClassSchemeController().addObserver(this);
			}catch(Exception e) {}
			try {
				tree.getValue().getRemoveClassSchemeController().addObserver(this);
			}catch(Exception e) {}
			return;
		}
		
		if(!tree.isLeaf()) {
			for(TreeItem<TypeController> t : tree.getChildren()) {
				LinkToTreeClass(t);
			}
		}
	}
	
	public void loadProperties(){
		// for each Property, add a representation
		if(!classe.getProperties().isEmpty()){
			for(IPropertyAdapter prop : classe.getProperties()){
				TypePropertyRepresentation controller =  new TypePropertyRepresentation(prop,moment, treeClassTreeItem, main,this.classe);
				this.properties.getChildren().add(controller);
			}
		}
	}
	
	public void setColor(String color){
		className.setTextFill(Color.web(color));
		classe.setColor(color);
	}
	
	public Category getClasse(){
		return this.classe;
	}

	public void colorFocus() {
		this.setStyle("-fx-border-color : #039ED3");
	}
	
	public void resetFocusColor(){
		this.setStyle("-fx-border-color : white");

	}

	public void RemoveProperty(Property p){
		for(IPropertyAdapter t : this.classe.getProperties()) {
			if(t.getValue().equals(p.getName())) {
				this.classe.getProperties().remove(t);
				stack.push((Property) t);
				break;
			}
		}
		
		for (Node n : this.properties.getChildren()) {
			TypePropertyRepresentation tpr = (TypePropertyRepresentation) n;
			if(tpr.getProperty().getName().equals(p.getName())) {
				this.properties.getChildren().remove(n);
				break;
			}
		}
	}

	public void addProperty(Property p) {
		this.classe.addProperty(p);
		TypePropertyRepresentation controller =  new TypePropertyRepresentation(p, moment, treeClassTreeItem, main,this.classe);
		this.properties.getChildren().add(controller);
	}

	public void renameProperty(Property p, String text) {
		for(Node n : properties.getChildren()){
			TypePropertyRepresentation t = (TypePropertyRepresentation) n;
			if(t.getProperty().equals(p)){
				t.getProperty().setName(text);
				t.setPropertyName(text);
			}
		}
	}

	public void renameClass(String text) {
		this.classe.setName(text);
		this.className.setText(text);
	}
	 
	public static boolean ListcontainsTypeClassRep(ObservableList<Node> children, TypeCategoryRepresentationController t) {
		for (Object object : children) {
			TypeCategoryRepresentationController tmp = (TypeCategoryRepresentationController) object;
			
			if(tmp.getClasse().equals(t.getClasse())) {
				return true;
			}
		} 
		return false;
	}
	
	public static void RemoveTypeClassRepFromList(ObservableList<Node> children, TypeCategoryRepresentationController t) {
		for (Object object : children) {
			TypeCategoryRepresentationController tmp = (TypeCategoryRepresentationController) object;
			
			if(tmp.getClasse().equals(t.getClasse())) {
				children.remove(tmp);
				break;
			}
		} 
	}

	@Override
	public void updateVue(Observable obs, Object value) {
		// TODO Auto-generated method stub
		if(obs.getClass().equals(RenameCategorySchemeController.class)) {
			renameClass((String) value);
		}
		if(obs.getClass().equals(ChangeColorCategorySchemeController.class)) {
			setColor((String) value);
		}
		if(obs.getClass().equals(AddPropertySchemeController.class)) {
			Property n = new Property(((Property)value).getName());
			boolean contain = false;
			for(IPropertyAdapter t : classe.getProperties()) {
				if(t.getValue().equals(n.getName())) {
					contain = true;
					break;
				}
			}
			
			if(!this.classe.getProperties().contains(n)) {
				addProperty((Property) n);
			}
			else {
			//System.out.println("AH BAH VOILA !!!!!");
			}
		}
		if(obs.getClass().equals(RemovePropertySchemeController.class)) {
			Property toRemove = (Property) value;
		//System.out.println("Remove dans TypeClassRpzCtrl "+toRemove.getName());
			RemoveProperty(toRemove);
		}
		if(obs.getClass().equals(AddPropertySchemeWithValueController.class)) {
			addProperty(stack.pop());
		}
		if(obs.getClass().equals(RemoveCategorySchemeController.class)) {
		//System.out.println("DELETINGCLASS");
			moment.removeTypeClassRep(this);
		}
		if(obs.getClass().equals(AddCategorySchemeController.class)) {
		//System.out.println("REPUTINGCLASS");
			moment.putPreviousClassRep();
		}
	}
}
