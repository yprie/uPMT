/*****************************************************************************
 * InterviewTreeView.java
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

import java.io.IOException;
import java.util.Optional;

import application.Main;
import controller.MomentExpVBox;
import controller.TypeClassRepresentationController;
import controller.command.RemoveTypeCommand;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.TreeCell;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import model.Classe;
import model.DescriptionEntretien;
import utils.MainViewTransformations;

public class InterviewTreeView extends TreeCell<DescriptionEntretien>{
	
	private Main main;
	private TreeViewController controller;

	public InterviewTreeView(Main main) {
		this.main = main;
	}
	
	// override start edit to avoid glitches
	@Override
	public void startEdit() {
	} 
	
	public void deleteInterview(DescriptionEntretien interview){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Supression Entretien");
		alert.setHeaderText("Vous allez supprimer "+interview.getNom());
		
		ButtonType buttonTypeOne = new ButtonType("Valider");
		ButtonType buttonTypeCancel = new ButtonType("Annuler", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeOne){
			main.getCurrentProject().getEntretiens().remove(interview);
			this.getTreeItem().getParent().getChildren().remove(this.getTreeItem());
		}
	}
	
    @Override
    public void updateSelected(boolean selected) {
    	super.updateSelected(selected);
    	if (selected) {
        	this.controller.showButtons();
		}
    	else{
    		this.controller.hideButtons();
    	}
    }
	
	@Override
    protected void updateItem(DescriptionEntretien elem, boolean empty) {
		super.updateItem(elem, empty);
		
		if(empty || elem == null) {
	        setText(null);
	        setGraphic(null);
		}
		else{
			// if elem is an interview of the project
			DescriptionEntretien desc = ((DescriptionEntretien)elem);
			try {
		    	FXMLLoader loader = new FXMLLoader();
		        loader.setLocation(getClass().getResource("/view/EntretienTreeView.fxml"));
		        InterviewTreeViewController controller =  new InterviewTreeViewController(desc,this);
		        this.controller = controller;
		        loader.setController(controller);
		        BorderPane elementPane = (BorderPane) loader.load();
		        this.setGraphic(elementPane);
	    	}
	    	catch (IOException e) {
	    		e.printStackTrace();
	    	}
			
			if (desc.getNom() == "Entretiens") {
				try {
			    	FXMLLoader loader = new FXMLLoader();
			        loader.setLocation(getClass().getResource("/view/RootEntretienTreeView.fxml"));
			        RootInterviewTreeViewController controller =  new RootInterviewTreeViewController(desc,main);
			        this.controller = controller;
			        loader.setController(controller);
			        BorderPane elementPane;
					elementPane = (BorderPane) loader.load();
			        this.setGraphic(elementPane);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else
			{
				this.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						main.setCurrentDescription(desc);
						main.getTreeViewInterview().refresh();
						main.getMainViewController().updateGrid();
						main.getMainViewController().closeInspector();
					}
				});
				
				if((!main.getCurrentProject().getEntretiens().isEmpty()) && desc.equals(main.getCurrentDescription())){
					((InterviewTreeViewController)controller).setSelected("blue");
				}else{
					((InterviewTreeViewController)controller).setSelected("black");
				}
			}
		}
	}

}
