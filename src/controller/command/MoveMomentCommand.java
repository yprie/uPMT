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
import model.MomentExperience;
import model.Propriete;
import utils.MainViewTransformations;
import utils.Undoable;

public class MoveMomentCommand implements Command,Undoable{
	
	private Main main;
	private int toCol;
	private int initCol;
	private MomentExpVBox initMoment;
	private MomentExpVBox initParent;
	private RemoveMomentCommand rm;
	private AddMomentCommand add;
	
	public MoveMomentCommand(MomentExperience initMExp, int toCol, Main main){
		this(MainViewTransformations.getMomentVBoxByMoment(initMExp, main), toCol, main);
	}

	public MoveMomentCommand(MomentExpVBox initM, int toCol, Main main){
		this.main = main;
		this.toCol = toCol;
		this.initMoment = initM;
		initCol = initMoment.getCol();
		this.initParent = initM.getVBoxParent();
	}
	
	@Override
	public void undo() {
		this.initMoment = add.getMomentAfterChanges();
		System.out.println("--------------\n--------------\n--------------\n");
		System.out.println("Actuellement nous somme en face de "+initMoment.getMoment().getNom());
		initMoment.setVBoxParent(initParent);
		if(initParent!=null) {
			System.out.println("Son père actuel "+initParent.getMoment().getNom()+" = "+initMoment.getVBoxParent().getMoment().getNom());
		}
		else {
			System.out.println("Il n'a pas de parent");
		}
		toCol = initMoment.getCol();
		int tmp = initCol;
		initCol = toCol;
		toCol = tmp;
		int tmpInit=0;
		if(initCol>toCol) {
			initCol++;
			toCol--;
		}
		else toCol++;
		System.out.println("Veut aller de:"+initCol);
		System.out.println("Vers:"+toCol);
		System.out.println("getCol:"+initMoment.getCol());
		execute();
		main.needToSave();
	}

	@Override
	public void redo() {
		this.initMoment = add.getMomentAfterChanges();
		System.out.println("Actuellement nous somme en face de "+initMoment.getMoment().getNom());
		initMoment.setVBoxParent(initParent);
		if(initParent!=null) {
			System.out.println("Son père actuel "+initParent.getMoment().getNom()+" = "+initMoment.getVBoxParent().getMoment().getNom());
		}
		else {
			System.out.println("Il n'a pas de parent");
		}
		System.out.println("Sa colonne actuelle "+initCol);
		System.out.println("Sa destination :"+toCol);
		
		execute();
		main.needToSave();
	}

	@Override
	public String getUndoRedoName() {
		return "moveMoment";
	}

	@Override
	public void execute() {
		rm = new RemoveMomentCommand(initMoment,main);
		rm.execute();
		if(initMoment.hasParent())
			initMoment.setCol(toCol+1);
		else if(toCol<initCol)
			initMoment.setCol(toCol+1);
		else
			initMoment.setCol(toCol-1);
		add = new AddMomentCommand(initMoment, main);
		add.execute();
		MainViewTransformations.loadGridData(main.getGrid(), main, main.getCurrentDescription());
		main.needToSave();
		//System.out.println(MainViewTransformations.allMomentsToString(main));
	}

	
	@Override
	public boolean canExecute() {
		return false;
	}

}
