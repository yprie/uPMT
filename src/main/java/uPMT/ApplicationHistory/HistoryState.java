package ApplicationHistory;

import java.util.Stack;
import java.util.UUID;

public class HistoryState {

    private Stack<ICommand> previous;
    private Stack<ICommand> next;
    private UUID currentUserActionId;

    HistoryState() {
        previous = new Stack<ICommand>();
        next = new Stack<ICommand>();
    }

    Stack<ICommand> getPreviousStack() {return previous;}
    Stack<ICommand> getNextStack() {return next;}
    UUID getCurrentUserActionId() { return currentUserActionId; }

    void startNewUserAction() { currentUserActionId = UUID.randomUUID(); }
}
