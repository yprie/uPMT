package application.Project.Models;

import Components.InterviewPanel.Models.InterviewText;
import Persistency.ProjectSaver;
import Components.SchemaTree.Cell.Models.SchemaTreeRoot;
import interviewSelector.Models.Interview;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;

public class Project implements Serializable {

    private SimpleStringProperty name;
    private SimpleObjectProperty<SchemaTreeRoot> schemaTreeRoot;

    private SimpleListProperty<Interview> interviews;
    private ReadOnlyListWrapper<Interview> readOnlyInterviews;


    public Project(String name, SchemaTreeRoot baseScheme) {
        this.name = new SimpleStringProperty(name);
        this.schemaTreeRoot = new SimpleObjectProperty<SchemaTreeRoot>(baseScheme);

        this.interviews = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.readOnlyInterviews = new ReadOnlyListWrapper<>(this.interviews);
    }

    public String getName() { return this.name.get(); };
    public StringProperty nameProperty() { return this.name; }

    public SchemaTreeRoot getSchemaTreeRoot() { return schemaTreeRoot.get(); };
    public SimpleObjectProperty<SchemaTreeRoot> schemaTreeRootProperty() {return schemaTreeRoot;};

    public void addInterview(Interview i) { interviews.add(i); }
    public void removeInterview(Interview i) { interviews.remove(i); }
    public ReadOnlyListWrapper<Interview> interviewsProperty() { return readOnlyInterviews; }

    public void saveAs(String name, String path) throws IOException {
        if(!name.contains(".upmt"))
            name += ".upmt";
        ProjectSaver.save(this, path+name);
    }
}
