package controller.command;

import application.Main;
import controller.MomentExpVBox;
import model.DescriptionInterview;
import model.MomentExperience;
import utils.MainViewTransformations;
import utils.Undoable;

public class MoveMomentToMomentCommand  implements Command,Undoable{
	private MomentExperience moment;
	private MomentExperience newParent;
	private int toCol;
	private Main main;
	private DescriptionInterview dataBefore;
	private DescriptionInterview dataAfter;
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
		main.getCurrentProject().addEntretiens(indexInterview, (DescriptionInterview)this.dataBefore.clone());
		
		//Edit Current Interview
		main.setCurrentDescription((DescriptionInterview)this.dataBefore.clone());
		MainViewTransformations.updateGrid(main);
		main.needToSave();
	}

	@Override
	public void redo() {
		//Update Interview in the project
		main.getCurrentProject().removeEntretiens(indexInterview);
		main.getCurrentProject().addEntretiens(indexInterview, (DescriptionInterview)this.dataAfter.clone());
		
		//Edit Current Interview
		main.setCurrentDescription((DescriptionInterview)this.dataAfter.clone());
		MainViewTransformations.updateGrid(main);
		main.needToSave();
	}

	@Override
	public String getUndoRedoName() {
		return "moveMoment";
	}

	@Override
	public void execute() {
		dataBefore = (DescriptionInterview)main.getCurrentDescription().clone();
		indexInterview = new Integer(MainViewTransformations.getInterviewIndex(main.getCurrentDescription(), main));
		if(moment.hasParent()) 
			if(  (moment.getParent(main).equals(newParent))  &&  (moment.getGridCol()<toCol)  ) toCol--;
		MainViewTransformations.deleteMoment(moment, main);
		
		this.newParent.addSubMoment(toCol, moment);
		
		MainViewTransformations.updateGrid(main);
	    main.needToSave();
	    dataAfter  = (DescriptionInterview)main.getCurrentDescription().clone();
	}
	
	@Override
	public boolean canExecute() {
		return false;
	}
}
