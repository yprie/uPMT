package application.History;

public class HistoryManagerFactory {

    private static HistoryState state = new HistoryState();

    public static HistoryManager createHistoryManager() {
        return new HistoryManager(state);
    }

}
