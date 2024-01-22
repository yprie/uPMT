package models;

import application.history.HistoryManager;
import components.modelisationSpace.category.controllers.ConcreteCategoryController;
import components.schemaTree.Cell.Controllers.SchemaTreeMomentTypeController;
import components.schemaTree.Cell.SchemaTreePluggable;
import components.schemaTree.Cell.Utils;
import components.schemaTree.Cell.Visitors.SchemaTreePluggableVisitor;
import components.toolbox.controllers.MomentTypeController;
import components.toolbox.history.commands.RemoveCategoryFromMomentType;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.DataFormat;
import utils.removable.IRemovable;

import java.util.LinkedList;

public class SchemaMomentType extends SchemaElement implements IRemovable {
    public static final DataFormat format = new DataFormat("SchemaMomentType");
    private SimpleBooleanProperty exists;
    private SimpleIntegerProperty nbUsesInModelisation;

    private MomentTypeController momentTypeController;

    private ListProperty<SchemaCategory> categories;
    private SimpleBooleanProperty transitional;
    private SimpleStringProperty color;
    private SchemaTreeMomentTypeController schemaTreeMomentTypeController;
    public SchemaMomentType(Moment moment, MomentTypeController momentTypeController) {
        super(moment.getName());
        super.expanded = new SimpleBooleanProperty(false);
        this.exists = new SimpleBooleanProperty(true);
        this.nbUsesInModelisation = new SimpleIntegerProperty(0);
        this.momentTypeController = momentTypeController;

        this.categories = new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));
        for (int i = 0; i < moment.getCategories().size(); i++) {
            addCategory(moment.getCategories().get(i).getSchemaCategory(), -1);
        }

        this.transitional = new SimpleBooleanProperty(moment.getTransitional());
        this.color = new SimpleStringProperty(moment.getColor());
    }

    public SchemaMomentType(String name, String color, boolean transitional) {
        super(name);
        super.expanded = new SimpleBooleanProperty(false);
        this.exists = new SimpleBooleanProperty(true);
        this.nbUsesInModelisation = new SimpleIntegerProperty(0);
        this.momentTypeController = new MomentTypeController(this);

        this.categories = new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));
        this.transitional = new SimpleBooleanProperty(transitional);
        this.color = new SimpleStringProperty(color);
    }

    @Override
    public boolean canContain(SchemaTreePluggable item) {
        return Utils.IsSchemaTreeCategory(item);
    }

    @Override
    public boolean hasChild(SchemaTreePluggable item) {
        return this.categories.contains(item);
    }

    @Override
    public void addChild(SchemaTreePluggable item) {
        if (Utils.IsSchemaTreeCategory(item)) {
            addCategory((SchemaCategory)item, -1);
        }
        else {
            throw new IllegalArgumentException("(SchemaMomentType::addChild) Can't receive this kind of child !");
        }
    }

    @Override
    public void addChildAt(SchemaTreePluggable item, int index) {
        if (Utils.IsSchemaTreeCategory(item)) {
            addCategory((SchemaCategory)item, index);
        }
        else {
            throw new IllegalArgumentException("(SchemaMomentType::addChildAt) Can't receive this kind of child !");
        }
    }

    @Override
    public void removeChild(SchemaTreePluggable item) {
        if(Utils.IsSchemaTreeCategory(item))
            removeCategory((SchemaCategory)item);
        else
            throw new IllegalArgumentException("(SchemaProperty::removeChild) Can't receive this kind of child !");
    }

    @Override
    public int getChildIndex(SchemaTreePluggable item) {
        int r = this.categories.indexOf(item);
        if (r == -1)
            throw new IllegalArgumentException("(SchemaProperty::addChild) Can't receive this kind of child !");
        return r;
    }

    @Override
    public void accept(SchemaTreePluggableVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean canChangeParent() {
        return true;
    }

    @Override
    public boolean mustBeRenamed() {
        return false;
    }

    @Override
    public DataFormat getDataFormat() {
        return SchemaMomentType.format;
    }

    @Override
    public boolean isDraggable() {
        return true;
    }

    @Override
    public String getIconPath() {
        return "toolBox_icon.png";
    }

    @Override
    public void setExists(boolean b) {
        exists.set(b);
    }

    @Override
    public ObservableBooleanValue existsProperty() {
        return exists;
    }

    public void setNumberOfUsesInModelisation(int nbUses) { this.nbUsesInModelisation.set(nbUses); }

    public ReadOnlyIntegerProperty numberOfUsesInModelisationProperty() { return this.nbUsesInModelisation; }

    public MomentTypeController getMomentTypeController() {
        return momentTypeController;
    }

    public void setMomentTypeController(MomentTypeController momentTypeController) {
        this.momentTypeController = momentTypeController;
    }

    public void addCategory(SchemaCategory sc, int index){
        if(index == -1) {
            categories.add(sc);
        }
        else {
            categories.add(index, sc);
        }
        bindListener(sc);
    }
    public void removeCategory(SchemaCategory p){
        categories.remove(p);
    }


    private void bindListener(SchemaCategory sc) {
        SchemaMomentType smt = this;
        sc.existsProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if(!t1){
                    HistoryManager.addCommand(new RemoveCategoryFromMomentType(smt, sc), false);
                    sc.existsProperty().removeListener(this);
                }
            }
        });
    }

    public ListProperty<SchemaCategory> categoriesProperty() { return categories; }

    public ObservableList<SchemaCategory> getCategories() {
        return categories.get();
    }

    public Moment createMoment() {
        return new Moment(super.getName(), this.categories.get(), this.transitional.get(), this.color.get());
    }

    public boolean getTransitional() {
        return transitional.get();
    }

    public String getColor() {
        return color.get();
    }
    public void setColor(String color) {
        this.color.set(color);
        momentTypeController.updateColor();
    }
}
