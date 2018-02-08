package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.Main;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Descripteme;
import model.DescriptionEntretien;
import model.Projet;
import utils.MainViewTransformations;
import javafx.application.Platform;


public class NewInterviewPane implements Initializable{

	private @FXML TextField nameId;
	private @FXML TextField partiEnt;
	private @FXML DatePicker dateEnt;

	private @FXML Button btnSelectFile;
	private @FXML Button btnVeri;
	private @FXML Button btnAnnu;
	private @FXML Label nomFichier;
	private @FXML Label newIntFileName;
	private File fichierChoisi;
	
	
	private Main main;
	private Stage window;
	private BooleanProperty canCreate;
	
	public NewInterviewPane(Main main,Stage window) {
		this.main = main;
		this.window = window;
		canCreate = new SimpleBooleanProperty(true);
		
	}
	public NewInterviewPane(Main main) {
		this.main = main;
		this.window = window;
		canCreate = new SimpleBooleanProperty(true);
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
//		this.btnVeri.disableProperty().bind(canCreate);
//		nameId.requestFocus();
//		partiEnt.requestFocus();
//		dateEnt.requestFocus();
		window.show();
	}
	
	
	public void annulerClick(){
		window.close();
	}
	
	public void verifierClick(){
		LocalDate d = dateEnt.getValue();	
		String text="";
		try {
			File fileDir =fichierChoisi;

			BufferedReader br = new BufferedReader(
			   new InputStreamReader(
	                      new FileInputStream(fileDir), "UTF8"));

			
			String line = br.readLine();
	        while (line != null || !line.equals(null)) {
		        text = text + line + "\n";
	            line=br.readLine();
	        }

	                br.close();
		    }catch (Exception e) {
	    	System.out.println(e.getMessage());
	    }
	    //System.out.println(text);
		
		DescriptionEntretien de = new DescriptionEntretien(new Descripteme(text), nameId.getText());
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
		main.launchMainView();
		main.refreshDataTreeView();
		main.needToSave();
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
			newIntFileName.setText(fichierChoisi.getPath());
			canCreate.set(false);
		}
		else {
			newIntFileName.setText("/");
			canCreate.set(true);
		}
	}
}
