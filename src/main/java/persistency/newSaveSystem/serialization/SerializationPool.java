package persistency.newSaveSystem.serialization;

import java.util.HashMap;

public class SerializationPool<K, V> {

    HashMap<K, V> hashmap = new HashMap<>();

    public void add(K key, V s) {
        if(contain(key))
            throw new IllegalArgumentException("Serializable with key = " + key + " is already present in the pool !");
        hashmap.put(key, s);
    }

    public boolean contain(K key) {
        return hashmap.containsKey(key);
    }

    public V get(K key) {
        if(!contain(key))
            throw new IllegalArgumentException("Serializable with id = " + key + " is not present in the pool !");
        return hashmap.get(key);
    }


}
