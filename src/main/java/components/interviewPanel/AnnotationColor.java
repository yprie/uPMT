package components.interviewPanel;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AnnotationColor {
    private static final Map<String, String> colors = new HashMap<>() {
        {
            put("YELLOW", "#FFDC97");
            put("RED", "#FF9797");
            put("BLUE", "#7084B0");
            put("GREEN", "#7BCF7B");
        }
    };

    public static Color getColor(String name) {
        return Color.web(colors.get(name));
    }

    public static String getHexa(String name) {
        return colors.get(name);
    }

    public static List<String> getNames() {
        return new ArrayList<>(colors.keySet());
    }

    public static String YELLOW() {
        return "#FFDC97";
    }
    public static Color ColorYELLOW() {
        return Color.web("#FFDC97");
    }

    public static String RED() {
        return "#FF9797";
    }
    public static Color ColorRED() {
        return Color.web("#FF9797");
    }

    public static String BLUE() {
        return "#7084B0";
    }
    public static Color ColorBLUE() {
        return Color.web("#7084B0");
    }

    public static String GREEN() {
        return "#7BCF7B";
    }
    public static Color ColorGREEN() {
        return Color.web("#7BCF7B");
    }

}
