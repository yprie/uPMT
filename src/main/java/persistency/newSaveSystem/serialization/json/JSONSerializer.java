package persistency.newSaveSystem.serialization.json;

import javafx.scene.paint.Color;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import persistency.newSaveSystem.serialization.ObjectSerializer;
import persistency.newSaveSystem.serialization.Serializable;
import persistency.newSaveSystem.serialization.SerializationPool;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Stack;
import java.util.function.Function;

public class JSONSerializer implements ObjectSerializer {

    private JSONWritePool write_pool;
    private SerializationPool<Object, Serializable> serializationPool;
    private Stack<Serializable> write_stack;

    private JSONReadPool read_pool;
    private SerializationPool<Integer, Object> models_pool;
    private Stack<Serializable> read_stack;

    private JSONObject jsonObject;

    //Writing constructor
    public JSONSerializer(JSONObject jsonObject, JSONWritePool pool, Stack<Serializable> write_stack, SerializationPool<Object, Serializable> serializationPool) {
        this.jsonObject = jsonObject;
        this.write_pool = pool;
        this.write_stack = write_stack;
        this.serializationPool = serializationPool;
    }

    //Reading constructor
    public JSONSerializer(JSONObject jsonObject, JSONReadPool pool, SerializationPool<Integer, Object> models_pool) {
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
    public Color getColor(String s) {
        return Color.valueOf(getString(s));
    }

    @Override
    public void writeColor(String name, Color c) {
        writeString(name, c.toString());
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
            result.initReading();
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

    public boolean getExists(String name) {
        if (jsonObject.has(name)) {
            return true;
        }
        return false;
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
                serializable.initReading();
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
    public <T> SerializationPool<Object, Serializable> getSerializationPool() {
        return serializationPool;
    }

    @Override
    public SerializationPool<Integer, Object> getModelsPool() {
        return models_pool;
    }

    @Override
    public String setListSuffix(String name) {
        return name + "_list";
    }


    private JSONObject fillJSONObject(Serializable object) {
        JSONObject obj = new JSONObject();
        JSONSerializer serializer = new JSONSerializer(obj, write_pool, write_stack, serializationPool);

        int id = object.getSerializationId();

        //If in the pool we save the reference
        if(write_pool.contain(id)){
            object.saveReferenced(serializer);
        }
        //If in the save stack we also save the reference to avoid cycles
        else if(write_stack.contains(object)){
            object.saveReferenced(serializer);
        }
        else {
            write_pool.add(id, serializer);
            write_stack.push(object);
            object.save(serializer);
            write_stack.pop();
        }

        return obj;
    }
}
