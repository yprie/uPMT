/*****************************************************************************
 * AddMomentCommand.java
 *****************************************************************************
 * Copyright � 2017 uPMT
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

import java.util.LinkedList;
import java.util.Random;

import application.Main;
import controller.MomentExpVBox;
import javafx.scene.control.Control;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import model.DescriptionInterview;
import model.MomentExperience;
import model.Property;
import utils.MainViewTransformations;
import utils.Undoable;

public class AddMomentCommand implements Command,Undoable{
	
	private MomentExperience moment;
	private MomentExperience parentMoment=null;
	private Main main;
	private DescriptionInterview dataBefore;
	private DescriptionInterview dataAfter;
	private int indexInterview;
	private int index;
	
	public AddMomentCommand(int index, MomentExperience moment, Main main){
		this.moment = moment;
		this.main = main;
		this.index = index;
		
	}
	
	public AddMomentCommand(int index, MomentExperience moment, MomentExperience parentMoment, Main main){
		this(index, moment, main);
		this.parentMoment = parentMoment;
	}

	@Override
	public void undo() {
		//Update Interview in the project
		main.getCurrentProject().removeEntretiens(indexInterview);
		main.getCurrentProject().addEntretiens(indexInterview, (DescriptionInterview)this.dataBefore.clone());
		
		//Edit Current Interview
		main.setCurrentDescription((DescriptionInterview)this.dataBefore.clone());
		MainViewTransformations.updateGrid(main);
		main.needToSave();
	}

	@Override
	public void redo() {
		//Update Interview in the project
		main.getCurrentProject().removeEntretiens(indexInterview);
		main.getCurrentProject().addEntretiens(indexInterview, (DescriptionInterview)this.dataAfter.clone());
		
		//Edit Current Interview
		main.setCurrentDescription((DescriptionInterview)this.dataAfter.clone());
		MainViewTransformations.updateGrid(main);
		main.needToSave();
	}

	@Override
	public String getUndoRedoName() {
		return "ajoutMoment";
	}

	@Override
	public void execute() {
		try {
			this.dataBefore = (DescriptionInterview)main.getCurrentDescription().clone();
			indexInterview = new Integer(MainViewTransformations.getInterviewIndex(main.getCurrentDescription(), main));
			if(parentMoment!=null) {
				parentMoment.addSubMoment(index, moment);
			}
			else
				main.getCurrentDescription().addMoment(index, moment);
			MainViewTransformations.updateGrid(main);
		    main.needToSave();
		    this.dataAfter  = (DescriptionInterview)main.getCurrentDescription().clone();
		}catch(Exception e) {e.printStackTrace();}
	}
	
	@Override
	public boolean canExecute() {
		return false;
	}

}
