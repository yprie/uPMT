package persistency.newSaveSystem.serialization.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import persistency.newSaveSystem.serialization.ObjectSerializer;
import persistency.newSaveSystem.serialization.Serializable;
import persistency.newSaveSystem.serialization.SerializationPool;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.Function;

public class JSONSerializer implements ObjectSerializer {

    private JSONWritePool write_pool;
    private JSONReadPool read_pool;
    private SerializationPool<Object> models_pool;
    private JSONObject jsonObject;

    //Writing constructor
    public JSONSerializer(JSONObject jsonObject, JSONWritePool pool) {
        this.jsonObject = jsonObject;
        this.write_pool = pool;
    }

    //Reading constructor
    public JSONSerializer(JSONObject jsonObject, JSONReadPool pool, SerializationPool<Object> models_pool) {
        this.jsonObject = jsonObject;
        this.read_pool = pool;
        this.models_pool = models_pool;
    }


    @Override
    public String getString(String s) {
        return jsonObject.getString(s);
    }

    @Override
    public String getFacultativeString(String s, String defaultstr) {
        try {
            return getString(s);
        }
        catch (JSONException e){
            return defaultstr;
        }
    }

    @Override
    public void writeString(String name, String s) {
        jsonObject.put(name, s);
    }

    @Override
    public void writeFacultativeString(String name, String s) {
        if(s != null)
            writeString(name, s);
    }


    @Override
    public int getInt(String s) {
        return jsonObject.getInt(s);
    }

    @Override
    public void writeInt(String name, int value) {
        jsonObject.put(name, value);
    }

    @Override
    public boolean getBoolean(String s) {
        return jsonObject.getBoolean(s);
    }

    @Override
    public void writeBoolean(String name, boolean b) {
        jsonObject.put(name, b);
    }

    @Override
    public LocalDate getLocalDate(String s) {
        return LocalDate.parse(getString(s), dateFormat);
    }

    @Override
    public void writeLocalDate(String name, LocalDate d) {
        writeString(name, d.format(dateFormat));
    }


    @Override
    public <T extends Serializable> T getObject(String name, Function<ObjectSerializer, T> serializableCreator) {
        JSONObject object = jsonObject.getJSONObject(name);
        int id = object.getInt("@id");
        if(read_pool.contain(id)){
            //Take from pool with the required cast
            return (T) read_pool.get(id);
        }
        else{
            //Add in the pool
            JSONSerializer serializer = new JSONSerializer(object, read_pool, models_pool);
            T result = serializableCreator.apply(serializer);
            read_pool.add(id, result);
            return result;
        }
    }

    @Override
    public <T extends Serializable> T getFacultativeObject(String name, Function<ObjectSerializer, T> serializableCreator) {
        try {
            return getObject(name, serializableCreator);
        }
        catch (JSONException e){
            return null;
        }
    }

    @Override
    public void writeObject(String name, Serializable object) {
        jsonObject.put(name, fillJSONObject(object));
    }

    @Override
    public void writeFacultativeObject(String name, Serializable object) {
        if(object != null)
            writeObject(name, object);
    }


    @Override
    public <T extends Serializable> ArrayList<T> getArray(String name, Function<ObjectSerializer, T> serializableCreator) {
        JSONArray array = jsonObject.getJSONArray(name);
        ArrayList<T> result = new ArrayList<>();
        for(int i = 0; i < array.length(); i++) {
            int id = array.getJSONObject(i).getInt("@id");
            if(read_pool.contain(id)) {
                result.add((T) read_pool.get(id));
            }
            else {
                JSONSerializer serializer = new JSONSerializer(array.getJSONObject(i), read_pool, models_pool);
                T serializable = serializableCreator.apply(serializer);
                read_pool.add(id, serializable);
                result.add(serializable);
            }
        }
        return result;
    }

    @Override
    public void writeArray(String name, ArrayList<? extends Serializable> objects) {
        JSONArray arr = new JSONArray();
        for(Serializable object : objects) {
            arr.put(fillJSONObject(object));
        }
        jsonObject.put(name, arr);
    }

    @Override
    public SerializationPool<Object> getModelsPool() {
        return models_pool;
    }

    @Override
    public String setListSuffix(String name) {
        return name + "_list";
    }


    private JSONObject fillJSONObject(Serializable object) {
        JSONObject obj = new JSONObject();
        JSONSerializer serializer = new JSONSerializer(obj, write_pool);

        int id = object.getSerializationId();
        //Write the id in every case
        serializer.writeInt("@id", id);

        //Only write the header if in the pool
        if(write_pool.contain(id)){
            object.saveReferenced(serializer);
        }
        //Adding the serializer in the pool
        else {
            write_pool.add(id, serializer);
            //Write head and body for the first apparition
            object.save(serializer);
        }
        return obj;
    }
}
