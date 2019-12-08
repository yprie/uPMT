package ApplicationHistory;

import java.util.UUID;

public abstract class ICommand<ExecuteResult, UnexecuteResult> {

    UUID userActionIdentifier;

    void setUserActionIdentifier(UUID id) { userActionIdentifier = id;}
    UUID getUserActionIdentifier() { return userActionIdentifier; }

    public abstract ExecuteResult execute();
    public abstract UnexecuteResult unexecute();

}
