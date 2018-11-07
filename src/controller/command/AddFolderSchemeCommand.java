package controller.command;

import java.util.LinkedList;

import application.Main;
import controller.controller.TypeController;
import controller.typeTreeView.TypeTreeView;
import javafx.application.Platform;
import javafx.scene.control.TreeItem;
import model.Category;
import model.Folder;
import model.Schema;
import utils.Undoable;

public class AddFolderSchemeCommand implements Command,Undoable{
	

	
	private int numberFolder;
	private TypeTreeView parentTree;
	private TreeItem<TypeController> newFolder;
	private Main main;
	
	public AddFolderSchemeCommand(TypeTreeView parentTree, int number, Main main) {
		this.numberFolder = number;
		this.parentTree = parentTree;
		this.main = main;
	}
	
	
	@Override
	public void undo() {
		RemoveFolderSchemeCommand cmd = new RemoveFolderSchemeCommand(newFolder, main);
		cmd.execute();
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
		newFolder = this.parentTree.addFolder(this.numberFolder);
		main.needToSave();
	}

	@Override
	public boolean canExecute() {
		return false;
	}
}
