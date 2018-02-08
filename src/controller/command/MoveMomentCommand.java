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
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import model.DescriptionEntretien;
import model.MomentExperience;
import model.Propriete;
import utils.MainViewTransformations;
import utils.Undoable;

public class MoveMomentCommand implements Command,Undoable{
	
	private MomentExperience moment;
	private int toCol;
	private Main main;
	private DescriptionEntretien dataBefore;
	private DescriptionEntretien dataAfter;
	private int indexInterview;
	
	public MoveMomentCommand(MomentExperience moment, int toCol, Main main){
		this.moment = moment;
		this.main = main;
		this.toCol = toCol;
	}
	

	@Override
	public void undo() {
		//Update Interview in the project
		main.getCurrentProject().removeEntretiens(indexInterview);
		main.getCurrentProject().addEntretiens(indexInterview, (DescriptionEntretien)this.dataBefore.clone());
		
		//Edit Current Interview
		main.setCurrentDescription((DescriptionEntretien)this.dataBefore.clone());
		MainViewTransformations.updateGrid(main);
		main.needToSave();
	}

	@Override
	public void redo() {
		//Update Interview in the project
		main.getCurrentProject().removeEntretiens(indexInterview);
		main.getCurrentProject().addEntretiens(indexInterview, (DescriptionEntretien)this.dataAfter.clone());
		
		//Edit Current Interview
		main.setCurrentDescription((DescriptionEntretien)this.dataAfter.clone());
		MainViewTransformations.updateGrid(main);
		main.needToSave();
	}

	@Override
	public String getUndoRedoName() {
		return "moveMoment";
	}

	@Override
	public void execute() {
		try {
			dataBefore = (DescriptionEntretien)main.getCurrentDescription().clone();
			indexInterview = new Integer(MainViewTransformations.getInterviewIndex(main.getCurrentDescription(), main));

			if(!moment.hasParent() && moment.getGridCol()<toCol) toCol--;
			MainViewTransformations.deleteMoment(moment, main);
			
			main.getCurrentDescription().addMoment(toCol, moment);
			
			MainViewTransformations.updateGrid(main);
		    main.needToSave();
		    dataAfter  = (DescriptionEntretien)main.getCurrentDescription().clone();
		}catch(Exception e) {e.printStackTrace();}
	}
	
	@Override
	public boolean canExecute() {
		return false;
	}

}
