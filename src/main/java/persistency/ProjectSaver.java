package persistency;

import models.Project;
import org.json.JSONObject;
import persistency.newSaveSystem.SProject;
import persistency.newSaveSystem.serialization.json.JSONSerializer;
import persistency.newSaveSystem.serialization.json.JSONWritePool;

import java.io.*;

public class ProjectSaver {

    public static void save(Project project, String fullPath) throws IOException {
        JSONObject obj = new JSONObject();
        JSONWritePool pool = new JSONWritePool();
        JSONSerializer serializer = new JSONSerializer(obj, pool);
        SProject p = new SProject(project);
        p.save(serializer);

        BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath));
        writer.append(obj.toString(4));
        writer.close();
    }

}
