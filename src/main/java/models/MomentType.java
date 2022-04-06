package models;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.util.LinkedList;

public class MomentType extends Moment {
    private SchemaMomentType schemaMomentType;

    public MomentType(Moment moment) {
        super(moment.getName());
        super.setParent(moment.getParent());
        super.setController(moment.getController());
        super.setJustification(new Justification());
        super.setComment(moment.getComment());
        ListProperty<ConcreteCategory> newCategoriesProperties =  new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));
        for (int i = 0; i < moment.getCategories().size(); i++) {
            newCategoriesProperties.add(moment.getCategories().get(i));
            newCategoriesProperties.get(i).noJustificationNoPropertiesValues();
        }
        super.setCategories(newCategoriesProperties);
        super.setCommentVisible(moment.isCommentVisible());
        super.setCollapsed(moment.isCollapsed());
        super.setTransitional(moment.getTransitional());
        super.setColor(moment.getColor());

        this.schemaMomentType = new SchemaMomentType(moment.getName(), this);
    }

    public SchemaMomentType getSchemaMomentType() {
        return schemaMomentType;
    }
}
