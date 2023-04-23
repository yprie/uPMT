package components.comparison;

import java.util.ArrayList;

public class line<T> {
    String title;
    int length;
    private ArrayList<T> values;  //arraylist of concretecategory or moment

    public line() {
        this.title = "unNamed";
        this.length = 0;
        this.values = new ArrayList<>();
    }

    public line(String title, int length, ArrayList<T> values) {
        this.title = title;
        this.length = length;
        this.values = values;
    }

    public String getTitle() {
        return title;
    }

    public int getLength() {
        return length;
    }

    public ArrayList<T> getValues() {
        return values;
    }
}
