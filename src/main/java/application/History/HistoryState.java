package application.History;

import java.util.Stack;
import java.util.UUID;

public class HistoryState {

    private Stack<ModelUserActionCommand> previous;
    private Stack<ModelUserActionCommand> next;
    private UUID currentUserActionId;

    HistoryState() {
        previous = new Stack<ModelUserActionCommand>();
        next = new Stack<ModelUserActionCommand>();
    }

    Stack<ModelUserActionCommand> getPreviousStack() {return previous;}
    Stack<ModelUserActionCommand> getNextStack() {return next;}
    UUID getCurrentUserActionId() { return currentUserActionId; }

    void startNewUserAction() { currentUserActionId = UUID.randomUUID(); }
}
