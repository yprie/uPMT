

package controller.command;

import controller.controller.Observer;
import application.Main;
import controller.controller.Observable;
import utils.Undoable;

public class ChangeExtractProperty implements Command,Undoable{

	private Observable observable;
	private String oldExtract;
	private String newExtract;
	private Main main;
	
	public ChangeExtractProperty(Observable observable, String oldExtract,String newExtract, Main m) {
		this.observable = observable;
		this.oldExtract = oldExtract;
		this.newExtract = newExtract;
		main = m;
	}
	
	@Override
	public void undo() {
		//System.out.println("actuellement:"+newExtract);
		//System.out.println("change en:"+oldExtract);
		observable.update(oldExtract);
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
		//System.out.println("nouveau:"+newExtract);
		//System.out.println("ancien:"+oldExtract);
		observable.update(newExtract);
		main.needToSave();
	}

	@Override
	public boolean canExecute() {
		return false;
	}

}
