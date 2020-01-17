package persistency.newSaveSystem.serialization;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.function.Function;

public interface ObjectSerializer {

    static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("d/MM/yyyy");
    enum SerializationType { Object, Array };

    String getString(String s);
    String getFacultativeString(String s, String defaultstr);

    void writeString(String name, String s);
    void writeFacultativeString(String name, String s);

    int getInt(String s);
    void writeInt(String name, int value);

    boolean getBoolean(String s);
    void writeBoolean(String name, boolean b);

    LocalDate getLocalDate(String s);
    void writeLocalDate(String name, LocalDate d);

    <T extends Serializable> T getObject(String name, Function<ObjectSerializer, T> serializableCreator);
    void writeObject(String name, Serializable object);

    <T extends Serializable> ArrayList<T> getArray(String name, Function<ObjectSerializer, T> serializableCreator);
    void writeArray(String name, ArrayList<? extends Serializable> objects);

    //get the concrete object pool to use it when reading a save. To reuse already created objects.
    SerializationPool<Object> getModelsPool();

    String setListSuffix(String name);
}
