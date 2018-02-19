
package controller.controller;

import java.util.LinkedList;

import model.Propriete;

public class PropertyExtractController implements controller.controller.Observable{

	private Propriete mPropriete;
	private LinkedList<Observer> ObsMomentNames;
	
	public PropertyExtractController(Propriete moment) {
		this.mPropriete = moment;
		ObsMomentNames = new LinkedList<Observer>();
	}

	@Override
	public void update(Object value) {
		//mPropriete.setMorceauDescripteme(((String) value));
		//System.out.println("UPDATE");
		for(Observer obs : ObsMomentNames) {
			obs.updateVue(this, value);
		}
	}

	@Override
	public void addObserver(Observer o) {
		ObsMomentNames.add(o);
	}
	
	public void setPropriete(Propriete p) {
		this.mPropriete = p;
	}

	@Override
	public void updateModel(Object value) {
		this.mPropriete = (Propriete) value;
	}
	
	@Override
	public void RemoveObserver(Observer o) {
		ObsMomentNames.remove(o);
	}
	
	public Propriete getProperty() {
		return mPropriete;
	}
}
