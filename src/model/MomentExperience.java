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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import application.Main;
import javafx.scene.paint.Color;
import utils.MainViewTransformations;

public class MomentExperience implements Serializable, Cloneable {

	private Date date = new Date();
	private String duree;
	private String nom;
	private String morceauDescripteme;
	private LinkedList<Type> types;
	private LinkedList<Enregistrement> enregistrements;
	private LinkedList<MomentExperience> sousMoments;
	private int parentMomentID=-1;
	private int parentCol=-1;
	private int gridCol;
	private String couleur = "#D3D3D3";
	private Propriete currentProperty =null;
	private int id;
	private int row;
	private transient SimpleDateFormat formater = new SimpleDateFormat("HH:mm:ss");
	
	/*public MomentExperience clone() {
		MomentExperience ret = new MomentExperience(nom, row, gridCol);
		if(date!=null)
			ret.setDate((Date)date.clone());
		ret.setDuree(duree);
		ret.setMorceauDescripteme(morceauDescripteme);
		//Types
		LinkedList<Type> retTypes = new LinkedList<Type>();
		for(int i=0;i<types.size();i++) {
			retTypes.add(types.get(i).clone());
		}
		ret.setTypes(retTypes);
		//Enregistrements
		LinkedList<Enregistrement> retEnregistrements = new LinkedList<Enregistrement>();
		for(int i=0;i<enregistrements.size();i++) {
			retEnregistrements.add(enregistrements.get(i).clone());
		}
		ret.setEnregistrements(retEnregistrements);
		for(int i=0;i<sousMoments.size(); i++) {
			ret.addSousMoment(sousMoments.get(i).clone());
		}
		ret.setGridCol(gridCol);
		ret.setID(id);
		ret.setCouleur(couleur);
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

	public MomentExperience(String nom,int row,int col){
		this.sousMoments = new LinkedList<MomentExperience>();
		this.enregistrements = new LinkedList<Enregistrement>();
		this.types = new LinkedList<Type>();
		this.nom = nom;
		this.row = row;
		this.gridCol = col;
		try {
			this.date = this.formater.parse("00:00:00");
		} catch (ParseException e) {
		}
		id=MomentID.generateID();
	}
	
	
	public MomentExperience() {
		this("-----", 0,0);
	}


	public int getID() {
		return id;
	}
	
	protected void setID(int id) {
		this.id = id;
	}
	
	public Date getDate() {
		return date;
	}
	
	public String getDateString() {
		return formater.format(date);
	}

	public void setDate(String date) throws ParseException {
		try {
			this.date = this.formater.parse(date);
		} catch (ParseException e) {
			throw e;
		}
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

	public LinkedList<Type> getTypes() {
		return types;
	}

	public void setTypes(LinkedList<Type> caracteristiques) {
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
	
	public void addSousMoment(MomentExperience m) {
		if(!this.sousMoments.contains(m)) {
			m.setParent(this);
			this.sousMoments.add(m);
			updateSousMomentPos();
		}
	}
	
	public void addSousMoment(int index, MomentExperience m) {
		if(!this.sousMoments.contains(m)) {
			m.setParent(this);
			this.sousMoments.add(index, m);
			updateSousMomentPos();
		}
	}
	
	public MomentExperience removeSousMoment(MomentExperience m) {
		return removeSousMoment(m.getGridCol());
	}
	
	public MomentExperience removeSousMoment(int index) {
		MomentExperience m = this.sousMoments.remove(index);
		m.setParent(null);
		updateSousMomentPos();
		return m;
	}
	
	public boolean hasParent() {
		return (this.parentMomentID>=0);
	}
	
	protected void updateSousMomentPos() {
		for(int i=0; i<sousMoments.size(); i++) {
		//System.out.println("Moment "+sousMoments.get(i).getNom()+", id:"+sousMoments.get(i).getID()+" ["+i+";0]");
			sousMoments.get(i).setParent(this);
			sousMoments.get(i).setGridCol(i);
			sousMoments.get(i).setRow(row+1);
			sousMoments.get(i).updateSousMomentPos();
			sousMoments.get(i).setParentCol(this.parentCol);
		}
	}
	
	public int getParentCol() {
		if(!this.hasParent()) {
			return this.gridCol;
		}
		else return this.parentCol;
	}
	
	public void setParent(MomentExperience m) {
		if(m!=null) {
			this.parentMomentID = m.getID();
			this.parentCol = m.getParentCol();
			setRow(m.getRow()+1);
			this.setGridCol(m.getGridCol());
		}
		else{
			this.parentMomentID=-1;
			this.parentCol = this.gridCol;
			setRow(0);
		}
	}
	
	protected void setParentCol(int col) {
		this.parentCol = col;
	}
	
	public int getRow() {return row;}
	public void setRow(int r) {row = r;}
	
	public int getParentID() {
		return this.parentMomentID;
	}
	
	public MomentExperience getParent(Main main) {
		return MainViewTransformations.getMomentByID(this.parentMomentID, main);
	}
	
	public void setSousMoments(LinkedList<MomentExperience> sousMoments) {
		this.sousMoments = sousMoments;
	}

	@Override
	public boolean equals(Object arg0) {
		try {
			MomentExperience e = (MomentExperience)arg0;
			return this.getID()==e.getID();
		}catch(Exception e) {
			return false;
		}
	}

	public String getCouleur() {
		return couleur;
	}

	public void setCouleur(String couleur) {
		this.couleur = couleur;
	}
	
	public void setCurrentProperty(Propriete n) {
		currentProperty = n;
	}
	
	public Propriete getCurrentProperty() {
		return currentProperty;
	}

}
