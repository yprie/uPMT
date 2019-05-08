/*****************************************************************************
 * ChangePropertyValueController.java
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

package controller.controller;

import java.util.LinkedList;

import controller.TypeCategoryRepresentationController;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Category;
import model.MomentExperience;
import model.Property;
import model.Type;

public class ChangePropertyValueController implements controller.controller.Observable{

	private Type property;
	private LinkedList<Observer> ObsTypesNames;
	
	public ChangePropertyValueController(Type property) {
		this.property = (Type) property;
		ObsTypesNames = new LinkedList<Observer>();
	}

	@Override
	public void update(Object value) {
		((Property) property).setValue((String) value);
		for(Observer obs : ObsTypesNames) {
			obs.updateVue(this, value);
		}
	}

	@Override
	public void addObserver(Observer o) {
		ObsTypesNames.add(o);
	}

	@Override
	public void updateModel(Object value) {
		if(property==null) {
		}
		else {
			this.property = (Property) value;
		}
	}
	
	@Override
	public void RemoveObserver(Observer o) {
		ObsTypesNames.remove(o);
	}
	
	public Property getProperty() {
		return (Property) this.property;
	}
}
