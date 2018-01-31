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
import java.util.HashSet;
import java.util.LinkedList;

import javafx.scene.paint.Color;
import model.Projet;

public abstract class Utils {
	
	public static void loadProjects(LinkedList<Projet> projects){
		HashSet<String> projectNames = loadProjectsNames();
		
		
		if(projectNames.isEmpty()){
			// For debug purposes
			////System.out.println("No projects to load");
		}else{
			////System.out.println("Loading projects");
			for (String s : projectNames) {
				if(s.contains(Projet.FORMAT)) {
					projects.add(Projet.load(s));
				}
			}
		}
	}

	
	public static boolean checkRecovery() {
		HashSet<String> projectNames = loadProjectsNames();
		boolean ret = false;
		
		if(projectNames.isEmpty()){
			// For debug purposes
			////System.out.println("No projects to load");
		}else{
			////System.out.println("Loading projects");
			for (String s : projectNames) {
				if(s.contains(Projet.FORMAT)) {
					if(projectNames.contains(Projet.RECOVERY+s)) {
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

		File[] files = new File(Projet.PATH).listFiles();
		if(files==null) {
			new File(Projet.PATH).mkdir();
			files = new File(Projet.PATH).listFiles();
		}
		//If this pathname does not denote a directory, then listFiles() returns null. 

		for (File file : files) {
		    if (file.isFile()) {
		        results.add(file.getName());
		        ////System.out.println(file.getName());
		    }
		}
		
		
		
		return results;
	}
	
	public static void deleteRecovery() {
		File[] files = new File(Projet.PATH).listFiles();
		//If this pathname does not denote a directory, then listFiles() returns null. 

		for (File file : files) {
		    if (file.isFile()) {
		    	if(file.getName().contains(Projet.RECOVERY))
		    		file.delete();
		    }
		}
	}
	
	public static void replaceRecovery() {

		//Search RecpveryFiles
		File[] files = new File(Projet.PATH).listFiles();
		//If this pathname does not denote a directory, then listFiles() returns null. 
		for (File file : files)
		    if (file.isFile()) 
		    	if(file.getName().contains(Projet.RECOVERY)) {
		    		String fileName = file.getName().replace(Projet.RECOVERY, "");
		    		//System.out.println();
		    		File fileToDelete = new File(Projet.PATH+fileName);
		    		fileToDelete.delete();
		    		file.renameTo(new File(Projet.PATH+fileName));
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
