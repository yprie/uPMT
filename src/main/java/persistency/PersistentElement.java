package persistency;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public interface PersistentElement {

    void write(ByteArrayOutputStream bytes, Serializer sr);
    void read(ByteBuffer bytes, Serializer sr);
}
