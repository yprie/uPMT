package persistency.newSaveSystem;

import application.project.models.Project;
import components.interviewSelector.models.Interview;
import persistency.newSaveSystem.serialization.ObjectSerializer;
import persistency.newSaveSystem.serialization.Serializable;

import java.util.ArrayList;

public class SProject extends Serializable<Project> {

    public static final int version = 1;
    public static final String modelName = "project";

    public String name;
    public SSchemaTreeRoot schemaTreeRoot;
    public ArrayList<SInterview> interviews;

    public SProject(ObjectSerializer serializer) {
        super(serializer);
    }

    public SProject(Project modelReference) {
        super(modelName, version, modelReference);
        //TODO
        this.name = modelReference.getName();
        this.schemaTreeRoot = new SSchemaTreeRoot(modelReference.getSchemaTreeRoot());
        this.interviews = new ArrayList<>();
        for(Interview i : modelReference.interviewsProperty()){
            interviews.add(new SInterview(i));
        }
    }

    @Override
    protected void addStrategies() {

    }

    @Override
    protected void read() {
        name = serializer.getString("name");
        schemaTreeRoot = serializer.getObject(SSchemaTreeRoot.modelName, SSchemaTreeRoot::new);
        interviews = serializer.getArray(serializer.setListSuffix(SInterview.modelName), SInterview::new);
    }

    @Override
    public void write(ObjectSerializer serializer) {
        serializer.writeString("name", name);
        serializer.writeObject(SSchemaTreeRoot.modelName, schemaTreeRoot);
        serializer.writeArray(serializer.setListSuffix(SInterview.modelName), interviews);
    }

    @Override
    protected Project createModel() {
        Project p = new Project(this.name, schemaTreeRoot.convertToModel());

        for(SInterview i: interviews) {
            p.addInterview(i.convertToModel());
        }

        return p;
    }

}
