/*****************************************************************************
 * InterviewTreeView.java
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

package controller.interviewTreeView;

import java.io.IOException;
import java.util.Optional;

import application.Main;
import controller.MomentExpVBox;
import controller.TypeCategoryRepresentationController;
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
import model.Category;
import model.DescriptionInterview;
import utils.MainViewTransformations;

public class InterviewTreeView extends TreeCell<DescriptionInterview>{
	
	private Main main;
	private TreeViewController controller;

	public InterviewTreeView(Main main) {
		this.main = main;
	}
	
	// override start edit to avoid glitches
	@Override
	public void startEdit() {
	} 
	
	protected Main getMain() {
		return main;
	}
	
	public void deleteInterview(DescriptionInterview interview){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(main._langBundle.getString("delete_interview"));
		alert.setHeaderText(main._langBundle.getString("delete_interview_text_alert")+interview.getName());
		
		ButtonType buttonTypeOne = new ButtonType(main._langBundle.getString("confirm"));
		ButtonType buttonTypeCancel = new ButtonType(main._langBundle.getString("cancel"), ButtonData.CANCEL_CLOSE);
 
		alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeOne){
			main.getCurrentProject().removeEntretiens(interview);
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
    protected void updateItem(DescriptionInterview elem, boolean empty) {   // function to use
		super.updateItem(elem, empty);
		
		if(empty || elem == null) {
	        setText(null);
	        setGraphic(null);
		}
		else{
			// if elem is an interview of the project
			DescriptionInterview desc = ((DescriptionInterview)elem);
			try {
		    	FXMLLoader loader = new FXMLLoader();
		        loader.setLocation(getClass().getResource("/view/EntretienTreeView.fxml"));
		        InterviewTreeViewController controller =  new InterviewTreeViewController(desc,this);
		        this.controller = controller;
		        System.out.println("controller "+desc);
		        
		        
		        
		        loader.setController(controller);
		        BorderPane elementPane = (BorderPane) loader.load();
		        this.setGraphic(elementPane);
		        
		        if(!desc.getName().equals("Interviews")) {
					
		        }
	    	}
	    	catch (IOException e) {
	    		e.printStackTrace();
	    	}
			if (desc.getName().equals("Interviews")) {
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
				function_test(desc);
				/*main.setCurrentDescription(desc);
				main.getTreeViewInterview().refresh();
				main.getMainViewController().updateGrid();*/
			}
		}
	}
	
	
	public void function_test(DescriptionInterview desc) {
		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				main.setCurrentDescription(desc);
				System.out.println("yoo");
				main.getTreeViewInterview().refresh();
				main.getMainViewController().updateGrid();
				
				// The event for changing the interviews
				//((InterviewTreeViewController)controller).setSelected("blue");
				

				
				
			}
		});
		
		if((!main.getCurrentProject().getInterviews().isEmpty()) && desc.equals(main.getCurrentDescription())){
			System.out.println("blue");
			((InterviewTreeViewController)controller).setSelected("blue");
		}else{
			System.out.println("black");
			((InterviewTreeViewController)controller).setSelected("black");
		}
	}

}
