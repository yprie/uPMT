package Project.Persistency.v1;

import Project.Persistency.PersistentProject;
import Project.Persistency.Serializer;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class ProjectV1 implements PersistentProject {

    private final int VERSION = 1;
    public String name;
    public SchemaTreeRootV1 schemaTreeRoot;


    @Override
    public void write(ByteArrayOutputStream bytes, Serializer sr) {
        SerializerV1.writeString(bytes, name);
        sr.writeObject(bytes, schemaTreeRoot);
    }

    @Override
    public void read(ByteBuffer bytes, Serializer sr) {
        this.name = SerializerV1.readString(bytes);
        this.schemaTreeRoot = sr.readObject(bytes, new SchemaTreeRootV1());
    }

    @Override
    public int getVersion() {
        return VERSION;
    }


    @Override
    public PersistentProject upgradeToNextVersion() {
        //In the futur the conversion to ProjectV2 will start here !
        return null;
    }
}
