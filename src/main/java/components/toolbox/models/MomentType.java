package components.toolbox.models;

import components.toolbox.controllers.MomentTypeController;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import models.ConcreteCategory;
import models.Justification;
import models.Moment;

import java.util.LinkedList;

public class MomentType extends Moment {
    private MomentTypeController momentTypeController;

    public MomentType(Moment moment, MomentTypeController momentTypeController) {
        super(moment.getName());
        super.setController(moment.getController());
        super.setJustification(new Justification());
        super.setComment(moment.getComment());
        ListProperty<ConcreteCategory> newCategoriesProperties =  new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));
        for (int i = 0; i < moment.getCategories().size(); i++) {
            ConcreteCategory concreteCategory = new ConcreteCategory(moment.getCategories().get(i).getSchemaCategory());
            newCategoriesProperties.add(concreteCategory);
            newCategoriesProperties.get(i).noJustificationNoPropertiesValues();
        }
        super.setCategories(newCategoriesProperties);
        super.setCommentVisible(moment.isCommentVisible());
        super.setCollapsed(moment.isCollapsed());
        super.setTransitional(moment.getTransitional());
        super.setColor(moment.getColor());
        this.momentTypeController = momentTypeController;
    }

    public MomentTypeController getMomentTypeController() {
        return momentTypeController;
    }
}
