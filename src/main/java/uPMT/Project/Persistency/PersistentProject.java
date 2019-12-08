package Project.Persistency;

import java.io.Serializable;

public interface PersistentProject extends PersistentElement {
    int getVersion();
    PersistentProject upgradeToNextVersion();
}
