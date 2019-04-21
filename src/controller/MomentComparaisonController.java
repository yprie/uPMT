package controller;


import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.Tooltip;
import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javax.swing.ToolTipManager;
import java.awt.Dimension;
import java.awt.Toolkit;
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
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.ScrollBar;
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
import javafx.scene.layout.AnchorPane;
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
import utils.EllipsisListCell;
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


public class MomentComparaisonController  implements Initializable {
	
	private @FXML Button buttonCloseStats;
	private @FXML Pane centralPane;
	private @FXML HBox anchorPane;
	private Main main;
	private Stage window;
	private Paint backgroundPaint;
	private GridPane statsGrid;
	private BorderPane test;
	private ArrayList<Accordion> listAccordion;
	private double largeurNoeudEnfant = 0;
	private double largeurRacineParent = 120;
	private double lastWidth;
	private final int ROW_HEIGHT = 24;
	private double lengthInit;
	private int idName;
	private int idMax;
	private int PADDING = 2;
	private boolean ok=true;
	private ArrayList<MomentExperience> momentSave = new ArrayList<MomentExperience>();
	
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
		int cptInterviewName=0;
		
		try {
			initCssFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//init size 
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		lengthInit=((screenSize.getWidth()-255)/10);
		initLenthOfMoment();
		System.out.println(screenSize.getWidth() + "  " + lengthInit);
		
		MomentComparaison.getInstance().update(main);
		ArrayList<ArrayList<ArrayList<MomentExperience>>> listMainMoments = MomentComparaison.getmMoments();
		MomentComparaison.initLengthMoment(lengthInit);
		System.out.println(MomentComparaison.getmaxNbMoment());
		initTag(listMainMoments);
		VBox layoutV = new VBox();
		
		for(ArrayList<ArrayList<MomentExperience>> allMoments : MomentComparaison.getmMoments()) { //separation de la liste en deux
			PADDING = 1;
			HBox layoutH = new HBox();
			layoutH.setStyle( "-fx-border-color: grey; -fx-padding: 20 0 20 0; -fx-border-width: 0 0 3 0 ; -fx-border-style: segments(10, 15, 15, 15)  line-cap round ;"); 
			boolean isRacine = true;
			
			int nbRacineMax = allMoments.size();
			int nbRacine = 0;
			//System.out.println("jjjjjj" + allMoments.toString() + allMoments.size());
			largeurNoeudEnfant = lengthInit/allMoments.size();
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
					
	
					ListView<Category> listCategoryDisplay = new ListView<Category>();
					//ListView<String> listCategoryDisplay = new ListView<String>();
					//listCategoryDisplay.setPrefHeight(categoryList.size() * ROW_HEIGHT + 2);
					listCategoryDisplay.getItems().addAll(moment.getCategories());
					
					//debut
					listCategoryDisplay.setPrefHeight(moment.getCategories().size() * ROW_HEIGHT);
					listCategoryDisplay.getItems().addAll(moment.getCategories());
					//fin
					listCategoryDisplay.setStyle("-fx-background-color:"+moment.getColor()+";");
					listCategoryDisplay.setId("list"+moment.getID());
					
					//debut
					listCategoryDisplay.setCellFactory(lv -> {
			            ListCell<Category> cell = new ListCell<Category>() {
			                @Override
			                public void updateItem(Category item, boolean empty) {
			                    super.updateItem(item, empty);
			                    setStyle("-fx-padding: 2 2 2 2");
			                    //listCategoryDisplay.setCursor(Cursors.Hand);
			                    if (empty) {
			                        setText(null);
			                    } else {
			                    	Tooltip tooltip = new Tooltip();
			                        setText(item.getName());
			                        if(item.getProperties().size()>0) {
			                        	tooltip.setText(item.getProperties().toString());
			                        	//tooltip.setId("tool");
			                        	//ToolTipManager.sharedInstance().setDismissDelay(20000);
			                        	//tooltip.de
			                        } else {
			                        	
			                        	tooltip.hide();
			                        }
			                        bindTooltip(this, tooltip);
			                       // setTooltip(tooltip);
			                        
			                        //item.getProperties()
			                    }
			                }
			            };
			            
			            cell.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
			            	 //ToolTipManager.sharedInstance().setDismissDelay(90000);
			            });
						
			            return cell ;
			        });
					
					//fin
					
					//listCategoryDisplay.getItems().addAll(categoryList);
					listCategoryDisplay.setId("list"+moment.getID());
					listCategoryDisplay.setStyle("-fx-background-color:"+moment.getColor()+";");
					//listCategoryDisplay.setPrefHeight(categoryList.size() * ROW_HEIGHT + 2);
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
						writeInCssFile(scollBarColor(listCategoryDisplay.getId(),moment.getColor()));
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					Accordion momentBox = new Accordion();
					momentBox.setId("moment"+idName);
					listAccordion.add(momentBox);
					//System.out.println("NAME 1 " + moment.getName() + "     " + momentBox.getId());
					
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
							

							if(cptInterviewName==1) {
								Label labelTitleInterview = new Label(subMoment.getInterviewName());
								labelTitleInterview.setPrefSize(200, 0);
								labelTitleInterview.setVisible(false);
								cptInterviewName++;
								rowTree.getChildren().add(labelTitleInterview);
							}
							ListView<Category> listCategoryDisplay = new ListView<Category>();
							
							//listCategoryDisplay.setId("list"+subMoment.getID());
							//listCategoryDisplay.setPrefHeight(categoryList.size() * ROW_HEIGHT + 2);
							//listCategoryDisplay.setPrefWi(categoryList.size() * ROW_HEIGHT + 2);
							//listCategoryDisplay.setStyle("-fx-background-color:"+subMoment.getColor()+";");
							
							listCategoryDisplay.getItems().addAll(subMoment.getCategories());
							
							//debut
							listCategoryDisplay.setPrefHeight(subMoment.getCategories().size() * ROW_HEIGHT);
							//listCategoryDisplay.getItems().setPadding(new Insets(4, 4, 4, 4));
							//listCategoryDisplay.setPadding(new Insets(4, 4, 4, 4));
							//listCategoryDisplay.setPrefHeight(subMoment.getCategories().size() * ROW_HEIGHT);
							listCategoryDisplay.getItems().addAll(subMoment.getCategories());
							//fin
							listCategoryDisplay.setStyle("-fx-background-color:"+subMoment.getColor()+";");
							listCategoryDisplay.setId("list"+subMoment.getID());
							
							
							// TEST AVEC ...
							//listCategoryDisplay.setCellFactory((ListView<Category> param) -> new EllipsisListCell());
							
							
							//debut
							listCategoryDisplay.setCellFactory(lv -> {
					            ListCell<Category> cell = new ListCell<Category>() {
					                @Override
					                public void updateItem(Category item, boolean empty) {
					                    super.updateItem(item, empty);
					                   
					                    setStyle("-fx-padding: 2 2 2 2");
					                    //listCategoryDisplay.setCursor(Cursors.Hand);
					                    if (empty) {
					                        setText(null);
					                    } else {
					                    	Tooltip tooltip = new Tooltip();
					                        setText(item.getName());
					                        //setText(item.toString());
					                        //setTextOverrun(OverrunStyle.ELLIPSIS);
					                        //setEllipsisString("...");
					                        //ICI
					                        prefWidthProperty().bind(listCategoryDisplay.widthProperty().subtract(4));
					                        setMaxWidth(Control.USE_PREF_SIZE);
					                        //FIN
					                        if(item.getProperties().size()>0) {
					                        	tooltip.setText(item.getProperties().toString());
					                        	//tooltip.setId("tool");
					                        	//ToolTipManager.sharedInstance().setDismissDelay(20000);
					                        	//tooltip.de
					                        } else {
					                        	tooltip.hide();
					                        }
					                        bindTooltip(this, tooltip);
					                       // setTooltip(tooltip);
					                        
					                        //item.getProperties()
					                    }
					                }
					            };
					            
					            cell.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
					            	 //ToolTipManager.sharedInstance().setDismissDelay(90000);
					            });
								
					            return cell ;
					        });
							
							//fin
							
							
							
							TitledPane titleMoment = new TitledPane(subMoment.getName(), listCategoryDisplay);
							//titleMoment.setText(subMoment.getName());
							
							Accordion momentBox = new Accordion();

							
							titleMoment.setStyle("-fx-border-color: grey; -fx-border-width: 2px;");
							

							
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
							
							

							try {
								/**************/
								writeInCssFile("#" + titleMoment.getId() + " > *.content { -fx-background-color:"+ subMoment.getColor() +"; }");
								
								writeInCssFile("#" + listCategoryDisplay.getId() + " .list-cell:even { -fx-background-color:"+ subMoment.getColor() +"; }");
								writeInCssFile("#" + listCategoryDisplay.getId() + " .list-cell:odd { -fx-background-color:"+ subMoment.getColor() +"; }");
								writeInCssFile("#" + titleMoment.getId() + " > .title { -fx-background-color:"+ subMoment.getColor() +"; }");
								writeInCssFile("#" + titleMoment.getId() + " > .title { -fx-background-color:"+ subMoment.getColor() +"; }");
								writeInCssFile(scollBarColor(listCategoryDisplay.getId(),subMoment.getColor()));
							
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

		final String dir = System.getProperty("user.home")+"/.upmt/test.css";
		this.centralPane.getStylesheets().add("file:///"+dir);
		TitledPane t = new TitledPane("oooo", new Button("B1"));
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Button button = new Button();
		button.setText("Hover Me!");
		Tooltip tt = new Tooltip();
		tt.setText("Text on Hover");
		layoutV.getChildren().add(button);
		button.setTooltip(tt);
		
		this.centralPane.getChildren().add(layoutV);

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
					/*
					ArrayList<String> categoryList = new ArrayList<String>();
					for(Category c : subMomentOfSubMoment.getCategories()){
						categoryList.add(c.toString());	
					}
					*/
					ListView<Category> listCategoryDisplay = new ListView<Category>();
					
					listCategoryDisplay.setStyle("-fx-border-color: red; -fx-border-width: 0px Opx 8px 0px;");
					//debut
					listCategoryDisplay.setPrefHeight(subMomentOfSubMoment.getCategories().size() * ROW_HEIGHT);
					listCategoryDisplay.getItems().addAll(subMomentOfSubMoment.getCategories());
					//fin
					listCategoryDisplay.setStyle("-fx-background-color:"+subMomentOfSubMoment.getColor()+";");
					listCategoryDisplay.setId("list"+subMomentOfSubMoment.getID());
					
					//debut
					listCategoryDisplay.setCellFactory(lv -> {
			            ListCell<Category> cell = new ListCell<Category>() {
			                @Override
			                public void updateItem(Category item, boolean empty) {
			                    super.updateItem(item, empty);
			                    setStyle("-fx-padding: 2 2 2 2");
			                    //listCategoryDisplay.setCursor(Cursors.Hand);
			                    if (empty) {
			                        setText(null);
			                    } else {
			                    	Tooltip tooltip = new Tooltip();
			                        setText(item.getName());
			                        if(item.getProperties().size()>0) {
			                        	tooltip.setText(item.getProperties().toString());
			                        	//tooltip.setId("tool");
			                        	//ToolTipManager.sharedInstance().setDismissDelay(20000);
			                        	//tooltip.de
			                        } else {
			                        	tooltip.hide();
			                        	//tooltip.setText(" ");
			                        }
			                        bindTooltip(this, tooltip);
			                       // setTooltip(tooltip);
			                        
			                        //item.getProperties()
			                    }
			                }
			            };
			            
			            cell.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
			            	 //ToolTipManager.sharedInstance().setDismissDelay(90000);
			            });
						
			            return cell ;
			        });
					
					//fin
					
					try {
						writeInCssFile("#" + listCategoryDisplay.getId() + " .list-cell:even { -fx-background-color:"+ subMomentOfSubMoment.getColor() +"; }");
						writeInCssFile("#" + listCategoryDisplay.getId() + " .list-cell:odd { -fx-background-color:"+ subMomentOfSubMoment.getColor() +"; }");
						writeInCssFile(scollBarColor(listCategoryDisplay.getId(),subMomentOfSubMoment.getColor()));
						//writeInCssFile("#tool { -fx-show-duration: 20s;}");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
					TitledPane titleMoment = new TitledPane(subMomentOfSubMoment.getName(), listCategoryDisplay);
					
					titleMoment.setAlignment(Pos.CENTER);
					titleMoment.setId("title"+subMomentOfSubMoment.getID());
					titleMoment.setStyle("-fx-border-color: grey; -fx-border-width: 2px;");
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
		colTree.getChildren().add(rowTree);
	}
	
	public static void bindTooltip(final Node node, final Tooltip tooltip){
		node.setOnMouseEntered(new EventHandler<MouseEvent>(){
		      @Override  
		      public void handle(MouseEvent event) {
		         // +15 moves the tooltip 15 pixels below the mouse cursor;
		         // if you don't change the y coordinate of the tooltip, you
		         // will see constant screen flicker
		    	//node.setCursor(Cursor.HAND); //Change cursor to hand
		         tooltip.show(node, event.getScreenX(), event.getScreenY() + 15);
		      }
		   
	});
		node.setOnMouseExited(new EventHandler<MouseEvent>(){
		      @Override
		      public void handle(MouseEvent event){
		         tooltip.hide();
		      }
		   });
	}
	
	public String scollBarColor(String id, String color) {
		return  "#"+id+" .scroll-bar:horizontal .track,"
		+"#"+id+" .scroll-bar:vertical .track{ -fx-background-color : " + color +"; -fx-border-color :transparent;-fx-background-radius : 0.0em;-fx-border-radius :2.0em}"
		+ "#"+id+" .scroll-bar:horizontal .increment-button , .scroll-bar:horizontal .decrement-button { -fx-background-color :transparent; -fx-background-radius : 0.0em; -fx-padding :0.0 0.0 10.0 0.0;}"
		+ "#"+id+" .scroll-bar:horizontal { -fx-pref-width: 1.5;}"
		+ "#"+id+" .scroll-bar { -fx-font-size: 2px;}"
		+ "#"+id+" .scroll-bar:vertical .increment-button , .scroll-bar:vertical .decrement-button {-fx-background-color :" + color +";-fx-background-radius : 0.0em; -fx-padding :0.0 10.0 0.0 0.0;}"

		+ "#"+id+" .scroll-bar .increment-arrow,.scroll-bar .decrement-arrow{-fx-shape : \" \";    -fx-padding :0.15em 0.0;}"
		+ "#"+id+" .corner { -fx-background-color : " + color +" }"
		+ "#"+id+" .scroll-bar:vertical .increment-arrow,.scroll-bar:vertical .decrement-arrow{-fx-shape : \" \";    -fx-padding :0.0 0.15em;}"

		+ "#"+id+" .scroll-bar:horizontal .thumb,.scroll-bar:vertical .thumb {-fx-background-color :derive(black,90.0%);-fx-background-insets : 2.0, 0.0, 0.0;-fx-background-radius : 2.0em;}"

		+ "#"+id+" .scroll-bar:horizontal .thumb:hover,.scroll-bar:vertical .thumb:hover {-fx-background-color :derive(#4D4C4F,10.0%);-fx-background-insets : 2.0, 0.0, 0.0;-fx-background-radius : 2.0em;}";
			
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
		    FileWriter fileWriter = null;
		    
		    File file = new File(dir+File.separator + "test.css");
			
			
			if(file.exists()){
			    file.delete();
			}
			file.createNewFile();
			fileWriter = new FileWriter(dir+File.separator+"test.css", true);
			
			BufferedWriter bufferWriter = new BufferedWriter(fileWriter);
			bufferWriter.close();
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
	
	public void initLenthOfMoment() {
		if((lengthInit/MomentComparaison.getmaxNbMoment())<2.0){
			lengthInit += 150;
		} else if((lengthInit/MomentComparaison.getmaxNbMoment())<5.0){
			lengthInit += 120;
		} else if((lengthInit/MomentComparaison.getmaxNbMoment())<7.0){
			lengthInit += 100;
		} else if((lengthInit/MomentComparaison.getmaxNbMoment())<10.0) {
			lengthInit += 80;
		} else if((lengthInit/MomentComparaison.getmaxNbMoment())<13.0) {
			lengthInit += 60;
		} else if((lengthInit/MomentComparaison.getmaxNbMoment())<15.0) {
			lengthInit += 40;
		} else if((lengthInit/MomentComparaison.getmaxNbMoment())<20.0){
			lengthInit += 20;
		} else if((lengthInit/MomentComparaison.getmaxNbMoment())<25.0){
			lengthInit += 15;
		}
	}
	
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

