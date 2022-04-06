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

import application.configuration.AppSettings;
import components.schemaTree.Services.categoryUsesCounter.SchemaCategoryUsesCounter;
import components.schemaTree.Services.propertyUsesCounter.SchemaPropertyUsesCounter;
import components.toolbox.initalizable.ToolBoxControllers;
import javafx.scene.layout.HBox;
import models.Project;
import models.Interview;
import components.modelisationSpace.controllers.ModelisationSpaceController;
import components.schemaTree.Controllers.SchemaTreeController;
import components.interviewPanel.Controllers.InterviewPanelController;
import application.configuration.Configuration;
import components.interviewSelector.appCommands.InterviewSelectorCommandFactory;
import components.interviewSelector.controllers.InterviewSelectorController;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

	private Project project;

	private @FXML SplitPane mainSplitPane;
	private @FXML SplitPane leftPane;
	private @FXML HBox paneOfTextArea;
	private @FXML ModelisationSpaceController modelisationSpaceController;
	private @FXML ToolBoxControllers toolBox;
	private @FXML HBox modelMomentBox;
	private @FXML Button btnZoomMinus;
	private @FXML Button btnZoomPlus;
	private @FXML Button btnModelisationFull;
	private @FXML Button btnScreenShared;
	private @FXML Button btnTextAreaFull;
	private @FXML VBox boxModelisationSpace;

	private InterviewSelectorController interviewSelector;
	private InterviewPanelController interviewPanel;
	private ToolBoxControllers toolBoxControllers;

	private ChangeListener<Interview> onSelectedInterviewChanges = (observableValue, o, t1) -> {
		if(t1 != null){
			modelisationSpaceController.setRootMoment(t1.getRootMoment());
		}
		else {
			modelisationSpaceController.setRootMoment(null);
		}
	};

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
		paneOfTextArea.getChildren().add(InterviewPanelController.createInterviewPanel(interviewPanel));

		interviewSelectorCommandfactory.selectCurrentInterview(project.getSelectedInterview(), false).execute();
		if(project.getSelectedInterview() != null)
			modelisationSpaceController.setRootMoment(project.getSelectedInterview().getRootMoment());

		btnZoomMinus.setOnAction((event -> {
			if (AppSettings.zoomLevelProperty.get() >= 10) {
				AppSettings.zoomLevelProperty.set(AppSettings.zoomLevelProperty.get() - 10);
			}
		}));

		btnZoomPlus.setOnAction((event -> {
			if (AppSettings.zoomLevelProperty.get() <= 190) {
				AppSettings.zoomLevelProperty.set(AppSettings.zoomLevelProperty.get() + 10);
			}
		}));

		btnModelisationFull.setOnAction((event -> {
			mainSplitPane.setDividerPosition(1,1);
		}));

		btnTextAreaFull.setOnAction((event -> {
			mainSplitPane.setDividerPosition(1,0.0);
		}));

		btnScreenShared.setOnAction(((event -> {
			mainSplitPane.setDividerPosition(1,0.7);
		})));

		// Initialisation de la ToolBox avec tout ce qui suit (TemplateSpaceController)
		ToolBoxControllers toolBoxControllers = new ToolBoxControllers();
		toolBox.getChildren().add(toolBoxControllers.createToolBoxControllers(toolBoxControllers, project));
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		refreshContent();
		project.selectedInterviewProperty().addListener(onSelectedInterviewChanges);

		SchemaCategoryUsesCounter service = new SchemaCategoryUsesCounter(project, modelisationSpaceController.getHooks());
		SchemaPropertyUsesCounter service2 = new SchemaPropertyUsesCounter(project, modelisationSpaceController.getHooks());
	}

	public ModelisationSpaceController getModelisationSpaceController(){
		return this.modelisationSpaceController;
	}
}
