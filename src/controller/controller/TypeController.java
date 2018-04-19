/*****************************************************************************
 * TypeController.java
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

package controller.controller;

import controller.typeTreeView.TypeTreeViewController;
import model.Category;
import model.Folder;
import model.Type;

public class TypeController {
	
	private Type type;
	private Type parent;
	
	private RenameClassSchemeController classNameController;
	private ChangeColorClassSchemeController classColorController;
	private AddPropertySchemeController addPropertyController;
	private RemovePropertySchemeController removePropertySchemeController;
	private AddPropertySchemeWithValueController addPropertySchemeWithValueController;
	private RenamePropertyController propertyNameController;
	private ChangePropertyValueController changePropertyValueController;
	private RemoveClassSchemeController removeClassSchemeController;
	private AddClassSchemeController addClassSchemeController;
	private TypeTreeViewController typeTreeViewController;
	
	public TypeController(Type t, Type parent) {
		this.type = t;
		this.parent = parent;
		this.classNameController = new RenameClassSchemeController(t);
		this.classColorController = new ChangeColorClassSchemeController(t);
		this.propertyNameController = new RenamePropertyController(t, parent);
		this.changePropertyValueController = new ChangePropertyValueController(t);
		if(t.isCategory()) {
			System.out.println("C'est en effet une categorie du nom de "+t.getName());
			this.removePropertySchemeController = new RemovePropertySchemeController((Category)t);
			this.addPropertyController = new AddPropertySchemeController((Category)t);
			this.addPropertySchemeWithValueController = new AddPropertySchemeWithValueController((Category)t);
		}
		if(parent!=null) {
			if(parent.isFolder()) {
				this.addClassSchemeController = new AddClassSchemeController((Folder)parent);
				this.removeClassSchemeController = new RemoveClassSchemeController((Folder)parent);
			}
			else if(t.isCategory()) System.out.println("Mais son père n'est pas un dossier ? :"+parent.getName());
		}else {
			if(t.isCategory()) System.out.println("Mais son père est null ???");
		}
	}
	
	public TypeTreeViewController getTypeTreeViewController() {
		return this.typeTreeViewController;
	}
	
	public void setTypeTreeViewController(TypeTreeViewController c) {
		this.typeTreeViewController = c;
	}

	public Type getType() {
		return type;
	}
	
	public Type getParent() {
		return parent;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public RenameClassSchemeController getClassNameController() {
		return classNameController;
	}
	
	public ChangeColorClassSchemeController getClassColorController() {
		return classColorController;
	}
	
	public AddPropertySchemeController getAddPropertySchemeController() {
		return addPropertyController;
	}
	
	public RemovePropertySchemeController getRemovePropertySchemeController() {
		return this.removePropertySchemeController;
	}
	
	public AddPropertySchemeWithValueController GetAddPropertySchemeWithValueController(){
		return this.addPropertySchemeWithValueController;
	}

	public RenamePropertyController getRenamePropertyController() {
		return this.propertyNameController;
	}
	
	public ChangePropertyValueController getChangePropertyValueController() {
		return this.changePropertyValueController;
	}
	
	public RemoveClassSchemeController getRemoveClassSchemeController() {
		return removeClassSchemeController;
	}

	public AddClassSchemeController getAddClassSchemeController() {
		return addClassSchemeController;
	}
}
