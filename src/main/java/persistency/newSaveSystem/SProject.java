package persistency.newSaveSystem;

import models.Project;
import models.Interview;
import persistency.newSaveSystem.serialization.ObjectSerializer;
import persistency.newSaveSystem.serialization.Serializable;

import java.util.ArrayList;

public class SProject extends Serializable<Project> {

    public static final int version = 1;
    public static final String modelName = "project";

    public String name;
    public SSchemaTreeRoot schemaTreeRoot;
    public ArrayList<SInterview> interviews;
    public SInterview selectedInterview;

    public SProject(ObjectSerializer serializer) {
        super(serializer);
    }

    public SProject(ObjectSerializer serializer, Project modelReference) {
        super(serializer, modelName, version, modelReference);
    }

    @Override
    public void init(Project modelReference) {
        this.name = modelReference.getName();
        this.schemaTreeRoot = new SSchemaTreeRoot(serializer, modelReference.getSchemaTreeRoot());
        this.interviews = new ArrayList<>();
        for(Interview i : modelReference.interviewsProperty()){
            interviews.add(new SInterview(serializer, i));
        }

        if(modelReference.getSelectedInterview() != null){
            this.selectedInterview = new SInterview(serializer, modelReference.getSelectedInterview());
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
        selectedInterview = serializer.getFacultativeObject("selectedInterview", SInterview::new);
    }

    @Override
    public void write(ObjectSerializer serializer) {
        serializer.writeString("name", name);
        serializer.writeObject(SSchemaTreeRoot.modelName, schemaTreeRoot);
        serializer.writeArray(serializer.setListSuffix(SInterview.modelName), interviews);
        serializer.writeFacultativeObject("selectedInterview", selectedInterview);
    }

    @Override
    protected Project createModel() {
        Project p = new Project(this.name, schemaTreeRoot.convertToModel());

        for(SInterview i: interviews) {
            p.addInterview(i.convertToModel());
        }

        if(selectedInterview != null)
            p.setSelectedInterview(selectedInterview.convertToModel());

        return p;
    }

}
