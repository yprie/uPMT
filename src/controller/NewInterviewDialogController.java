/*****************************************************************************
 * NewInterviewDialogController.java
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
import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.Main;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Accordion;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import model.Descripteme;
import model.DescriptionEntretien;
import utils.MainViewTransformations;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;

public class NewInterviewDialogController implements Initializable{

	private @FXML TextField nomEntretien;
	private @FXML TextField participantEntretien;
	private @FXML DatePicker dateEntretien;

	private @FXML Button btnChoisirFichir;
	private @FXML Button btnValider;
	private @FXML Button btnAnnuler;
	private @FXML Label nomFichier;
	private @FXML TitledPane collapse_step1;
	private @FXML TitledPane collapse_step2;
	private @FXML TitledPane collapse_step3;
	private @FXML TitledPane collapse_step4;
	private @FXML TitledPane collapse_step5;
	private @FXML Accordion accordion;
	private File fichierChoisi;
	
	private TitledPane current_step;
	private Main main;
	private Stage window;
	private BooleanProperty canCreate;
	
	public NewInterviewDialogController(Main main,Stage window) {
		this.main = main;
		this.window = window;
		canCreate = new SimpleBooleanProperty(true);
		
	}
	public NewInterviewDialogController(Main main) {
		this.main = main;
		this.window = window;
		canCreate = new SimpleBooleanProperty(true);
	}
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		this.btnValider.disableProperty().bind(canCreate);
		current_step = collapse_step1;
		collapse_step1.getContent().requestFocus();
		accordion.expandedPaneProperty().addListener(
            (ObservableValue<? extends TitledPane> ov, TitledPane old_val, 
            TitledPane new_val) -> {
                if (new_val != null) {
                	current_step = new_val;
                    String id = new_val.getId();
                    if(id.equals("collapse_step1")){
                    	nomEntretien.requestFocus();
                    }else if(id.equals("collapse_step2")){
                    	participantEntretien.requestFocus();
                    }else if(id.equals("collapse_step3")){
                    	dateEntretien.requestFocus();
                    }else if(id.equals("collapse_step4")){
                    	
                    }else if(id.equals("collapse_step5")){
                    	
                    }
                }
          });
	}
	
	@FXML
	public void onEnter(ActionEvent ae){
	   nextQuest();
	}
	
	public void nextQuest(){
		String id = current_step.getId();
		if(id.equals("collapse_step1")){
        	current_step.setExpanded(false);
        	current_step = collapse_step2;
        	current_step.setExpanded(true);
        }else if(id.equals("collapse_step2")){
        	current_step.setExpanded(false);
        	current_step = collapse_step3;
        	current_step.setExpanded(true);
        }else if(id.equals("collapse_step3")){
        	current_step.setExpanded(false);
        	current_step = collapse_step4;
        	current_step.setExpanded(true);
        }else if(id.equals("collapse_step4")){
        	current_step.setExpanded(false);
        	current_step = collapse_step5;
        	current_step.setExpanded(true);
        }else if(id.equals("collapse_step5")){
        	
        }
	}
	
	public void annulerClick(){
		window.close();
	}
	public void validerClick(){
		
		LocalDate d = dateEntretien.getValue();		
		String text = "";
	    try {
	    	String filePath = fichierChoisi.getPath();
	        BufferedReader br = new BufferedReader(new FileReader(filePath));
	        String line = br.readLine();
	        while (line != null || !line.equals(null)) {
		        text = text + line + "\n";
	            line=br.readLine();
	        }
	    } catch (Exception e) {
	    	System.out.println(e.getMessage());
	    }
	    System.out.println(text);
		
		DescriptionEntretien de = new DescriptionEntretien(new Descripteme(text), nomEntretien.getText());
		de.setDateEntretien(d);
		main.getCurrentProject().addEntretiens(de);
		main.setCurrentDescription(de);
		main.refreshDataTreeView();
		main.getMainViewController().addGridPaneInterview(de);
		main.getMainViewController().updateGrid();
		
		// in case the center was set to null because of automatic interview Creation
		if (main.getRootLayout().getCenter() == null) {
			main.launchMainView();
		}
		main.getCurrentProject().save();
		window.close();
	}
	
	public void openFileChooser(){
		FileChooser fileChooser = new FileChooser();
		ArrayList<String> l = new ArrayList<String>();
		l.add("*.txt");
		l.add("*.text");
		fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichiers texte",l)
            );
		fileChooser.setTitle("Choisir verbatim Entretien");
		fichierChoisi = fileChooser.showOpenDialog(null);
		if(fichierChoisi != null){
			canCreate.set(false);
		}
	}
}
