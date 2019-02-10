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
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.MomentExperience;
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
	
	//[A B C, B D, D, C E, E] faire une fonction lookingForChild(ArrayList<ME>: pour BC les enfants de A, il cherche dans la liste ou est ce que B est le parent + que ce parent à des fils (Si B a plusieurs fils, il place tous les fils de B, Si B a pas de fils, il laisse de la place, il place, ou C es le parent, il place.

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		MomentComparaison.getInstance().update(main);
		//MomentComparaison.printRowMoments();
		float largeurNoeudEnfant = 100;
		float largeurRacineParent;
		//System.out.println("LALILALALALALALALLALALALALAL");
		
		
		ArrayList<ArrayList<MomentExperience>> mm = MomentComparaison.getmMoments();
		initTag(mm);
		System.out.println("BBBBBBBBBBBBBBBBBBBBB" + mm.toString());
		HBox layout = new HBox();
		largeurNoeudEnfant = largeurNoeudEnfant/mm.size();
		largeurRacineParent = largeurNoeudEnfant;
		int nbRacineMax = mm.size();
		int nbRacine = 0;
		boolean isRacine = true;
		for(ArrayList<MomentExperience> moments : MomentComparaison.getmMoments()) { //separation de la liste en deux
			//System.out.println("SIIIIIIIIZE " + mm.size());
				VBox colTree = new VBox();
				//colTree.setSpacing(100+largeurNoeudEnfant);
				//colTree.setMaxSize(1000, 1000);
				for(MomentExperience moment : moments){
					
					if(nbRacine < nbRacineMax && isRacine) {
						//System.out.println("ooook");
						System.out.println("moment " + moment.getName() + " "+ largeurRacineParent);
						isRacine=false;
						HBox rowTree = new HBox();
						//rowTree.setSpacing(largeurNoeudEnfant);
						//rowTree.setMaxSize(1000, 1000);
						TitledPane t11 = new TitledPane(moment.getName(), new Button("B1"));
						Accordion accordion2 = new Accordion();
						accordion2.getPanes().addAll(t11);
						rowTree.getChildren().add(accordion2);
						colTree.getChildren().add(rowTree);
						nbRacine++;
						moment.setTag(true);
					} 
					
					if(moment.getSubMoments().size()>0) {
						//System.out.println("LLLLLLLLLLLLLLLLLL " + moment.getName());
						/*
						HBox rowTree2 = new HBox();
						//rowTree.setSpacing(largeurNoeudEnfant);
						//rowTree.setMaxSize(1000, 1000);
						
						TitledPane t131 = new TitledPane(moment.getName(), new Button("B1"));
						Accordion accordion3 = new Accordion();
						accordion3.getPanes().addAll(t131);
						rowTree2.getChildren().add(accordion3);
						colTree.getChildren().add(rowTree2);
						
						*/
						HBox rowTree = new HBox();
						//rowTree.setSpacing(largeurNoeudEnfant);
						//rowTree.setMaxSize(1000, 1000);
						System.out.println("momenttttt " + moment.getName() + " "+ moment.getSubMoments().size());
						largeurNoeudEnfant = largeurNoeudEnfant/moment.getSubMoments().size();
						for(MomentExperience m : moment.getSubMoments()) { //pour tous les sous moments de A
							if(m.isTag()==false) {
								//je les met
								System.out.println("moment " + m.getName() + " "+ largeurNoeudEnfant);
								TitledPane t11 = new TitledPane(m.getName(), new Button("B1"));
								Accordion accordion2 = new Accordion();
								accordion2.getPanes().addAll(t11);
								rowTree.getChildren().add(accordion2);
								m.setTag(true);
							}
							
							
							
							
							//TitledPane t11 = new TitledPane(m.getName(), new Button("B1"));
							//Accordion accordion2 = new Accordion();
						    //accordion2.getPanes().addAll(t11);
						    //statsGrid.getChildren().add(accordion2);
							//System.out.println("sous MOMO " + m.getName());
							//System.out.println("largeur moment 2 " + largeur);

							
							
							
							
							
							
							/*
							for(MomentExperience moment2 : moments){ //la liste
								if(m.getID() == moment2.getID() && moment2.getSubMoments().size()>0) { //si le sous moment est egal a un moment parent de la liste
									for(MomentExperience m2 : moment2.getSubMoments()) {
									//je l'ajoute dans la lignée
									TitledPane t111 = new TitledPane(m2.getName(), new Button("B1"));
									Accordion accordion12 = new Accordion();
									accordion12.getPanes().addAll(t111);
									rowTree.getChildren().add(accordion12);
									}
								
								}
								
								
							}
							*/
							
						}
						
						System.out.println("NEW LINE");
						colTree.getChildren().add(rowTree);
						search(moment.getSubMoments(), moments, colTree);
					}
					
				}
				//HBox statsGrid = new HBox();
				//System.out.println("UUUUUUUUUUUUUUUUU");
				//System.out.println("UUUUUUUUUUUUUUUUU");
				//System.out.println("MOMO " + moments.getName() + "     " + moments.getParentID());
				//System.out.println("largeur moment 1 " + largeur);
				
				
				/*
				if(moments.getSubMoments().size()>0) { //si y a des enfants on veut juste afficher le parent
					isMoment=true;
				} else { 
					statsGrid.setSpacing(100+largeur);
					statsGrid.setMaxSize(1000, 1000);
					TitledPane t1 = new TitledPane(moments.getName(), new Button("B1"));
					Accordion accordion = new Accordion();
				    accordion.getPanes().addAll(t1);
				    statsGrid.getChildren().add(accordion);
				}
				*/
				

				// Des qu'il y a un UUUUU changer de VBOX (peut etre faire un for pour creer des VBOX selon la size de la liste moments
					//if(isMoment==true) {
				/*
						statsGrid.setSpacing(100+largeur);
						statsGrid.setMaxSize(1000, 1000);
						TitledPane t1 = new TitledPane(moments.getName(), new Button("B1"));
						Accordion accordion = new Accordion();
					    accordion.getPanes().addAll(t1);
					    statsGrid.getChildren().add(accordion);
					    //System.out.println("oooooooooooooooooooooooooooooooo");
					//}
					isMoment=false;
					
				   */

				    //System.out.println("UUUUUUUUUUUUUUUUU");
			    

			    
			    

				//statsGrid2.getChildren().add(statsGrid);
				isRacine=true;
				largeurNoeudEnfant=largeurRacineParent;
				layout.getChildren().add(colTree);
				

		}
		//statsGrid = new GridPane();
		//statsGrid.setAlignment(Pos.CENTER);
		//statsGrid.setStyle("-fx-padding:  15 0 0 0;");
		 
		this.centralPane.getChildren().add(layout);
	    
		/* display all */

		//this.centralPane.getChildren().add(statsGrid2);
		 
		buttonCloseStats.setText(main._langBundle.getString("close"));
		
		
	}
	
	public void search(LinkedList<MomentExperience> linkedList, ArrayList<MomentExperience> moments, VBox colTree) {
		HBox rowTree = new HBox();
		for(MomentExperience subMoment : linkedList){ //la liste
			//si le sous moment est egal a un moment parent de la liste
				for(MomentExperience moment : moments) {
					if(moment.getID()==subMoment.getID()) {
						for(MomentExperience s : moment.getSubMoments()) {
							if(s.isTag()==false) {
								TitledPane t111 = new TitledPane(s.getName(), new Button("B1"));
								Accordion accordion12 = new Accordion();
								accordion12.getPanes().addAll(t111);
								rowTree.getChildren().add(accordion12);
								s.setTag(true);
							}
						}
						
					}
				//je l'ajoute dans la lignée
				
				}
			
			}
		colTree.getChildren().add(rowTree);
			
			
		}
	
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

