package controller.command;

import application.Main;
import model.DescriptionInterview;
import model.MomentExperience;
import utils.MainViewTransformations;
import utils.Undoable;

public class RemoveMomentCommand implements Command,Undoable{

	private MomentExperience moment;
	private Main main;
	private DescriptionInterview dataBefore;
	private DescriptionInterview dataAfter;
	private int indexInterview;
	
	public RemoveMomentCommand(MomentExperience moment, Main main){
		this.moment = moment;
		this.main = main;
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
		return "removeMoment";
	}

	@Override
	public void execute() {
		dataBefore = (DescriptionInterview)main.getCurrentDescription().clone();
		indexInterview = new Integer(MainViewTransformations.getInterviewIndex(main.getCurrentDescription(), main));
		MainViewTransformations.deleteMoment(moment, main);
		
		MainViewTransformations.updateGrid(main);
	    main.needToSave();
	    dataAfter  = (DescriptionInterview)main.getCurrentDescription().clone();
	    main.needToSave();
	}
	
	@Override
	public boolean canExecute() {
		return false;
	}

}
