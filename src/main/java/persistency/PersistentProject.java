package persistency;

public interface PersistentProject extends PersistentElement {
    int getVersion();
    PersistentProject upgradeToNextVersion();
}
