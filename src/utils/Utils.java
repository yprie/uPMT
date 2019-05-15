/*****************************************************************************
 * Utils.java
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
	
	
	
	
	
	public static String toRGBCode( Color color )
    {
        return String.format( "#%02X%02X%02X",
            (int)( color.getRed() * 255 ),
            (int)( color.getGreen() * 255 ),
            (int)( color.getBlue() * 255 ) );
    }
	
	
	

}
