package controller.command;

import java.text.ParseException;

import application.Main;
import controller.MomentExpVBox;
import utils.Undoable;

public class ChangeDateMomentCommand  implements Command,Undoable{
	private Main main;
	private String mNewText, mOldText;
	private MomentExpVBox moment;
	
	public ChangeDateMomentCommand(String newText, String oldText, MomentExpVBox momentBox, Main main){
		this.main = main;
		this.mNewText = newText;
		this.mOldText = oldText;
		this.moment = momentBox;
	}
	

	@Override
	public void undo() {
		try {
			moment.getMoment().setDate(this.mOldText);
			moment.editMenuTime(this.mOldText);
			main.needToSave();
		} catch (ParseException e) {}
	}

	@Override
	public void redo() {
		execute();
	}

	@Override
	public String getUndoRedoName() {
		return "changeDateMoment";
	}

	@Override
	public void execute() {
		try {
			moment.getMoment().setDate(this.mNewText);
			moment.editMenuTime(this.mNewText);
			main.needToSave();
		} catch (ParseException e) {}
	}
	
	@Override
	public boolean canExecute() {
		return false;
	}
}
