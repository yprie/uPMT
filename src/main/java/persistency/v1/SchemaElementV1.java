package persistency.v1;

import persistency.PersistentElement;
import persistency.Serializer;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class SchemaElementV1 implements PersistentElement {

    public String name;
    public boolean expanded;

    @Override
    public void write(ByteArrayOutputStream bytes, Serializer sr) {
        SerializerV1.writeString(bytes, name);
        Serializer.writeBoolean(bytes, expanded);
    }

    @Override
    public void read(ByteBuffer bytes, Serializer sr) {
        this.name = SerializerV1.readString(bytes);
        this.expanded = Serializer.readBoolean(bytes);
    }
}
