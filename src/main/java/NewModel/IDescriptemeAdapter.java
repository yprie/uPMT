package NewModel;

import java.io.Serializable;

public interface IDescriptemeAdapter extends Serializable, Cloneable {

    Object clone();
    boolean equals(Object o);
    void setTexte(String t);
    String getTexte();
    String toString();

}