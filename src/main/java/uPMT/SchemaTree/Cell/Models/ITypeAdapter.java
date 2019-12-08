package SchemaTree.Cell.Models;

public interface ITypeAdapter {

    String toString();
    String getName();
    void setName(String name);
    String getDescription();
    void setDescription(String mDescription);
    String getColor();
    void setColor(String mColor);
    String typeToString();

    boolean isFolder();

    boolean isCategory();

    boolean isProperty();

    boolean isSchema();

    void removeChild(ITypeAdapter t);

    void addChild(ITypeAdapter t);

    void addChild(int index, ITypeAdapter t);
}