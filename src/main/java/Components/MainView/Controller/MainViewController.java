/*****************************************************************************
 * MainViewController.java
 *****************************************************************************
 * Copyright 锟 2017 uPMT
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

package Components.MainView.Controller;

import application.Project.Models.Project;
import Components.SchemaTree.Controllers.SchemaTreeController;
import Components.InterviewPanel.Controllers.InterviewPanelController;
import application.Configuration.Configuration;
import interviewSelector.commands.InterviewSelectorCommandFactory;
import interviewSelector.controllers.InterviewSelectorController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

	private Project project;

	private @FXML SplitPane mainSplitPane;
	private @FXML SplitPane leftPane;

	private InterviewSelectorController interviewSelector;
	private @FXML SplitPane paneOfTextArea;

	public static Node createMainView(MainViewController controller) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(controller.getClass().getResource("/views/MainView/MainView.fxml"));
			loader.setController(controller);
			loader.setResources(Configuration.langBundle);
			loader.setClassLoader(controller.getClass().getClassLoader());
			return loader.load();
		}
		catch (IOException exception){
			exception.printStackTrace();
			return null;
		}
	}

	public MainViewController(Project project){
		this.project = project;
	}

	public void setProject(Project project) {
		this.project = project;
		refreshContent();
	}

	private void refreshContent() {
		//Set SchemaTree view
		leftPane.getItems().add(SchemaTreeController.createSchemaTree(project.getSchemaTreeRoot()));

		//Set interviewSelector
		if(interviewSelector != null)
			interviewSelector.unbind();
		interviewSelector = new InterviewSelectorController(project.interviewsProperty(), project.selectedInterviewProperty(), new InterviewSelectorCommandFactory(project));
		leftPane.getItems().add(InterviewSelectorController.createInterviewSelector(interviewSelector));

		//Set the interview panel
		InterviewPanelController interviewPanel = new InterviewPanelController(project.getSelectedInterview());
		paneOfTextArea.getItems().add(interviewPanel.createInterviewPanel(interviewPanel));
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		refreshContent();
	}
}
