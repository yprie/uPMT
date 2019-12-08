/*****************************************************************************
 * SelectDescriptemePartController.java
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

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import controller.command.ChangeExtractPropertyCommand;
import controller.command.RenameMomentCommand;
import controller.controller.PropertyExtractController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import model.Record;
import utils.UndoCollector;

public class SelectDescriptemePartController implements Initializable {
	
	
	private Main main;
	private Stage stage;
	private @FXML TextArea descriptemeArea;
	private @FXML Button acceptSelection, cancelButton;
	private String inspectorText;
	private String text = "";

	
	public SelectDescriptemePartController(Main main, Stage s) {
		this.main = main;
		this.stage = s;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.descriptemeArea.setText(main.getCurrentDescription().getDescripteme().getTexte().trim());
		this.descriptemeArea.setEditable(false);
	}
	
	@FXML
	public void validateSelection(){
		if(this.descriptemeArea.getSelectedText().trim().length()!=0)
			text = this.descriptemeArea.getSelectedText().trim();
		stage.close();
	}
	
	@FXML
	public void cancel() {
		stage.close();
	}
	
	public String getText() {
		return text;
	}
}
