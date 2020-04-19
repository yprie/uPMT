package components.interviewPanel.utils;


import javafx.scene.paint.Color;

public class TextStyle {
    private int nbDescripteme = 0;
    private boolean isAnnotation = false;
    private Color color = null;

    public TextStyle(boolean isDescripteme, boolean isAnnotation, Color color) {
        this.nbDescripteme = isDescripteme ? 1 : 0;
        this.isAnnotation = isAnnotation;
        this.color = color;
    }

    TextStyle(boolean isDescripteme) {
        this.nbDescripteme = isDescripteme ? 1 : 0;
    }

    TextStyle() {
    }

    @Override
    public String toString() {
        String str = "WordStyle: nbDescripteme: " + nbDescripteme;
        str += " isAnnotation: " + isAnnotation;
        if (color != null)
            str += " color: " + getCSSColor();
        return str;
    }

    void removeAnnotation() {
        this.isAnnotation = false;
        this.color = null;
    }

    void removeDescripteme() {
        nbDescripteme = Math.max(0, nbDescripteme-1);
    }

    void becomeDescripteme() {
        nbDescripteme++;
    }

    void becomeAnnotation(Color color) {
        this.isAnnotation = true;
        this.color = color;
    }

    public boolean getIsAnnotation() {
        return isAnnotation;
    }
    public boolean getIsDescripteme() {
        return nbDescripteme == 1 ? true : false;
    }
    public boolean getIsSeveralDescriptemes() {
        return nbDescripteme > 1 ? true : false;
    }

    public String getCSSColor() {
        return color.toString().replace("0x", "#");
    }

    public void brighter() {
        if (isAnnotation) {
            color = color.brighter();
        }
    }

    public void darker() {
        if (isAnnotation) {
            color = color.darker();
        }
    }
}
