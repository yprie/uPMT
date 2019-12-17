package Persistency;

import interviewSelector.Models.Interview;
import Components.InterviewPanel.Models.InterviewText;
import application.Project.Models.Project;
import Persistency.v1.*;
import Components.SchemaTree.Cell.Models.*;

import java.io.*;

public class ProjectSaver {

    private static ElementPool pool = new ElementPool();

    public static void save(Project project, String fullPath) throws IOException {
        File f = new File(fullPath);
        final FileOutputStream fichier = new FileOutputStream(f, false);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Serializer serializer = new Serializer();

        Serializer.writeInt(stream, ProjectLoader.CURRENT_VERSION);
        serializer.writeObject(stream, convertProject(project));

        fichier.write(stream.toByteArray());
        fichier.close();
        pool.clearExistingElements();
    }

    //conversion functions
    private static ProjectV1 convertProject(Project project) {
        ProjectV1 r = new ProjectV1();
        r.name = project.getName();
        r.schemaTreeRoot = convertSchemaTreeRoot(project.getSchemaTreeRoot());
        r.interviews = new PersistentListV1<InterviewV1>(InterviewV1::new);
        for(Interview i: project.interviewsProperty().get())
            r.interviews.add(convertInterview(i));
        return r;
    }

    private static InterviewV1 convertInterview(Interview interview) {
        return pool.getOrCreateElement(interview, interview1 -> {
            InterviewV1 r = new InterviewV1();
            r.participantName = interview1.getParticipantName();
            r.comment = interview1.getComment();
            r.date = interview1.getDate();
            r.interviewText = convertInterviewText(interview1.getInterviewText());
            return r;
        });
    }

    private static InterviewTextV1 convertInterviewText(InterviewText interviewText) {
        return pool.getOrCreateElement(interviewText, interviewText1 -> {
            InterviewTextV1 r = new InterviewTextV1();
            r.text = interviewText1.getText();
            return r;
        });
    }

    private static void convertSchemaElement(SchemaElement element, SchemaElementV1 elementV1) {
        elementV1.expanded = element.isExpanded();
        elementV1.name = element.getName();
    }

    private static SchemaTreeRootV1 convertSchemaTreeRoot(SchemaTreeRoot root) {
        return pool.getOrCreateElement(root, schemaTreeRoot -> {
            SchemaTreeRootV1 r = new SchemaTreeRootV1();
            convertSchemaElement(schemaTreeRoot, r);
            r.folders = new PersistentListV1<>(SchemaFolderV1::new);
            for(SchemaFolder f: schemaTreeRoot.foldersProperty()) { r.folders.add(convertSchemaFolder(f)); };
            return r;
        });
    }

    private static SchemaFolderV1 convertSchemaFolder(SchemaFolder folder) {
        return pool.getOrCreateElement(folder, schemaFolder -> {
            SchemaFolderV1 r = new SchemaFolderV1();
            convertSchemaElement(schemaFolder, r);

            r.folders = new PersistentListV1<>(() -> new SchemaFolderV1());
            for(SchemaFolder f: schemaFolder.foldersProperty()) { r.folders.add(convertSchemaFolder(f)); };

            r.categories = new PersistentListV1<>(() -> new SchemaCategoryV1());
            for(SchemaCategory c: schemaFolder.categoriesProperty()) { r.categories.add(convertSchemaCategory(c)); };

            return r;
        });
    }

    private static SchemaCategoryV1 convertSchemaCategory(SchemaCategory category) {
        return pool.getOrCreateElement(category, category1 -> {
            SchemaCategoryV1 r = new SchemaCategoryV1();
            convertSchemaElement(category1, r);

            r.properties = new PersistentListV1<>(() -> new SchemaPropertyV1());
            for(SchemaProperty p: category.propertiesProperty()) { r.properties.add(convertSchemaProperty(p)); };

            return r;
        });
    }

    private static SchemaPropertyV1 convertSchemaProperty(SchemaProperty property) {
        return pool.getOrCreateElement(property, property1 -> {
            SchemaPropertyV1 r = new SchemaPropertyV1();
            convertSchemaElement(property1, r);
            return r;
        });
    }
}
