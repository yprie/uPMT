package utils.command;

public interface Undoable<ExecuteResult, UndoableResult> extends Executable<ExecuteResult> {
    UndoableResult undo();
}
