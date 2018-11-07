package controller.command;

import application.Main;
import controller.controller.TypeController;
import controller.typeTreeView.TypeTreeView;
import javafx.scene.control.TreeItem;
import model.Category;
import utils.Undoable;

public class AddCategorySchemeCommand implements Command,Undoable{
	

	
	private int numberFolder;
	private TypeTreeView parentTree;
	private TreeItem<TypeController> newCategory;
	private Main main;
	
	public AddCategorySchemeCommand(TypeTreeView parentTree, int number, Main main) {
		this.numberFolder = number;
		this.parentTree = parentTree;
		this.main = main;
	}
	
	
	@Override
	public void undo() {
		RemoveCategoryFromParentCommand cmd = new RemoveCategoryFromParentCommand(
				newCategory.getValue(), 
				(Category) newCategory.getValue().getType(),
				newCategory.getParent(),
				main);
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
		newCategory = this.parentTree.addClass(this.numberFolder);
		main.needToSave();
	}

	@Override
	public boolean canExecute() {
		return false;
	}
}
