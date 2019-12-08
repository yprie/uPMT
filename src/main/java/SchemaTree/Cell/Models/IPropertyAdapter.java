package SchemaTree.Cell.Models;


import NewModel.IDescriptemeAdapter;
import SchemaTree.Cell.SchemaTreePluggable;

import java.io.Serializable;
import java.util.LinkedList;

public interface IPropertyAdapter extends Serializable, Cloneable, ITypeAdapter, SchemaTreePluggable {

    void setValue(String valeur);
    String getValue();
    LinkedList<IDescriptemeAdapter> getDescriptemes();
    void setDescriptemes(LinkedList<IDescriptemeAdapter> list);
    void removeDescripteme(IDescriptemeAdapter d);
    void addDescripteme(IDescriptemeAdapter d);
    String toString();
    boolean equals(Object o);
    IPropertyAdapter clone();
    String toStringDescripteme();

}
