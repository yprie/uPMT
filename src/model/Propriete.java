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

public class Propriete extends Type implements Serializable, Cloneable{
	
	private String valeur;
	private Descripteme extract;


	/*public Propriete clone() {
		Propriete ret = new Propriete(nom);
		ret.setDescription(description);
		ret.setCouleur(couleur);
		LinkedList<Type> retTypes = new LinkedList<Type>();
		for(int i=0;i<types.size();i++) {
			ret.addType(types.get(i).clone());
		}
		ret.setDescriptemeTxt(extract.getTexte());
		return ret;
	}*/
	
	public Object clone() {
		Propriete ret = null;
	    ret = (Propriete) super.clone();
	    ret.extract = (Descripteme) extract.clone();
	    return ret;
	}
	
	public Propriete(String nom) {
		super(nom);
	}
	
	public void setValeur(String valeur){
		this.valeur = valeur;
	}
	
	public String getValeur(){
		return this.valeur;
	}
	
	public Descripteme getDescripteme(){
		if(extract==null) {
			extract= new Descripteme("");
		}
		return this.extract;
	}
	
	public void setDescriptemeTxt(String texte){
		if(extract==null) {
			extract= new Descripteme(texte);
		}
		else
			this.extract.setTexte(texte);
	}
	
	@Override
	public String toString(){
		return this.nom + " : " + this.valeur;
	}

	@Override
	public boolean equals(Object obj) {
		try {
			Propriete other = (Propriete) obj;
			if(other.getName().equals(this.getName())) return true;
			else return false;
		}catch(Exception e) {return false;}
		
	}
	
	

}
