/*****************************************************************************
 * MainViewTransformations.java
 *****************************************************************************
 * Copyright © 2017 uPMT
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

import com.sun.javafx.geom.transform.GeneralTransform3D;

import application.Main;
import controller.MomentExpVBox;
import controller.TypeClassRepresentationController;
import controller.command.AddMomentCommand;
import controller.command.AddMomentToMomentCommand;
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
import javafx.scene.control.Control;
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
import model.SerializedMomentVBox;
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
						//draggedMoment ne peut pas être déposé sur lui même
						if(draggedMoment.equals(moment.getMoment()))
							cond = false;
						//draggedMoment ne peut pas être déposé sur ses enfants
						else if(moment.isAChildOf(draggedMoment))
							cond = false;
						//draggedMoment ne peut pas être déposé sur son père direct
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
		
		moment.getMomentPane().setOnDragDropped(new EventHandler<DragEvent>() {
		    public void handle(DragEvent event) {
		    	if(event.getDragboard().getString().equals("ajoutType")){
		    		AddTypeCommand cmd = new AddTypeCommand(moment,event,main);
			    	cmd.execute();
			    	UndoCollector.INSTANCE.add(cmd);
		    	}	
		    	if (event.getDragboard().getString().equals("ajoutMoment")) {
			    	AddMomentToMomentCommand cmd = new AddMomentToMomentCommand(moment,main);
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
					int initCol = (Integer)event.getDragboard().getContent(MomentExpVBox.realCol);
			    	MoveMomentToMomentCommand cmd = new MoveMomentToMomentCommand(serial, moment ,main);
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
		        int i = -10;
		        int pos = 0;
		        try {
		          pos = main.getGrid().getChildren().indexOf(p);
		          i = ((Integer)event.getDragboard().getContent(MomentExpVBox.realCol));
		          if (i < 0) i = -10;
		        } catch (Exception e) {
		          i = -10;
		          pos = 0;
		        }
		        //System.out.println(Math.abs(i - pos));
		        if (((event.getDragboard().getString().equals("ajoutMoment")) || 
		          (event.getDragboard().getString().equals("moveMoment"))) && 
		          (Math.abs(i - pos) > 1)) {
		          event.acceptTransferModes(TransferMode.ANY);
		        }
		        event.consume();
		    }
		});	
		
		p.setOnDragDropped(new EventHandler<DragEvent>() {
		    public void handle(DragEvent event) {
		    	if (event.getDragboard().getString().equals("ajoutMoment")) {
		    		MomentExpVBox moment = new MomentExpVBox(main);
			    	moment.setCol(main.getGrid().getColumnIndex(p)+1);
			    	addMomentExpBorderPaneListener(moment, main);
		    		//System.out.println("Panel, add a moment");
			    	AddMomentCommand cmd = new AddMomentCommand(moment,main);
			    	cmd.execute();
			    	UndoCollector.INSTANCE.add(cmd);
				}
		    	if (event.getDragboard().getString().equals("moveMoment")) {
		    		//System.out.println("Panel, move a moment");
		    		MomentExperience serial=null;
					try {
						serial = (MomentExperience)Serializer.deserialize((byte[])event.getDragboard().getContent(MomentExpVBox.df));
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
			    	MoveMomentCommand cmd = new MoveMomentCommand(serial, main.getGrid().getColumnIndex(p),main);
			    	cmd.execute();
			    	UndoCollector.INSTANCE.add(cmd);
				}
		    	event.consume();
		    } 
		});
	}
	
	public static void addMomentExpBorderPaneListener(MomentExpVBox mp, Main main){
		addBorderPaneMomentListener(mp, main);
		mp.setOnDragDropped(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				Integer colIndex = GridPane.getColumnIndex((Node) event.getSource());		        
		        // If the drag action is meant to add a Moment
		        if(event.getDragboard().getString().equals("ajoutMoment")){
			        mp.setCol(colIndex);
			        // Add moment to selected gridArea
			        AddMomentCommand cmd = new AddMomentCommand(mp,main);
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
	}
	
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
			MainViewTransformations.addMomentExpBorderPaneListener(tmp, main);
			loadTypes(tmp,main);
			loadSousMoment(tmp, main);
		}		
	}
	
	public static void loadTypes(MomentExpVBox mp,Main main){
		System.out.println("------------------------------------");
		System.out.println(mp.getMoment().getNom()+": ");
		for (Type t : mp.getMoment().getType()) {
			TypeClassRepresentationController classe = new TypeClassRepresentationController((Classe) t,mp,main);
			mp.getTypeSpace().getChildren().add(classe);
			addTypeListener(classe, mp, t, main);
			System.out.println(t.toString());
		}
		System.out.println("------------------------------------");
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
	
	// Method used to load the grid related to a certain Interview
	public static void loadGridData(GridPane grid,Main main, DescriptionEntretien d){
		// Grid initialisation ( reset )
		grid.getChildren().clear();
		grid.getColumnConstraints().clear();
		// Grid Creation
		// for each moment of the interview we add a collumn
		int k=0;
		for (int j = 0; j < d.getNumberCols(); j++) {
			if(k==0) {
				int width = 25;
				if(j>=d.getNumberCols()-1)width=180;
				ColumnConstraints c = new ColumnConstraints();
				c.setMinWidth(width);
				c.setPrefWidth(width);
				c.setMaxWidth(width);
				grid.getColumnConstraints().add(c);
				k++;
			}
			else {
				ColumnConstraints c = new ColumnConstraints();
				c.setMinWidth(180);
				c.setPrefWidth(Control.USE_COMPUTED_SIZE);
				c.setMaxWidth(Control.USE_COMPUTED_SIZE);
				grid.getColumnConstraints().add(c);
				k--;
			}
		}
		
		for (int i = 0; i < 1; i++) {
			k=0;
			for (int j = 0; j < d.getNumberCols(); j++) {
				// Creation of the Moment box	
				if(k==0) {
					int width = 25;
					if(j>=d.getNumberCols()-1)width=180;
					Pane p = new Pane();
					p.setPrefWidth(width);
					p.setMaxWidth(width);
					p.setMinWidth(width);
					p.setPrefHeight(200);
					p.setMinHeight(200);
					//p.setStyle("-fx-background-color:black;");
					addPaneOnDragListener(p, main);
					grid.add(p,j,i);
					k++;
				}
				else {
					//System.out.println("J'ajoute un moment à "+j);
					MomentExpVBox mp = new MomentExpVBox(main);
					addMomentExpBorderPaneListener(mp, main);
					
					MomentExperience mom;
					boolean hasMoment = false;
					if (main.getCurrentDescription() != null) {
						for (MomentExperience m : d.getMoments()) {
							//System.out.println(m.getNom()+" est à "+m.getGridCol()+" et j est à "+j);
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
					grid.add(mp,j,i);
					k--;
				}
			}
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
