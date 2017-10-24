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
	
	public TypeController(Type t, Type parent) {
		this.type = t;
		this.parent = parent;
		this.classNameController = new RenameClassSchemeController(t);
		this.classColorController = new ChangeColorClassSchemeController(t);
		this.addPropertyController = new AddPropertySchemeController(t);
		this.removePropertySchemeController = new RemovePropertySchemeController(t);
		this.addPropertySchemeWithValueController = new AddPropertySchemeWithValueController(t);
		this.propertyNameController = new RenamePropertyController(t, parent);
		this.changePropertyValueController = new ChangePropertyValueController(t);
		this.removeClassSchemeController = new RemoveClassSchemeController(parent);
		this.addClassSchemeController = new AddClassSchemeController(parent);
	}

	public Type getType() {
		return type;
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
