/*****************************************************************************
 * SelectDescriptemePartController.java
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

package controller;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import controller.command.RenameMomentCommand;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import utils.UndoCollector;

public class SelectDescriptemePartController implements Initializable {
	
	private Main main;
	private Stage stage;
	private @FXML TextArea descriptemeArea;
	private @FXML Button acceptSelection;
	private TextArea inspectorText;

	public SelectDescriptemePartController(Main main, Stage s,TextArea t) {
		this.main = main;
		this.stage = s;
		this.inspectorText = t;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.descriptemeArea.setText(main.getCurrentDescription().getDescripteme().getTexte().trim());
		this.descriptemeArea.setEditable(false);
	}
	
	@FXML
	public void validateSelection(){
		if(this.descriptemeArea.getSelectedText().trim().length()!=0){
			RenameMomentCommand cmd = new RenameMomentCommand(main.getCurrentMoment().getMomentExtractController(),
					main.getCurrentMoment().getMoment().getMorceauDescripteme(),this.descriptemeArea.getSelectedText().trim());
			cmd.execute();
			UndoCollector.INSTANCE.add(cmd);
		}else{
			RenameMomentCommand cmd = new RenameMomentCommand(main.getCurrentMoment().getMomentExtractController(),
					main.getCurrentMoment().getMoment().getMorceauDescripteme(),null);
			cmd.execute();
			UndoCollector.INSTANCE.add(cmd);
		}
		stage.close();
	}
}
