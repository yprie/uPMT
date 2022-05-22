package models;

import application.history.HistoryManager;
import components.modelisationSpace.category.modelCommands.RemoveConcreteCategory;
import components.modelisationSpace.moment.controllers.MomentController;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.DataFormat;
import utils.dragAndDrop.IDraggable;

import java.util.LinkedList;

public class Moment extends RootMoment implements IDraggable {

    public static final DataFormat format = new DataFormat("Moment");
    //public static final Integer maxMomentNameLength = 40;

    private SimpleStringProperty name;
    private Justification justification;

    private ListProperty<ConcreteCategory> categories;

    private SimpleStringProperty comment;
    private SimpleBooleanProperty commentVisible;

    private SimpleBooleanProperty collapsed;

    private SimpleBooleanProperty transitional; //true = transitional

    private SimpleStringProperty color = new SimpleStringProperty("ffffff");

    private RootMoment parent;
    private MomentController controller;

    public Moment(String name) {
        super();
        this.name = new SimpleStringProperty(name);
        this.comment = new SimpleStringProperty();
        this.justification = new Justification();
        this.categories = new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));
        this.commentVisible = new SimpleBooleanProperty(false);
        this.collapsed = new SimpleBooleanProperty();
        this.transitional = new SimpleBooleanProperty(false);
    }

    public Moment(String name, Moment parent) {
        super();
        this.name = new SimpleStringProperty(name);
        this.comment = new SimpleStringProperty();
        this.justification = new Justification();
        this.categories = new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));
        this.commentVisible = new SimpleBooleanProperty(false);
        this.collapsed = new SimpleBooleanProperty();
        this.transitional = new SimpleBooleanProperty(false);
        this.parent = parent;
    }

    public Moment(String name, RootMoment parent) {
        super();
        this.name = new SimpleStringProperty(name);
        this.comment = new SimpleStringProperty();
        this.justification = new Justification();
        this.categories = new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));
        this.commentVisible = new SimpleBooleanProperty(false);
        this.collapsed = new SimpleBooleanProperty();
        this.transitional = new SimpleBooleanProperty(false);
        this.parent = parent;
    }

    public Moment(String name, String comment, boolean commentVisible, Justification j) {
        super();
        this.name = new SimpleStringProperty(name);
        this.comment = new SimpleStringProperty(comment);
        this.justification = j;
        this.categories = new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));
        this.commentVisible = new SimpleBooleanProperty(commentVisible);
        this.collapsed = new SimpleBooleanProperty();
        this.transitional = new SimpleBooleanProperty(false);
    }

    public Moment(String name, Descripteme d) {
        super();
        this.name = new SimpleStringProperty(name);
        this.comment = new SimpleStringProperty();
        this.justification = new Justification();
        this.commentVisible = new SimpleBooleanProperty(false);
        this.categories = new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));
        this.collapsed = new SimpleBooleanProperty();
        this.transitional = new SimpleBooleanProperty(false);
    }

    public Moment(String name, String comment, boolean commentVisible, Justification j, boolean collapsed, boolean transitional) {
        super();
        this.name = new SimpleStringProperty(name);
        this.comment = new SimpleStringProperty(comment);
        this.justification = j;
        this.categories = new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));
        this.commentVisible = new SimpleBooleanProperty(commentVisible);
        this.collapsed = new SimpleBooleanProperty(collapsed);
        this.transitional = new SimpleBooleanProperty(transitional);
    }

    public Moment(String name, String comment, boolean commentVisible, Justification j, boolean collapsed, boolean transitional, String color) {
        super();
        this.name = new SimpleStringProperty(name);
        this.comment = new SimpleStringProperty(comment);
        this.justification = j;
        this.categories = new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));
        this.commentVisible = new SimpleBooleanProperty(commentVisible);
        this.collapsed = new SimpleBooleanProperty(collapsed);
        this.transitional = new SimpleBooleanProperty(transitional);
        this.color = new SimpleStringProperty(color);
    }

    public Moment(String name, String comment, boolean commentVisible, Justification j, boolean collapsed, boolean transitional, String color, ListProperty<ConcreteCategory> categories) {
        super();
        this.name = new SimpleStringProperty(name);
        this.comment = new SimpleStringProperty(comment);
        this.justification = j;
        this.categories = categories;
        this.commentVisible = new SimpleBooleanProperty(commentVisible);
        this.collapsed = new SimpleBooleanProperty(collapsed);
        this.transitional = new SimpleBooleanProperty(transitional);
        this.color = new SimpleStringProperty(color);
    }

    public Moment(String name, ObservableList<SchemaCategory> categories, boolean transitional, String color) {
        super();
        this.name = new SimpleStringProperty(name);
        this.comment = new SimpleStringProperty();
        this.justification = new Justification();

        ListProperty<ConcreteCategory> newCategoriesProperties =  new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));
        for (SchemaCategory category : categories) {
            newCategoriesProperties.add(new ConcreteCategory(category));
        }
        this.categories = newCategoriesProperties;

        this.commentVisible = new SimpleBooleanProperty(false);
        this.collapsed = new SimpleBooleanProperty();
        this.transitional = new SimpleBooleanProperty(transitional);
        this.color = new SimpleStringProperty(color);
    }

    public void setName(String name) {
        this.name.set(name);
    }
    public String getName() { return this.name.get(); }
    public ObservableValue<String> nameProperty() { return name; }

    public String getComment() { return comment.get(); }

    public SimpleStringProperty commentProperty() { return comment; }

    public void setComment(String comment) { this.comment.set(comment); }

    public Justification getJustification() { return justification; }

    public boolean isCommentVisible() { return commentVisible.get(); }

    public void setCommentVisible(boolean commentVisible) { this.commentVisible.set(commentVisible); }

    public SimpleBooleanProperty collapsedProperty() { return collapsed; }

    public boolean isCollapsed() { return collapsed.get(); }

    public void setCollapsed(boolean collapsed) { this.collapsed.set(collapsed); }

    public boolean getTransitional() { return this.transitional.get(); }
    public void setTransitional(boolean bool) {
        if (!this.momentsProperty().isEmpty() && bool) {
            throw new Error("remove the submoments before");
        }
        else {
            this.transitional.set(bool);
        }
    }

    public RootMoment getParent() { return parent;}

    public void setParent(RootMoment parent) {
        this.parent = parent;
    }

    public void addParent(RootMoment parent) {this.parent = parent;}

    public void addCategory(ConcreteCategory cc) {
        boolean added = false;
        for (int i = 0; i < categories.size(); i++) {
            if (0 < categories.get(i).getName().compareTo(cc.getName())) {
                categories.add(i, cc);
                added = true;
                break;
            }
        }
        if (!added) categories.add(cc);

        bindListener(cc);
    }

    public void removeCategory(ConcreteCategory cc) {
        categories.remove(cc);
    }
    public ObservableList<ConcreteCategory> concreteCategoriesProperty() { return categories; }

    public int indexOfSchemaCategory(SchemaCategory sc) {
        int index = -1;
        for(int i = 0; i < categories.size(); i++)
            if(categories.get(i).isSchemaCategory(sc))
                return i;
        return index;
    }

    private void bindListener(ConcreteCategory category) {
        Moment m = this;
        category.existsProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if(!t1){
                    HistoryManager.addCommand(new RemoveConcreteCategory(m, category, category.getController()), false);
                    category.existsProperty().removeListener(this);
                }
            }
        });
    }

    public boolean hadThisCategory(ConcreteCategory category) {
        for (ConcreteCategory concreteCategory : categories) {
            if (category.getSchemaCategory() == concreteCategory.getSchemaCategory()) {
                return true;
            }
        }
        return false;
    }

    public ConcreteCategory getCategory(ConcreteCategory category) {
        for (ConcreteCategory concreteCategory : categories) {
            if (category.getSchemaCategory() == concreteCategory.getSchemaCategory()) {
                return concreteCategory;
            }
        }
        return null;
    }

    public int getDepth() {
        int parent_depth = 0;
        if (parent != null) {
            if (parent instanceof Moment) {
                parent_depth = ((Moment) parent).getDepth();
            }
        }
        return parent_depth+1;
    }

    public MomentController getController() {
        return controller;
    }

    public void setController(MomentController controller) {
        this.controller = controller;
    }

    public String getColor() {
        return color.get();
    }

    public void setColor(String color) {
        this.color.set(color);
        controller.updateColor();
    }

    @Override
    public DataFormat getDataFormat() {
        return format;
    }

    @Override
    public boolean isDraggable() {
        return true;
    }

    public ObservableList<ConcreteCategory> getCategories() {
        return categories.get();
    }

}
