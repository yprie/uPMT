package persistency;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class Serializer {

    private static final byte FULL_OBJECT = 0x0;
    private static final byte OBJECT_REFERENCE = 0x1;

    private ElementPool pool = new ElementPool();

    public void writeObject(ByteArrayOutputStream bytes, PersistentElement e) {
        if(pool.getExistingElement(e) != null){
            bytes.write(OBJECT_REFERENCE);
            Serializer.writeInt(bytes, pool.getExistingElementId(e));
        }
        else {
            bytes.write(FULL_OBJECT);
            pool.addExistingElement(e, e);
            Serializer.writeInt(bytes, pool.getExistingElementId(e));
            e.write(bytes, this);
        }
    }

    public <E extends PersistentElement> E readObject(ByteBuffer bytes, E e) {
        byte b = bytes.get();
        int id = Serializer.readInt(bytes);
        switch (b){
            case OBJECT_REFERENCE:
                return pool.getExistingElementById(id);
            case FULL_OBJECT:
                pool.addExistingElementById(id, e);
                e.read(bytes, this);
                return e;
        }
        return null;
    }

    public static void writeInt(ByteArrayOutputStream bytes, int i) {
        bytes.writeBytes(ByteBuffer.allocate(4).putInt(i).array());
    }

    public static int readInt(ByteBuffer bytes){
        return bytes.getInt();
    }

    public static void writeBoolean(ByteArrayOutputStream bytes, boolean b) {
        bytes.write(b ? 0x1 : 0x0);
    }
    public static boolean readBoolean(ByteBuffer bytes) { return bytes.get() != 0x0; }
}
