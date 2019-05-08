

package controller.command;

import controller.controller.Observer;
import model.Descripteme;

import java.util.LinkedList;

import application.Main;
import controller.controller.Observable;
import utils.Undoable;

public class ChangeExtractPropertyCommand implements Command,Undoable{

	private Observable observable;
	private LinkedList<Descripteme> oldDescriptemes;
	private LinkedList<Descripteme> newDescriptemes;
	private Main main;
	
	public ChangeExtractPropertyCommand(Observable observable, LinkedList<Descripteme> oldDescriptemes,LinkedList<Descripteme> newDescriptemes, Main m) {
		this.observable = observable;
		this.oldDescriptemes = oldDescriptemes;
		this.newDescriptemes = newDescriptemes;
		main = m;
	}
	
	@Override
	public void undo() {
		observable.update(oldDescriptemes);
		main.needToSave();
	}

	@Override
	public void redo() {
		execute();
	}

	@Override
	public String getUndoRedoName() {
		return null;
	}

	@Override
	public void execute() {
		observable.update(newDescriptemes);
		main.needToSave();
	}

	@Override
	public boolean canExecute() {
		return false;
	}

}
