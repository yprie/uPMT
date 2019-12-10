package Persistency;

public interface PersistentProject extends PersistentElement {
    int getVersion();
    PersistentProject upgradeToNextVersion();
}
