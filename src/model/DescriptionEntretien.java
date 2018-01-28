/*****************************************************************************
 * DescriptionEntretien.java
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
import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map.Entry;

public class DescriptionEntretien implements Serializable{
	
	private Descripteme descripteme;
	private LinkedList<MomentExperience> moments;
	private LocalDate dateEntretien;
	private String participant;
	private String commentaire;
	private int numberCols;
	private String nom;
	
	public DescriptionEntretien(Descripteme d,String nomEntretiens){
		this.moments = new LinkedList<MomentExperience>();
		this.nom = nomEntretiens;
		this.numberCols = 1;
		this.descripteme = d;
	}
	
	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Descripteme getDescripteme() {
		return descripteme;
	}

	public void setDescripteme(Descripteme descripteme) {
		this.descripteme = descripteme;
	}

	public void setMoments(LinkedList<MomentExperience> moments) {
		this.moments = moments;
	}

	public void addMomentExp(MomentExperience m){
		this.moments.add(m);
	}
	
	public void addMomentExpAt(int pos, MomentExperience m){
		this.moments.add(pos, m);
	}
	
	public LinkedList<MomentExperience> getMoments(){
		return this.moments;
	}
	
	public void setDateEntretien(LocalDate ld){
		dateEntretien = ld;
	}
	public LocalDate getDateEntretien(){
		return dateEntretien;
	}
	public void setParticipant(String s){
		participant = s;
	}
	public String getParticipant(){
		return participant;
	}
	public void setCommentaire(String s){
		commentaire = s;
	}

	public String getCommentaire(){
		return commentaire;
	}
	public String toString(){
		return this.nom;
	}
	
	public int getNumberCols() {
		return numberCols;
	}

	public void setNumberCols(int numberCols) {
		this.numberCols = numberCols;
	}
	
}
