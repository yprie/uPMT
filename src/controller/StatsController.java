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
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Type;
import utils.IStats;


public class StatsController implements Initializable{

	private @FXML GridPane grid;
	private @FXML Button buttonCloseStats;

	private Main main;
	private Stage window;


	public StatsController(Main main, Stage window) {
		this.main = main;
		this.window = window;
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		IStats.getInstance().setParameters(main);
		IStats.update();
		//System.out.println(IStats.getCategories());
		//System.out.println(IStats.getInterviews());
		buttonCloseStats.setText(main._langBundle.getString("close"));
	}
	
	@FXML
    private void closeStats() {
       window.close();
    };

}

