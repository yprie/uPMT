package utils.modelControllers.HBox;

import utils.modelControllers.ModelControllerUpdateNotification;

public class HBoxModelUpdate extends ModelControllerUpdateNotification {

    public HBoxModelUpdate(int newIndex, int totalCount) {
        this.newIndex = newIndex;
        this.totalCount = totalCount;
    }

    public int newIndex;
    public int totalCount;

}
