package persistency;

import application.project.models.Project;
import components.modelisationSpace.moment.model.Moment;
import org.json.JSONObject;
import persistency.newSaveSystem.SProject;
import persistency.newSaveSystem.serialization.SerializationPool;
import persistency.newSaveSystem.serialization.json.JSONReadPool;
import persistency.newSaveSystem.serialization.json.JSONSerializer;

import java.io.FileReader;
import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Stack;

public class ProjectLoader {

    public static Project load(String path) throws IOException {
        FileReader fileReader = new FileReader(path);
        String fileContents = "";
        int i ;
        while((i =  fileReader.read())!=-1){
            char ch = (char)i;
            fileContents = fileContents + ch;
        }
        fileReader.close();

        JSONObject obj = new JSONObject(fileContents);
        JSONReadPool pool = new JSONReadPool();
        SerializationPool<Object> modelsPool = new SerializationPool<>();
        JSONSerializer serializer = new JSONSerializer(obj, pool, modelsPool);

        SProject p = new SProject(serializer);
        return p.convertToModel();

    }

}
