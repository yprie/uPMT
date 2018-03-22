package controller;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AboutController implements Initializable{

	private @FXML TextArea document;
	private @FXML Button buttonCloseHelp;

	private Main main;
	private Stage window;
	private TextArea doc;
	
//	public HelpController(Main main, Stage window, TextArea d) {
//		super();
//		this.main = main;
//		this.window = window;
//		this.doc = d;
//	}


	public AboutController(Main main, Stage window) {
		//super();
		this.main = main;
		this.window = window;
//		Text t = new Text();
//		t.setText("Micro-Phenomenology");
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
//		document.getContextMenu();
//		Text t = new Text();
//		t.setText("Micro-Phenomenology");

//		this.document.setText(main.getCurrentDescription().getDescripteme().getTexte().trim());
//		this.document.setEditable(false);
		buttonCloseHelp.setText(main._langBundle.getString("close"));
		//window.show();

	}
	
	public TextArea getDocument() {
		return document;
	}
	
	@FXML
    private void closeAbout() {
       window.close();
    };



	public void setDocument(TextArea document) {
		this.document = document;
		document.setEditable(false);
	}

}

