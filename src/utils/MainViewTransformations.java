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

import com.sun.javafx.geom.transform.GeneralTransform3D;

import application.Main;
import controller.MomentExpVBox;
import controller.TypeClassRepresentationController;
import controller.command.AddMomentCommand;
import controller.command.AddMomentToMomentCommand;
import controller.command.AddTypeCommand;
import controller.command.RemoveTypeCommand;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import model.Classe;
import model.DescriptionEntretien;
import model.MomentExperience;
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
		    	
		    	// setting the drag autorizations
		    	if((event.getDragboard().getString().equals("ajoutType") && doesntalreadyHasType) ||
		    			event.getDragboard().getString().equals("ajoutMoment")){
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
		    }
		});
		
		mp.setOnDragOver(new EventHandler<DragEvent>() {
		    public void handle(DragEvent event) {
		    	Integer colIndex = GridPane.getColumnIndex((Node) event.getSource());
		        MomentExpVBox tmp = (MomentExpVBox) main.getGrid().getChildren().get(colIndex);
		    	if(event.getDragboard().getString().equals("ajoutMoment") && mp.getChildren().size() < 2){
		    		event.acceptTransferModes(TransferMode.ANY);
			        event.consume();
		    	}
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
		for (Type t : mp.getMoment().getType()) {
			TypeClassRepresentationController classe = new TypeClassRepresentationController((Classe) t,mp,main);
			mp.getTypeSpace().getChildren().add(classe);
			addTypeListener(classe, mp, t, main);
		}
	}
	
	// Method used to load the grid related to a certain Interview
	public static void loadGridData(GridPane grid,Main main, DescriptionEntretien d){
		// Grid initialisation ( reset )
		grid.getColumnConstraints().clear();
		// Grid Creation
		// for each moment of the interview we add a collumn
		for (int j = 0; j < d.getNumberCols(); j++) {
			ColumnConstraints c = new ColumnConstraints();
			c.setMinWidth(180);
			c.setPrefWidth(Control.USE_COMPUTED_SIZE);
			c.setMaxWidth(Control.USE_COMPUTED_SIZE);
			grid.getColumnConstraints().add(c);
		}
		
		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < d.getNumberCols(); j++) {
				// Creation of the Moment box			
				MomentExpVBox mp = new MomentExpVBox(main);
				addMomentExpBorderPaneListener(mp, main);
				
				MomentExperience mom;
				boolean hasMoment = false;
				if (main.getCurrentDescription() != null) {
					for (MomentExperience m : d.getMoments()) {
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
			}
		}
	}

}
