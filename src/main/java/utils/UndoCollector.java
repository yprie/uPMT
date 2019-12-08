/*****************************************************************************
 * UndoCollector.java
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

package utils;

import java.util.ArrayDeque;
import java.util.Deque;

public final class UndoCollector {
	/** The default undo/redo collector. */
	public static final UndoCollector INSTANCE = new UndoCollector();

	/** Contains the undoable objects. */
	private final Deque<Undoable> undo;

	/** Contains the redoable objects. */
	private final Deque<Undoable> redo;

	/** The maximal number of undo. */
	private int sizeMax;

	private UndoCollector() {
		super();
		undo 	 = new ArrayDeque<>();
		redo 	 = new ArrayDeque<>();
		sizeMax  = 30;
	}

	/**
	 * Adds an undoable object to the collector.
	 * @param undoable The undoable object to add.
	 */
	public void add(final Undoable undoable) {
		if(undoable!=null && sizeMax>0) {
			if(undo.size()==sizeMax) {
				undo.removeLast();
			}

			undo.push(undoable);
			redo.clear(); /* The redoable objects must be removed. */
		}
	}

	/**
	 * Undoes the last undoable object.
	 */
	public void undo() {
		if(!undo.isEmpty()) {
			final Undoable undoable = undo.pop();
			undoable.undo();
			redo.push(undoable);
		}
	}

	/**
	 * Redoes the last undoable object.
	 */
	public void redo() {
		if(!redo.isEmpty()) {
			final Undoable undoable = redo.pop();
			undoable.redo();
			undo.push(undoable);
		}
	}

	/**
	 * @return The last undoable object name or "".
	 */
	public String getLastUndoMessage() {
		return undo.isEmpty() ? "" : undo.peek().getUndoRedoName();
	}

	/**
	 * @return The last redoable object name or "".
	 */
	public String getLastRedoMessage() {
		return redo.isEmpty() ? "" : redo.peek().getUndoRedoName();
	}

	/**
	 * @return The last undoable object or null.
	 */
	public Undoable getLastUndo() {
		return undo.isEmpty() ? null : undo.peek();
	}

	/**
	 * @return The last redoable object or null.
	 */
	public Undoable getLastRedo() {
		return redo.isEmpty() ? null : redo.peek();
	}

	/**
	 * @param max The max number of saved undoable objects. Must be great than 0.
	 */
	public void setSizeMax(final int max) {
		if(max>=0) {
			for(int i=0, nb=undo.size()-max; i<nb; i++) {
				undo.removeLast();
			}
			this.sizeMax = max;
		}
	}
	
	// For debug purposes
	public void showUndoRedoStack(){
		/*//System.out.println("undo Stack" + this.redo);
	//System.out.println("redo Stack" + this.undo);*/
	}
}
