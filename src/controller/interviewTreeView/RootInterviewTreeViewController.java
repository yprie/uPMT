/*****************************************************************************
 * RootInterviewTreeViewController.java
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

package controller.interviewTreeView;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.DescriptionEntretien;
import utils.ResourceLoader;

public class RootInterviewTreeViewController extends TreeViewController implements Initializable {
	
	private @FXML Label nomRoot;
	private @FXML Button addInterview;
	private @FXML ImageView rootIcon;
	private DescriptionEntretien interview;
	private Main main;

	public RootInterviewTreeViewController(DescriptionEntretien interview,Main  main) {
		this.interview = interview;
		this.main = main;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nomRoot.setText(interview.getNom());

		Node icon = new ImageView(ResourceLoader.loadImage("new.png"));
		this.addInterview.setGraphic(icon);
		
		rootIcon.setImage(ResourceLoader.loadImage("rootInterview.gif"));
		
		Tooltip addInter = new Tooltip(main._langBundle.getString("add_interview"));
		addInterview.setTooltip(addInter);
	}

	@FXML
	public void newInterview(){
		main.getRootLayoutController().newInterview();
	}

	@Override
	protected void hideButtons() {
	}

	@Override
	protected void showButtons() {
	}
}
