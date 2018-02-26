package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.Main;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.InputMethodEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Descripteme;
import model.DescriptionEntretien;

public class NewInterviewDialogTestController  implements Initializable{

	private @FXML TextField nomEntretien;
	private @FXML TextField participantEntretien;
	private @FXML TextArea commentaireEntretien;
	private @FXML DatePicker dateEntretien;

	private @FXML Button btnChoisirFichir;
	private @FXML Button btnValider;
	private @FXML Button btncancel;
	private @FXML Label newEntrFileName;
	private @FXML Label extraitFile;	
	
	private TitledPane current_step;
	private Main main;
	private Stage window;
	private File fichierChoisi;
	
	public NewInterviewDialogTestController(Main main,Stage window) {
		this.main = main;
		this.window = window;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		checkIfCanCreate();
		nomEntretien.textProperty().addListener((observable, oldValue, newValue) -> {
			checkIfCanCreate();
		});
		
		participantEntretien.setOnAction(new EventHandler<ActionEvent>(){ 
			public void handle(ActionEvent event){	
				if(nomEntretien.getText().equals("") && !"".equals(dateEntretien.getValue().toString())) {
					nomEntretien.setText(participantEntretien.getText() 
							+ "_" + dateEntretien.getValue().toString());
				}
				}});
		
		dateEntretien.setOnAction(new EventHandler<ActionEvent>(){ 
			public void handle(ActionEvent event){	
//				LocalDate date = dateEntretien.getValue();
				if(nomEntretien.getText().equals("") && !"".equals(participantEntretien.getText())) {
					nomEntretien.setText(participantEntretien.getText()
							+ "_" + dateEntretien.getValue().toString());
				}
				}});
	}
	
	public void openFileChooser(){
		FileChooser fileChooser = new FileChooser();
		ArrayList<String> l = new ArrayList<String>();
		l.add("*.txt");
		l.add("*.text");
		fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(main._langBundle.getString("text_files"),l)
            );
		fileChooser.setTitle(main._langBundle.getString("select_verbatim"));
		fichierChoisi = fileChooser.showOpenDialog(null);
		if(fichierChoisi != null){
			newEntrFileName.setText(fichierChoisi.getPath());
			
//			if(nomEntretien.getText().equals("")&& dateEntretien.getValue().toString()!= null) {
//				nomEntretien.setText(fichierChoisi.getName().replaceFirst("[.][^.]+$", "")
//						+ "_" + dateEntretien.getValue().toString());
//			}
			extraitFile.setText(getExtrait());
			checkIfCanCreate();
		}
		else {
			newEntrFileName.setText("/");
			extraitFile.setText("");
			checkIfCanCreate();
		}
	}

	private void checkIfCanCreate() {
		btnValider.setDisable(		newEntrFileName.getText().equals("/")
								|| 	nomEntretien.getText().replaceAll(" ", "").equals("")
							 );
	}
	
	private String getExtrait() {
		String ret = "";
		BufferedReader br = null;
		FileReader fr = null;
		try {
			br = new BufferedReader(
					   new InputStreamReader(
			                      new FileInputStream(newEntrFileName.getText()), "UTF8"));
			String sCurrentLine;
			int i=0;
			while ((sCurrentLine = br.readLine()) != null && i<3) {
				if(i!=0)ret+=" ";
				ret += sCurrentLine;
				i++;
			}
		} catch (IOException e) {}
		finally {
			try {
				if (br != null)br.close();
				if (fr != null)fr.close();
			} catch (IOException ex) {}
		}
		return ret;
	}
	
	public void annulerClick(){
		window.close();
	}
	
	public void validerClick(){
		LocalDate d = dateEntretien.getValue();	
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
	    //System.out.println(e.getMessage());
	    }
	    //System.out.println(text);
		
		DescriptionEntretien de = new DescriptionEntretien(new Descripteme(text), nomEntretien.getText());
		de.setDateEntretien(d);
		if(!participantEntretien.getText().replaceAll(" ", "").equals(""))
			de.setParticipant(participantEntretien.getText());
		if(!commentaireEntretien.getText().replaceAll(" ", "").equals(""))
			de.setcommentaire(commentaireEntretien.getText());
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
}
