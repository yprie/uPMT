/*****************************************************************************
 * AddTypeCommand.java
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

import SchemaTree.Cell.Models.ICategoryAdapter;
import application.Main;
import controller.MomentExpVBox;
import controller.typeTreeView.TypeTreeView;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.FlowPane;
import model.Folder;
import utils.Serializer;
import utils.Undoable;

public class AddTypeCommand implements Command,Undoable{
	
	private FlowPane flowpane;
	private DragEvent event;
	private String typeName ;
	private MomentExpVBox momentExpBorder;
	private ICategoryAdapter type;
	private Main main;
	private ICategoryAdapter dup;
	
	public AddTypeCommand(MomentExpVBox moment, DragEvent event, Main main) {
		try {
			this.event = event;
			typeName = event.getDragboard().getRtf();
			this.momentExpBorder = moment;
			this.main = main;
			type = (ICategoryAdapter) Serializer.deserialize((byte[]) event.getDragboard().getContent(TypeTreeView.TYPE));
			dup = type.clone();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	private void setClassByName(Folder f){
		for(ICategoryAdapter t : f.getCategories()){
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
