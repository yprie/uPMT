/*****************************************************************************
 * LaunchingScreenController.java
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

package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.scene.*;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import model.Project;
import utils.Utils;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class LaunchingScreenController implements Initializable{

	private @FXML Button btn_nouveauProjet;
	private @FXML Button btn_ouvrirProjetSelected;
	private @FXML Button btn_openProjectAs;
	private Main m_main;
	private @FXML ListView<Project> tousLesProjets;
	public Stage window;
	
	public LaunchingScreenController(Main main,Stage window) {
		// TODO Auto-generated constructor stub
		m_main = main;
		this.window = window;
		this.window.setOnCloseRequest(new EventHandler<WindowEvent>() {
	          public void handle(WindowEvent we) {
	        	  if(m_main.haveCurrentProject()) {
	        		  //System.out.println(m_main.getCurrentProject().getName());
	        		  window.close();
	        	  }
	        	  else {
		        	  Platform.exit();
		              System.exit(0);
	        	  }
	          }
	      }); 
		
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//TODO: in this part, we prompt a lauchingScreen Dialog for open or create new project
		for(Project p : m_main.getProjects()) {
			
		}
		ObservableList<Project> items = FXCollections.observableArrayList(m_main.getProjects());
		tousLesProjets.setItems(items);

		tousLesProjets.getFocusModel().focus(0);
		//tousLesProjets.setItems("ooo");
		tousLesProjets.setCellFactory(new Callback<ListView<Project>, ListCell<Project>>() {
	        @Override
	        public ListCell<Project> call(ListView<Project> param) {
	            
	        	ListCell<Project> cell = new ListCell<Project>() {

	                @Override
	                protected void updateItem(Project item, boolean empty) {
	                    super.updateItem(item, empty);
	                    if (item != null) {
	                    	setText(item.getName() + " (from " + item.getPath() +")");
	                    } else {
	                    	setText(""); 
	                    }
	                }
	            };
	            return cell;
	        }
	    });
		btn_ouvrirProjetSelected.setDisable(true);
		tousLesProjets.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Project>() {
			@Override
			public void changed(ObservableValue<? extends Project> arg0, Project arg1, Project arg2) {
				if (tousLesProjets.getSelectionModel().getSelectedItem() != null){
					btn_ouvrirProjetSelected.setDisable(false);
				}
				else {
					btn_ouvrirProjetSelected.setDisable(true);
				}
			}
          });
	}
	
	public void NewProjectDialog(){
		// TODO: This prompt a new stage to help create a new project
		
		Stage promptWindow = new Stage(StageStyle.UTILITY);
		//promptWindow.setAlwaysOnTop(true);
		promptWindow.initModality(Modality.APPLICATION_MODAL);
		promptWindow.setTitle(m_main._langBundle.getString("new_project"));
		
		try {
			FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/NouveauProjetDialogLayout.fxml"));
            loader.setController(new NewProjectDialogController(m_main,promptWindow,window));
            loader.setResources(m_main._langBundle);
            BorderPane layout = (BorderPane) loader.load();
			Scene main = new Scene(layout,404,250);
			promptWindow.setScene(main);
			promptWindow.showAndWait();
			
		} catch (IOException e) {
			// TODO Exit Program
			e.printStackTrace();
		}
	}
	
	/**
	 *  Open project from a path
	 */
	public void openProjectAs() throws IOException{
		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open a Project");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("uPMT", "*.upmt"));
		File file = fileChooser.showOpenDialog(this.window);
        if (file != null) {
        	String projectToLoad = file.getName().replace(".upmt", "");
        	boolean isProject = false;
        	for(Project project : m_main.getProjects()) {
        		if(project.getName().equals(projectToLoad)) {
        			m_main.setCurrentProject(project);
        			m_main.getPrimaryStage().setTitle("uPMT - "+m_main.getCurrentProject().getName()+".uPMT");
        			m_main.launchMainView();
        			isProject=true;
        		} 
        	}
        	
        	if(!isProject) { //project is not in the project's list 
        		String name = file.getPath();
        		name = name.replace("/"+file.getName(), "");
        		name = name.replace("\\"+file.getName(), "");
        		m_main.savePath(name);
        		Utils.loadProjects(m_main.getProjects(), m_main);
        		for(Project p : m_main.getProjects()) {
        			if(p.getName().equals(projectToLoad)) {
                		m_main.setCurrentProject(p);
                		m_main.launchMainView();
                		isProject=true;
               		} 
               	}
        	}
         }
        //window.close();
	}
	
	public void OpenProjectDialog(){
		//TODO: open the selected project in the right pane and close the window
		if (tousLesProjets.getSelectionModel().getSelectedItem() == null){
			
		} else {
			m_main.setCurrentProject(tousLesProjets.getSelectionModel().getSelectedItem());
			m_main.launchMainView();
			window.close();
		}
		
	}
	
}
