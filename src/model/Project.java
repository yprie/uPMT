/*****************************************************************************
 * Projet.java
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import javax.swing.JFileChooser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import utils.InterfaceAdapter;
import utils.Serializer;
import utils.UpdateVersions;
import utils.Utils;
import javafx.scene.control.Alert.AlertType;

public class Project implements Serializable {
	
	public static final String FORMAT = ".upmt";
	
	public static final String RECOVERY = "recovery_";
	public static int VERSION_OF_APP = 3;
	
	//--------------------------------------------
	
	private int SAVE_VERSION;
	private String mName;
	private Schema mSchema;
	private String path;
	private LinkedList<DescriptionInterview> mInterviews;

	public Project(String n,Schema s){
		this.mName = n;
		this.mInterviews = new LinkedList<DescriptionInterview>();
		this.mSchema = s;
		//this.path = "./save";
		this.path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath().replace("bin/", "save");
		
		SAVE_VERSION = VERSION_OF_APP;
	}
	
	public void initializePath() {
		if(this.path==null) {
			this.path="./save";
		}
	}
	
	public void addEntretiens(DescriptionInterview d){
		this.mInterviews.add(d);
	}
	
	public void addEntretiens(int index, DescriptionInterview d){
		this.mInterviews.add(index, d);
	}
	
	public DescriptionInterview removeEntretiens(int index) {
		return this.mInterviews.remove(index);
	}
	
	public boolean removeEntretiens(DescriptionInterview interview) {
		return this.mInterviews.remove(interview);
	}
	
	public String toString(){
		String str = "NomProjet = "+this.mName + "\n";
		for(DescriptionInterview e : mInterviews){
			str = str + e.toString() + "\n";
		}
		return str;
	}
	

	public void save(){
		initializePath();
		File autoSaveFile = new File(path+"/"+RECOVERY+mName+FORMAT);
		System.out.println(path+"/"+RECOVERY+mName+FORMAT);
		autoSaveFile.delete();
		saveData(path+"/"+mName+FORMAT);
		
	}
	
	/**
	 *  Save project in a different path
	 *  @param pathLocation: path to save
	 *  @param name: the project name to save 
	 */
	public void saveAs(String pathLocation, String name) throws IOException {
		String lastPath = this.path.replace(".upmt", "");
		if(name.contains(".upmt")) {
			name += "./upmt";            
		}
		this.setName(name);
		path = pathLocation.replace("/"+mName+FORMAT, "");
		this.setPath(path);
		File autoSaveFile = new File(lastPath+"/"+mName+FORMAT);
		File autoSaveRecoveryFile = new File(lastPath+"/"+RECOVERY+mName+FORMAT);
		autoSaveFile.delete();
		autoSaveRecoveryFile.delete();
		if(!pathLocation.contains(".upmt")) {
			pathLocation += FORMAT;
		}
		saveData(pathLocation);
	}
	
	public void autosave(){
		System.out.println("autoSave "+ this.path+"/"+RECOVERY+mName+FORMAT);
		saveData(this.path+"/"+RECOVERY+mName+FORMAT);
	}
	
	private void saveData(String filename) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Type.class, new InterfaceAdapter<Type>());
		gsonBuilder.setPrettyPrinting();
	    Gson gson = gsonBuilder.create(); 
		try (Writer writer = new FileWriter(filename)) {
		    gson.toJson(this, writer);
		    writer.close();
		}catch(Exception e) {
			//e.printStackTrace();
			//System.out.println("lol");
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
			fichier.close();
			oos.close();
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
		File f = new File(this.path+this.getName()+FORMAT);
		f.delete();
		
	}
	
	public void setVersion(int v) {
		this.SAVE_VERSION=v;
	}
	
	public static Project loadData(String projet, String path) {
		try {
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.registerTypeAdapter(Type.class, new InterfaceAdapter<Type>());
			gsonBuilder.setPrettyPrinting();
			Gson gson = gsonBuilder.create();
		    Project p;
		    int projectVersion = getVersionProj(path+projet);
		    System.out.println("version app " + Project.VERSION_OF_APP);
		    System.out.println("project version " + projectVersion);
		    System.out.println("project path " + path+projet);
		    if(Project.VERSION_OF_APP>projectVersion) {
		    	String updatedJson = updateSaveFile(path+projet, projectVersion);
				p = gson.fromJson(updatedJson, Project.class);
				p.save();
		    }
		    else {
		    	p = gson.fromJson(new FileReader(path+projet), Project.class);
		    }
			p.setVersion(Project.VERSION_OF_APP);
			p.save();
			p.reloadMomentParentLost();
			return p;
		} catch(Exception e) {
			//e.printStackTrace();
			//System.out.println(e.getMessage());
			return null;
		}
	}
	
	private static int getVersionProj(String path) {
		int projVersion=0;
		String json="";
		try {
			json = new String (Files.readAllBytes(Paths.get(path)));
			int i = json.indexOf("\"SAVE_VERSION\": ");
			if(i!=-1) {
				i= i+16;
				String currentVersionString = "";
				while(!json.substring(i, i+1).equals(",")) {
					currentVersionString+=json.substring(i, i+1);
					i++;
				}
				projVersion = Integer.parseInt(currentVersionString);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return projVersion;
	}
	
	private static String updateSaveFile(String path, int projVersion) {
		String ret="";
		try {
			ret = new String (Files.readAllBytes(Paths.get(path)));
			int i;
			for(i=projVersion; i<Project.VERSION_OF_APP;i++) {
				if(i==0) ret = UpdateVersions.version0To1(ret);
				if(i==1) ret = UpdateVersions.version1To2(ret);
				if(i==2) ret = UpdateVersions.version2To3(ret);
			}
	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	
	
	protected void reloadMomentParentLost() {
		
	}
	
	public static Project load(String projet){
		ObjectInputStream ois = null;
		
		try {
			final FileInputStream fichier = new FileInputStream(getPATH()+projet);
			ois = new ObjectInputStream(fichier);
			final Project p2 = (Project) ois.readObject();
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

	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public Schema getSchema() {
		return mSchema;
	}

	public void setSchema(Schema mSchema) {
		this.mSchema = mSchema;
	}

	public LinkedList<DescriptionInterview> getInterviews() {
		return mInterviews;
	}

	public void setInterviews(LinkedList<DescriptionInterview> mInterviews) {
		this.mInterviews = mInterviews;
	}

	public int getVersion() {
		return this.SAVE_VERSION;
	}
	
	public void setPath(String path) {
		this.path=path;
	}
	
	/**
	 * get project default path
	 * @return: default path
	 */
	public static String getPATH() {
		return "./save/";
	}
	
}
