/*****************************************************************************
 * ChangeColorClassCommand.java
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
import application.Main;
import controller.controller.Observable;
import utils.Undoable;

public class ChangeColorCategoryCommand implements Command,Undoable{

	private Observable observable;
	private String oldColor;
	private String newColor;
	private Main main;
	
	public ChangeColorCategoryCommand(Observable observable, String oldColor,String newColor, Main m) {
		this.observable = observable;
		this.oldColor = oldColor;
		this.newColor = newColor;
		main = m;
	}
	
	@Override
	public void undo() {
		observable.update(oldColor);
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
		observable.update(newColor);
		main.needToSave();
	}

	@Override
	public boolean canExecute() {
		return false;
	}

}
