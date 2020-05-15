package utils.dragAndDrop;

public class DragStore {

    private static IDraggable draggable;
    private static Object doubleObject;
    public static void setDraggable(IDraggable draggable) { DragStore.draggable = draggable; }
    public static <T extends IDraggable> T getDraggable() { return (T)draggable; }
    public static void setDoubleObject(Object object) { DragStore.doubleObject = object; }
    public static <T> T getDoubleObject() { return (T)doubleObject; }
    public static void clearStore() { draggable = null; doubleObject = null; }

}
