package controller;

import java.awt.CheckboxMenuItem;
import java.awt.Insets;
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
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Category;
import model.DescriptionInterview;
import model.MomentExperience;
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
		float largeur = 100;
		for(MomentExperience moments : MomentComparaison.getmMoments()) {
			System.out.println(moments.getName());
			System.out.println("largeur moment 1 " + largeur);
			if(moments.getSubMoments().size()>0) {
				largeur = largeur/moments.getSubMoments().size();
				for(MomentExperience m : moments.getSubMoments()) {
					
					System.out.println(m.getName());
					System.out.println("largeur moment 2 " + largeur);
				}
			}
		}
		
		//statsGrid = new GridPane();
		//statsGrid.setAlignment(Pos.CENTER);
		//statsGrid.setStyle("-fx-padding:  15 0 0 0;");
		 HBox statsGrid = new HBox();
		 VBox statsGrid2 = new VBox();
	        
		 statsGrid.setSpacing(10);
		 //statsGrid.setPadding(new Insets(15,20, 10,10));
		
		TitledPane t1 = new TitledPane("T1", new Button("B1"));
		Accordion accordion = new Accordion();
	    accordion.getPanes().addAll(t1);
	    statsGrid.getChildren().add(accordion);
	    
		TitledPane t2 = new TitledPane("T2", new Button("B2"));
		Accordion accordion2 = new Accordion();
	    accordion2.getPanes().addAll(t2);
	    statsGrid.getChildren().add(accordion2);
	    //statsGrid.getChildren().add(statsGrid2);
	    
	    Button b = new Button("bla");
	    statsGrid.getChildren().add(b);
	    
		/* display all */
		this.centralPane.getChildren().add(statsGrid);
		
		buttonCloseStats.setText(main._langBundle.getString("close"));
		
	}
    
	


	
	@FXML
    private void closeStats() {
		window.close();
    };
    
}

