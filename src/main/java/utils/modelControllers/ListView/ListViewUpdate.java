package utils.modelControllers.ListView;

import utils.modelControllers.ModelControllerUpdateNotification;

public class ListViewUpdate extends ModelControllerUpdateNotification {

    public ListViewUpdate(int newIndex, int totalCount) {
        this.newIndex = newIndex;
        this.totalCount = totalCount;
    }

    public int newIndex;
    public int totalCount;

}
