
package controller.controller;

import java.util.LinkedList;

import NewModel.IDescriptemeAdapter;
import SchemaTree.Cell.Models.IPropertyAdapter;
import model.Property;

public class PropertyExtractController implements controller.controller.Observable{

	private IPropertyAdapter mPropriete;
	private LinkedList<Observer> ObsMomentNames;
	
	public PropertyExtractController(IPropertyAdapter moment) {
		this.mPropriete = moment;
		ObsMomentNames = new LinkedList<Observer>();
	}

	@Override
	public void update(Object value) {
		mPropriete.setDescriptemes(((LinkedList<IDescriptemeAdapter>) value));
		for(Observer obs : ObsMomentNames) {
			obs.updateVue(this, value);
		}
	}

	@Override
	public void addObserver(Observer o) {
		ObsMomentNames.add(o);
	}
	
	public void setPropriete(Property p) {
		this.mPropriete = p;
	}

	@Override
	public void updateModel(Object value) {
		this.mPropriete = (Property) value;
	}
	
	@Override
	public void RemoveObserver(Observer o) {
		ObsMomentNames.remove(o);
	}
	
	public IPropertyAdapter getProperty() {
		return mPropriete;
	}
}
