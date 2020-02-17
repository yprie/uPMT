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

package components.mainView.controller;

import application.project.models.Project;
import components.modelisationSpace.controllers.ModelisationSpaceController;
import components.modelisationSpace.moment.model.Moment;
import components.modelisationSpace.moment.model.RootMoment;
import components.schemaTree.Controllers.SchemaTreeController;
import components.interviewPanel.Controllers.InterviewPanelController;
import application.configuration.Configuration;
import components.interviewSelector.appCommands.InterviewSelectorCommandFactory;
import components.interviewSelector.controllers.InterviewSelectorController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

	private Project project;

	private @FXML SplitPane mainSplitPane;
	private @FXML SplitPane leftPane;
	private @FXML SplitPane paneOfTextArea;
	private @FXML ModelisationSpaceController modelisationSpaceController;

	private InterviewSelectorController interviewSelector;
	private InterviewPanelController interviewPanel;

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

		//Set components.interviewSelector
		if(interviewSelector != null)
			interviewSelector.unbind();
		InterviewSelectorCommandFactory interviewSelectorCommandfactory = new InterviewSelectorCommandFactory(project);
		interviewSelector = new InterviewSelectorController(project.interviewsProperty(), project.selectedInterviewProperty(), interviewSelectorCommandfactory);
		leftPane.getItems().add(InterviewSelectorController.createInterviewSelector(interviewSelector));

		//Set the interview panel
		if(interviewPanel != null)
			interviewPanel.unbind();
		interviewPanel = new InterviewPanelController(project.selectedInterviewProperty(), mainSplitPane);
		paneOfTextArea.getItems().add(InterviewPanelController.createInterviewPanel(interviewPanel));

		//Update the current interview
		interviewSelectorCommandfactory.selectCurrentInterview(project.getSelectedInterview()).execute();

		//Set the modelisation space
		RootMoment moment = new RootMoment();
		Moment m1 = new Moment("Main");
		Moment m2 = new Moment("Main2");

		Moment m3 = new Moment("Sub1");
		Moment m4 = new Moment("Sub2");
		m1.addMoment(m3);
		m1.addMoment(m4);

		moment.addMoment(m1);
		moment.addMoment(m2);

		modelisationSpaceController.setRootMoment(moment);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		refreshContent();
	}
}
