/*****************************************************************************
 * RemoveFolderSchemeCommand.java
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

package controller.command;

import java.io.IOException;
import java.util.LinkedList;

import application.Main;
import controller.MomentExpVBox;
import controller.TypeClassRepresentationController;
import controller.controller.Observable;
import controller.controller.TypeController;
import controller.typeTreeView.TypeTreeView;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import model.Classe;
import model.Propriete;
import model.Type;
import utils.MainViewTransformations;
import utils.Undoable;

public class RemoveFolderSchemeCommand implements Command,Undoable{
	
	private TreeItem<TypeController> tree;
	
	private TreeItem<TypeController> oldClassTree;
	
	private LinkedList<Command> listeCommandes;
	private TreeItem<TypeController> parentFolder;
	private int folderPos;
	
	public RemoveFolderSchemeCommand(TreeItem<TypeController> parentTree) {
		this.tree = parentTree;
		listeCommandes = new LinkedList<Command>();
		folderPos = parentTree.getParent().getChildren().indexOf(parentTree);
	}
	
	public boolean containsFolder(TreeItem<TypeController> tree) {
		for(TreeItem<TypeController> child : tree.getChildren()) {
			if(child.getValue().getType().isFolder()) {
				return true;
			}
		}
		return false;
	}
	
	public void RemoveFolderRec(TreeItem<TypeController> parentTree) {
		for(TreeItem<TypeController> child : parentTree.getChildren()) {
			if(child.getValue().getType().isFolder() && containsFolder(child)) {
				RemoveFolderRec(child);
			}
			else if(child.getValue().getType().isFolder() && !containsFolder(child)) {
				for(TreeItem<TypeController> classe : child.getChildren()) {
					Platform.runLater(new Runnable() {	
						@Override
						public void run() {
							RemoveClassFromParentCommand cmd = new RemoveClassFromParentCommand(classe.getValue(), (Classe) classe.getValue().getType(),child);
							listeCommandes.add(cmd);
							cmd.execute();
						}
					});
				}		
			}else if (child.getValue().getType().isClass()) {
				Platform.runLater(new Runnable() {	
					@Override
					public void run() {
						RemoveClassFromParentCommand cmd = new RemoveClassFromParentCommand(child.getValue(), (Classe) child.getValue().getType(),parentTree);
						listeCommandes.add(cmd);
						cmd.execute();
					}
				});
			}
		}
		parentFolder = tree.getParent();
		parentFolder.getChildren().remove(tree);
		parentFolder.getValue().getType().getTypes().remove(tree.getValue().getType());
	}
	
	public void RebuildFolderRec() {
		parentFolder.getChildren().add(folderPos,tree);
		parentFolder.getValue().getType().getTypes().add(folderPos,tree.getValue().getType());
		for (Command cmd : listeCommandes) {
			((Undoable) cmd).undo();
		}
	}
	
	@Override
	public void undo() {
		RebuildFolderRec();
	}

	@Override
	public void redo() {
		listeCommandes.clear();
		execute();
	}

	@Override
	public String getUndoRedoName() {
		return null;
	}

	@Override
	public void execute() {
		RemoveFolderRec(tree);
	}

	@Override
	public boolean canExecute() {
		return false;
	}
}
