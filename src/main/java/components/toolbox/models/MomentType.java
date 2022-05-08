package components.toolbox.models;

import components.toolbox.controllers.MomentTypeController;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.DataFormat;
import models.ConcreteCategory;
import models.Justification;
import models.Moment;
import utils.dragAndDrop.IDraggable;

import java.util.LinkedList;

public class MomentType implements IDraggable {
    private MomentTypeController momentTypeController;
    public static final DataFormat format = new DataFormat("MomentType");
    private String name;
    private Justification justification;
    private ListProperty<ConcreteCategory> categories;
    private String comment;
    private boolean commentVisible;
    private boolean collapsed;
    private boolean transitional;
    private String color = "ffffff";

    public MomentType(Moment moment, MomentTypeController momentTypeController) {
        this.name = moment.getName();
        this.momentTypeController = momentTypeController;
        this.justification = moment.getJustification();
        ListProperty<ConcreteCategory> newCategoriesProperties =  new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));
        for (int i = 0; i < moment.getCategories().size(); i++) {
            ConcreteCategory concreteCategory = new ConcreteCategory(moment.getCategories().get(i).getSchemaCategory());
            newCategoriesProperties.add(concreteCategory);
            newCategoriesProperties.get(i).noJustificationNoPropertiesValues();
        }
        this.categories = newCategoriesProperties;
        this.comment = moment.getComment();
        this.commentVisible = moment.isCommentVisible();
        this.collapsed = moment.isCollapsed();
        this.transitional = moment.getTransitional();
        this.color = moment.getColor();
    }

    public MomentType(MomentType moment, MomentTypeController momentTypeController) {
        this.name = moment.getName();
        this.momentTypeController = momentTypeController;
        this.justification = moment.getJustification();
        ListProperty<ConcreteCategory> newCategoriesProperties =  new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));
        for (int i = 0; i < moment.getCategories().size(); i++) {
            ConcreteCategory concreteCategory = new ConcreteCategory(moment.getCategories().get(i).getSchemaCategory());
            newCategoriesProperties.add(concreteCategory);
            newCategoriesProperties.get(i).noJustificationNoPropertiesValues();
        }
        this.categories = newCategoriesProperties;
        this.comment = moment.getComment();
        this.commentVisible = moment.isCommentVisible();
        this.collapsed = moment.isCollapsed();
        this.transitional = moment.isTransitional();
        this.color = moment.getColor();
    }

    public MomentType(String name, String comment, boolean commentVisible, Justification justification, boolean collapsed, boolean transitional, String color, ListProperty<ConcreteCategory> categories) {
        this.name = name;
        this.comment = comment;
        this.commentVisible = commentVisible;
        this.justification = justification;
        this.collapsed = collapsed;
        this.transitional = transitional;
        this.color = color;
        this.categories = categories;
    }

    public Moment createConcreteMoment() {
        /* TODO  a changer */
        ListProperty<ConcreteCategory> newCategoriesProperties =  new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));
        for (int i = 0; i < this.getCategories().size(); i++) {
            ConcreteCategory concreteCategory = new ConcreteCategory(this.getCategories().get(i).getSchemaCategory());
            newCategoriesProperties.add(concreteCategory);
            newCategoriesProperties.get(i).noJustificationNoPropertiesValues();
        }
        this.categories = newCategoriesProperties;
        return new Moment(this.name, this.comment, this.commentVisible, this.justification, this.collapsed, this.transitional, this.color, this.categories);
    }

    public ObservableList<ConcreteCategory> concreteCategoriesProperty() { return categories; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MomentTypeController getMomentTypeController() {
        return momentTypeController;
    }

    public void setMomentTypeController(MomentTypeController momentTypeController) {
        this.momentTypeController = momentTypeController;
    }

    public Justification getJustification() {
        return justification;
    }

    public ObservableList<ConcreteCategory> getCategories() {
        return categories.get();
    }

    public ListProperty<ConcreteCategory> categoriesProperty() {
        return categories;
    }

    public String getComment() {
        return comment;
    }

    public boolean isCommentVisible() {
        return commentVisible;
    }

    public boolean isCollapsed() {
        return collapsed;
    }

    public boolean isTransitional() {
        return transitional;
    }

    public String getColor() {
        return color;
    }

    @Override
    public DataFormat getDataFormat() {
        return MomentType.format;
    }

    @Override
    public boolean isDraggable() {
        return true;
    }
}
