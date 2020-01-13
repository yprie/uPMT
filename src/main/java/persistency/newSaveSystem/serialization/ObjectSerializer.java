package persistency.newSaveSystem.serialization;

import java.util.ArrayList;

public interface ObjectSerializer {

    String getString(String s);
    void writeString(String name, String s);

    int getInt(String s);
    void writeInt(String name, int value);

    ObjectSerializer getObject(String name);
    void writeObject(String name, Serializable object);

    ArrayList<ObjectSerializer> getArray(String name);
    void writeArray(String name, ArrayList<? extends Serializable> objects);
}
