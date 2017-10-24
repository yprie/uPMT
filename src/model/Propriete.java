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

public class Propriete extends Type implements Serializable{
	
	private String valeur;
	private Descripteme extract;

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
		return this.extract;
	}
	
	public void SetDescriptemeTxt(String texte){
		this.extract.setTexte(texte);
	}
	
	@Override
	public String toString(){
		return this.nom + " : " + this.valeur;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Propriete other = (Propriete) obj;
		if (extract == null) {
			if (other.extract != null)
				return false;
		} else if (!extract.equals(other.extract))
			return false;
		if (valeur == null) {
			if (other.valeur != null)
				return false;
		} else if (!valeur.equals(other.valeur))
			return false;
		return true;
	}
	
	

}
