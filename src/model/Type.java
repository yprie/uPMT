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
	
	protected String mDescription;
	protected String mColor = "#000000";
	protected String mName;
	
	/*public boolean equals(Type t) {
		boolean ret = true;
		if(		   !t.getClass().equals(this.getClass())
				|| !t.getName().equals(this.getName())
				|| !t.getColor().equals(this.getColor())
		  ) ret = false;
		return ret;
	}*/
	
	public Type(String mName) {
		super();
		this.mName = mName;
		this.mDescription = "";
	}
	
	public String toString(){
		return this.mName;
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


	public String getColor() {
		return mColor;
	}

	public void setColor(String mColor) {
		this.mColor = mColor;
	}
	
	public String typeToString() {
		if(isFolder()) return "Folder";
		else if(isCategory()) return "Category";
		else if(isProperty()) return "Property";
		else if(isSchema()) return "Schema";
		else return null;
	}
	
    // Functions used to define the layout for the different kind of Types
	public boolean isFolder()
	{
		return this.getClass().equals(Folder.class);
	}
	
	public boolean isCategory()
	{
		return this.getClass().equals(Category.class);
	}
	
	public boolean isProperty()
	{
		return this.getClass().equals(Property.class);
	}
	
	public boolean isSchema()
	{
		return this.getClass().equals(Schema.class);
	}
	
	public void removeChild(Type t) {
		if(this.isSchema()){
			((Schema)this).removeFolder((Folder)t);
		}
		else if(this.isFolder()) {
			if(t.isCategory()) {
				((Folder)this).removeCategory((Category)t);
			}
			else if(t.isFolder()) {
				((Folder)this).removeFolder((Folder)t);
			}
		}
		else if(this.isCategory()) {
			((Category)this).removeProperty((Property)t);
		}
	}
	
	public void addChild(Type t) {
		if(this.isSchema()){
			((Schema)this).addFolder((Folder)t);
		}
		else if(this.isFolder()) {
			if(t.isCategory()) {
				((Folder)this).addCategory((Category)t);
			}
			else if(t.isFolder()) {
				((Folder)this).addFolder((Folder)t);
			}
		}
		else if(this.isCategory()) {
			((Category)this).addProperty((Property)t);
		}
	}
	
	public void addChild(int index, Type t) {
		if(this.isSchema()){
			((Schema)this).addFolder(index, (Folder)t);
		}
		else if(this.isFolder()) {
			if(t.isCategory()) {
				((Folder)this).addCategory(index, (Category)t);
			}
			else if(t.isFolder()) {
				((Folder)this).addFolder(index, (Folder)t);
			}
		}
		else if(this.isCategory()) {
			((Category)this).addProperty(index, (Property)t);
		}
	}
	
	public Type getChild(String name) {
		if(this.isSchema()){
			for(Folder f : ((Schema)this).getFolders()) {
				if(f.getName().equals(name)) return f;
			}
		}
		else if(this.isFolder()) {
			for(Folder f : ((Folder)this).getFolders()) {
				if(f.getName().equals(name)) return f;
			}
			for(Category c : ((Folder)this).getCategories()) {
				if(c.getName().equals(name)) return c;
			}
		}
		else if(this.isCategory()) {
			for(Property p : ((Category)this).getProperties()) {
				if(p.getName().equals(name)) return p;
			}
		}
		return null;
	}
	
}
