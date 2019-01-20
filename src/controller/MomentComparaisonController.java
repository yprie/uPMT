package controller;

import java.awt.CheckboxMenuItem;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Category;
import model.DescriptionInterview;
import model.Type;
import utils.IStats;
import utils.MomentComparaison;


public class MomentComparaisonController implements Initializable{

	private @FXML Button buttonCloseStats;
	
	private @FXML Pane centralPane;

	private Main main;
	private Stage window;
	private GridPane statsGrid;
	
	public MomentComparaisonController(Main main, Stage window) {
		this.main = main;
		this.window = window;
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		MomentComparaison.getInstance().update(main);
		statsGrid = new GridPane();
		statsGrid.setAlignment(Pos.CENTER);
		statsGrid.setStyle("-fx-padding:  15 0 0 0;");
		

		/* display all */
		this.centralPane.getChildren().add(statsGrid);
		buttonCloseStats.setText(main._langBundle.getString("close"));
		
	}
    
	


	
	@FXML
    private void closeStats() {
		window.close();
    };
    
}

