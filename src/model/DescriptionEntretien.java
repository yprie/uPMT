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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map.Entry;

public class DescriptionEntretien implements Serializable, Cloneable{
	
	private Descripteme descripteme;
	private LinkedList<MomentExperience> moments;
	private LocalDate dateEntretien;
	private String participant;
	private String commentaire;
	private String nom;
	
	@Override
	public boolean equals(Object o) {
		boolean ret=false;
		try {
			DescriptionEntretien d = (DescriptionEntretien)o;

			
			if(descripteme==null && d.descripteme==null) ret=true;
			else if(d.descripteme.equals(descripteme)) ret=true;
			//System.out.println("a "+ret);
			
				
			if(moments==null && d.moments==null) ret=true;
			else if(!(ret && d.moments.equals(moments))) ret=false;
			//System.out.println("b "+ret);

			
			if(dateEntretien==null && d.dateEntretien==null) ret=true;
			else if(!(ret && d.dateEntretien.equals(dateEntretien))) ret=false;
			//System.out.println("c "+ret);
			

			
			if(participant==null && d.participant==null) ret=true;
			else if(!(ret && d.participant.equals(participant))) ret=false;
			//System.out.println("d "+ret);

			
			if(commentaire==null && d.commentaire==null) ret=true;
			else if(!(ret && d.commentaire.equals(commentaire))) ret=false;
			//System.out.println("e "+ret);

			
			if(nom==null && d.nom==null) ret=true;
			else if(!(ret && d.nom.equals(nom))) ret=false;
			//System.out.println("f "+ret);
			
		}catch (Exception e) {ret = false;
		//System.out.println("erreur "+ret);
		}
		return ret;
	}
	
	/*public DescriptionEntretien clone() {
		DescriptionEntretien ret = new DescriptionEntretien(descripteme.clone(), new String(nom));
		ret.setCommentaire(commentaire);
		ret.setDateEntretien(dateEntretien);
		ret.setParticipant(participant);
		for(int i=0;i<moments.size(); i++) {
			ret.addMoment(moments.get(i).clone());
		}
		return ret;
	}*/
	
	/*public Object clone() {
		DescriptionEntretien ret = null;
	    try {
			ret = (DescriptionEntretien) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    ret.descripteme = (Descripteme)descripteme.clone();
	    return ret;
	}*/
	public Object clone() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(this);

			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (Object) ois.readObject();
		} catch (IOException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	
	public DescriptionEntretien(Descripteme d,String nomEntretiens){
		this.moments = new LinkedList<MomentExperience>();
		this.nom = nomEntretiens;
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

	public void addMoment(MomentExperience m){
		this.moments.add(m);
		m.setRow(0);
		updateMomentsPos();
	}
	
	public void addMoment(int index, MomentExperience m){
		this.moments.add(index, m);
		updateMomentsPos();
	}
	
	public void removeMomentExp(MomentExperience m) {
		this.moments.remove(m);
		updateMomentsPos();
	}
	
	
	private void updateMomentsPos() {
		for(int i=0; i<moments.size(); i++) {
		//System.out.println("Moment "+moments.get(i).getNom()+", id:"+moments.get(i).getID()+":["+i+";0]");
			moments.get(i).setGridCol(i);
			moments.get(i).setRow(0);
			moments.get(i).setParent(null);
			moments.get(i).updateSousMomentPos();
		}
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
	
	public int getNumberOfMoments() {
		return moments.size();
	}

	
}
