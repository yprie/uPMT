package controller;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Type;
import utils.IStats;


public class StatsController implements Initializable{

	private @FXML Button buttonCloseStats;
	private @FXML Pane centralPane;

	private Main main;
	private Stage window;


	public StatsController(Main main, Stage window) {
		this.main = main;
		this.window = window;
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		IStats.getInstance().update(main);
		GridPane statsGrid = new GridPane();
		statsGrid.setAlignment(Pos.CENTER);
		statsGrid.setHgap(25);
		statsGrid.setVgap(15);
		
		
		/* init columns */
		for (int i = 0; i < IStats.getInstance().getCategories().size(); i++) {
			Label l_cat = new Label(IStats.getCategories().get(i).getName());
			l_cat.setMinWidth(Region.USE_PREF_SIZE);
			l_cat.setMaxWidth(Region.USE_PREF_SIZE);
			statsGrid.setHalignment(l_cat, HPos.CENTER);
			statsGrid.add(l_cat, i+1, 0);
		}
		
		/* init rows */
		for (int j = 0; j < IStats.getInstance().getInterviews().size(); j++) {
			Label l_int = new Label(IStats.getInterviews().get(j).getName());
			l_int.setMinWidth(Region.USE_PREF_SIZE);
			l_int.setMaxWidth(Region.USE_PREF_SIZE);
			statsGrid.setHalignment(l_int, HPos.CENTER);
			statsGrid.add(l_int, 0, j+1);
		}
		
		/* set values of for each columns and rows */
		for (int i = 0; i < IStats.getInstance().getInterviews().size(); i++) {
			for (int j = 0; j < IStats.getInstance().getCategories().size(); j++) {
				int val = IStats.getInstance().nbOccurrences(IStats.getInstance().getInterviews().get(i), IStats.getInstance().getCategories().get(j));
				Label l_val = new Label(String.valueOf(val));
				statsGrid.setHalignment(l_val, HPos.CENTER);
				statsGrid.add(l_val, j+1, i+1);
			}
				
		}
		
		/* display all */
		this.centralPane.getChildren().add(statsGrid);
		buttonCloseStats.setText(main._langBundle.getString("close"));
	}
	
	@FXML
    private void closeStats() {
		window.close();
    };
    
}

