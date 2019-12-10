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

import java.util.LinkedList;

import application.Main;
import controller.controller.TypeController;
import javafx.application.Platform;
import javafx.scene.control.TreeItem;
import model.Category;
import model.Folder;
import model.Schema;
import utils.Undoable;

public class RemoveFolderSchemeCommand implements Command,Undoable{
	
	private TreeItem<TypeController> tree;
	
	private TreeItem<TypeController> oldClassTree;
	
	private LinkedList<Command> listeCommandes;
	private TreeItem<TypeController> parentFolder;
	private int folderPos;
	private Main main;
	public RemoveFolderSchemeCommand(TreeItem<TypeController> parentTree, Main main) {
		this.tree = parentTree;
		listeCommandes = new LinkedList<Command>();
		folderPos = parentTree.getParent().getChildren().indexOf(parentTree);
		this.main = main;
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
							RemoveCategoryFromParentCommand cmd = new RemoveCategoryFromParentCommand(
									classe.getValue(), 
									(Category) classe.getValue().getType(),
									child,
									main);
							listeCommandes.add(cmd);
							cmd.execute();
						}
					});
				}		
			}else if (child.getValue().getType().isCategory()) {
				Platform.runLater(new Runnable() {	
					@Override
					public void run() {
						RemoveCategoryFromParentCommand cmd = new RemoveCategoryFromParentCommand(
								child.getValue(), 
								(Category) child.getValue().getType(),
								parentTree,
								main);
						listeCommandes.add(cmd);
						cmd.execute();
					}
				});
			}
		}
		parentFolder = tree.getParent();
		parentFolder.getChildren().remove(tree);
		LinkedList<Folder> folders;
		if(parentFolder.getValue().getType().isSchema())
			folders = ((Schema)parentFolder.getValue().getType()).getFolders();
		else
			folders = ((Folder)parentFolder.getValue().getType()).getFolders();
		folders.remove(tree.getValue().getType());
	}
	
	public void RebuildFolderRec() {
		LinkedList<Folder> folders;
		if(parentFolder.getValue().getType().isSchema()) {
			folders = ((Schema)parentFolder.getValue().getType()).getFolders();
		}
		else {
			folders = ((Folder)parentFolder.getValue().getType()).getFolders();
		}
		parentFolder.getChildren().add(folderPos,tree);
		//folders.add(folderPos,(Folder)tree.getValue().getType());
		for (Command cmd : listeCommandes) {
			((Undoable) cmd).undo();
		}
	}
	
	@Override
	public void undo() {
		RebuildFolderRec();
		main.needToSave();
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
		main.needToSave();
	}

	@Override
	public boolean canExecute() {
		return false;
	}
}
