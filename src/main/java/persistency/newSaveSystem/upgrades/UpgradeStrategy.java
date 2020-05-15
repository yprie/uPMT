package persistency.newSaveSystem.upgrades;

import persistency.newSaveSystem.serialization.ObjectSerializer;

public interface UpgradeStrategy {

    int getPriorVersion();
    int getNextVersion();

    boolean upgrade(ObjectSerializer serializer);
}
