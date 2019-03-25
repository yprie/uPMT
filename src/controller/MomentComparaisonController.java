package controller;


import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.swing.GroupLayout.Alignment;

import application.Main;
import controller.MomentExpVBox;
import controller.TypeCategoryRepresentationController;
import controller.command.MoveTypeCommand;
import controller.command.RemoveTypeCommand;
import controller.controller.Observable;
import controller.controller.Observer;
import controller.controller.TypeController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Cell;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.effect.DropShadow;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import model.Category;
import model.DescriptionInterview;
import model.Folder;
import model.MomentExperience;
import model.Property;
import model.Schema;
import model.Type;
import utils.MainViewTransformations;
import utils.Serializer;
import utils.UndoCollector;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import model.Category;
import model.MomentExperience;
import utils.MomentComparaison;


public class MomentComparaisonController implements Initializable{

	//GERER LES ID + LES COULEURS + APPLIQUER PARTOUT + NETTOYER
	//METTRE AU MILIEU, CHANGER LA PLACE DE LA FLECHE
	//GERER L'INTERIEUR
	//BIEN ALIGNER QUAND PLEIN DE DIFFERENTES INTERVIEWS
	private @FXML Button buttonCloseStats;
	
	private @FXML Pane centralPane;

	private Main main;
	private Stage window;
	private Paint backgroundPaint;
	private GridPane statsGrid;
	private BorderPane test;
	private ArrayList<Accordion> listAccordion;
	double largeurNoeudEnfant = 0;
	double largeurRacineParent = 120;
	double lastWidth;
	final int ROW_HEIGHT = 24;
	private int idName;
	private int idMax;
	int PADDING = 2;
	boolean ok=true;
	
	ArrayList<MomentExperience> momentSave = new ArrayList<MomentExperience>();
	
	public MomentComparaisonController(Main main, Stage window) {
		this.main = main;
		this.window = window;
	}
	
	public void setColor(String col){
		
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		listAccordion=new ArrayList<Accordion>();
		ScrollPane scroll = new ScrollPane();
		scroll.setFitToHeight(true);
		scroll.setFitToWidth(true);
		//scroll.setPrefSize(300, 300);
		
		int cptInterviewName=0;
		//rowTree.setStyle("-fx-border-color: #2e8b57; -fx-border-width: 2px; -fx-border-radius: 20;  -fx-border-style: segments(10, 15, 15, 15)  line-cap round ;");
		try {
			initCssFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		MomentComparaison.getInstance().update(main);
		ArrayList<ArrayList<ArrayList<MomentExperience>>> listMainMoments = MomentComparaison.getmMoments();
		
		
		initTag(listMainMoments);
		VBox layoutV = new VBox();
		
		//System.out.println("jjjjjj" + listMainMoments.toString() + listMainMoments.size());
		for(ArrayList<ArrayList<MomentExperience>> allMoments : MomentComparaison.getmMoments()) { //separation de la liste en deux
			PADDING = 1;
			HBox layoutH = new HBox();
			layoutH.setStyle( "-fx-border-color: grey; -fx-padding: 20 0 20 0; -fx-border-width: 0 0 3 0 ; -fx-border-style: segments(10, 15, 15, 15)  line-cap round ;"); 
			boolean isRacine = true;
			
			int nbRacineMax = allMoments.size();
			int nbRacine = 0;
			//System.out.println("jjjjjj" + allMoments.toString() + allMoments.size());
			largeurNoeudEnfant = 120/allMoments.size();
			largeurRacineParent = largeurNoeudEnfant;

			System.out.println(largeurNoeudEnfant);
			System.out.println(largeurRacineParent);
			for(ArrayList<MomentExperience> moments : allMoments) {
				
			VBox colTree = new VBox();
			
			for(MomentExperience moment : moments) {
				
				//System.out.println("WIIDTH " + moment.getmWidth());
				moment.setmWidth(moment.getmWidth()/allMoments.size());	
				
				if(allMoments.size()>1) {
					moment.setmWidth(largeurRacineParent);	
				} else {
					moment.setmWidth(moment.getmWidth()/allMoments.size());	
				}
				
				
				//System.out.println("1 "+moment.getmWidth());
				//System.out.println(allMoments.size());
				if(nbRacine < nbRacineMax && isRacine) {
					isRacine=false;
					HBox rowTree = new HBox();
					
					if(cptInterviewName==0) {
						Label labelTitleInterview = new Label(moment.getInterviewName());
						//text1.setStyle("-fx-font-weight: bold");
						labelTitleInterview.setStyle("-fx-font-weight: bold;");
						labelTitleInterview.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
						labelTitleInterview.setPrefSize(200, 0);
						cptInterviewName++;
						rowTree.getChildren().add(labelTitleInterview);
					}
					
					
					//category display
					ArrayList<String> categoryList = new ArrayList<String>();
					for(Category c : moment.getCategories()){
						categoryList.add(c.toString());	
					}

					ListView<String> listCategoryDisplay = new ListView<String>();
					//listCategoryDisplay.setPrefHeight(categoryList.size() * ROW_HEIGHT + 2);
					
					listCategoryDisplay.getItems().addAll(categoryList);
					listCategoryDisplay.setId("list"+moment.getID());
					listCategoryDisplay.setStyle("-fx-background-color:"+moment.getColor()+";");
					listCategoryDisplay.setPrefHeight(categoryList.size() * ROW_HEIGHT + 2);
					TitledPane titleMoment = new TitledPane(moment.getName(), listCategoryDisplay);
					titleMoment.setStyle("-fx-border-color: grey; -fx-border-width: 2px;");
					titleMoment.setId("title"+moment.getID());
					//titleMoment.setEffect(new DropShadow(20, Color.BLACK));
					titleMoment.setAlignment(Pos.CENTER);
					//titleMoment.setStyle("-fx-border-color: lightgray;");
					
					try {
						writeInCssFile("#" + listCategoryDisplay.getId() + " .list-cell:even { -fx-background-color:"+ moment.getColor() +"; }");
						writeInCssFile("#" + listCategoryDisplay.getId() + " .list-cell:odd { -fx-background-color:"+ moment.getColor() +"; }");
						writeInCssFile("#" + titleMoment.getId() + " > .title { -fx-background-color:"+ moment.getColor() +"; }");
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					Accordion momentBox = new Accordion();
					momentBox.setId("moment"+idName);
					listAccordion.add(momentBox);
					//System.out.println("NAME 1 " + moment.getName() + "     " + momentBox.getId());
					//momentBox.setPrefWidth(largeurRacineParent*10);
					momentBox.setPadding(new Insets(4, PADDING*1.5, 4, PADDING*1.5));
					momentBox.setMinWidth(largeurRacineParent*10);
					momentBox.setMaxWidth(largeurRacineParent*10);
					
					//momentBox.setStyle("-fx-border-color: grey;");
					momentBox.getPanes().addAll(titleMoment);
					
					momentBox.expandedPaneProperty().addListener((ObservableValue<? extends TitledPane> observable, TitledPane oldPane, TitledPane newPane) -> {
						expand(momentBox);
					});
					
					rowTree.getChildren().add(momentBox);
					colTree.getChildren().add(rowTree);
					nbRacine++;
					moment.setTag(true);
						
				} 
				idName++;
				idMax=idName;
				PADDING = PADDING + 2;
				if(moment.getSubMoments().size()>0) {
					
					HBox rowTree = new HBox();
					for(MomentExperience subMoment : moment.getSubMoments()) { //pour tous les sous moments de A
						
						subMoment.setmWidth(moment.getmWidth()/moment.getSubMoments().size());
						//subMoment.setmWidth(subMoment.getmWidth()-(PADDING/2));
						//System.out.println("2 "+subMoment.getmWidth());
						if(subMoment.isTag()==false) {

							//category display
							ArrayList<String> categoryList = new ArrayList<String>();
							
							for(Category c : subMoment.getCategories()){
								categoryList.add(c.toString());	
							}
							if(cptInterviewName==1) {
								Label labelTitleInterview = new Label(subMoment.getInterviewName());
								labelTitleInterview.setPrefSize(200, 0);
								labelTitleInterview.setVisible(false);
								cptInterviewName++;
								rowTree.getChildren().add(labelTitleInterview);
							}
							ListView<String> listCategoryDisplay = new ListView<String>();
							
							listCategoryDisplay.setId("list"+subMoment.getID());
							listCategoryDisplay.setPrefHeight(categoryList.size() * ROW_HEIGHT + 2);
							listCategoryDisplay.setStyle("-fx-background-color:"+subMoment.getColor()+";");
							listCategoryDisplay.getItems().addAll(categoryList);

							TitledPane titleMoment = new TitledPane(subMoment.getName(), listCategoryDisplay);
							titleMoment.setStyle("-fx-border-color: grey; -fx-border-width: 2px;");
							
							//titleMoment.setStyle("-fx-border-color: lightgray;");
							Accordion momentBox = new Accordion();
							momentBox.setId("moment"+idName);
							listAccordion.add(momentBox);
							//System.out.println("NAME 1 " + subMoment.getName() + "     " + momentBox.getId());
							//momentBox.setPrefWidth(subMoment.getmWidth()*10);
							momentBox.setPadding(new Insets(4, PADDING*1.5, 4, PADDING*1.5));
							//momentBox.setStyle("-fx-padding: 0 20 0 20;");
							//momentBox.setStyle("-fx-border-color: grey;");
							momentBox.setMaxWidth((subMoment.getmWidth()*10)-2);
							momentBox.setMinWidth((subMoment.getmWidth()*10)-2);
							//momentBox.setExpandedPane(titleMoment);
							momentBox.getPanes().addAll(titleMoment);
						
							momentBox.setId("moment"+idName);
							titleMoment.setId("title"+subMoment.getID());
							

							
							//momentBox.setOnMouseClicked(mousehandler);
							/*
							int isClose=0;
							momentBox.expandedPaneProperty().addListener(new 
									ChangeListener<TitledPane>() {
										@Override
										public void changed(ObservableValue<? extends TitledPane> observable,
												TitledPane oldPane, TitledPane newValue) {
											 Boolean expand = true; // This value will change to false if there's (at least) one pane that is in "expanded" state, so we don't have to expand anything manually
										        for(TitledPane pane: momentBox.getPanes()) {
										            if(pane.isExpanded()) {
										                expand = false;
										            }
										        }

										        if((expand == true) && (oldPane != null)) {
										            Platform.runLater(() -> {
										            	momentBox.setExpandedPane(oldPane);
										            });
										        }
										        */
        
							momentBox.expandedPaneProperty().addListener((ObservableValue<? extends TitledPane> observable, TitledPane oldPane, TitledPane newPane) -> {
								expand(momentBox);
							});
							
							
												//momentBox.setExpandedPane(momentBox.getPanes().get(0));
											//titleMoment.setCollapsible(false);
											//System.out.println(titleMoment.isCollapsible());
											/*
													if(titleMoment.isCollapsible()) {
														//titleMoment.setExpanded(false);
														System.out.println("fermé");
													} else {
														for(Accordion accordion : listAccordion) {
															if(!accordion.equals(null) && accordion.getId().equals(momentBox.getId())) {
																
																accordion.setExpandedPane(accordion.getPanes().get(0));
																titleMoment.setCollapsible(true);
																//accordion.setExpanded(accordion.getPanes().get(0));
															} 
														}
													}
													
										}
										
						     });
							*/
							titleMoment.setAlignment(Pos.CENTER);
							//displayArrow(titleMoment);
							Region title = (Region) titleMoment.lookup("#"+titleMoment.getId());
							
							
							//System.out.println("ooooooooooooo" + subMoment.toString().length());

							try {
								writeInCssFile("#" + listCategoryDisplay.getId() + " .list-cell:even { -fx-background-color:"+ subMoment.getColor() +"; }");
								writeInCssFile("#" + listCategoryDisplay.getId() + " .list-cell:odd { -fx-background-color:"+ subMoment.getColor() +"; }");
								writeInCssFile("#" + titleMoment.getId() + " > .title { -fx-background-color:"+ subMoment.getColor() +"; }");
								//writeInCssFile("#" + titleMoment.getId() + " > .title { -fx-background-color:"+ subMoment.getColor() +"; }");
								//writeInCssFile(".titled-pane > .title { -fx-alignment: center-right; }");
							
							} catch (IOException e) {
								e.printStackTrace();
							}
							
							rowTree.getChildren().add(momentBox);
							subMoment.setTag(true);
							momentSave.add(subMoment);	
						}
							
					}
					idName++;
					idMax=idName;
					System.out.println("EEEEND");
					colTree.getChildren().add(rowTree);
					search(momentSave, moments, colTree);
					
				}	
			}
			isRacine=true;
			largeurNoeudEnfant=largeurRacineParent;
			layoutH.getChildren().add(colTree);
		}
			
			
			layoutV.getChildren().add(layoutH);
			cptInterviewName=0;
	}
		//final String dir = System.getProperty("user.home")+"/.upmt";
		final String dir = System.getProperty("user.home")+"/.upmt/test.css";
		//String css = this.getClass().getResource("file:///src/test.css").toExternalForm();
		//System.out.println("DIR " + css);
		this.centralPane.getStylesheets().add("file:///"+dir);
		//this.centralPane.getStylesheets().add(getClass().getResource("../src/application.css").toExternalForm());
		//layoutV.setStyle("-fx-border-width: 0px, 0px, 5px, 0px;  -fx-border-style: segments(10, 15, 15, 15)  line-cap round ;  -fx-border-color: blue ; ");
		//System.out.println("EEEEND " + idMax);
		TitledPane t = new TitledPane("oooo", new Button("B1"));
		//layoutV.setPrefSize(900, 500);
		layoutV.setPrefSize(200, 200);
		scroll.setContent(layoutV);
		this.centralPane.getChildren().add(scroll);

		
		buttonCloseStats.setText(main._langBundle.getString("close"));
	}
	
	
	
	/**
	* Search for all subMoments children if they don't have their own children ... 
	* @param subMoments of a parent
	* @param moments list
	* @param colTree for place children in same col
	*/
	public void search(ArrayList<MomentExperience> subMoments, ArrayList<MomentExperience> moments, VBox colTree) {
		PADDING = PADDING + 2;
		double init = lastWidth;
		int cpt=0;
		int i = 0;
		int j =0;
		int cptInterviewName=0;
		int width;
		//System.out.println(subMoments.toString());
		HBox rowTree = new HBox();
		lastWidth = largeurNoeudEnfant;
		ArrayList<MomentExperience> momentSaveCopy = new ArrayList<MomentExperience>();
		while (i < subMoments.size()) {
			
			MomentExperience subMoment = subMoments.get(i);
			if(subMoment.getSubMoments().size()>0) {
				
				for(MomentExperience subMomentOfSubMoment : subMoment.getSubMoments()) {
					
					subMomentOfSubMoment.setmWidth(subMoment.getmWidth()/subMoment.getSubMoments().size());
					largeurNoeudEnfant=lastWidth/subMoment.getSubMoments().size();
					//System.out.println(subMomentOfSubMoment.getName());
					
					//category display
					ArrayList<String> categoryList = new ArrayList<String>();
					for(Category c : subMomentOfSubMoment.getCategories()){
						categoryList.add(c.toString());	
					}
					ListView<String> listCategoryDisplay = new ListView<String>();
					listCategoryDisplay.setStyle("-fx-border-color: red; -fx-border-width: 0px Opx 8px 0px;");
					listCategoryDisplay.setPrefHeight(categoryList.size() * ROW_HEIGHT);
					listCategoryDisplay.getItems().addAll(categoryList);
					listCategoryDisplay.setStyle("-fx-background-color:"+subMomentOfSubMoment.getColor()+";");
					listCategoryDisplay.setId("list"+subMomentOfSubMoment.getID());
					
					try {
						writeInCssFile("#" + listCategoryDisplay.getId() + " .list-cell:even { -fx-background-color:"+ subMomentOfSubMoment.getColor() +"; }");
						writeInCssFile("#" + listCategoryDisplay.getId() + " .list-cell:odd { -fx-background-color:"+ subMomentOfSubMoment.getColor() +"; }");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
					TitledPane titleMoment = new TitledPane(subMomentOfSubMoment.getName(), listCategoryDisplay);
					//titleMoment.setStyle("-fx-border-color: lightgray;");
					titleMoment.setAlignment(Pos.CENTER);
					titleMoment.setId("title"+subMomentOfSubMoment.getID());
					titleMoment.setStyle("-fx-border-color: grey; -fx-border-width: 2px;");
					//displayArrow(titleMoment);
					//System.out.println(titleMoment.getId());
					try {
						writeInCssFile("#" + titleMoment.getId() + " > .title { -fx-background-color:"+ subMomentOfSubMoment.getColor() +"; }");
					} catch (IOException e) {
						e.printStackTrace();
					}
					if(cptInterviewName==0) {
						Label labelTitleInterview = new Label(subMomentOfSubMoment.getInterviewName());
						labelTitleInterview.setPrefSize(200, 0);
						labelTitleInterview.setVisible(false);
						cptInterviewName++;
						rowTree.getChildren().add(labelTitleInterview);
					}
					//System.out.println("NAME 2 " + subMomentOfSubMoment.getName());
					Accordion momentBox = new Accordion();
					momentBox.setId("moment"+idName);
					listAccordion.add(momentBox);
					//System.out.println("NAME 1 " + subMomentOfSubMoment.getName() + "     " + momentBox.getId());
					
					momentBox.setPadding(new Insets(4, PADDING*1.5, 4, PADDING*1.5));
					
					//momentBox.setPrefWidth(subMomentOfSubMoment.getmWidth()*10);
					momentBox.setMaxWidth((subMomentOfSubMoment.getmWidth()*10)-2);
					momentBox.setMinWidth((subMomentOfSubMoment.getmWidth()*10)-2);
					//momentBox.getId().setExpandedPane(titleMoment);
					if(ok==true) {
					//momentBox.setStyle("-fx-background-color: grey;");
					ok=false;
					}
					momentBox.getPanes().addAll(titleMoment);
					momentBox.expandedPaneProperty().addListener((ObservableValue<? extends TitledPane> observable, TitledPane oldPane, TitledPane newPane) -> {
						expand(momentBox);
					});
					//momentBox.set
					rowTree.getChildren().add(momentBox);
					subMomentOfSubMoment.setTag(true);
					momentSaveCopy.add(subMomentOfSubMoment);
				}
			lastWidth = init;
			} else {
				
				TitledPane titleMoment = new TitledPane("momentVide", new Button("b"));
				titleMoment.setVisible(false);
				Accordion momentBox = new Accordion();
				momentBox.setId("hideMoment");
				double size = subMoment.getmWidth();
				listAccordion.add(momentBox);
				//System.out.println(size);
				//momentBox.setPrefWidth(size*10);
				momentBox.setPadding(new Insets(4, PADDING*2, 4, PADDING*2));
				momentBox.setMaxWidth((size*10)-2);
				momentBox.setMinWidth((size*10)-2);
				momentBox.getPanes().addAll(titleMoment);
				rowTree.getChildren().add(momentBox);
				momentBox.setDisable(true);

			}
		i++; 
		}
		momentSave.clear();		
		momentSave.addAll(momentSaveCopy);
		//System.out.println("momment save " + momentSaveCopy.toString());
		largeurNoeudEnfant = lastWidth;
		//System.out.println("finish");
		colTree.getChildren().add(rowTree);
	}
	
	public void displayArrow(TitledPane titledPane) {
		Label collapseButton = new Label();
		//collapseButton.setBackground(new Background(new BackgroundFill(Color.gray(0.8), CornerRadii.EMPTY, Insets.EMPTY)));
		collapseButton.textProperty().bind(
		    Bindings.when(titledPane.expandedProperty())
		        .then("\u25bc").otherwise("\u25b6"));
		titledPane.setGraphic(collapseButton);
		titledPane.setContentDisplay(ContentDisplay.RIGHT);
	}
	
	public void initCssFile() throws IOException {
		try {
			
			final String dir = System.getProperty("user.home")+"/.upmt";
			System.out.println("DIR " + dir);
		    FileWriter fileWriter = null;
		    //final String dirSystem.getProperty("user.home")+"/.upmt/path.json";
		    File file = new File(dir+File.separator + "test.css");
			//File file2 = new File(dir+File.separator+"jar" + File.separator + "application" + File.separator + "test.css");
			
			if(file.exists()){
			    file.delete();
			}
			file.createNewFile();
			fileWriter = new FileWriter(dir+File.separator+"test.css", true);
			//fileWriter = new FileWriter(dir+File.separator+"jar" + File.separator + "application" + File.separator + "test.css", true);
			BufferedWriter bufferWriter = new BufferedWriter(fileWriter);
			//bufferWriter.write(".accordion .title > .arrow-button{ -fx-padding: 10;}");
			//bufferWriter.write(".vbox {-fx-border-color: #2e8b57; -fx-border-width: 2px; -fx-padding: 10;-fx-spacing: 8;}");
			bufferWriter.close();
			//bufferWriter.write(".hbox {-fx-border-color: #2e8b57; -fx-border-width: 2px; -fx-padding: 10;-fx-spacing: 8;}");
			fileWriter.close();
			
		} catch (IOException e1) {
			
			e1.printStackTrace();
			
		}
	}
	
	public void writeInCssFile(String toWrite) throws IOException {
		try {
			
			final String dir = System.getProperty("user.home")+"/.upmt"; 
		    FileWriter fileWriter = null;
		    fileWriter = new FileWriter(dir+File.separator+"test.css", true);
		    //fileWriter = new FileWriter(dir+File.separator+"jar" + File.separator + "application" + File.separator + "test.css", true);
			BufferedWriter bufferWriter = new BufferedWriter(fileWriter);
			bufferWriter.write(toWrite + "\n");
			bufferWriter.close();
			fileWriter.close();
			
		} catch (IOException e1) {
			
			e1.printStackTrace();
			
		}
	}
	
	/**
	* Tagger one moment that was already placed
	* @param listMoment: list of moments
	*/
	public void initTag(ArrayList<ArrayList<ArrayList<MomentExperience>>> listMoment) {
		
		for(ArrayList<ArrayList<MomentExperience>> moments : MomentComparaison.getmMoments()) {
			//moments.setTag(false);
			for(ArrayList<MomentExperience> allmoments : moments){
				for(MomentExperience moment : allmoments){
					moment.setTag(false);
					for(MomentExperience m : moment.getSubMoments()) {
						m.setTag(false);
					}
				}
			}	
		}
	}
	
	EventHandler<MouseEvent> mousehandler = new EventHandler<MouseEvent>() {
		
	    @Override
	    public void handle(MouseEvent mouseEvent) {
	       // System.out.println("hi");
	    }
	};
	
	@FXML
    private void closeStats() {
		window.close();
    };
    
    
    /*
     * 
     * 
     Button button =new Button(" close ");
			Button button2 =new Button(" open ");
			button2.setOnAction((ActionEvent event) -> {
				for(Accordion a : listAccordion) {
					if(!a.equals(null)) {
						System.out.println(a.getPanes().get(0).isExpanded());
						System.out.println(a.getId());
						a.getPanes().get(0).setExpanded(true);
					}
				}
		    });
			
			button.setOnAction((ActionEvent event) -> {
				for(Accordion a : listAccordion) {
					if(!a.equals(null)) {
						System.out.println(a.getPanes().get(0).isExpanded());
						
						a.getPanes().get(0).setExpanded(false);
						//a.setExpandedPane(a.getPanes().get(0));
						//a.setExpanded(true);
					}
					
					//a.setExpandedPane(t);
				}
		        //accordion.setExpandedPane(titledPane); // Expanded Pane works!
		         * 
		    layoutV.getChildren().add(button);
			layoutV.getChildren().add(button2);
		    });
     */
    
	/**
	* Expand all accordion in the same
	* @param momentBox: the accordion
	*/
    public void expand(Accordion momentBox) {
    	if(momentBox.getPanes().get(0).isExpanded()) {
	    	for(Accordion a : listAccordion) {
	    		if(!a.equals(null)) {
	    			if(!a.getId().equals("hideMoment")) {
	    				if(a.getId().equals(momentBox.getId())) {
	    					if(a.getId().equals(momentBox.getId())) {
	    						System.out.println(a.getId());
	    						a.getPanes().get(0).setExpanded(true);
	    					}
	        			}
	    			}
	    		}
	    	}
    	} else {
    		for(Accordion a : listAccordion) {
    			if(!a.equals(null)) {
	    			if(!a.getId().equals("hideMoment")) {
	    				if(a.getId().equals(momentBox.getId())) {
	    					a.getPanes().get(0).setExpanded(false);
	    				}
	    			}
	    		}
	    	}	
	    }
    }
    
}

