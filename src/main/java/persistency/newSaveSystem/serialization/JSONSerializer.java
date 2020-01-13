package persistency.newSaveSystem.serialization;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONSerializer implements ObjectSerializer {

    private JSONSerializerPool pool;
    private JSONObject jsonObject;

    public JSONSerializer(JSONObject jsonObject, JSONSerializerPool pool) {
        this.jsonObject = jsonObject;
        this.pool = pool;
    }





    @Override
    public String getString(String s) {
        return jsonObject.getString(s);
    }

    @Override
    public void writeString(String name, String s) {
        jsonObject.put(name, s);
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
    public ObjectSerializer getObject(String name) {
        JSONObject object = jsonObject.getJSONObject(name);
        int id = object.getInt("@id");
        if(pool.contain(id)){
            //Take from pool
            return pool.get(id);
        }
        else{
            //Add in the pool
            JSONSerializer serializer = new JSONSerializer(object, pool);
            pool.add(id, serializer);
            return serializer;
        }
    }

    @Override
    public void writeObject(String name, Serializable object) {
        jsonObject.put(name, fillJSONObject(object));
    }





    @Override
    public ArrayList<ObjectSerializer> getArray(String name) {
        JSONArray array = jsonObject.getJSONArray(name);
        ArrayList<ObjectSerializer> result = new ArrayList<>();
        for(int i = 0; i < array.length(); i++) {
            int id = array.getJSONObject(i).getInt("@id");
            JSONSerializer serializer;
            if(pool.contain(id)) {
                serializer = pool.get(id);
            }
            else {
                serializer = new JSONSerializer(array.getJSONObject(i), pool);
                pool.add(id, serializer);
                result.add(serializer);
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








    private JSONObject fillJSONObject(Serializable object) {
        JSONObject obj = new JSONObject();
        JSONSerializer serializer = new JSONSerializer(obj, pool);

        int id = object.getSerializationId();
        //Write the id in every case
        serializer.writeInt("@id", id);

        //Only write the header if in the pool
        if(pool.contain(id)){
            object.saveReferenced(serializer);
        }
        //Adding the serializer in the pool
        else {
            pool.add(id, serializer);
            //Write head and body for the first apparition
            object.save(serializer);
        }
        return obj;
    }
}
