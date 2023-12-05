package components.comparison;

import persistency.newSaveSystem.SConcreteCategory;
import persistency.newSaveSystem.SMoment;

import java.util.ArrayList;

public class block {
    private final String title;
    private line<SMoment> firstLine;
    private ArrayList<line<ArrayList<SConcreteCategory>>> dimensionLines;

    public block(String title, line<SMoment> firstLine, ArrayList<line<ArrayList<SConcreteCategory>>> dimensionLines) {
        this.title = title;
        this.firstLine = firstLine;
        this.dimensionLines = dimensionLines;
    }

    public block() {
        this.title = "unNamed";
        this.firstLine = new line<>();
        this.dimensionLines = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public line<SMoment> getFirstLine() {
        return firstLine;
    }

    public ArrayList<line<ArrayList<SConcreteCategory>>> getDimensionLines() {
        return dimensionLines;
    }
}
