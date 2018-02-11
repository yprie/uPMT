/*****************************************************************************
 * MainViewTransformations.java
 *****************************************************************************
 * Copyright � 2017 uPMT
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston MA 02110-1301, USA.
 *****************************************************************************/

package utils;

import java.awt.Frame;
import java.io.IOException;
import java.util.LinkedList;

import com.sun.javafx.geom.transform.GeneralTransform3D;

import application.Main;
import controller.MomentExpVBox;
import controller.TypeClassRepresentationController;
import controller.command.AddMomentCommand;
import controller.command.AddTypeCommand;
import controller.command.MoveMomentCommand;
import controller.command.MoveMomentToMomentCommand;
import controller.command.RemoveTypeCommand;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.MenuItem;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import model.Classe;
import model.DescriptionEntretien;
import model.MomentExperience;
import model.Serializer;
import model.Type;

public abstract class MainViewTransformations {
	
	public static void addBorderPaneMomentListener(MomentExpVBox moment, Main main){

		moment.getMomentPane().setOnDragOver(new EventHandler<DragEvent>() {
		    public void handle(DragEvent event) {
		    	// Checking if a type is already present
		    	boolean doesntalreadyHasType = true;
		    	String typeType = event.getDragboard().getRtf(); 
		    	for (Node n : moment.getTypeSpace().getChildren()) {
					TypeClassRepresentationController type = (TypeClassRepresentationController)n;
					if (type.getClasse().getName().equals(typeType)) {
						doesntalreadyHasType = false;
						break;
					}	
				}
		    	
		    	boolean cond = true;
		    	MomentExperience draggedMoment=null;
				try {
					draggedMoment = (MomentExperience)Serializer.deserialize((byte[])event.getDragboard().getContent(MomentExpVBox.df));
					if(draggedMoment!=null) {
						//draggedMoment ne peut pas etre depose sur lui meme
						if(draggedMoment.equals(moment.getMoment()))
							cond = false;
						//draggedMoment ne peut pas etre depose sur ses enfants
						else if(moment.isAChildOf(draggedMoment))
							cond = false;
						//draggedMoment ne peut pas etre depose sur son pere direct
						else if(moment.isDirectParentOf(draggedMoment))
							cond = false;
					}
					else cond = false;
				} catch (Exception e) {}
				
				
				//System.out.println(event.getDragboard().getString());
		    	// setting the drag autorizations
		    	if(((event.getDragboard().getString().equals("ajoutType") && doesntalreadyHasType)
		    			|| event.getDragboard().getString().equals("ajoutMoment")
		    			|| event.getDragboard().getString().equals("moveMoment"))
		    			&& cond){
			        event.acceptTransferModes(TransferMode.ANY);
		    	}		    	
		    	event.consume();
		    	
		    }
		});	
		
		/* Right click to delete a moment */
		moment.getMomentPane().addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
			ContextMenu contextMenu = new ContextMenu();
			MenuItem menu1 = new MenuItem("Delete");
	        menu1.setOnAction(p -> {
	            //todo do sth
	        	moment.deleteMoment();
	        });
	        contextMenu.getItems().add(menu1);
	        
	        MenuItem menu2 = new MenuItem("Copy");
	        menu2.setOnAction(p -> {
	            //todo do sth
	        	
	        });
	        contextMenu.getItems().add(menu2);
		    if (e.getButton().equals(MouseButton.SECONDARY) || e.isControlDown()) {
		        contextMenu.show(moment, e.getScreenX(), e.getScreenY());
		    }
		    else {
		    	contextMenu.hide();
		    }
		});
		
		moment.getMomentPane().setOnDragDropped(new EventHandler<DragEvent>() {
		    public void handle(DragEvent event) {
		    	if(event.getDragboard().getString().equals("ajoutType")){
		    		AddTypeCommand cmd = new AddTypeCommand(moment,event,main);
			    	cmd.execute();
			    	UndoCollector.INSTANCE.add(cmd);
		    	}	
		    	if (event.getDragboard().getString().equals("ajoutMoment")) {
		    		//Add Moment to a Moment : int: index in sous-moment / Moment: parentMoment / Main
			    	AddMomentCommand cmd = new AddMomentCommand(moment.getMoment().getSousMoments().size(),moment.getMoment(),main);
			    	cmd.execute();
			    	UndoCollector.INSTANCE.add(cmd);
				}
		    	if (event.getDragboard().getString().equals("moveMoment")) {
		    		MomentExperience serial=null;
					try {
						serial = (MomentExperience)Serializer.deserialize((byte[])event.getDragboard().getContent(MomentExpVBox.df));
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
			    	MoveMomentToMomentCommand cmd = new MoveMomentToMomentCommand(serial, moment.getMoment(),moment.getMoment().getSousMoments().size() ,main);
			    	cmd.execute();
			    	UndoCollector.INSTANCE.add(cmd);
				}
		    	event.setDropCompleted(true);
		    	event.consume();
		    } 
		});
		

		//Click the moment panel
		moment.getMomentPane().setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton().equals(MouseButton.PRIMARY)) {
					main.setCurrentMoment(moment);
					boolean childHasFocus = false; 
					for(Node type : moment.getTypeSpace().getChildren()){
						TypeClassRepresentationController tt = (TypeClassRepresentationController) type;
						if (tt.isFocused()) {
							childHasFocus = true;
						}
					}
					if(!childHasFocus){
						moment.requestFocus();
					}
				}
			}
		});	
		
		moment.focusedProperty().addListener(new ChangeListener<Boolean>()
		{
		    @Override
		    public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
		    {
		        if (newPropertyValue)
		        {
		        	moment.setBorderColor("#039ED3");
		        }
		        else
		        {
		        	moment.setBorderColor("black");
		        }
		    }
		});
	}
	
	
	/*
	 * Listener du panel qu'il y a entre plusieurs moment
	 * */
	public static void addPaneOnDragListener(Pane p, Main main) {
		p.setOnDragOver(new EventHandler<DragEvent>() {
		    public void handle(DragEvent event) {
		    	// setting the drag autorizations
		    	boolean cond = true;
		    	MomentExperience draggedMoment=null;
				try {
					draggedMoment = (MomentExperience)Serializer.deserialize((byte[])event.getDragboard().getContent(MomentExpVBox.df));
					if(draggedMoment!=null) {
						if(!draggedMoment.hasParent()) {
							int pos = main.getGrid().getChildren().indexOf(p)/2;
							int i = ((Integer)event.getDragboard().getContent(MomentExpVBox.realCol));
							cond = ((pos-i) > 1) || ((pos-i)<0) ;
							System.out.println("pos-i="+(pos-i)+" -- 1 < "+(pos-i)+" < 0 ?"+cond);
						}
						else System.out.println(draggedMoment.getNom()+" a un parent:"+draggedMoment.getParent().getNom());
					}
					else cond = false;
				} catch (Exception e) {System.out.println("null");}
		       
		        //System.out.println(Math.abs(i - pos));
		        if ((event.getDragboard().getString().equals("ajoutMoment")) || 
		          (event.getDragboard().getString().equals("moveMoment") && cond) ) {
		          event.acceptTransferModes(TransferMode.ANY);
		        }
		        event.consume();
		    }
		});	
		
		p.setOnDragDropped(new EventHandler<DragEvent>() {
		    public void handle(DragEvent event) {
		    	int pos = main.getGrid().getColumnIndex(p)/2;
		    	if (event.getDragboard().getString().equals("ajoutMoment")) {
		    		System.out.println("On ajoute un nouveau moment � l'index "+pos);
			    	AddMomentCommand cmd = new AddMomentCommand(pos,main);
			    	cmd.execute();
			    	UndoCollector.INSTANCE.add(cmd);
				}
		    	if (event.getDragboard().getString().equals("moveMoment")) {
		    		System.out.println("Panel, move a moment in index "+pos);
		    		MomentExperience serial=null;
					try {
						serial = (MomentExperience)Serializer.deserialize((byte[])event.getDragboard().getContent(MomentExpVBox.df));
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
			    	MoveMomentCommand cmd = new MoveMomentCommand(serial, pos,main);
			    	cmd.execute();
			    	UndoCollector.INSTANCE.add(cmd);
				}
		    	event.consume();
		    } 
		});
	}
	
	/*public static void addMomentExpBorderPaneListener(MomentExpVBox mp, Main main){
		addBorderPaneMomentListener(mp, main);
		mp.setOnDragDropped(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				Integer colIndex = GridPane.getColumnIndex((Node) event.getSource());		        
		        // If the drag action is meant to add a Moment
		        if(event.getDragboard().getString().equals("ajoutMoment")){
			        //mp.setCol(colIndex);
			        // Add moment to selected gridArea
			        AddMomentCommand cmd = new AddMomentCommand(mp.getMoment(),main);
					cmd.execute();
					UndoCollector.INSTANCE.add(cmd);
			        event.setDropCompleted(true); 
			    	event.consume();
				}
		        if(event.getDragboard().getString().equals("moveMoment")){
		        	MomentExperience serial=null;
					try {
						serial = (MomentExperience)Serializer.deserialize((byte[])event.getDragboard().getContent(MomentExpVBox.df));
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
			        //System.out.println("On est la");
			        mp.setMoment(serial);
			        //System.out.println("Nom: "+mp.getMoment().getNom());
			        mp.setCol(colIndex);
			        // Add moment to selected gridArea
			        AddMomentCommand cmd = new AddMomentCommand(mp,main);
					cmd.execute();
					UndoCollector.INSTANCE.add(cmd);
			        event.setDropCompleted(true); 
			    	event.consume();
				}
		    }
		});
		
		mp.setOnDragOver(new EventHandler<DragEvent>() {
		    public void handle(DragEvent event) {
		    	Integer colIndex = GridPane.getColumnIndex((Node) event.getSource());
		    	try {
			        MomentExpVBox tmp = (MomentExpVBox) main.getGrid().getChildren().get(colIndex);
			    	if((event.getDragboard().getString().equals("ajoutMoment")||event.getDragboard().getString().equals("moveMoment")) && mp.getChildren().size() < 2){
			    		event.acceptTransferModes(TransferMode.ANY);
				        event.consume();
			    	}
		    	}catch(ClassCastException e) {}
		    }
		});
	}*/
	
	public static void addTypeListener(TypeClassRepresentationController boutType,MomentExpVBox m,Type type,Main main){
		boutType.focusedProperty().addListener(new ChangeListener<Boolean>()
		{
		    @Override
		    public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
		    {
		        if (newPropertyValue)
		        {
		        	boutType.colorFocus();	
		        }
		        else
		        {
		        	boutType.resetFocusColor();
		        }
		    }
		});
		
		boutType.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				boutType.requestFocus();
			}
		});
		
		boutType.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if ((event.getCode().equals(KeyCode.DELETE) || event.getCode().equals(KeyCode.BACK_SPACE)) & boutType.isFocused()) {
					RemoveTypeCommand cmd = new RemoveTypeCommand(boutType,m,main);
					cmd.execute();
					UndoCollector.INSTANCE.add(cmd);
				}
			}
		});
	}
	
	public static void loadSousMoment(MomentExpVBox mev,Main main){
		if(mev.getChildren().size() == 1){
			mev.getChildren().add(mev.getSousMomentPane());
		}
		for (MomentExperience m: mev.getMoment().getSousMoments()) {
			MomentExpVBox tmp = new MomentExpVBox(main);
			tmp.setMoment(m);
			tmp.setVBoxParent(mev);
			tmp.showMoment();
			tmp.LoadMomentData();


			ColumnConstraints c = new ColumnConstraints();
			mev.getSousMomentPane().getColumnConstraints().add(c);
			mev.getSousMomentPane().add(tmp,mev.getSousMomentPane().getColumnConstraints().size()-1,0);
			MainViewTransformations.addBorderPaneMomentListener(tmp, main);
			loadTypes(tmp,main);
			loadSousMoment(tmp, main);
		}		
	}
	
	public static void loadTypes(MomentExpVBox mp,Main main){
		//System.out.println("------------------------------------");
		//System.out.println(mp.getMoment().getNom()+": ");
		for (Type t : mp.getMoment().getTypes()) {
			TypeClassRepresentationController classe = new TypeClassRepresentationController((Classe) t,mp,main);
			mp.getTypeSpace().getChildren().add(classe);
			addTypeListener(classe, mp, t, main);
			//System.out.println(t.toString());
		}
		//System.out.println("------------------------------------");
	}
	
	/*public static Color ContrastColor(Color iColor){
	   // Calculate the perceptive luminance (aka luma) - human eye favors green color... 
	   double luma = ((0.299 * iColor.getRed()) + (0.587 * iColor.getGreen()) + (0.114 * iColor.getBlue())) / 255;
	   // Return black for bright colors, white for dark colors
	   return luma > 0.5 ? Color.BLACK : Color.WHITE;
	}*/
	
	public static Color ContrastColor(Color color) {
		  double y = (299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000;
		  System.out.println(y);
		  return y*255 >= 128 ? Color.BLACK : Color.WHITE;
		}
	
	public static void updateGrid(Main main) {
		loadGridData(main.getGrid(), main, main.getCurrentDescription());
	}
	
	public static String allMomentsToString(Main main) {
		String ret="Tous les moments: {\n";
		for(Node n: main.getGrid().getChildren()) {
			try {
				MomentExpVBox m = (MomentExpVBox)n;
				ret+=m.toString();
			}catch(ClassCastException e) {}
		}
		ret+="}";
		return ret;
	}
	
	public static int getInterviewIndex(DescriptionEntretien e, Main main) {
		LinkedList<DescriptionEntretien> tmp = main.getCurrentProject().getEntretiens();
		int ret=-1;
		for(int i=0; i<tmp.size();i++) {
			//System.out.println(main.getCurrentDescription().toString() +" - "+ tmp.get(i).toString());
			if(tmp.get(i).equals(main.getCurrentDescription())) {
				//System.out.println(main.getCurrentDescription().toString() +" - "+ tmp.get(i).toString());
				ret = i;
				break;
			}
		}
		//System.out.println("On trouve pour index: "+ret+" contre "+main.getProjects().indexOf(main.getCurrentProject()));
		return ret;
	}
	
	// Method used to load the grid related to a certain Interview
	public static void loadGridData(GridPane grid,Main main, DescriptionEntretien d){
		// Grid initialisation ( reset )
		grid.getChildren().clear();
		grid.getColumnConstraints().clear();
		// Grid Creation
		// for each moment of the interview we add a collumn
		for (int j = 0; j < d.getNumberOfMoments(); j++) {
				//System.out.println("On ajoute deux colonnes, "+d.getNumberOfMoments());
				ColumnConstraints c = new ColumnConstraints();
				c.setMinWidth(25);
				c.setPrefWidth(25);
				c.setMaxWidth(25);
				grid.getColumnConstraints().add(c);
				
				ColumnConstraints c2 = new ColumnConstraints();
				c2.setMinWidth(180);
				c2.setPrefWidth(Control.USE_COMPUTED_SIZE);
				c2.setMaxWidth(Control.USE_COMPUTED_SIZE);
				grid.getColumnConstraints().add(c2);
		}
		//Ajout de la colonne de fin
		ColumnConstraints c = new ColumnConstraints();
		c.setMinWidth(180);
		c.setPrefWidth(180);
		c.setMaxWidth(180);
		grid.getColumnConstraints().add(c);
		
		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < d.getNumberOfMoments(); j++) {
				//System.out.println("On ajoute un panel");
				// Creation of the Moment box
				int width = 25;
				//if(j>=d.getNumberOfMoments()-1)width=180;
				Pane p = new Pane();
				p.setPrefWidth(width);
				p.setMaxWidth(width);
				p.setMinWidth(width);
				p.setPrefHeight(200);
				p.setMinHeight(200);
				//p.setStyle("-fx-background-color:black;");
				addPaneOnDragListener(p, main);
				grid.add(p,j*2,i);
				
				//System.out.println("On ajoute un moment");
				//System.out.println("J'ajoute un moment � "+j);
				MomentExpVBox mp = new MomentExpVBox(main);
				addBorderPaneMomentListener(mp, main);
				
				MomentExperience mom;
				boolean hasMoment = false;
				if (main.getCurrentDescription() != null) {
					for (MomentExperience m : d.getMoments()) {
						//System.out.println(m.getNom()+" est � "+m.getGridCol()+" et j est � "+j);
						if(m.getGridCol() == j){
							mom = m;
							mp.setMoment(mom);
							hasMoment = true;
						}
					}
				}
				if (hasMoment) {
					mp.showMoment();
					mp.LoadMomentData();
					loadTypes(mp, main);
					loadSousMoment(mp,main);
				}				
				grid.add(mp,(j*2)+1,i);
			}
		}
		Pane p = new Pane();
		p.setPrefWidth(180);
		p.setMaxWidth(180);
		p.setMinWidth(180);
		p.setPrefHeight(200);
		p.setMinHeight(200);
		//p.setStyle("-fx-background-color:black;");
		addPaneOnDragListener(p, main);
		grid.add(p,d.getNumberOfMoments()*2,0);
	}
	
	public static void deleteMoment(MomentExperience toCompare, Main main) {
		for(MomentExperience current:main.getCurrentDescription().getMoments()) {
			System.out.println("On compare "+current.getNom()+" � "+toCompare.getNom());
			if(current.equals(toCompare)) {
				main.getCurrentDescription().removeMomentExp(current);
				System.out.println("On supprime "+current.getNom());
				break;
			}
			else {
				deleteMomentFromParent(current, toCompare);
			}
			System.out.println("On ne supprime pas "+current.getNom());
		}
	}
	
	public static void deleteMomentFromParent(MomentExperience parent, MomentExperience toCompare) {
		for(MomentExperience current:parent.getSousMoments()) {
			System.out.println("**On compare "+current.getNom()+" � "+toCompare.getNom());
			if(current.equals(toCompare)) {
				parent.removeSousMoment(current);
				System.out.println("**On supprime "+current.getNom());
				break;
			}
			else {
				deleteMomentFromParent(current, toCompare);
			}
			System.out.println("**On ne supprime pas "+current.getNom());
		}
	}
	
	public static MomentExpVBox getMomentVBoxByMoment(MomentExperience e, Main main) {
		MomentExpVBox ret=null;
		int i = 0;
		for(Node n : main.getGrid().getChildren()) {
			if(ret!=null)break;
			try {
				MomentExpVBox m = (MomentExpVBox)n;
				if(m.getMoment().equals(e)){
					ret = m;
					break;
				}
				else {
					if(!m.getSousMomentPane().getChildren().isEmpty()) {
						ret = getMomentVBoxByMomentFromParent(m, e, main);
					}
				}
			}catch(ClassCastException exc) {}//Si exception alors c'est un panel
		}
		return ret;
	}
	
		public static MomentExpVBox getMomentVBoxByMomentFromParent(MomentExpVBox moment, MomentExperience e, Main main){
			MomentExpVBox ret = null;
				for (Node child : moment.getSousMomentPane().getChildren()) {
					if(ret!=null) {
						break;
					}
					MomentExpVBox childMEV = (MomentExpVBox)child;
					if (childMEV.getMoment().equals(e)) {
						ret = childMEV;
						break;
					}
					else{
						ret = getMomentVBoxByMomentFromParent(childMEV, e, main);
					}
				}
			return ret;
		}

		public static void setDragCursor(Region r) {
			r.setOnMouseEntered(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent arg0) {
					r.setCursor(Cursor.MOVE);
				}
			});
			r.setOnMouseExited(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent arg0) {
					r.setCursor(Cursor.DEFAULT);
				}
			});
		}
		
}
