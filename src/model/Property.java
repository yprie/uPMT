/*****************************************************************************
 * Propriete.java
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

package model;

import java.io.Serializable;
import java.util.LinkedList;

public class Property extends Type implements Serializable, Cloneable{
	
	private String mValue;
	private LinkedList<Descripteme> mDescriptemes;

	
	public Property(String nom) {
		super(nom);
		mDescriptemes = new LinkedList<Descripteme>();
	}
	
	public void setValue(String valeur){
		this.mValue = valeur;
	}
	
	public String getValue(){
		return this.mValue;
	}
	
	public LinkedList<Descripteme> getDescriptemes(){
		return this.mDescriptemes;
	}
	
	public void setDescriptemes(LinkedList<Descripteme> mD){
		if(mD==null) mD = new LinkedList<Descripteme>();
		this.mDescriptemes = mD;
	}
	
	public void removeDescripteme(Descripteme d) {
		if(this.mDescriptemes.contains(d)) {
			this.mDescriptemes.remove(d);
		}
	}
	
	public void addDescripteme(Descripteme d) {
		this.mDescriptemes.add(d);
	}
	
	@Override
	public String toString(){
		return this.mName + " : " + this.mValue;
	}

	@Override
	public boolean equals(Object obj) {
		try {
			Property other = (Property) obj;
			if(other.getName().equals(this.getName())) return true;
			else return false;
		}catch(Exception e) {return false;}
		
	}
	
	public Property clone(){
		Property newp = new Property(this.getName());
		newp.setDescription(mDescription);
		newp.setColor(mColor);
		newp.setValue(mValue);
		if(this.mDescriptemes!=null) {
			for(Descripteme d : this.mDescriptemes) {
				newp.addDescripteme(new Descripteme(d.getTexte()));
			}
		}
		else newp.mDescriptemes = new LinkedList<Descripteme>(); 
		return newp;
	}
	
	/**
	 * to string Descripteme in a property
	 * @return: list of descripteme regarding a property
	 */
	public String toStringDescripteme(){
		String res="";
		for(Descripteme d : mDescriptemes) {
			res+=d.toString() + " \\\\\\ ";
		}
		if(res.length()>0) {
			res = res.substring(0, res.length()-4);
		} 
		return res;
	}
}
