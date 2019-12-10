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

package application.MainView.Controller;

import Project.Models.Project;
import SchemaTree.SchemaTree;
import application.Configuration.Configuration;
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
	private @FXML SchemaTree schemaTree;
	private @FXML TreeView<String> treeViewInterview;

	public static Node createMainView(MainViewController controller) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(controller.getClass().getResource("../Views/MainView.fxml"));
			loader.setController(controller);
			loader.setResources(Configuration.langBundle);
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
		//Default interview tree for the moment.
		treeViewInterview = new TreeView<>();
		treeViewInterview.setRoot(new TreeItem<String>("Interviews"));

		schemaTree.setTreeRoot(project.getSchemaTreeRoot());
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		refreshContent();
	}
}
