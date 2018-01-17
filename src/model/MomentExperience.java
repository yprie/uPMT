/*****************************************************************************
 * MomentExperience.java
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
import java.util.Date;
import java.util.LinkedList;

import javafx.scene.paint.Color;

public class MomentExperience implements Serializable {

	private Date date;
	private String duree;
//	private String debut;
//	private String fin;
	private String nom;
	private String morceauDescripteme;
	private LinkedList<Type> types;
	private LinkedList<Enregistrement> enregistrements;
	private LinkedList<MomentExperience> sousMoments;
	private int gridCol;
	private String couleur = "#D3D3D3";

	public MomentExperience(String nom,int row,int col){
		this.sousMoments = new LinkedList<MomentExperience>();
		this.enregistrements = new LinkedList<Enregistrement>();
		this.types = new LinkedList<Type>();
		this.nom = nom;
		this.gridCol = col;
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getDuree() {
		return duree;
	}

	public void setDuree(String d) {
		duree = d;
	}


	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getMorceauDescripteme() {
		return morceauDescripteme;
	}

	public void setMorceauDescripteme(String morceauDescripteme) {
		this.morceauDescripteme = morceauDescripteme;
	}

	public LinkedList<Type> getType() {
		return types;
	}

	public void setCaracteristiques(LinkedList<Type> caracteristiques) {
		this.types = caracteristiques;
	}

	public LinkedList<Enregistrement> getEnregistrements() {
		return enregistrements;
	}

	public void setEnregistrements(LinkedList<Enregistrement> enregistrements) {
		this.enregistrements = enregistrements;
	}

	public int getGridCol() {
		return gridCol;
	}

	public void setGridCol(int gridCol) {
		this.gridCol = gridCol;
	}
	

	public LinkedList<MomentExperience> getSousMoments() {
		return sousMoments;
	}

	public void setSousMoments(LinkedList<MomentExperience> sousMoments) {
		this.sousMoments = sousMoments;
	}

	public boolean equals(Object arg0) {
		MomentExperience mom2 = (MomentExperience)arg0;
		return this.nom == mom2.nom &&
			this.date == mom2.date &&
			this.types == mom2.types;
	}

	public String getCouleur() {
		return couleur;
	}

	public void setCouleur(String couleur) {
		this.couleur = couleur;
	}
}
