package persistency.newSaveSystem.serialization;

import java.util.HashMap;

public class JSONSerializerPool {

    HashMap<Integer, JSONSerializer> hashmap = new HashMap<>();

    void add(int id, JSONSerializer s) {
        if(contain(id))
            throw new IllegalArgumentException("Serializable with id = " + id + " is already present in the pool !");
        hashmap.put(id, s);
    }

    boolean contain(int id) {
        return hashmap.containsKey(id);
    }

    JSONSerializer get(int id) {
        if(!contain(id))
            throw new IllegalArgumentException("Serializable with id = " + id + " is not present in the pool !");
        return hashmap.get(id);
    }


}
