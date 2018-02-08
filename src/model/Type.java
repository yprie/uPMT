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
import java.util.LinkedList;

public abstract class Type implements Serializable, Cloneable{
	
	protected LinkedList<Type> types;
	protected String description;
	protected String couleur = "#000000";
	protected String nom;
	
	public Object clone() {
		Type t = null;
	    try {
	    	// On récupère l'instance à renvoyer par l'appel de la 
	      	// méthode super.clone()
	    	t = (Type) super.clone();
	    } catch(CloneNotSupportedException cnse) {
	      	// Ne devrait jamais arriver car nous implémentons 
	      	// l'interface Cloneable
	      	cnse.printStackTrace(System.err);
	    }
	    t.types = (LinkedList<Type>) types.clone();
	    // on renvoie le clone
	    return t;
	}
	
	public Type(String nom) {
		super();
		this.nom = nom;
		this.types = new LinkedList<Type>();
		this.description = "";
	}
	
	public void addType(Type s){
		this.types.add(s);
	}
	
	public LinkedList<Type> getTypes(){
		return this.types;
	}
	
	public String toString(){
		return this.nom + this.types;
	}
	
	public String getName(){
		return this.nom;
	}
	
	public void setName(String name){
		this.nom = name;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean equals(Object o){
		Type tmp = (Type)o;
		if(tmp != null){
			return tmp.nom.equals(this.nom) &&
					tmp.description.equals(this.description) &&
					tmp.getTypes().equals(this.getTypes());
		}else{
			return false;
		}		
	}

	public String getCouleur() {
		return couleur;
	}

	public void setCouleur(String couleur) {
		this.couleur = couleur;
	}
	
    // Functions used to define the layout for the different kind of Types
	public boolean isFolder()
	{
		return this.getClass().equals(new Dossier("").getClass());
	}
	
	public boolean isClass()
	{
		return this.getClass().equals(new Classe("").getClass());
	}
	
	public boolean isProperty()
	{
		return this.getClass().equals(new Propriete("").getClass());
	}
	
}
