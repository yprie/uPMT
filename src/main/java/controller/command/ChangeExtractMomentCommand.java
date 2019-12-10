/*****************************************************************************
 * AddExtractMomentCommand.java
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

import NewModel.IDescriptemeAdapter;

import java.util.LinkedList;

import application.Main;
import controller.controller.Observable;
import utils.Undoable;

public class ChangeExtractMomentCommand implements Command, Undoable{

	private Observable observable;
	private LinkedList<IDescriptemeAdapter> oldDescriptemes;
	private LinkedList<IDescriptemeAdapter> newDescriptemes;
	private Main main;
	
	public ChangeExtractMomentCommand(Observable observable, LinkedList<IDescriptemeAdapter> oldDescriptemes, LinkedList<IDescriptemeAdapter> newDescriptemes, Main main) {
		this.observable = observable;
		this.oldDescriptemes = oldDescriptemes;
		this.newDescriptemes = newDescriptemes;
		this.main = main;
	}
	
	@Override
	public void undo() {
		observable.update(oldDescriptemes);
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
		observable.update(newDescriptemes);
		main.needToSave();
	}

	@Override
	public boolean canExecute() {
		return false;
	}

}
