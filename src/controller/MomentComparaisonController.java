package controller;

import java.awt.Insets;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.application.Application;
import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Category;
import model.MomentExperience;
import utils.MomentComparaison;


public class MomentComparaisonController implements Initializable{

	private @FXML Button buttonCloseStats;
	
	private @FXML Pane centralPane;

	private Main main;
	private Stage window;
	private GridPane statsGrid;
	double largeurNoeudEnfant = 0;
	double largeurRacineParent = 100;
	double lastWidth;
	
	public MomentComparaisonController(Main main, Stage window) {
		this.main = main;
		this.window = window;
	}
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		

		boolean isRacine = true;
		
		MomentComparaison.getInstance().update(main);
		ArrayList<ArrayList<MomentExperience>> listMainMoments = MomentComparaison.getmMoments();
		int nbRacineMax = listMainMoments.size();
		int nbRacine = 0;
		HBox layout = new HBox();
		initTag(listMainMoments); //???????????????????
		//System.out.println("BBBBBBBBBBBBBBBBBBBBB" + listAllmoments.toString());
		largeurNoeudEnfant = largeurRacineParent/listMainMoments.size();
		largeurRacineParent = largeurNoeudEnfant;
		
		
		for(ArrayList<MomentExperience> moments : MomentComparaison.getmMoments()) { //separation de la liste en deux
			VBox colTree = new VBox();
			
			for(MomentExperience moment : moments){
					
				if(nbRacine < nbRacineMax && isRacine) {
					
					System.out.println("moment parent" + moment.getName() + " "+ largeurRacineParent);
					isRacine=false;
					HBox rowTree = new HBox();
					
					//category display
					ArrayList<String> categoryList = new ArrayList<String>();
					for(Category c : moment.getCategories()){
						categoryList.add(c.toString());	
					}
					ListView<String> listCategoryDisplay = new ListView<String>();
					listCategoryDisplay.setPrefSize(100, 100);
					listCategoryDisplay.getItems().addAll(categoryList);
					
					TitledPane titleMoment = new TitledPane(moment.getName(), new Button("B1"));
					Accordion momentBox = new Accordion();
					momentBox.setPrefWidth(largeurRacineParent*10);
					momentBox.getPanes().addAll(titleMoment);
					rowTree.getChildren().add(momentBox);
					colTree.getChildren().add(rowTree);
					nbRacine++;
					moment.setTag(true);
						
				} 
				//lastWidth = largeurNoeudEnfant;
				if(moment.getSubMoments().size()>0) {
						
					HBox rowTree = new HBox();
					largeurNoeudEnfant = largeurNoeudEnfant/moment.getSubMoments().size();
					//System.out.println("SOUS LISTE TAILLE " + moment.getName() + " "+ moment.getSubMoments().size());
						//rowTree.setSpacing(largeurNoeudEnfant);
						//rowTree.setMaxSize(1000, 1000);
					//System.out.println("momenttttt " + moment.getName() + " "+ moment.getSubMoments().size());
					//System.out.println("moment " + moment.getName() + " "+ largeurRacineParent);
					
					for(MomentExperience m : moment.getSubMoments()) { //pour tous les sous moments de A
						//System.out.println("SOUS LISTE TAILLE " + moment.getName() + " "+ moment.getSubMoments().size());
						//System.out.println("noeud enfant " + " "+ largeurNoeudEnfant);
						
						if(m.isTag()==false) {
							System.out.println("momentt " + m.getName() + " "+ largeurNoeudEnfant);
							
							//category display
							ArrayList<String> categoryList = new ArrayList<String>();
							for(Category c : m.getCategories()){
								categoryList.add(c.toString());	
							}
							ListView<String> listCategoryDisplay = new ListView<String>();
							listCategoryDisplay.setPrefSize(100, 100);
							listCategoryDisplay.getItems().addAll(categoryList);
			
							TitledPane titleMoment = new TitledPane(m.getName(), listCategoryDisplay);
							Accordion momentBox = new Accordion();
							momentBox.setPrefWidth(largeurNoeudEnfant*10);
							momentBox.getPanes().addAll(titleMoment);
							rowTree.getChildren().add(momentBox);
							m.setTag(true);
								
						}
							
					}
						
					//System.out.println("NEW LINE " + lastWidth);
					colTree.getChildren().add(rowTree);
					search(moment.getSubMoments(), moments, colTree);
					
				}
					
			}
				
			isRacine=true;
			largeurNoeudEnfant=largeurRacineParent;
			layout.getChildren().add(colTree);
				

		}
		//statsGrid = new GridPane();
		//statsGrid.setAlignment(Pos.CENTER);
		//statsGrid.setStyle("-fx-padding:  15 0 0 0;");
		this.centralPane.getChildren().add(layout);
		buttonCloseStats.setText(main._langBundle.getString("close"));
		
	}
	
	/**
	* Search for all subMoments children if they don't have their own children ... 
	* @param subMoments of a parent
	* @param moments list
	* @param colTree for place children in same col
	*/
	public void search(LinkedList<MomentExperience> subMoments, ArrayList<MomentExperience> moments, VBox colTree) {
		HBox rowTree = new HBox();
		//largeurNoeudEnfant = lastWidth;
		System.out.println(largeurNoeudEnfant);
		lastWidth = largeurNoeudEnfant;
		System.out.println("colonne finish " + lastWidth);
		for(MomentExperience subMoment : subMoments){ //la liste
			//si le sous moment est egal a un moment parent de la liste
			for(MomentExperience moment : moments) {
				
				if(moment.getID()==subMoment.getID()) {
					largeurNoeudEnfant = largeurNoeudEnfant/moment.getSubMoments().size();
					for(MomentExperience m : moment.getSubMoments()) {
						//System.out.println("moment " + m.getName() + " "+ largeurNoeudEnfant);
						System.out.println("momento " + m.getName() + " "+ largeurNoeudEnfant);
						if(m.isTag()==false) {
							
							//category display
							ArrayList<String> categoryList = new ArrayList<String>();
							for(Category c : m.getCategories()){
								categoryList.add(c.toString());	
							}
							ListView<String> listCategoryDisplay = new ListView<String>();
							listCategoryDisplay.setPrefSize(100, 100);
							listCategoryDisplay.getItems().addAll(categoryList);
							
							TitledPane titleMoment = new TitledPane(m.getName(), listCategoryDisplay);
							
							Accordion momentBox = new Accordion();
							momentBox.setPrefWidth(largeurNoeudEnfant*10);
							momentBox.getPanes().addAll(titleMoment);
							rowTree.getChildren().add(momentBox);
							m.setTag(true);
							
						}
						
					}
						
				}
			}
			largeurNoeudEnfant = lastWidth;
			System.out.println("finish");
			
		}
		colTree.getChildren().add(rowTree);
	}
	
	/**
	* Tagger one moment that was already placed
	* @param listMoment: list of moments
	*/
	public void initTag(ArrayList<ArrayList<MomentExperience>> listMoment) {
		for(ArrayList<MomentExperience> moments : MomentComparaison.getmMoments()) {
			for(MomentExperience moment : moments){
				moment.setTag(false);
				for(MomentExperience m : moment.getSubMoments()) {
					m.setTag(false);
				}
			}
		}
	}
	
	@FXML
    private void closeStats() {
		window.close();
    };
    
}

