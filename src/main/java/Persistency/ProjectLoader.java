package Persistency;

import interviewSelector.Models.Interview;
import Components.InterviewPanel.Models.InterviewText;
import application.Project.Models.Project;
import Persistency.v1.*;
import Components.SchemaTree.Cell.Models.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ProjectLoader {

    final static int CURRENT_VERSION = 1;
    private static ElementPool pool = new ElementPool();

    public static Project load(String path) throws IOException {
        final FileInputStream fichier = new FileInputStream(path);
        ByteBuffer buffer = ByteBuffer.wrap(fichier.readAllBytes());
        fichier.close();

        //Checking the version
        PersistentProject p;
        int version = Serializer.readInt(buffer);
        switch(version) {
            case 1: p = new ProjectV1(); break;
            default: throw new IllegalArgumentException("Unrecognized UPMT project version !");
        }

        //Filling the ProjectVi
        Serializer serializer = new Serializer();
        serializer.readObject(buffer, p);

        //Upgrade the datastructure if needed !
        while(p.getVersion() != CURRENT_VERSION)
            p = p.upgradeToNextVersion();

        Project project = convertProject(p);
        pool.clearExistingElements();
        return project;
    }

    private static Project convertProject(PersistentProject project) {
        ProjectV1 p = (ProjectV1)project;
        Project r = new Project(p.name, convertSchemaTreeRoot(p.schemaTreeRoot));
        for(InterviewV1 i: p.interviews)
            r.addInterview(convertInterview(i));
        return r;
    }

    private static Interview convertInterview(InterviewV1 interview) {
        return pool.getOrCreateElement(interview, (interviewV1) -> {
            return new Interview("FakeTitle", interviewV1.participantName, interviewV1.date, convertInterviewText(interviewV1.interviewText));
        });
    }

    private static InterviewText convertInterviewText(InterviewTextV1 interviewText) {
        return pool.getOrCreateElement(interviewText, (interviewTextV1) -> {
            return new InterviewText(interviewTextV1.text);
        });
    }

    private static void convertSchemaElement(SchemaElementV1 elementV1, SchemaElement element) {
        element.expandedProperty().set(elementV1.expanded);
        element.nameProperty().set(elementV1.name);
    }

    private static SchemaTreeRoot convertSchemaTreeRoot(SchemaTreeRootV1 treeRoot) {
        return pool.getOrCreateElement(treeRoot, schemaTreeRootV1 -> {
            SchemaTreeRoot r = new SchemaTreeRoot(schemaTreeRootV1.name);
            convertSchemaElement(schemaTreeRootV1, r);
            for(SchemaFolderV1 f: schemaTreeRootV1.folders) { r.addChild(convertSchemaFolder(f)); };
            return r;
        });
    }

    private static SchemaFolder convertSchemaFolder(SchemaFolderV1 folder) {
        return pool.getOrCreateElement(folder, schemaFolderV1 -> {
            SchemaFolder r =  new SchemaFolder(schemaFolderV1.name);
            convertSchemaElement(schemaFolderV1, r);
            for(SchemaFolderV1 f : schemaFolderV1.folders) { r.addChild(convertSchemaFolder(f)); };
            for(SchemaCategoryV1 c : schemaFolderV1.categories) { r.addChild(convertSchemaCategory(c)); };
            return r;
        });
    }

    private static SchemaCategory convertSchemaCategory(SchemaCategoryV1 category) {
        return pool.getOrCreateElement(category, schemaCategoryV1 -> {
            SchemaCategory r = new SchemaCategory(schemaCategoryV1.name);
            convertSchemaElement(schemaCategoryV1, r);
            for(SchemaPropertyV1 p: schemaCategoryV1.properties) { r.addChild(convertSchemaProperty(p));};
            return r;
        });
    }

    private static SchemaProperty convertSchemaProperty(SchemaPropertyV1 property) {
        return pool.getOrCreateElement(property, propertyV1 -> {
            SchemaProperty r = new SchemaProperty(propertyV1.name);
            convertSchemaElement(propertyV1, r);
            return r;
        });
    }
}
