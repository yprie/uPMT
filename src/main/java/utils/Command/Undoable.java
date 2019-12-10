package utils.Command;

public interface Undoable<ExecuteResult, UndoableResult> extends Executable<ExecuteResult> {
    UndoableResult undo();
}
