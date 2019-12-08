package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import SchemaTree.Cell.Models.ICategoryAdapter;
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
import javafx.stage.Stage;
import model.DescriptionInterview;
import utils.IStats;


public class StatsCategoryController implements Initializable{

	private @FXML Button buttonCloseStats;
	
	private @FXML MenuItem allInterviews;
	private @FXML MenuItem allCategories;
	private @FXML MenuItem noInterview;
	private @FXML MenuItem noCategory;
	
	private @FXML Pane centralPane;
	
	private @FXML Menu interviewChoice;
	private @FXML Menu categoryChoice;
	
	private ObservableList<Node[]> rows = FXCollections.observableArrayList();
	private ObservableList<Node[]> columns = FXCollections.observableArrayList();
	private ArrayList<Integer> total;

	private Main main;
	private Stage window;
	private GridPane statsGrid;
	
	public StatsCategoryController(Main main, Stage window) {
		this.main = main;
		this.window = window;
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		IStats.getInstance().update(main);
		statsGrid = new GridPane();
		statsGrid.setAlignment(Pos.CENTER);
		statsGrid.setStyle("-fx-padding:  15 0 0 0;");
		
		
		//interviewChoice.setItems(FXCollections.observableArrayList("All interviews", "Interview 1", "Interview 2"));
		//interviewChoice.setValue("All interviews");
		
		this.setInterviewMenu();
		this.setCategoryMenu();
		
		allInterviews.setOnAction(e -> selectAll(allInterviews, true));
		allCategories.setOnAction(e -> selectAll(allCategories, true));
		noInterview.setOnAction(e -> selectAll(noInterview, false));
		noCategory.setOnAction(e -> selectAll(noCategory, false));
		
		
		/* categoryChoice.setItems(FXCollections.observableArrayList("All categories", "Category 1", "Category 2"));
		categoryChoice.setValue("All categories"); */
		
		
		/* init columns = categories */
		for (int i = 0; i < IStats.getInstance().getCategories().size(); i++) {
			
			ICategoryAdapter category = IStats.getCategories().get(i);
			Label l_cat = new Label(category.getName());
			l_cat.setMinWidth(Region.USE_PREF_SIZE);
			l_cat.setMaxWidth(Region.USE_PREF_SIZE);
			l_cat.setStyle("-fx-padding: 15 10 15 10; -fx-font-weight: bold; -fx-text-fill: " + category.getColor() + ";" );
			statsGrid.setHalignment(l_cat, HPos.CENTER);
			statsGrid.add(l_cat, i+1, 0);
			//this.addNode(l_cat, i+1, 0);
			
		}
		
			
		total = new ArrayList<Integer>(Collections.nCopies(IStats.getInstance().getCategories().size(), 0));
		
		
		/* set values of for each columns and rows */
		for (int i = 0; i <= IStats.getInstance().getInterviews().size(); i++) {
			
			
			/* TOTAL */
			
			if(i == IStats.getInstance().getInterviews().size()) {
				
				/* set total label */
				Label l_tot = new Label("Total");
				l_tot.setMinWidth(Region.USE_PREF_SIZE);
				l_tot.setMaxWidth(Region.USE_PREF_SIZE);
				l_tot.setStyle("-fx-padding: 10 15 10 15; -fx-font-weight: bold;");
				statsGrid.setHalignment(l_tot, HPos.CENTER);
				statsGrid.add(l_tot, 0, i+1);
				
				/* total frequency values */
				for(int j = 0; j < IStats.getInstance().getCategories().size(); j++) {
					Label l_val = new Label(String.valueOf(total.get(j)));
					l_val.setStyle("-fx-padding: 10 10 10 10; -fx-font-weight: bold;");
					statsGrid.setHalignment(l_val, HPos.CENTER);
					statsGrid.add(l_val, j+1, i+1);
				}
				
			}
			
			/* INTERVIEWS */
			else {
				
				/* set interview label */ 
				Label l_int = new Label(IStats.getInterviews().get(i).getName());
				l_int.setMinWidth(Region.USE_PREF_SIZE);
				l_int.setMaxWidth(Region.USE_PREF_SIZE);
				l_int.setStyle("-fx-padding: 5 15 5 15;");
				statsGrid.setHalignment(l_int, HPos.CENTER);
				statsGrid.add(l_int, 0, i+1);
				
				/* frequency values */
				for (int j = 0; j < IStats.getInstance().getCategories().size(); j++) {
					int val = IStats.getInstance().nbOccurrences(IStats.getInstance().getInterviews().get(i), IStats.getInstance().getCategories().get(j));
					Label l_val = new Label(String.valueOf(val));
					l_val.setStyle("-fx-padding: 5 10 5 10;");
					statsGrid.setHalignment(l_val, HPos.CENTER);
					statsGrid.add(l_val, j+1, i+1);
					total.set(j, total.get(j)+val);
				}
				
			}
				
		}
		
		/* display all */
		this.centralPane.getChildren().add(statsGrid);
		buttonCloseStats.setText(main._langBundle.getString("close"));
		
	}
    
	
	
	
	///* set the check menu actions and the initial status (selected) *////
    
    private void setInterviewMenu() {
    	ArrayList<DescriptionInterview> list = IStats.getInstance().getInterviews();
    	for (DescriptionInterview descriptionInterview : list) {
    		CheckMenuItem add = new CheckMenuItem(descriptionInterview.getName());
    		add.setOnAction(e -> {
    			if (!add.isSelected()) {
    				this.deleteInterviewRow(add);
    			} else { this.addInterviewRow(add); }
    			
    		});
    		add.setSelected(true);
			interviewChoice.getItems().addAll(add);
		}
    }
    
    
    private void setCategoryMenu() {
    	ArrayList<ICategoryAdapter> list = IStats.getInstance().getCategories();
    	for (ICategoryAdapter category : list) {
    		CheckMenuItem add = new CheckMenuItem(category.getName());
    		add.setOnAction(e -> {
    			if (!add.isSelected()) {
    				this.deleteCategoryColumn(add);
    			} else { this.addCategoryColumn(add); }
    			
    		});
    		add.setSelected(true);
			categoryChoice.getItems().addAll(add);
		}
    }
    
    
    
    

    
    
    
    
    ///* Functions for updating the gridpane *///
    
    
    private void selectAll(MenuItem menu, boolean bool) {
    	
    	for (MenuItem menuItem : menu.getParentMenu().getItems()) {
			
    		if(menuItem.getClass().equals((new CheckMenuItem()).getClass())) {
				
    			if (((CheckMenuItem) menuItem).isSelected() != bool) {
				
					((CheckMenuItem) menuItem).setSelected(bool);
					
					if (menu.equals(allInterviews)) {
						addInterviewRow((CheckMenuItem) menuItem);
					} 
					
					else if (menu.equals(noInterview)) {
						deleteInterviewRow((CheckMenuItem) menuItem);
					}
					
					else if (menu.equals(allCategories)) {
						addCategoryColumn((CheckMenuItem) menuItem);
					}
					
					else if (menu.equals(noCategory)) {
						deleteCategoryColumn((CheckMenuItem) menuItem);
					}
					
				}
			}
		}
    }
    
    
    
    
    private void addCategoryColumn(CheckMenuItem item) {
    	
    	Label l_cat = new Label(item.getText());
    	ICategoryAdapter category = IStats.getInstance().getCategory(l_cat.getText());
    	
		l_cat.setMinWidth(Region.USE_PREF_SIZE);
		l_cat.setMaxWidth(Region.USE_PREF_SIZE);
		l_cat.setStyle("-fx-padding: 15 10 15 10; -fx-font-weight: bold; -fx-text-fill: " + category.getColor() + ";" );
		

    	int i = 1;
    	int index = IStats.getInstance().getCategories().indexOf(category); //find the index of the column removed
		
		statsGrid.setHalignment(l_cat, HPos.CENTER);
		statsGrid.add(l_cat, index + 1 , 0);
		
		for (MenuItem menuItem : interviewChoice.getItems()) {  //we look for all interviews
			if(menuItem.getClass().equals((new CheckMenuItem()).getClass())) {  //if it is a CheckMenuItem (pblm with AllInterviews option)
				if (((CheckMenuItem) menuItem).isSelected()) { 					//if it is selected, then we add it.
					int val = IStats.getInstance().nbOccurrences(IStats.getInstance().getInterview(menuItem.getText()), IStats.getInstance().getCategory(l_cat.getText()));
					Label l_val = new Label(String.valueOf(val));
					l_val.setStyle("-fx-padding: 5 10 5 10;");
					statsGrid.setHalignment(l_val, HPos.CENTER);
					statsGrid.add(l_val, index + 1, i );
					i++;
				}
			}
		}
		
		Label l_tot = new Label(String.valueOf(total.get(index)));
		l_tot.setStyle("-fx-padding: 10 10 10 10; -fx-font-weight: bold;");
		statsGrid.setHalignment(l_tot, HPos.CENTER);
		statsGrid.add(l_tot, index+1, i);
		
    }
    
    
    
    private void deleteCategoryColumn(CheckMenuItem item) {
    	Label label = findNodeGridPane(item);
    	statsGrid.getChildren().removeIf(node -> GridPane.getColumnIndex(node) == GridPane.getColumnIndex(label));
    	//ArrayList<Node> column = this.findColumn(item);
    	//statsGrid.getChildren().removeAll(column);
    	

		
    }
    
    private void addInterviewRow(CheckMenuItem item) {
    	
    	Label l_int = new Label(item.getText());
		l_int.setMinWidth(Region.USE_PREF_SIZE);
		l_int.setMaxWidth(Region.USE_PREF_SIZE);
		l_int.setStyle("-fx-padding: 5 15 5 15;");

    	int i = 1;
    	int index = IStats.getInstance().getInterviews().indexOf(IStats.getInstance().getInterview(l_int.getText()));
    	
		statsGrid.setHalignment(l_int, HPos.CENTER);
		statsGrid.add(l_int, 0, index + 1);
		
		for (MenuItem menuItem : categoryChoice.getItems()) {  //we look for all interviews
			if(menuItem.getClass().equals((new CheckMenuItem()).getClass())) {  //if it is a CheckMenuItem (pblm with AllInterviews option)
				if (((CheckMenuItem) menuItem).isSelected()) { 					//if it is selected, then we add it.
					int val = IStats.getInstance().nbOccurrences(IStats.getInstance().getInterview(l_int.getText()), IStats.getInstance().getCategory(menuItem.getText()));
					Label l_val = new Label(String.valueOf(val));
					l_val.setStyle("-fx-padding: 5 10 5 10;");
					statsGrid.setHalignment(l_val, HPos.CENTER);
					statsGrid.add(l_val, i , index + 1);
					i++;
				}
			}
		}
    }
    
    private void deleteInterviewRow(CheckMenuItem item) {
    	
    	Node label = findNodeGridPane(item);
    	statsGrid.getChildren().removeIf(node -> GridPane.getRowIndex(node) == GridPane.getRowIndex(label));
    	
    }
    
    
    /* find cell, column or row */
    
    private Label findNodeGridPane(CheckMenuItem item) {
    	for (Node label : statsGrid.getChildren()) {
    		
    		if(((Label) label).getText() == item.getText()) {
    			
    			return ((Label) label);
    		}
			
		}
    	return null;	
    }
    
    
    /* private ArrayList<Node> findColumn(CheckMenuItem item) {
    	
    	ArrayList<Node> res = new ArrayList<Node>();
    	Label label = findCell(item);
    	
    	for (Node node : statsGrid.getChildren()) {
            if(statsGrid.getColumnIndex(node) == statsGrid.getColumnIndex(label)) {
                res.add(node);
            }
        }

        return res;
    	
    } */
    
    /* private void addNode(Node node, int indexR, int indexC) {
    	this.rows.get(indexR)[indexC] = node;
    	this.columns.get(indexC)[indexR] = node;
    } */
    

	
	@FXML
    private void closeStats() {
		window.close();
    };
    
}

