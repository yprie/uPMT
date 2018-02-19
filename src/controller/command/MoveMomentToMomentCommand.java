package controller.command;

import application.Main;
import controller.MomentExpVBox;
import model.DescriptionEntretien;
import model.MomentExperience;
import utils.MainViewTransformations;
import utils.Undoable;

public class MoveMomentToMomentCommand  implements Command,Undoable{
	private MomentExperience moment;
	private MomentExperience newParent;
	private int toCol;
	private Main main;
	private DescriptionEntretien dataBefore;
	private DescriptionEntretien dataAfter;
	private int indexInterview;
	
	public MoveMomentToMomentCommand(MomentExperience moment, MomentExperience parent, int toCol, Main main){
		this.moment = moment;
		this.main = main;
		this.toCol = toCol;
		this.newParent = parent;
	}
	

	@Override
	public void undo() {
		//Update Interview in the project
		main.getCurrentProject().removeEntretiens(indexInterview);
		main.getCurrentProject().addEntretiens(indexInterview, (DescriptionEntretien)this.dataBefore.clone());
		
		//Edit Current Interview
		main.setCurrentDescription((DescriptionEntretien)this.dataBefore.clone());
		MainViewTransformations.updateGrid(main);
		main.needToSave();
	}

	@Override
	public void redo() {
		//Update Interview in the project
		main.getCurrentProject().removeEntretiens(indexInterview);
		main.getCurrentProject().addEntretiens(indexInterview, (DescriptionEntretien)this.dataAfter.clone());
		
		//Edit Current Interview
		main.setCurrentDescription((DescriptionEntretien)this.dataAfter.clone());
		MainViewTransformations.updateGrid(main);
		main.needToSave();
	}

	@Override
	public String getUndoRedoName() {
		return "moveMoment";
	}

	@Override
	public void execute() {
		dataBefore = (DescriptionEntretien)main.getCurrentDescription().clone();
		indexInterview = new Integer(MainViewTransformations.getInterviewIndex(main.getCurrentDescription(), main));
		if(moment.hasParent()) 
			if(  (moment.getParent().equals(newParent))  &&  (moment.getGridCol()<toCol)  ) toCol--;
		System.out.println("Enleve le moment à la position "+moment.getGridCol()+" et l'ajoute à la col "+toCol);
		MainViewTransformations.deleteMoment(moment, main);
		
		this.newParent.addSousMoment(toCol, moment);
		
		MainViewTransformations.updateGrid(main);
	    main.needToSave();
	    dataAfter  = (DescriptionEntretien)main.getCurrentDescription().clone();
	}
	
	@Override
	public boolean canExecute() {
		return false;
	}
}
