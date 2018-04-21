/*****************************************************************************
 * Utils.java
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

package utils;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import application.Main;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import model.Project;

public abstract class Utils {
	
	public static void loadProjects(LinkedList<Project> projects, Main main){
		HashSet<String> projectNames = loadProjectsNames();
		
		
		if(projectNames.isEmpty()){
			// For debug purposes
			//System.out.println("No projects to load");
		}else{
			System.out.println("Loading projects");
			for (String s : projectNames) {
				if(s.contains(Project.FORMAT)) {
					//projects.add(Projet.load(s));
					Project p = Project.loadData(s);
					if(p==null) {
						Alert alert = new Alert(AlertType.CONFIRMATION);
				        //alert.setTitle("Error, version conflict");
						alert.setTitle(main._langBundle.getString("error_version"));
				        //alert.setHeaderText("Your saves are in conflict with this new version.");
						alert.setHeaderText(main._langBundle.getString("error_version_text_alarm"));
				        //alert.setContentText("Please contact us in the github repository.");
						alert.setContentText(main._langBundle.getString("error_version_text_contact"));

				        Optional<ButtonType> result = alert.showAndWait();
				        if (result.get() == ButtonType.OK){
				    		alert.close();
				        } else {
				            alert.close();
				        }
					}
					else
						projects.add(p);
					
				}
			}
		}
	}

	
	public static boolean checkRecovery() {
		HashSet<String> projectNames = loadProjectsNames();
		boolean ret = false;
		
		if(projectNames.isEmpty()){
			// For debug purposes
			//System.out.println("No projects to load");
		}else{
			//System.out.println("Loading projects");
			for (String s : projectNames) {
				if(s.contains(Project.FORMAT)) {
					if(projectNames.contains(Project.RECOVERY+s)) {
						ret=true;
						break;
					}
				}
			}
		}
		return ret;
	}
	
	private static HashSet<String> loadProjectsNames() {
		HashSet<String> results = new HashSet<String>();

		File[] files = new File(Project.PATH).listFiles();
		if(files==null) {
			new File(Project.PATH).mkdir();
			files = new File(Project.PATH).listFiles();
		}
		//If this pathname does not denote a directory, then listFiles() returns null. 

		for (File file : files) {
		    if (file.isFile()) {
		        results.add(file.getName());
		        //System.out.println(file.getName());
		    }
		}
		
		
		
		return results;
	}
	
	public static void deleteRecovery() {
		File[] files = new File(Project.PATH).listFiles();
		//If this pathname does not denote a directory, then listFiles() returns null. 

		for (File file : files) {
		    if (file.isFile()) {
		    	if(file.getName().contains(Project.RECOVERY))
		    		file.delete();
		    }
		}
	}
	
	public static void replaceRecovery() {

		//Search RecpveryFiles
		File[] files = new File(Project.PATH).listFiles();
		//If this pathname does not denote a directory, then listFiles() returns null. 
		for (File file : files)
		    if (file.isFile()) 
		    	if(file.getName().contains(Project.RECOVERY)) {
		    		String fileName = file.getName().replace(Project.RECOVERY, "");
		    		//System.out.println();
		    		File fileToDelete = new File(Project.PATH+fileName);
		    		fileToDelete.delete();
		    		file.renameTo(new File(Project.PATH+fileName));
		    	}
		
		
	}
	
	
	public static String version0To1(String json) {
		String ret = json;
		//Renommage des classes
		ret = ret.replaceAll("model.Dossier", "model.Folder");
		ret = ret.replaceAll("model.Classe", "model.Category");
		ret = ret.replaceAll("model.Propriete", "model.Property");
		
		//Project
		ret = ret.replaceAll("\"nom\"", "\"mName\"");
		ret = ret.replaceAll("\"schemaProjet\"", "\"mSchema\"");
		ret = ret.replaceAll("\"entretiens\"", "\"mInterviews\"");
		
		//DescriptionInterview
		ret = ret.replaceAll("\"descripteme\"", "\"mDescripteme\"");
		ret = ret.replaceAll("\"moments\"", "\"mMoments\"");
		ret = ret.replaceAll("\"dateEntretien\"", "\"mDateInterview\"");
		ret = ret.replaceAll("\"participant\"", "\"mParticipant\"");
		ret = ret.replaceAll("\"commentaire\"", "\"mComment\"");
		
		//MomentExperience
		ret = ret.replaceAll("\"date\"", "\"mDate\"");
		ret = ret.replaceAll("\"types\"", "\"mTypes\"");
		ret = ret.replaceAll("\"enregistrements\"", "\"mRecords\"");
		ret = ret.replaceAll("\"sousMoments\"", "\"mSubMoments\"");
		ret = ret.replaceAll("\"parentMomentID\"", "\"mParentMomentID\"");
		ret = ret.replaceAll("\"parentCol\"", "\"mParentCol\"");
		ret = ret.replaceAll("\"gridCol\"", "\"mGridCol\"");
		ret = ret.replaceAll("\"couleur\"", "\"mColor\"");
		ret = ret.replaceAll("\"id\"", "\"mID\"");
		ret = ret.replaceAll("\"row\"", "\"mRow\"");
		ret = ret.replaceAll("\"currentProperty\"", "\"mCurrentProperty\"");
		ret = ret.replaceAll("\"morceauDescripteme\"", "\"mDescripteme\"");
		
		//Type
		ret = ret.replaceAll("\"description\"", "\"mDescription\"");
		ret = ret.replaceAll("\"valeur\"", "\"mValue\"");
		ret = ret.replaceAll("\"extract\"", "\"mExtract\"");
		
		//Descripteme
		ret = ret.replaceAll("\"texte\"", "\"mText\"");
		
		//Record
		ret = ret.replaceAll("\"link\"", "\"mLink\"");
		
		//System.out.println(ret);
		return ret;
	}
	
	public static String version1To2(String json) {
		String ret = json;
		try {
			//On convertit le fichier en Objet Json
			JsonParser parser = new JsonParser();
			JsonObject rootObj = parser.parse(json).getAsJsonObject();
			//On récupère le schéma pour convertir les anciens type en nouveau
			JsonObject schema = rootObj.getAsJsonObject("mSchema");
			/*rootObj.remove("SAVE_VERSION");
			rootObj.addProperty("SAVE_VERSION", 2);*/
			//On change mTypes en mFolders
			schema.add("mFolders", schema.get("mTypes"));
			schema.remove("mTypes");
			//Pour chaque dossier du schema on applique la conversion
			JsonArray folders = schema.getAsJsonArray("mFolders");
			for(JsonElement f : folders) {
				f = Utils.version2Folder(f.getAsJsonObject());
			}
			
			//Pour chaque interview on regarde chaque moment pour convertir également chaque type
			JsonArray interviews = rootObj.getAsJsonArray("mInterviews");
			for(JsonElement i : interviews) {
				JsonArray moments = i.getAsJsonObject().getAsJsonArray("mMoments");
				for(JsonElement moment : moments) {
					moment = Utils.version2Moment(moment.getAsJsonObject());
				}
			}
			
			
			ret = rootObj.toString();
			
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	protected static JsonElement version2Moment(JsonObject moment) {
		System.out.println("Pour le moment: "+moment.get("mName"));
		JsonArray types = moment.getAsJsonArray("mTypes");
		System.out.println("Les types: "+types);
		JsonArray cat = new JsonArray();
		for(JsonElement type : types) {
			JsonObject o = type.getAsJsonObject();
			JsonObject c = o.get("data").getAsJsonObject();
			//On convertit aussi les propriétés de la catégorie
			JsonArray prop = new JsonArray();
			for(JsonElement p : c.get("mTypes").getAsJsonArray()) {
				prop.add(p.getAsJsonObject().get("data"));
			}
			c.add("mProperties", prop);
			cat.add(c);
		}
		moment.add("mCategories", cat);
		moment.remove("mTypes");
		JsonArray moments = moment.getAsJsonArray("mSubMoments");
		for(JsonElement sub : moments) {
			sub = Utils.version2Moment(sub.getAsJsonObject());
		}
		
		return moment;
	}
	
	
	protected static JsonElement version2Folder(JsonObject folder) {
		//On recupère les données du dossier pour le mettre au bon endroit
		folder.remove("type");
		folder.add("tmp", folder.get("data").getAsJsonObject().get("mTypes"));
		folder.add("mName", folder.get("data").getAsJsonObject().get("mName"));
		folder.add("mDescription", folder.get("data").getAsJsonObject().get("mDescription"));
		folder.add("mColor", folder.get("data").getAsJsonObject().get("mColor"));
		folder.remove("data");
		//On regarde tous les types pour séparer les catégories des sous dossiers
		JsonArray types = folder.getAsJsonArray("tmp");
		JsonArray cat = new JsonArray();
		JsonArray fold = new JsonArray();
		for(JsonElement type : types) {
			JsonObject o = type.getAsJsonObject();
			//Si c'est une catégorie
			if(o.get("type").getAsString().equals("model.Category")) {
				JsonObject c = o.get("data").getAsJsonObject();
				//On convertit aussi les propriétés de la catégorie
				JsonArray prop = new JsonArray();
				for(JsonElement p : c.get("mTypes").getAsJsonArray()) {
					prop.add(p.getAsJsonObject().get("data"));
				}
				c.add("mProperties", prop);
				cat.add(c);
			}
			//Si c'est un dossier
			else {
				//On convertit le dossier
				fold.add(Utils.version2Folder(o.getAsJsonObject()));
			}
		}
		folder.add("mCategories", cat);
		folder.add("mFolders", fold);
		folder.remove("tmp");
		
		return folder;
	}
	
	
	public static String toRGBCode( Color color )
    {
        return String.format( "#%02X%02X%02X",
            (int)( color.getRed() * 255 ),
            (int)( color.getGreen() * 255 ),
            (int)( color.getBlue() * 255 ) );
    }
	
	
	

}
