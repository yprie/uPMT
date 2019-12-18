package persistency.v1;

import persistency.Serializer;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class SchemaPropertyV1 extends SchemaElementV1 {

    @Override
    public void write(ByteArrayOutputStream bytes, Serializer sr) {
        super.write(bytes, sr);
    }

    @Override
    public void read(ByteBuffer bytes, Serializer sr) {
        super.read(bytes, sr);
    }
}
