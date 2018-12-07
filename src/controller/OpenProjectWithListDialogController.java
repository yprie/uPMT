/*****************************************************************************
 * OpenProjectWithListDialogController.java
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
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

import application.Main;
import model.Project;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.stage.*;
import javafx.util.Callback;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class OpenProjectWithListDialogController implements Initializable{

	private @FXML ListView<Project> tousLesProjets;
	private @FXML Button btn_ouvrir;
	private @FXML Button btn_cancel;
	private LinkedList<Project> m_projectList;
	private Main main;
	private Stage window;
	
	public OpenProjectWithListDialogController(Main main,Stage window){
		this.main = main;
		m_projectList = main.getProjects();
		this.window = window;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ObservableList<Project> items = FXCollections.observableArrayList (m_projectList);
		tousLesProjets.setItems(items);
		tousLesProjets.getFocusModel().focus(0);
	}
	
	public void closeDialog(){
		window.close();
	}
	
	public void openProject(){
		window.close();
		main.getPrimaryStage().show();
		main.setCurrentProject(tousLesProjets.getFocusModel().getFocusedItem());
		
	}
}
