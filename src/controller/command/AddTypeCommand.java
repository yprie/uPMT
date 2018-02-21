/*****************************************************************************
 * AddTypeCommand.java
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

import java.io.IOException;

import application.Main;
import controller.MomentExpVBox;
import controller.TypeClassRepresentationController;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import model.Classe;
import model.Propriete;
import model.Type;
import utils.MainViewTransformations;
import utils.Undoable;

public class AddTypeCommand implements Command,Undoable{
	
	private FlowPane flowpane;
	private DragEvent event;
	private String typeName ;
	private MomentExpVBox momentExpBorder;
	private Classe type;
	private Main main;
	private Classe dup;
	
	public AddTypeCommand(MomentExpVBox moment, DragEvent event, Main main) {
		this.event = event;
		typeName = event.getDragboard().getRtf();
		this.momentExpBorder = moment;
		this.main = main;

		for(Type t : main.getCurrentProject().getSchemaProjet().getTypes()){
			if(t.getName().equals(typeName)){
				type = (Classe) t;
			}
			else{
				setClassByName(t);
			}
		}
		setClassByName(main.getCurrentProject().getSchemaProjet());
		dup = duplicate(type);
	}
	
	private void setClassByName(Type type){
		for(Type t : type.getTypes()){

			if(t.getName().equals(typeName)){
				this.type = (Classe) t;
			}
			
			for(Type t2: t.getTypes()){
				setClassByName(t2);
			}
		}
	}

	@Override
	public void undo() {
		this.momentExpBorder.getMomentAddTypeController().update(null);
		main.needToSave();
	}

	@Override
	public void redo() {
		execute();
	}

	@Override
	public String getUndoRedoName() {
		return "addType";
	}
	
	private Classe duplicate(Classe c){
		Classe newc = new Classe(c.getName());
		newc.setCouleur(c.getCouleur());
		for(Type t : c.getTypes()){
			newc.addType(new Propriete(t.getName()));
		}
		return newc;
	}

	@Override
	public void execute() {

		this.momentExpBorder.getMomentAddTypeController().update(dup);
		main.needToSave();
	}

	@Override
	public boolean canExecute() {
		return false;
	}

}
