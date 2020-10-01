package application.configuration;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

// This is where to store the global settings of the application.
// Theses settings are stored in the property file. See `Configuration.savePropertiesFile()`.
public class AppSettings {
    public static SimpleBooleanProperty autoScrollWhenReveal = new SimpleBooleanProperty();
    public static int delayRevealDescripteme;
    public static SimpleIntegerProperty zoomLevelProperty = new SimpleIntegerProperty();
}
