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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import model.MomentExperience;
import model.Propriete;
import utils.MainViewTransformations;
import utils.Undoable;

public class AddMomentCommand implements Command,Undoable{
	
	private MomentExpVBox moment;
	private Main main;
	private MomentExpVBox momentAfterChange;
	
	public AddMomentCommand(MomentExpVBox mp, Main main){
		this.moment = mp;
		this.main = main;
		momentAfterChange = moment;
	}

	@Override
	public void undo() {
		RemoveMomentCommand cmd = new RemoveMomentCommand(moment, main);
		cmd.execute();
		momentAfterChange=cmd.getMomentAfterChanges();
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
		System.out.println("On veut ajouter " + moment.getMoment().getNom() + " à l'index " + moment.getCol());
	    main.getCurrentDescription().setNumberCols(main.getCurrentDescription().getNumberCols() + 2);
	    
	    int pos = moment.getCol() / 2;
	    
	    main.getCurrentDescription().addMomentExpAt(pos, moment.getMoment());
	    

	    for (int i = 0; i < main.getGrid().getChildren().size(); i++) {
	      try {
	        MomentExpVBox m = (MomentExpVBox)main.getGrid().getChildren().get(i);
	        String print = "On bouge " + m.getMoment().getNom() + " de " + m.getCol();
	        if (m.getCol() >= moment.getCol())
	          m.setCol(m.getCol() + 2);
	        print = print + " à " + m.getCol();
	      }
	      catch (ClassCastException localClassCastException) {}
	    }
	    


	    moment.setVBoxParent(null);
	    momentAfterChange = moment;
	    MainViewTransformations.loadGridData(main.getGrid(), main, main.getCurrentDescription());
	    main.needToSave();
	}

	public MomentExpVBox getMomentAfterChanges() {
		return momentAfterChange;
	}
	
	@Override
	public boolean canExecute() {
		return false;
	}

}
