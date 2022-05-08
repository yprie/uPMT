package persistency.newSaveSystem.serialization;

import javafx.scene.paint.Color;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.function.Function;

public interface ObjectSerializer {

    static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("d/MM/yyyy");

    boolean getExists(String setListSuffix);

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

    Color getColor(String s);
    void writeColor(String name, Color c);

    <T extends Serializable> T getObject(String name, Function<ObjectSerializer, T> serializableCreator);
    <T extends Serializable> T getFacultativeObject(String name, Function<ObjectSerializer, T> serializableCreator);

    void writeObject(String name, Serializable object);
    void writeFacultativeObject(String name, Serializable object);

    <T extends Serializable> ArrayList<T> getArray(String name, Function<ObjectSerializer, T> serializableCreator);
    void writeArray(String name, ArrayList<? extends Serializable> objects);

    //get the serialized object from the real object. Used to avoid cycling problems
    <T> SerializationPool<Object, Serializable> getSerializationPool();

    //get the concrete object pool to use it when reading a save. To reuse already created objects.
    SerializationPool<Integer, Object> getModelsPool();

    String setListSuffix(String name);
}
