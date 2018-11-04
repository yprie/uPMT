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
import controller.TypeCategoryRepresentationController;
import controller.typeTreeView.TypeTreeView;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import model.Category;
import model.Folder;
import model.Property;
import model.Type;
import utils.MainViewTransformations;
import utils.Serializer;
import utils.Undoable;

public class AddTypeCommand implements Command,Undoable{
	
	private FlowPane flowpane;
	private DragEvent event;
	private String typeName ;
	private MomentExpVBox momentExpBorder;
	private Category type;
	private Main main;
	private Category dup;
	
	public AddTypeCommand(MomentExpVBox moment, DragEvent event, Main main) {
		try {
			this.event = event;
			typeName = event.getDragboard().getRtf();
			this.momentExpBorder = moment;
			this.main = main;
			//type = MainViewTransformations.getCategory(typeName, main.getCurrentProject().getSchema());
			type = (Category) Serializer.deserialize((byte[]) event.getDragboard().getContent(TypeTreeView.TYPE));
			dup = type.clone();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	private void setClassByName(Folder f){
		for(Category t : f.getCategories()){
			if(t.getName().equals(typeName)){
				this.type = t;
			}
			/*for(Type t2: t.getTypes()){
				setClassByName(t2);
			}*/
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
	

	@Override
	public void execute() {
		System.out.println(momentExpBorder.getMoment().getName());
		try {
		this.momentExpBorder.getMomentAddTypeController().update(dup);
		}catch(Exception e) {
			e.printStackTrace();
		}
		main.needToSave();
	}

	@Override
	public boolean canExecute() {
		return false;
	}

}
