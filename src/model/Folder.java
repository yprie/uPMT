/*****************************************************************************
 * Dossier.java
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
import java.util.LinkedList;

public class Folder extends Type implements Serializable, Cloneable{

	protected LinkedList<Folder> mFolders;
	protected LinkedList<Category> mCategories;
	
	public Folder(String nom) {
		super(nom);
		mFolders  = new LinkedList<Folder>();
		mCategories  = new LinkedList<Category>();
	}
	
	public Folder clone() {
		Folder ret = new Folder(mName);
		ret.setDescription(mDescription);
		ret.setColor(mColor);
		for(Folder folder: mFolders) {
			ret.addFolder(folder.clone());
		}
		for(Category category: mCategories) {
			ret.addCategory(category.clone());
		}
		return ret;
	}
	
	@Override
	public boolean equals(Object c) {
		if(c.getClass()!=this.getClass()) {
			return false;
		}
		else {
			boolean ret = true;
			Folder f = (Folder)c;
			if(!f.mName.equals(mName)) ret=false;
			return ret;
		}
	}
	
	public void addFolder(Folder f) {
		mFolders.add(f);
	}
	
	public void removeFolder(Folder f) {
		mFolders.remove(f);
	}
	
	public LinkedList<Folder> getFolders(){
		return this.mFolders;
	}
	
	public LinkedList<Category> getCategories(){
		return this.mCategories;
	}
	
	public void addCategory(Category c) {
		mCategories.add(c);
	}
	
	public void removeCategory(Category c) {
		mCategories.remove(c);
	}

}
