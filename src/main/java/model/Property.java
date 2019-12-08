/*****************************************************************************
 * Propriete.java
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

package model;

import NewModel.IDescriptemeAdapter;
import SchemaTree.Cell.Models.IPropertyAdapter;
import SchemaTree.Cell.Models.ITypeAdapter;
import SchemaTree.Cell.SchemaTreePluggable;
import SchemaTree.Cell.Visitors.SchemaTreePluggableVisitor;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.DataFormat;

import java.util.LinkedList;

public class Property extends Type implements IPropertyAdapter  {
	
	private String mValue;
	private LinkedList<IDescriptemeAdapter> mDescriptemes;

	
	public Property(String nom) {
		super(nom);
		mDescriptemes = new LinkedList<IDescriptemeAdapter>();
	}
	
	public void setValue(String valeur){
		this.mValue = valeur;
	}
	
	public String getValue(){
		return this.mValue;
	}
	
	public LinkedList<IDescriptemeAdapter> getDescriptemes(){
		return this.mDescriptemes;
	}
	
	public void setDescriptemes(LinkedList<IDescriptemeAdapter> mD){
		if(mD==null) mD = new LinkedList<IDescriptemeAdapter>();
		this.mDescriptemes = mD;
	}

	public void removeDescripteme(IDescriptemeAdapter d) {
		if(this.mDescriptemes.contains(d)) {
			this.mDescriptemes.remove(d);
		}
	}

	public void addDescripteme(IDescriptemeAdapter d) {
		this.mDescriptemes.add(d);
	}





	@Override
	public String toString(){
		return this.mName + " : " + this.mValue;
	}

	@Override
	public void removeChild(ITypeAdapter t) {
		super.removeChild(t);
	}

	@Override
	public void addChild(ITypeAdapter t) {
		super.addChild(t);
	}

	@Override
	public void addChild(int index, ITypeAdapter t) {
		super.addChild(index, t);
	}

	@Override
	public boolean equals(Object obj) {
		try {
			Property other = (Property) obj;
			if(other.getName().equals(this.getName())) return true;
			else return false;
		}catch(Exception e) {return false;}
		
	}

	@Override
	public Property clone(){
		Property newp = new Property(this.getName());
		newp.setDescription(mDescription);
		newp.setColor(mColor);
		newp.setValue(mValue);
		if(this.mDescriptemes!=null) {
			for(IDescriptemeAdapter d : this.mDescriptemes) {
				newp.addDescripteme(new Descripteme(d.getTexte()));
			}
		}
		else newp.mDescriptemes = new LinkedList<IDescriptemeAdapter>();
		return newp;
	}
	
	/**
	 * to string Descripteme in a property
	 * @return: list of descripteme regarding a property
	 */
	public String toStringDescripteme(){
		String res="";
		for(IDescriptemeAdapter d : mDescriptemes) {
			res+=d.toString() + " \\\\\\ ";
		}
		if(res.length()>0) {
			res = res.substring(0, res.length()-4);
		} 
		return res;
	}



	//Temporary methods for the old model to fit the I<...>Adapter interface

	@Override
	public void accept(SchemaTreePluggableVisitor visitor) {

	}

	@Override
	public boolean mustBeRenamed() {
		return false;
	}

	@Override
	public void setMustBeRenamed(boolean YoN) {

	}

	@Override
	public boolean isExpanded() {
		return false;
	}

	@Override
	public BooleanProperty expandedProperty() {
		return null;
	}

	@Override
	public boolean canContain(SchemaTreePluggable item) {
		return false;
	}

	@Override
	public void addChild(SchemaTreePluggable item) {

	}

	@Override
	public void removeChild(SchemaTreePluggable item) {

	}


	@Override
	public StringProperty nameProperty() {
		return null;
	}

	@Override
	public String getIconPath() {
		return null;
	}

	@Override
	public DataFormat getDataFormat() {
		return null;
	}

	@Override
	public boolean isDraggable() {
		return false;
	}
}
