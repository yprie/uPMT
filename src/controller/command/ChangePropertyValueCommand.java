/*****************************************************************************
 * ChangePropertyValueCommand.java
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

import controller.controller.Observer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;
import model.Type;
import controller.TypePropertyRepresentation;
import controller.controller.Observable;
import utils.Undoable;

public class ChangePropertyValueCommand implements Command,Undoable{

	private String oldName;
	private String newName;
	private TypePropertyRepresentation property;
	
	public ChangePropertyValueCommand(TypePropertyRepresentation p,String oldName,String newName) {
		this.oldName = oldName;
		this.newName = newName;
		this.property = p;
	}
	
	@Override
	public void undo() {
		property.getPropertyController().getChangePropertyValueController().update(oldName);
		
		if(property.getMain().getMainViewController().isInspectorOpen()) {
			property.getMain().getMainViewController().renderInspector();
		}
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
		property.getPropertyController().getChangePropertyValueController().update(newName);
		
		if(property.getMain().getMainViewController().isInspectorOpen()) {
			property.getMain().getMainViewController().renderInspector();
		}	
	}

	@Override
	public boolean canExecute() {
		return false;
	}

}
