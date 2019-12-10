/*****************************************************************************
 * RemoveClassFromParentCommand.java
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

import application.Main;
import controller.controller.TypeController;
import javafx.scene.control.TreeItem;
import model.Category;
import utils.Undoable;

public class RemoveCategoryFromParentCommand implements Command,Undoable{
	
	private TypeController controller;
	private Category classe;
	private TreeItem<TypeController> tree;
	private Main main;
	private TreeItem<TypeController> oldClassTree;
	
	public RemoveCategoryFromParentCommand(TypeController controller, Category classe, TreeItem<TypeController> parentTree, Main m) {
		this.classe = classe;
		this.controller = controller;
		this.tree = parentTree;
		
		for (TreeItem<TypeController> prop : tree.getChildren() ) {
			if(prop.getValue().getType().getName().equals(classe.getName())) {
				this.oldClassTree = prop;
				break;
			}
		}
		main = m;
	}
	
	@Override
	public void undo() {
		controller.getAddClassSchemeController().update(classe);
		tree.getChildren().add(oldClassTree);
		main.needToSave();
	}

	@Override
	public void redo() {
		execute();
	}

	@Override
	public String getUndoRedoName() {
		return null;
	}

	@Override
	public void execute() {
		controller.getRemoveClassSchemeController().update(classe);
		tree.getChildren().remove(oldClassTree);
		main.needToSave();
	}

	@Override
	public boolean canExecute() {
		return false;
	}

}
