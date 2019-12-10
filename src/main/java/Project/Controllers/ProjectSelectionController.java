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

package Project.Controllers;

import Project.Models.Project;
import Project.Persistency.ProjectLoader;
import application.Configuration.Configuration;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProjectSelectionController implements Initializable{

	public enum State { CLOSED, SUCCESS }
	private State state;
	private Project resultProject;

	private Stage stage;
	private @FXML Button btn_nouveauProjet;
	private @FXML Button btn_ouvrirProjetSelected;
	private @FXML Button btn_openProjectAs;
	private @FXML ListView<String> tousLesProjets;


	public static ProjectSelectionController openProjectSelection() {
		Stage stage = new Stage(StageStyle.UTILITY);
		stage.setTitle(Configuration.langBundle.getString("home"));
		stage.initModality(Modality.APPLICATION_MODAL);
		ProjectSelectionController controller = new ProjectSelectionController(stage);
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(controller.getClass().getResource("../Views/ProjectSelectionView.fxml"));
			loader.setController(controller);
			loader.setResources(Configuration.langBundle);
			BorderPane layout = (BorderPane) loader.load();
			Scene launchingScene = new Scene(layout);
			//ENLEVER LE COMMENTAIRE POUR ACTIVER LA BETA CSS FLAT DESIGN
			/*if(activateBetaDesign) {
				rootLayout.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				//rootLayout.getStylesheets().add(getClass().getResource("t.css").toExternalForm());
			}*/
			stage.setScene(launchingScene);
			stage.showAndWait();
			return controller;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//TODO: in this part, we prompt a lauchingScreen Dialog for open or create new project
		ObservableList<String> items = FXCollections.observableArrayList(Configuration.getProjectsPath());
		tousLesProjets.setItems(items);
		tousLesProjets.getFocusModel().focus(0);
		btn_ouvrirProjetSelected.setDisable(true);
		tousLesProjets.getSelectionModel().selectedItemProperty().addListener((arg0, arg1, arg2) ->
				btn_ouvrirProjetSelected.setDisable(tousLesProjets.getSelectionModel().getSelectedItem() == null)
		);
	}

	public ProjectSelectionController(Stage stage) {
		this.stage = stage;
		this.state = State.CLOSED;
	}

	public void NewProjectDialog(){
		NewProjectController newProjectController = NewProjectController.createNewProject();
		if(newProjectController.getState() == NewProjectController.State.SUCCESS) {
			resultProject = newProjectController.getCreatedProject();
			state = State.SUCCESS;
			stage.close();
		}
		else {
			ProjectDialogBox.projectCreationFailed();
		}
	}
	
	/**
	 *  Open project from a path
	 */
	public void openProject() throws IOException {
		OpenProjectController controller = OpenProjectController.createOpenProjectController(stage);
		if(controller.getState() == OpenProjectController.State.SUCCESS){
			resultProject = controller.getResultProject();
			state = State.SUCCESS;
			stage.close();
		}
	}
	
	public void OpenProjectDialog(){
		try {
			resultProject = ProjectLoader.load(tousLesProjets.getSelectionModel().getSelectedItem());
			state = State.SUCCESS;
			stage.close();
		} catch (Exception e) {
			ProjectDialogBox.projectLoadingFailed();
		}
	}

	public State getState() {
		return state;
	}
	public Project getChoosenProject() { return resultProject; }

}
