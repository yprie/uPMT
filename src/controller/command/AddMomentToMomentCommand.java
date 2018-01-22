/*****************************************************************************
 * AddMomentToMomentComnand.java
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
import javafx.scene.layout.ColumnConstraints;
import utils.MainViewTransformations;
import utils.Undoable;

public class AddMomentToMomentCommand implements Command, Undoable {
	
	private Main main;
	private MomentExpVBox momentPane;
	private MomentExpVBox newMoment;
	
	public AddMomentToMomentCommand(MomentExpVBox moment, Main main){
		this.main = main;
		this.momentPane = moment;
		this.newMoment = new MomentExpVBox(main, true);
	}

	@Override
	public void undo() {
		if(this.momentPane.getSousMomentPane().getColumnConstraints().size() > 0){
			this.momentPane.getSousMomentPane().getChildren().remove(momentPane.getSousMomentPane().getColumnConstraints().size()-1);
			this.momentPane.getSousMomentPane().getColumnConstraints().remove(momentPane.getSousMomentPane().getColumnConstraints().size()-1);
			momentPane.getMoment().getSousMoments().remove(newMoment.getMoment());
			main.needToSave();
		}
	}

	@Override
	public void redo() {
		ColumnConstraints c = new ColumnConstraints();
		this.momentPane.getSousMomentPane().getColumnConstraints().add(c);
		this.momentPane.getSousMomentPane().add(newMoment,momentPane.getSousMomentPane().getColumnConstraints().size()-1,0);
		momentPane.getMoment().getSousMoments().add(newMoment.getMoment());
		main.needToSave();

	}

	@Override
	public String getUndoRedoName() {
		return "ajout Moment dans Moment";
	}

	@Override
	public void execute() {		
		if(this.momentPane.getChildren().size() == 1){
			this.momentPane.getChildren().add(this.momentPane.getSousMomentPane());
		}
		ColumnConstraints c = new ColumnConstraints();
		this.momentPane.getSousMomentPane().getColumnConstraints().add(c);
		this.momentPane.getSousMomentPane().add(newMoment,momentPane.getSousMomentPane().getColumnConstraints().size()-1,0);
		newMoment.showMoment();
		MainViewTransformations.addMomentExpBorderPaneListener(newMoment, main);
		momentPane.getMoment().getSousMoments().add(newMoment.getMoment());
		main.needToSave();
	}
		

	@Override
	public boolean canExecute() {
		return false;
	}

}
