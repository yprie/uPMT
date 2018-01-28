package controller;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HelpController implements Initializable{

	private @FXML TextArea document;

	private Main main;
	private Stage window;
	private TextArea doc;
	
//	public HelpController(Main main, Stage window, TextArea d) {
//		super();
//		this.main = main;
//		this.window = window;
//		this.doc = d;
//	}


	public HelpController(Main main, Stage window) {
		super();
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
		window.show();
	}
	
	public TextArea getDocument() {
		return document;
	}


	public void setDocument(TextArea document) {
		this.document = document;
		document.setEditable(false);
	}

}

