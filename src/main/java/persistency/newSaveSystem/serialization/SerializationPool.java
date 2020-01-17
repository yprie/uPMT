package persistency.newSaveSystem.serialization;

import java.util.HashMap;

public class SerializationPool<T> {

    HashMap<Integer, T> hashmap = new HashMap<>();

    public void add(int id, T s) {
        if(contain(id))
            throw new IllegalArgumentException("Serializable with id = " + id + " is already present in the pool !");
        hashmap.put(id, s);
    }

    public boolean contain(int id) {
        return hashmap.containsKey(id);
    }

    public T get(int id) {
        if(!contain(id))
            throw new IllegalArgumentException("Serializable with id = " + id + " is not present in the pool !");
        return hashmap.get(id);
    }


}
