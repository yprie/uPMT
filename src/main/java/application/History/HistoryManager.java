package application.History;

import application.appCommands.ApplicationCommandFactory;
import javafx.beans.property.ReadOnlyBooleanProperty;

import java.util.UUID;

public class HistoryManager {

    private static ApplicationCommandFactory applicationCommandFactory;
    private static HistoryState state = new HistoryState();

    public static void init(ApplicationCommandFactory applicationCommandFactory) { HistoryManager.applicationCommandFactory = applicationCommandFactory; }

    public static void addCommand(ModelUserActionCommand cmd, boolean newModelUserActionCommand) {
        state.addCommand(cmd, newModelUserActionCommand);
        applicationCommandFactory.projectSavingStatusChanged().execute();
    }
    public static UUID getCurrentCommandId() { return state.getCurrentCommandId(); };

    public static void goBack() {
        state.unexecuteUserAction();
        applicationCommandFactory.projectSavingStatusChanged().execute();
    }
    public static void goForward() {
        state.executeUserAction();
        applicationCommandFactory.projectSavingStatusChanged().execute();
    }

    public static void clearActionStack() { state.clear(); }

    public static ReadOnlyBooleanProperty canGoBackProperty() { return state.canGoBackProperty(); }
    public static ReadOnlyBooleanProperty canGoForwardProperty() { return state.canGoForwardProperty(); }

}
