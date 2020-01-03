package persistency.v1;

import persistency.PersistentElement;
import persistency.Serializer;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class InterviewTextV1 implements PersistentElement {

    public String text;

    @Override
    public void write(ByteArrayOutputStream bytes, Serializer sr) {
        SerializerV1.writeString(bytes, text);
    }

    @Override
    public void read(ByteBuffer bytes, Serializer sr) {
        this.text = SerializerV1.readString(bytes);
    }
}
