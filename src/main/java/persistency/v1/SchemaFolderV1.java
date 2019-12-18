package persistency.v1;

import persistency.Serializer;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class SchemaFolderV1 extends SchemaElementV1 {

    public PersistentListV1<SchemaFolderV1> folders;
    public PersistentListV1<SchemaCategoryV1> categories;

    @Override
    public void write(ByteArrayOutputStream bytes, Serializer sr) {
        super.write(bytes, sr);
        sr.writeObject(bytes, folders);
        sr.writeObject(bytes, categories);
    }

    @Override
    public void read(ByteBuffer bytes, Serializer sr) {
        super.read(bytes, sr);
        this.folders = sr.readObject(bytes, new PersistentListV1<SchemaFolderV1>(SchemaFolderV1::new));
        this.categories = sr.readObject(bytes, new PersistentListV1<SchemaCategoryV1>(SchemaCategoryV1::new));
    }
}
