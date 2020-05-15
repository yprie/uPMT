package models;

import javafx.scene.paint.Color;

public class AnnotationColor {
    private String name;
    private String hexa; // hexadecimal code of the color

    public AnnotationColor(String name, String hexa) {
        this.name = name;
        this.hexa = hexa;
    }


    public String getName() {
        return name;
    }

    public String getHexa() {
        return hexa;
    }

    public Color getColor() {
        return Color.web(hexa);
    }
}
