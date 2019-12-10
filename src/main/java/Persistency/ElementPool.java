package Persistency;

import java.util.HashMap;
import java.util.function.Function;

class ElementPool {

    private HashMap<Integer, Object> referencer = new HashMap<Integer, Object>();

    void clearExistingElements() { referencer.clear(); }

    int getExistingElementId(Object o) {
        return System.identityHashCode(o);
    }

    void addExistingElement(Object key, Object value) {
        referencer.put(getExistingElementId(key), value);
    }

    void addExistingElementById(int id, Object v) {
        referencer.put(id, v);
    }

    <T> T getExistingElement(Object key) {
        int uniqueId = getExistingElementId(key);
        return referencer.containsKey(uniqueId) ? (T)referencer.get(uniqueId) : null;
    }

    <T> T getExistingElementById(int id) {
        if(!referencer.containsKey(id))
            throw new IllegalArgumentException("Unable to find the existing object !");
        return (T)referencer.get(id);
    }

    public <C, B> C getOrCreateElement(B baseElement, Function<B, C> convertBaseElement) {
        C e = getExistingElement(baseElement);
        if(e == null){
            e = convertBaseElement.apply(baseElement);
            addExistingElement(baseElement, e);
        }
        return e;
    }
}
