package SchemaTree.Cell.Models;

import SchemaTree.Cell.SchemaTreePluggable;

import java.io.Serializable;
import java.util.LinkedList;

public interface ICategoryAdapter extends Serializable, Cloneable, ITypeAdapter, SchemaTreePluggable {

    void addProperty(IPropertyAdapter s);
    void addProperty(int index, IPropertyAdapter s);
    void removeProperty(IPropertyAdapter s);
    LinkedList<IPropertyAdapter> getProperties();
    ICategoryAdapter clone();
    boolean equals(Object o);
}