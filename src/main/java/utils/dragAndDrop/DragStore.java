package utils.dragAndDrop;

public class DragStore {

    private static IDraggable draggable;

    public static void setDraggable(IDraggable draggable) { DragStore.draggable = draggable; }
    public static <T extends IDraggable> T getDraggable() { return (T)draggable; }
    public static void clearStore() { draggable = null; }

}
