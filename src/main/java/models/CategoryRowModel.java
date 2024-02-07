package models;

import java.util.List;

public class CategoryRowModel {

    private String name;
    private SchemaCategory category;
    private boolean selected;
    private List<Interview> interviews;
    private List<Moment> moments;
    private List<ConcreteProperty> properties;
    private List<String> propertiesValues;

    private int nbOfUse;


    public CategoryRowModel(SchemaCategory category){
        this.category = category;

    }

    public SchemaCategory getCategory() {
        return this.category;
    }

    public int getNbOfUses() {
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
