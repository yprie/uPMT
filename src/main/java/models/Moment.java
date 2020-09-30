package models;

import application.history.HistoryManager;
import components.modelisationSpace.category.modelCommands.RemoveConcreteCategory;
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

    public Moment(String name) {
        super();
        this.name = new SimpleStringProperty(name);
        this.comment = new SimpleStringProperty();
        this.justification = new Justification();
        this.categories = new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));
        this.commentVisible = new SimpleBooleanProperty(false);
        this.collapsed = new SimpleBooleanProperty();
    }

    public Moment(String name, String comment, boolean commentVisible, Justification j) {
        super();
        this.name = new SimpleStringProperty(name);
        this.comment = new SimpleStringProperty(comment);
        this.justification = j;
        this.categories = new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));
        this.commentVisible = new SimpleBooleanProperty(commentVisible);
        this.collapsed = new SimpleBooleanProperty();
    }

    public Moment(String name, Descripteme d) {
        super();
        this.name = new SimpleStringProperty(name);
        this.comment = new SimpleStringProperty();
        this.justification = new Justification();
        this.commentVisible = new SimpleBooleanProperty(false);
        this.categories = new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));
        this.collapsed = new SimpleBooleanProperty();
    }

    public Moment(String name, String comment, boolean commentVisible, Justification j, boolean collapsed) {
        super();
        this.name = new SimpleStringProperty(name);
        this.comment = new SimpleStringProperty(comment);
        this.justification = j;
        this.categories = new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));
        this.commentVisible = new SimpleBooleanProperty(commentVisible);
        this.collapsed = new SimpleBooleanProperty(collapsed);
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

    public void addCategory(ConcreteCategory cc) {
        categories.add(cc);
        bindListener(cc);
    }

    public void addCategory(int index, ConcreteCategory cc) {
        if(index == categories.size()) {
            addCategory(cc);
        }
        else {
            categories.add(index, cc);
            bindListener(cc);
        }
     }
    public void removeCategory(ConcreteCategory cc) {
        categories.remove(cc);
    }
    public ObservableList<ConcreteCategory> concreteCategoriesProperty() { return categories; }
    public int indexOfConcreteCategory(ConcreteCategory cc) {
        int index = -1;
        for(int i = 0; i < categories.size(); i++)
            if(categories.get(i) == cc)
                return i;
        return index;
    }
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
                    HistoryManager.addCommand(new RemoveConcreteCategory(m, category), false);
                    category.existsProperty().removeListener(this);
                }
            }
        });
    }

    public boolean hadThisCategory(ConcreteCategory category) {
        boolean had = false;
        for (ConcreteCategory concreteCategory : categories) {
            if (category.getSchemaCategory() == concreteCategory.getSchemaCategory()) {
                had = true;
                break;
            }
        }
        return had;
    }
    @Override
    public DataFormat getDataFormat() {
        return format;
    }

    @Override
    public boolean isDraggable() {
        return true;
    }

}
