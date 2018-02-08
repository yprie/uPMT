package controller.command;

import java.util.LinkedList;

import application.Main;
import controller.MomentExpVBox;
import javafx.scene.Node;
import model.DescriptionEntretien;
import model.MomentExperience;
import utils.MainViewTransformations;
import utils.Undoable;

public class RemoveMomentCommand implements Command,Undoable{

	private MomentExperience moment;
	private Main main;
	private DescriptionEntretien dataBefore;
	private DescriptionEntretien dataAfter;
	private int indexInterview;
	
	public RemoveMomentCommand(MomentExperience moment, Main main){
		this.moment = moment;
		this.main = main;
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
		return "removeMoment";
	}

	@Override
	public void execute() {
		dataBefore = (DescriptionEntretien)main.getCurrentDescription().clone();
		indexInterview = new Integer(MainViewTransformations.getInterviewIndex(main.getCurrentDescription(), main));
		MainViewTransformations.deleteMoment(moment, main);
		
		MainViewTransformations.updateGrid(main);
	    main.needToSave();
	    dataAfter  = (DescriptionEntretien)main.getCurrentDescription().clone();
	    main.needToSave();
	}
	
	@Override
	public boolean canExecute() {
		return false;
	}

}
