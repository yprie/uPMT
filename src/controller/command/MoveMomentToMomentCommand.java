package controller.command;

import application.Main;
import controller.MomentExpVBox;
import model.MomentExperience;
import utils.MainViewTransformations;
import utils.Undoable;

public class MoveMomentToMomentCommand  implements Command,Undoable{
	private Main main;
	private MomentExpVBox initMoment;
	private MomentExpVBox initMoment2;
	private MomentExpVBox toMoment;
	private MomentExpVBox initParent;
	private int initCol;
	private RemoveMomentCommand rm;
	private AddMomentToMomentCommand add;
	
	public MoveMomentToMomentCommand(MomentExperience initMExp, MomentExpVBox toM, Main main){
		this(MainViewTransformations.getMomentVBoxByMoment(initMExp, main), toM, main);
	}
	
	public MoveMomentToMomentCommand(MomentExpVBox initM, MomentExpVBox toM, Main main){
		this.main = main;
		this.initMoment = initM;
		this.initCol = initMoment.getCol();
		this.toMoment = toM;
		this.initParent = initM.getVBoxParent();
	}

	@Override
	public void undo() {
	    initMoment = add.getMomentAfterChanges();
	    
	    toMoment = initParent;
	    if (toMoment != null) {
	      initParent = initMoment.getVBoxParent();
	      initCol = initMoment.getCol();
	      System.out.println("A l'origine il avait pour parent " + toMoment.getMoment().getNom());
	      execute();
	    }
	    else {
	      MoveMomentCommand mv = new MoveMomentCommand(initMoment, initCol - 1, main);
	      mv.execute();
	      System.out.println("A l'origine il n'avait pas de parent et était à la colonne " + initCol);
	    }
	}

	@Override
	public void redo() {
		 System.out.println("REDO MOVEMOMENT TOMOMENT");
		    initMoment = add.getMomentAfterChanges();
		    toMoment = initParent;
		    if (toMoment != null) {
		      initParent = initMoment.getVBoxParent();
		      initCol = initMoment.getCol();
		      System.out.println("A l'origine il avait pour parent " + toMoment.getMoment().getNom());
		      execute();
		    }
		    else {
		      MoveMomentCommand mv = new MoveMomentCommand(initMoment, initCol - 1, main);
		      mv.execute();
		      System.out.println("A l'origine il n'avait pas de parent et était à la colonne " + initCol);
		    }
	}

	@Override
	public String getUndoRedoName() {
		return "moveMoment";
	}

	@Override
	public void execute() {
		if(initMoment.hasParent())
			System.out.println("L'element qu'on veut bouger a 1 parent");
		else
			System.out.println("L'element qu'on veut bouger a 0 parent");
		System.out.println("L'element qu'on veut bouger a pour index "+initMoment.getCol());
		
		rm = new RemoveMomentCommand(initMoment,main);
		rm.execute();
		
		add = new AddMomentToMomentCommand(initMoment, toMoment, main);
		add.execute();
		
		//initMoment2 = add.getMomentAfterChanges();
		//MainViewTransformations.loadGridData(main.getGrid(), main, main.getCurrentDescription());
		
		main.needToSave();
	}

	
	@Override
	public boolean canExecute() {
		return false;
	}


}
