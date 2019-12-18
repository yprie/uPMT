package application.history;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;

import java.util.Stack;
import java.util.UUID;

public class HistoryState {

    private Stack<ModelUserActionCommand> previous;
    private Stack<ModelUserActionCommand> next;
    private UUID currentUserActionId;

    private ReadOnlyBooleanWrapper canGoBack;
    private ReadOnlyBooleanWrapper canGoForward;

    HistoryState() {
        previous = new Stack<ModelUserActionCommand>();
        next = new Stack<ModelUserActionCommand>();
        canGoBack = new ReadOnlyBooleanWrapper(false);
        canGoForward = new ReadOnlyBooleanWrapper(false);
    }

    void addCommand(ModelUserActionCommand command, boolean startNewUserAction) {
        if(startNewUserAction)
            currentUserActionId = UUID.randomUUID();

        if(!(!canGoBack() && !startNewUserAction)) {
            command.setUserActionIdentifier(currentUserActionId);
            next.removeAllElements();
            canGoForward.set(false);
            next.push(command);
            executeSingleAction();
        }
        else
            command.execute();

    }

    void executeUserAction() {
        if(canGoForward()){
            currentUserActionId = next.peek().getUserActionIdentifier();
            boolean keepExecuting;
            do {
                executeSingleAction();
                keepExecuting = canGoForward() && next.peek().getUserActionIdentifier() == currentUserActionId;
            } while(keepExecuting);
        }
    }

    void unexecuteUserAction() {
        if(canGoBack()){
            currentUserActionId = previous.peek().getUserActionIdentifier();
            boolean keepUnexecuting;
            do {
                unexecuteSingleAction();
                keepUnexecuting = canGoBack() && previous.peek().getUserActionIdentifier() == currentUserActionId;
            } while(keepUnexecuting);
        }
    }

    void clear() {
        currentUserActionId = null;
        previous.clear();
        next.clear();
        canGoBack.set(false);
        canGoForward.set(false);
    }

    ReadOnlyBooleanProperty canGoBackProperty() { return canGoBack.getReadOnlyProperty(); }
    ReadOnlyBooleanProperty canGoForwardProperty() { return canGoForward.getReadOnlyProperty(); }

    UUID getCurrentCommandId() { return previous.size() > 0 ? previous.peek().getUserActionIdentifier() : null; };

    private boolean canGoBack() {
        canGoBack.set(previous.size() > 0);
        return canGoBack.get();
    }

    private boolean canGoForward() {
        canGoForward.set(next.size() > 0);
        return canGoForward.get();
    }

    private void executeSingleAction() {
        ModelUserActionCommand c = next.pop();
        c.execute();
        previous.push(c);
        canGoBack.set(true);
    }

    private void unexecuteSingleAction() {
        ModelUserActionCommand c = previous.pop();
        c.undo();
        next.push(c);
        canGoForward.set(true);
    }
}
