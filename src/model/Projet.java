/*****************************************************************************
 * Projet.java
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Properties;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import utils.InterfaceAdapter;
import javafx.scene.control.Alert.AlertType;

public class Projet implements Serializable {
	
	public static final String FORMAT = ".upmt";
	public static final String PATH = "./save/";
	public static final String RECOVERY = "recovery_";
	
	private String nom;
	private Schema schemaProjet;
	private LinkedList<DescriptionEntretien> entretiens;

	public Projet(String n,Schema s){
		this.nom = n;
		this.entretiens = new LinkedList<DescriptionEntretien>();
		this.schemaProjet = s;
	}
	
	public void addEntretiens(DescriptionEntretien d){
		this.entretiens.add(d);
	}
	
	public void addEntretiens(int index, DescriptionEntretien d){
		this.entretiens.add(index, d);
	}
	
	public DescriptionEntretien removeEntretiens(int index) {
		return this.entretiens.remove(index);
	}
	
	public boolean removeEntretiens(DescriptionEntretien interview) {
		return this.entretiens.remove(interview);
	}
	
	public String toString(){
		String str = "NomProjet = "+this.nom + "\n";
		for(DescriptionEntretien e : entretiens){
			str = str + e.toString() + "\n";
		}
		return str;
	}
	
	public String getName(){
		return this.nom;
	}

	public void save(){
		//delete autosave file
		File autoSaveFile = new File(PATH+RECOVERY+nom+FORMAT);
		autoSaveFile.delete();
		//saveFile(PATH+nom+FORMAT);
		saveData(PATH+nom+FORMAT);
		
	}
	
	public void autosave(){
		//saveFile(PATH+RECOVERY+nom+FORMAT);	
		saveData(PATH+RECOVERY+nom+FORMAT);
	}
	
	private void saveData(String filename) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Type.class, new InterfaceAdapter<Type>());
		gsonBuilder.setPrettyPrinting();
	    Gson gson = gsonBuilder.create(); 

		try (Writer writer = new FileWriter(filename)) {
		    gson.toJson(this, writer);
		}catch(Exception e) {
			//e.printStackTrace();
			System.out.println("lol");
			
		}
	}
	
	private void saveFile(String filename){
		// save in it's own file
		ObjectOutputStream oos = null;
		try {
			final FileOutputStream fichier = new FileOutputStream(filename);
			oos = new ObjectOutputStream(fichier);
			oos.writeObject(this);
			oos.flush();
		} catch (final java.io.IOException e) {
			e.printStackTrace();
		} finally {
			try 
			{
				if (oos != null) {
					oos.flush();
					oos.close();
				}
			} catch (final IOException ex) {
				ex.printStackTrace();
			}
		}		
	}
	
	
	public void remove(){
		File f = new File(PATH+this.getName()+FORMAT);
		f.delete();
	}
	
	public static Projet loadData(String projet) {
		try {
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.registerTypeAdapter(Type.class, new InterfaceAdapter<Type>());
			gsonBuilder.setPrettyPrinting();
		    Gson gson = gsonBuilder.create(); 
			Projet p = gson.fromJson(new FileReader(PATH+projet), Projet.class);
			p.reloadMomentParentLost();
			return p;
		}catch(Exception e) {
			//e.printStackTrace();
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	protected void reloadMomentParentLost() {
		
	}
	
	public static Projet load(String projet){
		ObjectInputStream ois = null;
		
		try {
			final FileInputStream fichier = new FileInputStream(PATH+projet);
		//System.out.println(projet);
			ois = new ObjectInputStream(fichier);
			final Projet p2 = (Projet) ois.readObject();
			return p2;

		}
		catch(InvalidClassException e) {
	        return null;
		}
		catch (final java.io.IOException e) {
			e.printStackTrace();
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
		}
		finally 
		{
			try {
				if (ois != null) {
					ois.close();
				}
			} 
			catch (final IOException ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Schema getSchemaProjet() {
		return schemaProjet;
	}

	public void setSchemaProjet(Schema schemaProjet) {
		this.schemaProjet = schemaProjet;
	}

	public LinkedList<DescriptionEntretien> getEntretiens() {
		return entretiens;
	}

	public void setEntretiens(LinkedList<DescriptionEntretien> entretiens) {
		this.entretiens = entretiens;
	}

	
}
