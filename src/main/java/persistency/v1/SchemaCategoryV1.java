package persistency.v1;

import persistency.Serializer;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class SchemaCategoryV1 extends SchemaElementV1 {

    public PersistentListV1<SchemaPropertyV1> properties;

    @Override
    public void write(ByteArrayOutputStream bytes, Serializer sr) {
        super.write(bytes, sr);
        sr.writeObject(bytes, properties);
    }

    @Override
    public void read(ByteBuffer bytes, Serializer sr) {
        super.read(bytes, sr);
        this.properties = sr.readObject(bytes, new PersistentListV1<SchemaPropertyV1>(SchemaPropertyV1::new));
    }
}
