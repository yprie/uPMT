/*****************************************************************************
 * Classe.java
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

import SchemaTree.Cell.Models.ICategoryAdapter;
import SchemaTree.Cell.Models.IPropertyAdapter;
import SchemaTree.Cell.SchemaTreePluggable;
import SchemaTree.Cell.Visitors.SchemaTreePluggableVisitor;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.DataFormat;

import java.util.LinkedList;

public class Category extends Type implements ICategoryAdapter {

	protected LinkedList<IPropertyAdapter> mProperties;
	
	public Category(String nom) {
		super(nom);
		mProperties  = new LinkedList<IPropertyAdapter>();
	}

	@Override
	public ICategoryAdapter clone(){
		Category newc = new Category(this.getName());
		newc.setColor(this.getColor());
		for(IPropertyAdapter t : this.getProperties()){
			newc.addProperty(t.clone());
		}
		return newc;
	}
	
	public void addProperty(IPropertyAdapter s){
		this.mProperties.add(s);
	}
	
	public void addProperty(int index, IPropertyAdapter s){
		this.mProperties.add(index, s);
	}
	
	public void removeProperty(IPropertyAdapter s){
		this.mProperties.remove(s);
	}
	
	public LinkedList<IPropertyAdapter> getProperties(){
		return this.mProperties;
	}
	
	@Override
	public boolean equals(Object o){
		if(!o.getClass().equals(Category.class)) {
			return false;
		}
		Category tmp = (Category)o;
		if(tmp != null){
			return tmp.mName.equals(this.mName);
		}else{
			return false;
		}		
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
		return true;
	}
}