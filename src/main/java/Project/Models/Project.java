package Project.Models;

import Project.Persistency.ProjectSaver;
import SchemaTree.Cell.Models.SchemaTreeRoot;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.io.Serializable;

public class Project implements Serializable {

    private SimpleStringProperty name;
    private SimpleObjectProperty<SchemaTreeRoot> schemaTreeRoot;

    public Project(String name, SchemaTreeRoot baseScheme) {
        this.name = new SimpleStringProperty(name);
        this.schemaTreeRoot = new SimpleObjectProperty<SchemaTreeRoot>(baseScheme);
    }

    public String getName() { return this.name.get(); };
    public StringProperty nameProperty() { return this.name; }

    public SchemaTreeRoot getSchemaTreeRoot() { return schemaTreeRoot.get(); };
    public SimpleObjectProperty<SchemaTreeRoot> schemaTreeRootProperty() {return schemaTreeRoot;};

    public void saveAs(String name, String path) throws IOException {
        if(!name.contains(".upmt"))
            name += ".upmt";
        ProjectSaver.save(this, path+name);
    }
}
