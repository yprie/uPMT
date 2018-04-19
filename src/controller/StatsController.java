package controller;

import java.awt.CheckboxMenuItem;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import model.Category;
import model.DescriptionInterview;
import model.Property;
import model.Type;
import utils.IStats;


public class StatsController implements Initializable{

	private @FXML Button buttonCloseStats;
	
	private @FXML MenuItem allInterview;
	private @FXML MenuItem allCategories;
	private @FXML MenuItem allProperties;
	
	private @FXML Pane centralPane;
	
	private @FXML Menu interviewChoice;
	private @FXML Menu categoryChoice;
	private @FXML Menu propertyChoice;
	
	private ObservableList<Node[]> rows = FXCollections.observableArrayList();
	private ObservableList<Node[]> columns = FXCollections.observableArrayList();

	private Main main;
	private Stage window;
	private GridPane statsGrid;
	
	private int nextRowIndex = 1;
	private int nextColIndex = 1;

	public StatsController(Main main, Stage window) {
		this.main = main;
		this.window = window;
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		IStats.getInstance().update(main);
		statsGrid = new GridPane();
		statsGrid.setAlignment(Pos.CENTER);
		statsGrid.setHgap(25);
		statsGrid.setVgap(15);
		
		
		//interviewChoice.setItems(FXCollections.observableArrayList("All interviews", "Interview 1", "Interview 2"));
		//interviewChoice.setValue("All interviews");
		
		this.setInterviewMenu();
		this.setCategoryMenu();
		this.setPropertyMenu();
		
		allInterview.setOnAction(e -> selectAll(allInterview));
		allCategories.setOnAction(e -> selectAll(allCategories));
		allProperties.setOnAction(e -> selectAll(allProperties));
		
		
		
		/* propertyChoice.setItems(FXCollections.observableArrayList("All Properties", "Property 1", "Property 2"));
		propertyChoice.setValue("All properties");
		
		categoryChoice.setItems(FXCollections.observableArrayList("All categories", "Category 1", "Category 2"));
		categoryChoice.setValue("All categories"); */
		
		
		/* init columns */
		for (int i = 0; i < IStats.getInstance().getCategories().size(); i++) {
			
			Label l_cat = new Label(IStats.getCategories().get(i).getName());
			l_cat.setMinWidth(Region.USE_PREF_SIZE);
			l_cat.setMaxWidth(Region.USE_PREF_SIZE);
			statsGrid.setHalignment(l_cat, HPos.CENTER);
			statsGrid.add(l_cat, i+1, 0);
			//this.addNode(l_cat, i+1, 0);
			
			nextColIndex ++;
		}
		
		/* init rows */
		for (int j = 0; j < IStats.getInstance().getInterviews().size(); j++) {
			Label l_int = new Label(IStats.getInterviews().get(j).getName());
			l_int.setMinWidth(Region.USE_PREF_SIZE);
			l_int.setMaxWidth(Region.USE_PREF_SIZE);
			statsGrid.setHalignment(l_int, HPos.CENTER);
			statsGrid.add(l_int, 0, j+1);
			//this.addNode(l_int, 0, j+1);
			
			nextRowIndex ++;
		}
		
		/* set values of for each columns and rows */
		for (int i = 0; i < IStats.getInstance().getInterviews().size(); i++) {
			for (int j = 0; j < IStats.getInstance().getCategories().size(); j++) {
				int val = IStats.getInstance().nbOccurrences(IStats.getInstance().getInterviews().get(i), IStats.getInstance().getCategories().get(j));
				Label l_val = new Label(String.valueOf(val));
				statsGrid.setHalignment(l_val, HPos.CENTER);
				statsGrid.add(l_val, j+1, i+1);
				//this.addNode(l_val, j+1, i+1);
			}
				
		}
		
		/* display all */
		this.centralPane.getChildren().add(statsGrid);
		buttonCloseStats.setText(main._langBundle.getString("close"));
		//System.out.println(statsGrid.getChildren());
		
	}
	
	@FXML
    private void closeStats() {
		window.close();
    };
    
    
    
    private void setInterviewMenu() {
    	ArrayList<DescriptionInterview> list = IStats.getInstance().getInterviews();
    	for (DescriptionInterview descriptionInterview : list) {
    		CheckMenuItem add = new CheckMenuItem(descriptionInterview.getName());
    		add.setOnAction(e -> {
    			if (!add.isSelected()) {
    				this.deleteRow(add);
    			} else { this.addRow(add); }
    			
    		});
    		add.setSelected(true);
			interviewChoice.getItems().addAll(add);
		}
    }
    
    
    private void setPropertyMenu() {
    	ArrayList<Category> list = IStats.getInstance().getCategories();
    	for (Category category : list) {
    		for (Property p : category.getProperties()) {
        		CheckMenuItem add = new CheckMenuItem(category.getName() + " : " + p.getName());
        		add.setOnAction(e -> {
        			if (!add.isSelected()) {
        				this.deleteColumn(add);
        			} else { this.addColumn(add); }
        		});
        		add.setSelected(false);
    			propertyChoice.getItems().addAll(add);
			}
		}
    }
    
    
    private void setCategoryMenu() {
    	ArrayList<Category> list = IStats.getInstance().getCategories();
    	for (Category category : list) {
    		CheckMenuItem add = new CheckMenuItem(category.getName());
    		add.setOnAction(e -> {
    			if (!add.isSelected()) {
    				this.deleteColumn(add);
    			} else { this.addColumn(add); }
    			
    		});
    		add.setSelected(true);
			categoryChoice.getItems().addAll(add);
		}
    }
    
    
    private void selectAll(MenuItem menu) {
    	
    	for (MenuItem menuItem : menu.getParentMenu().getItems()) {
			if(menuItem.getClass().equals((new CheckMenuItem()).getClass())) {
				if (!((CheckMenuItem) menuItem).isSelected()) {
					((CheckMenuItem) menuItem).setSelected(true);
					if (menu.equals(allInterview)) {
						addRow((CheckMenuItem) menuItem);
					}
					else {
						addColumn((CheckMenuItem) menuItem);
					}
				}
			}
		}
    }
    
    
    
    
    
    /* Functions for updating the gridpane */
    
    
    private void addColumn(CheckMenuItem item) {
    	System.out.println("Ajout colonne");
    	
    }
    
    private void deleteColumn(CheckMenuItem item) {
    	Label label = findCell(item);
    	statsGrid.getChildren().removeIf(node -> GridPane.getColumnIndex(node) == GridPane.getColumnIndex(label));
    }
    
    private void addRow(CheckMenuItem item) {
    	System.out.println("Ajout ligne");
    	
    }
    
    private void deleteRow(CheckMenuItem item) {
    	Label label = findCell(item);
    	statsGrid.getChildren().removeIf(node -> GridPane.getRowIndex(node) == GridPane.getRowIndex(label));    	
    	
    }
    
    
    /* find column and row */
    
    private Label findCell(CheckMenuItem item) {
    	for (Node label : statsGrid.getChildren()) {
    		
    		if(((Label) label).getText() == item.getText()) {
    			
    			return ((Label) label);
    		}
			
		}
    	return null;	
    }
    
    /* private void addNode(Node node, int indexR, int indexC) {
    	this.rows.get(indexR)[indexC] = node;
    	this.columns.get(indexC)[indexR] = node;
    } */
}

