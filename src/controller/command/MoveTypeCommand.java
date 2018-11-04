package controller.command;

import java.io.IOException;
import java.util.LinkedList;

import application.Main;
import controller.MomentExpVBox;
import controller.controller.TypeController;
import controller.typeTreeView.TypeTreeView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.FlowPane;
import model.Category;
import model.Descripteme;
import model.Folder;
import model.MomentExperience;
import model.Property;
import model.Schema;
import model.Type;
import utils.MainViewTransformations;
import utils.Serializer;
import utils.Undoable;

public class MoveTypeCommand  implements Command,Undoable{
	
	private DragEvent event;
	private Main main;
	private Type newParent,oldParent,child;
	private TreeItem<TypeController> itemChild, itemNewParent, itemOldParent;
	private LinkedList<MomentExperience> moments;
	
	public MoveTypeCommand(DragEvent event, TypeTreeView p, TypeTreeView nP,  Main m) {
		try{
			this.event = event;
		
		this.main = m;
		this.newParent = nP.getController().getType();
		
		child = (Type) Serializer.deserialize((byte[]) event.getDragboard().getContent(TypeTreeView.TYPE));
		this.oldParent = p.getTreeItem().getParent().getValue().getType();
		//this.child = p.getTreeItem().getValue().getType();
		TreeView<TypeController> tree =  main.getTreeViewSchema();
		TreeItem<TypeController> root = tree.getRoot();
		/*this.itemChild = getTreeItemByType(child, root);
		this.itemNewParent = getTreeItemByType(newParent, root);
		this.itemOldParent = getTreeItemByType(oldParent, root);
		*/
		this.itemOldParent = p.getTreeItem().getParent();
		this.itemNewParent = nP.getTreeItem();
		System.out.println("Nouveau Parent:"+this.newParent.getName());
		System.out.println("Ancien Parent:"+this.oldParent.getName());
		System.out.println("Enfant:"+this.child.getName());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	private TreeItem<TypeController> getTreeItemByType(Type t, TreeItem<TypeController> root) {
		TreeItem<TypeController> ret=null;
		if(root.getValue()!=null) {
			if(root.getValue().getType().equals(t)) {
				ret = root;
			}
		}
		if(ret==null) {
			for(TreeItem<TypeController> item : root.getChildren()) {
				ret= getTreeItemByType(t, item);
				if(ret!=null)break;
			}
		}
		return ret;
	}
	
	

	@Override
	public void undo() {
		if(child.isFolder()||(child.isCategory())) {
			this.itemNewParent.getChildren().remove(itemChild);
			newParent.removeChild(child);
			this.itemOldParent.getChildren().add(itemChild);
			oldParent.addChild(child);
		}
		else if(child.isProperty()) {
			RemovePropertyFromClassCommand cmd = new RemovePropertyFromClassCommand(this.itemNewParent.getValue(), (Property)child, this.itemNewParent, main);
			cmd.execute();
			AddPropertyToClassCommand cmd2 = new AddPropertyToClassCommand(this.itemOldParent.getValue(), (Property)child, this.itemOldParent, main);
			cmd2.execute();
		}
		main.refreshDataTreeView();
		main.needToSave();
	}

	@Override
	public void redo() {
		execute();
	}

	@Override
	public String getUndoRedoName() {
		return "moveType";
	}
	

	@Override
	public void execute() {
		if(child.isFolder()||(child.isCategory())) {
			this.itemOldParent.getChildren().remove(itemChild);
			oldParent.removeChild(child);
			this.itemNewParent.getChildren().add(itemChild);
			newParent.addChild(child);
		}
		else if(child.isProperty()) {
			try {
				if(((Property)child).getDescriptemes()==null) ((Property)child).setDescriptemes(new LinkedList<Descripteme>());
				if( ( !((Property)child).getDescriptemes().isEmpty() || ((Property)child).getValue()!=null) && moments==null) {
					moments = getMomentsContainsCatExcept(main.getCurrentDescription().getMoments(), (Category)oldParent, (Category)newParent);
				}
				System.out.println("value: "+((Property)child).getValue());
				RemovePropertyFromClassCommand cmd = new RemovePropertyFromClassCommand(this.itemOldParent.getValue(), (Property)child, this.itemOldParent, main);
				cmd.execute();
				System.out.println("value2: "+((Property)child).getValue());
				AddPropertyToClassCommand cmd2 = new AddPropertyToClassCommand(this.itemNewParent.getValue(), (Property)child, this.itemNewParent, main);
				cmd2.execute();
				System.out.println("value3: "+((Property)child).getValue());
				if( ( !((Property)child).getDescriptemes().isEmpty() || ((Property)child).getValue()!=null) && moments==null) {
					for(MomentExperience m : moments) {
						
					}
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		main.refreshDataTreeView();
		main.needToSave();
	}
	
	public void updateMomentCat(MomentExperience m, Category newC) {
		for(Category c : m.getCategories()) {
			if(c.getName().equals(newC.getName())) {
				m.getCategories().remove(c);
				m.getCategories().add(newC);
				break;
			}
		}
	}
	
	public LinkedList<MomentExperience> getMomentsContainsCatExcept(LinkedList<MomentExperience> root, Category category, Category exception){
		LinkedList<MomentExperience> ret = new LinkedList<MomentExperience>();
		for(MomentExperience m : root) {
			if(m.getCategories().contains(category) && !m.getCategories().contains(exception)) {
				ret.add(m);
				break;
			}
			if(!m.getSubMoments().isEmpty())
				ret.addAll(getMomentsContainsCatExcept(m.getSubMoments(), category, exception));
		}
		return ret;
	}
	

	@Override
	public boolean canExecute() {
		return false;
	}

}