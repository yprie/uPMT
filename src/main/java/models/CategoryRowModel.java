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

    }


}
