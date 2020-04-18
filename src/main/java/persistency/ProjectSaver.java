package persistency;

import models.Project;
import org.json.JSONObject;
import persistency.newSaveSystem.SProject;
import persistency.newSaveSystem.serialization.Serializable;
import persistency.newSaveSystem.serialization.SerializationPool;
import persistency.newSaveSystem.serialization.json.JSONSerializer;
import persistency.newSaveSystem.serialization.json.JSONWritePool;

import java.io.*;
import java.util.Stack;

public class ProjectSaver {

    public static void save(Project project, String fullPath) throws IOException {
        JSONObject obj = new JSONObject();
        JSONWritePool pool = new JSONWritePool();
        Stack<Serializable> write_stack = new Stack<>();
        SerializationPool<Object, Serializable> serializationPool = new SerializationPool<>();
        JSONSerializer serializer = new JSONSerializer(obj, pool, write_stack, serializationPool);
        SProject p = new SProject(serializer, project);
        p.save(serializer);

        BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath));
        writer.append(obj.toString(4));
        writer.close();
    }

}
