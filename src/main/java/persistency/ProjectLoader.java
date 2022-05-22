package persistency;

import models.Project;
import org.json.JSONObject;
import persistency.newSaveSystem.SProject;
import persistency.newSaveSystem.serialization.SerializationPool;
import persistency.newSaveSystem.serialization.json.JSONReadPool;
import persistency.newSaveSystem.serialization.json.JSONSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ProjectLoader {

    public static Project load(String path) throws IOException {
        File fileToRead = new File(path);
        String fileContents;
        FileInputStream file_input = new FileInputStream(fileToRead);
        byte[] content = new byte[(int) fileToRead.length()];
        file_input.read(content);
        file_input.close();
        fileContents = new String(content, StandardCharsets.UTF_8);

        JSONObject obj = new JSONObject(fileContents);
        JSONReadPool pool = new JSONReadPool();
        SerializationPool<Integer, Object> modelsPool = new SerializationPool<>();
        JSONSerializer serializer = new JSONSerializer(obj, pool, modelsPool);

        SProject p = new SProject(serializer);
        p.initReading();
        return p.convertToModel();
    }

}
