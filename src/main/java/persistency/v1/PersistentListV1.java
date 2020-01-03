package persistency.v1;

import persistency.PersistentElement;
import persistency.Serializer;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.function.Supplier;

public class PersistentListV1<E extends PersistentElement> extends LinkedList<E> implements PersistentElement{

    private Supplier<E> newElementCreator;

    public PersistentListV1(Supplier<E> newElementCreator) {
        this.newElementCreator = newElementCreator;
    }

    @Override
    public void write(ByteArrayOutputStream bytes, Serializer sr) {
        Serializer.writeInt(bytes, this.size());
        for(E element: this)
            sr.writeObject(bytes, element);
    }

    @Override
    public void read(ByteBuffer bytes, Serializer sr) {
        this.clear();
        int size = Serializer.readInt(bytes);
        for(int i = 0; i < size; i++) {
            add(sr.readObject(bytes, newElementCreator.get()));
        }
    }

}
