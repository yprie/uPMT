/*****************************************************************************
 * MomentRemoveTypeController.java
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

import controller.TypeClassRepresentationController;
import model.MomentExperience;
import model.Type;

public class MomentRemoveTypeController implements controller.controller.Observable{

	private MomentExperience momentExp;
	private LinkedList<Observer> ObsMomentNames;
	
	public MomentRemoveTypeController(MomentExperience moment) {
		this.momentExp = moment;
		ObsMomentNames = new LinkedList<Observer>();
	}

	@Override
	public void update(Object value) {
		if(momentExp.getType().contains(((TypeClassRepresentationController) value).getClasse())) {
			momentExp.getType().remove(((TypeClassRepresentationController) value).getClasse());
		}else {
			momentExp.getType().add(((TypeClassRepresentationController) value).getClasse());
		}
		
		for(Observer obs : ObsMomentNames) {
			obs.updateVue(this, value);
		}
	}

	@Override
	public void addObserver(Observer o) {
		ObsMomentNames.add(o);
	}

	@Override
	public void updateModel(Object value) {
		this.momentExp = (MomentExperience) value;
	}
	
	@Override
	public void RemoveObserver(Observer o) {
		ObsMomentNames.remove(o);
	}
}
