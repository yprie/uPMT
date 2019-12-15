package application.Project.Models;

import Components.InterviewTree.Cell.Model.InterviewTreeRoot;
import Persistency.ProjectSaver;
import Components.SchemaTree.Cell.Models.SchemaTreeRoot;
import application.Configuration.Configuration;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.io.Serializable;

public class Project implements Serializable {

    private SimpleStringProperty name;
    private SimpleObjectProperty<SchemaTreeRoot> schemaTreeRoot;
    private SimpleObjectProperty<InterviewTreeRoot> interviewTreeRoot;



    public Project(String name, SchemaTreeRoot baseScheme) {
        this.name = new SimpleStringProperty(name);
        this.schemaTreeRoot = new SimpleObjectProperty<SchemaTreeRoot>(baseScheme);
        //TODO: to be replaced by getting it from persistence
        this.interviewTreeRoot = new SimpleObjectProperty<>(new InterviewTreeRoot(Configuration.langBundle.getString("interviews")));
    }

    public String getName() { return this.name.get(); };
    public StringProperty nameProperty() { return this.name; }

    public SchemaTreeRoot getSchemaTreeRoot() { return schemaTreeRoot.get(); };
    public SimpleObjectProperty<SchemaTreeRoot> schemaTreeRootProperty() {return schemaTreeRoot;};

    public InterviewTreeRoot getInterviewTreeRoot() { return interviewTreeRoot.get(); }
    public SimpleObjectProperty<InterviewTreeRoot> interviewTreeRootProperty() {return interviewTreeRoot;};

    public void saveAs(String name, String path) throws IOException {
        if(!name.contains(".upmt"))
            name += ".upmt";
        ProjectSaver.save(this, path+name);
    }
}
