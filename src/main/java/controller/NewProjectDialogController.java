/*****************************************************************************
 * NewProjectDialogController.java
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
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import model.Project;
import model.Schema;

public class NewProjectDialogController implements Initializable{

	private @FXML Button valider;
	private @FXML Button cancel;
	private @FXML ChoiceBox<Schema> choixSchema;
	private @FXML TextField nomProjet;
	
	private Main main;
	private Stage window;
	private Stage launchingScreenWindow;

	public NewProjectDialogController(Main main,Stage window,Stage launchingScreenWindow) {
		this.main = main;
		this.window = window;
		this.launchingScreenWindow = launchingScreenWindow;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		choixSchema.getItems().add(main.getDefaultSchema());
		for (Project p : main.getProjects()) {
			choixSchema.getItems().add(p.getSchema());
		}
		// change the text to make it pretty
		choixSchema.setConverter(new StringConverter<Schema>() {
			
			@Override
			public String toString(Schema object) {
				// TODO Auto-generated method stub
				return object.getName();
			}
			
			@Override
			public Schema fromString(String string) {
				// TODO Auto-generated method stub
				return new Schema(string);
			}
		});
		choixSchema.setValue(main.getDefaultSchema());
	}
	
	public void createProject(){
		Project p = new Project(nomProjet.getText(), this.choixSchema.getValue());
		p.getSchema().setName(nomProjet.getText());
		main.setProjectInCreation(p);
		window.close();
		launchingScreenWindow.close();		
	}
	
	public void closeWindow(){
		window.close();
	}

}
