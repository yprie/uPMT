package ApplicationHistory;

import java.util.UUID;

public class HistoryManager {

    HistoryState state;

    public HistoryManager(HistoryState state) {
        this.state = state;
    }

    public void executeUserAction() {
        UUID actionIdentifier = state.getNextStack().peek().getUserActionIdentifier();
        while(canGoForward(actionIdentifier)) {
            ICommand c = state.getNextStack().pop();
            c.execute();
            state.getPreviousStack().push(c);
        }
    }

    public void unexecuteUserAction() {
        UUID actionIdentifier = state.getPreviousStack().peek().getUserActionIdentifier();
        while(canGoForward(actionIdentifier)) {
            ICommand c = state.getPreviousStack().pop();
            c.unexecute();
            state.getNextStack().push(c);
        }
    }

    public void startNewUserAction() {
        state.startNewUserAction();
    }

    public void addCommand(ICommand command) {
        command.setUserActionIdentifier(state.getCurrentUserActionId());
        command.execute();
        state.getPreviousStack().push(command);
        state.getNextStack().removeAllElements();
    }

    private boolean canGoBack(UUID lastCommandIdentifier) {
        return state.getPreviousStack().size() > 0 && state.getPreviousStack().peek().getUserActionIdentifier() == lastCommandIdentifier;
    }

    private boolean canGoForward(UUID lastCommandIdentifier) {
        return state.getNextStack().size() > 0 && state.getNextStack().peek().getUserActionIdentifier() == lastCommandIdentifier;
    }
}
