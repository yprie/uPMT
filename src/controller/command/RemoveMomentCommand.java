package controller.command;

import java.util.LinkedList;

import application.Main;
import controller.MomentExpVBox;
import javafx.scene.Node;
import utils.MainViewTransformations;
import utils.Undoable;

public class RemoveMomentCommand implements Command,Undoable{
	
	private MomentExpVBox moment;
	private Main main;
	private MomentExpVBox parent=null;
	private MomentExpVBox momentAfterChange;
	
	public RemoveMomentCommand(MomentExpVBox m, Main main) {
		System.out.println("Nouvelle instance de supression");
	    moment = m;
	    momentAfterChange = moment;
	    this.main = main;
	    if (moment.hasParent()) {
	      parent = moment.getVBoxParent();
	      System.out.println("L'élément qu'on veut supprimer a 1 parent");
	    }
	    else {
	      System.out.println("L'élément qu'on veut supprimer a 0 parent");
	      parent = null;
	    }
	    System.out.println("L'élément qu'on veut supprimer a pour index " + moment.getCol());
	}

	@Override
	public void undo() {
		if (parent == null) {
		      AddMomentCommand cmd = new AddMomentCommand(momentAfterChange, main);
		      cmd.execute();
		      momentAfterChange = cmd.getMomentAfterChanges();
		    }
		    else {
		      AddMomentToMomentCommand cmd = new AddMomentToMomentCommand(momentAfterChange, parent, main);
		      cmd.execute();
		      momentAfterChange = cmd.getMomentAfterChanges();
		    }
	}

	@Override
	public void redo() {
		execute();
	}

	@Override
	public String getUndoRedoName() {
		return "removeMoment";
	}
	
	public MomentExpVBox getMomentAfterChanges() {
		return momentAfterChange;
	}
	

	@Override
	public void execute() {
		if (parent != null) {
		      System.out.println("On supprime " + moment.getMoment().getNom() + " qui est dans un parent.");
		      removeMomentFromParent(moment.getVBoxParent());
		    }
		    else
		    {
		      System.out.println("On supprime " + moment.getMoment().getNom());
		      main.getCurrentDescription().setNumberCols(main.getCurrentDescription().getNumberCols() - 2);
		      main.getCurrentDescription().getMoments().remove(moment.getMoment());
		      LinkedList<Node> moments = new LinkedList();
		      
		      Node a = (Node)main.getGrid().getChildren().remove(moment.getCol() - 1);
		      Node b = (Node)main.getGrid().getChildren().remove(moment.getCol() - 1);
		      for (int i = 0; i < main.getGrid().getChildren().size(); i++) {
		        moments.add((Node)main.getGrid().getChildren().get(i));
		      }
		      moments.remove(a);
		      moments.remove(b);
		      main.getGrid().getChildren().clear();
		      for (int j = 0; j < moments.size(); j++) {
		        main.getGrid().add((Node)moments.get(j), j, 0);
		      }
		      main.getGrid().getColumnConstraints().remove(moment.getCol() - 1);
		      main.getGrid().getColumnConstraints().remove(moment.getCol() - 1);
		      
		      for (int i = 0; i < main.getGrid().getChildren().size(); i++) {
		        try {
		          MomentExpVBox m = (MomentExpVBox)main.getGrid().getChildren().get(i);
		          if (m.getCol() >= moment.getCol()) {
		            m.setCol(m.getCol() - 2);
		          }
		        }
		        catch (ClassCastException localClassCastException) {}
		      }
		    }
		    
		    momentAfterChange = moment;
		    
		    utils.MainViewTransformations.loadGridData(main.getGrid(), main, main.getCurrentDescription());
		    
		    main.needToSave();

	}
	

	private void removeMomentFromParent(MomentExpVBox parent){
		if (!parent.getSousMomentPane().getChildren().isEmpty()) {
			for (Node child : parent.getSousMomentPane().getChildren()) {
				MomentExpVBox childMEV = (MomentExpVBox)child;
				if (childMEV.getMoment().equals(this.moment.getMoment())) {
					parent.getSousMomentPane().getChildren().remove(child);
					parent.getMoment().getSousMoments().remove(this.moment.getMoment());
					break;
				}
				else{
					removeMomentFromParent(childMEV);
				}
			}
		}
	}
	

	@Override
	public boolean canExecute() {
		// TODO Auto-generated method stub
		return false;
	}

}
