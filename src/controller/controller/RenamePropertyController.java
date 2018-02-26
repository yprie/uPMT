/*****************************************************************************
 * RenamePropertyController.java
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

package controller.controller;

import java.util.LinkedList;

import controller.TypeClassRepresentationController;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Classe;
import model.MomentExperience;
import model.Type;

public class RenamePropertyController implements controller.controller.Observable{

	private Type property;
	private LinkedList<Observer> ObsTypesNames;
	private Type parent;
	
	public RenamePropertyController(Type property, Type parent2) {
		this.property = property;
		this.parent = parent2;
		ObsTypesNames = new LinkedList<Observer>();
	}

	@Override
	public void update(Object value) {
		
		boolean hasName = false;
		for(Type t : parent.getTypes()) {
			if(t.getName().equals((String)value)) {
				hasName = true;
				break;
			}
		}
		if(!hasName) {
			property.setName((String) value);
			for(Observer obs : ObsTypesNames) {
				obs.updateVue(this, value);
			}
		}else {
			Platform.runLater(new Runnable() {
				
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Invalid name");
					//alert.setTitle(main._langBundle.getString("invalid_name");
					alert.setHeaderText(null);
					alert.setContentText("Properties from the same class cannot have the same name");
					//alert.setTitle(main._langBundle.getString("properties_name_invalid");
					alert.showAndWait();
				}
			});
			
		}
	}

	@Override
	public void addObserver(Observer o) {
		ObsTypesNames.add(o);
	}

	@Override
	public void updateModel(Object value) {
		this.property = (Type) value;
	}
	
	@Override
	public void RemoveObserver(Observer o) {
		ObsTypesNames.remove(o);
	}
}
