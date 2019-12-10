package Persistency.v1;

import Persistency.Serializer;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class SchemaTreeRootV1 extends SchemaElementV1 {

    public PersistentListV1<SchemaFolderV1> folders;

    @Override
    public void write(ByteArrayOutputStream bytes, Serializer sr) {
        super.write(bytes, sr);
        sr.writeObject(bytes, folders);
    }

    @Override
    public void read(ByteBuffer bytes, Serializer sr) {
        super.read(bytes, sr);
        this.folders = sr.readObject(bytes, new PersistentListV1<SchemaFolderV1>(SchemaFolderV1::new));
    }
}
