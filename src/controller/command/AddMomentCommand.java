/*****************************************************************************
 * AddMomentCommand.java
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
import controller.MomentExpVBox;
import javafx.scene.control.Control;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import utils.MainViewTransformations;
import utils.Undoable;

public class AddMomentCommand implements Command,Undoable{
	
	private MomentExpVBox momentExpPane;
	private Main main;
	
	public AddMomentCommand(MomentExpVBox mp, Main main){
		this.momentExpPane = mp;
		this.main = main;
	}

	@Override
	public void undo() {
		System.out.println(momentExpPane);
		momentExpPane.hideMoment();
		main.getCurrentDescription().getMoments().remove(momentExpPane.getMoment());
		// if last moment added a new col, delete that col
		if(momentExpPane.getCol() == main.getCurrentDescription().getNumberCols()-2 && (main.getGrid().getColumnConstraints().size() > 1)){
			main.getGrid().getColumnConstraints().remove(main.getGrid().getColumnConstraints().size()-1);
			main.getCurrentDescription().setNumberCols(main.getCurrentDescription().getNumberCols()-1);
		}
		main.needToSave();
	}

	@Override
	public void redo() {
		execute();
	}

	@Override
	public String getUndoRedoName() {
		return "addMoment";
	}

	@Override
	public void execute() {
		momentExpPane.showMoment();
		main.getCurrentDescription().addMomentExp(momentExpPane.getMoment());
		
		if(momentExpPane.getCol() == main.getCurrentDescription().getNumberCols()-1){
			MomentExpVBox mb = new MomentExpVBox(main, true);
			MainViewTransformations.addMomentExpBorderPaneListener(mb, main);
			ColumnConstraints c = new ColumnConstraints();
			c.setMinWidth(180);
			c.setPrefWidth(Control.USE_COMPUTED_SIZE);
			c.setMaxWidth(Control.USE_COMPUTED_SIZE);
			// add a new col and the borderPane
			main.getGrid().getColumnConstraints().add(c);
			main.getGrid().add(mb,main.getCurrentDescription().getNumberCols(), 0);
			// increase the number of col by one
			main.getCurrentDescription().setNumberCols(main.getCurrentDescription().getNumberCols()+1);
		}
		main.needToSave();
	}

	
	@Override
	public boolean canExecute() {
		return false;
	}

}
