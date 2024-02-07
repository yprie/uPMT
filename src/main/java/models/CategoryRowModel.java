package models;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CategoryRowModel {

    private String name;
    private SchemaCategory category;
    private boolean selected;
    private List<Interview> interviews;
    private List<Moment> moments;
    private ObservableList<SchemaProperty> properties;
    private List<String> propertiesValues;

    private ReadOnlyIntegerProperty nbOfUse;


    public CategoryRowModel(SchemaCategory category){
        this.category = category;
        this.interviews = category.getInterviews();
        this.nbOfUse = category.numberOfUsesInModelisationProperty();
        this.interviews = FXCollections.observableArrayList(category.getInterviews());
        this.properties = FXCollections.observableArrayList(category.propertiesProperty());

        this.moments = FXCollections.observableArrayList();
        Set<Moment> uniqueMoments = new HashSet<>();

        for (Interview interview : interviews) {
            RootMoment rootMoment = interview.getRootMoment();
            uniqueMoments.addAll(rootMoment.momentsProperty());
        }

        this.moments.addAll(uniqueMoments);
    }

    public SchemaCategory getCategory() {
        return this.category;
    }

    public ReadOnlyIntegerProperty getNbOfUses() {
        return this.nbOfUse;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Boolean getSelected() {
        return this.selected;
    }

    public List<String> getPropertiesValues() {
        return this.propertiesValues;
    }


}
