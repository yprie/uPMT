/*****************************************************************************
 * Type.java
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import controller.typeTreeView.TypeTreeViewController;

public abstract class Type implements Serializable, Cloneable{
	
	protected LinkedList<Type> mTypes;
	protected String mDescription;
	protected String mColor = "#000000";
	protected String mName;
	
	
	public Type(String mName) {
		super();
		this.mName = mName;
		this.mTypes = new LinkedList<Type>();
		this.mDescription = "";
	}
	
	public void addType(Type s){
		this.mTypes.add(s);
	}
	
	public LinkedList<Type> getTypes(){
		return this.mTypes;
	}
	
	public String toString(){
		return this.mName + this.mTypes;
	}
	
	public String getName(){
		return this.mName;
	}
	
	public void setName(String name){
		this.mName = name;
	}
	
	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String mDescription) {
		this.mDescription = mDescription;
	}

	@Override
	public boolean equals(Object o){
		Type tmp = (Type)o;
		if(tmp != null){
			return tmp.mName.equals(this.mName) &&
					tmp.mDescription.equals(this.mDescription) &&
					tmp.getTypes().equals(this.getTypes());
		}else{
			return false;
		}		
	}

	public String getColor() {
		return mColor;
	}

	public void setColor(String mColor) {
		this.mColor = mColor;
	}
	
    // Functions used to define the layout for the different kind of Types
	public boolean isFolder()
	{
		return this.getClass().equals(new Folder("").getClass());
	}
	
	public boolean isCategory()
	{
		return this.getClass().equals(new Category("").getClass());
	}
	
	public boolean isProperty()
	{
		return this.getClass().equals(new Property("").getClass());
	}
	
	
	
}
