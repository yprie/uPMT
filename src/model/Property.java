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

import java.io.Serializable;
import java.util.LinkedList;

public class Property extends Type implements Serializable, Cloneable{
	
	private String mValue;
	private Descripteme mExtract;

	
	public Property(String nom) {
		super(nom);
	}
	
	public void setValue(String valeur){
		this.mValue = valeur;
	}
	
	public String getValue(){
		return this.mValue;
	}
	
	public Descripteme getExtract(){
		if(mExtract==null) {
			mExtract= new Descripteme("");
		}
		return this.mExtract;
	}
	
	public void setExtract(String texte){
		if(mExtract==null) {
			mExtract= new Descripteme(texte);
		}
		else
			this.mExtract.setTexte(texte);
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
	
	

}
