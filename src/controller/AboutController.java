package controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.ResourceLoader;

public class AboutController implements Initializable{

	private @FXML TextArea document;
	private @FXML Button buttonCloseHelp;
	private @FXML Button buttonLink2Github;

	private Main main;
	private Stage window;
	private TextArea doc;
	
	public AboutController(Main main, Stage window) {
		//super();
		this.main = main;
		this.window = window;
		
//		Text t = new Text();
//		t.setText("Micro-Phenomenology");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
//		this.document.setText(main.getCurrentDescription().getDescripteme().getTexte().trim());
//		this.document.setEditable(false);
		buttonCloseHelp.setText(main._langBundle.getString("close"));
		Image res = ResourceLoader.loadImage("github.png");
		ImageView icon = new ImageView(res);
		icon.prefWidth(10);
		icon.maxWidth(10);
		icon.maxHeight(10);
		icon.prefHeight(10);
		buttonLink2Github.setGraphic(icon);
//		window.show();
	}
		
	public void link2Github() {
		Stage web = new Stage();
		
		buttonLink2Github.setOnAction((ActionEvent action)->{ 
			try {
				java.awt.Desktop.getDesktop().browse(new URI("https://github.com/coco35700/uPMT"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
        });
	     //web.setAlwaysOnTop(true); 
	     web.centerOnScreen();
	     web.close(); 
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

