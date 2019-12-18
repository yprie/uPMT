package persistency.v1;

import persistency.PersistentProject;
import persistency.Serializer;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class ProjectV1 implements PersistentProject {

    private final int VERSION = 1;
    public String name;
    public SchemaTreeRootV1 schemaTreeRoot;
    public PersistentListV1<InterviewV1> interviews;
    public InterviewV1 selectedInterview;

    @Override
    public void write(ByteArrayOutputStream bytes, Serializer sr) {
        SerializerV1.writeString(bytes, name);
        sr.writeObject(bytes, schemaTreeRoot);
        sr.writeObject(bytes, interviews);
    }

    @Override
    public void read(ByteBuffer bytes, Serializer sr) {
        this.name = SerializerV1.readString(bytes);
        this.schemaTreeRoot = sr.readObject(bytes, new SchemaTreeRootV1());
        this.interviews = sr.readObject(bytes, new PersistentListV1<InterviewV1>(InterviewV1::new));
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
