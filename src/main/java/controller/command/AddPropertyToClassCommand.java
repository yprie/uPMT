/*****************************************************************************
 * AddPropertyToClassCommand.java
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
import model.Property;
import utils.MainViewTransformations;
import utils.Undoable;

public class AddPropertyToClassCommand implements Command,Undoable{
	
	private TypeController controller;
	private Property newp;
	private TreeItem<TypeController> tree;
	private Main main;
	
	public AddPropertyToClassCommand(TypeController controller, Property newp, TreeItem<TypeController> treeItem, Main m) {
		this.newp = newp;
		this.controller = controller;
		this.tree = treeItem;
		main = m;
	}
	
	@Override
	public void undo() {
		// remove Property from treeView
		for (TreeItem<TypeController> prop : tree.getChildren() ) {
			if(prop.getValue().getType().getName().equals(newp.getName())) {
				tree.getChildren().remove(prop);
				break;
			}
		}
		controller.getRemovePropertySchemeController().update(newp);
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
		// adding the new property to the scheme
		TreeItem<TypeController> newType = new TreeItem<TypeController>();
		TypeController tc = new TypeController(newp, controller.getType());
		
        newType.setValue(tc);

        tree.getChildren().add(newType);
        

        tree.setExpanded(true);	
        
        
        controller.getAddPropertySchemeController().update(newp);
        
        MainViewTransformations.updateGrid(main);
        main.needToSave();
	}

	@Override
	public boolean canExecute() {
		return false;
	}

}
