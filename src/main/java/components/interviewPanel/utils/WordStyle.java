package components.interviewPanel.utils;


import javafx.scene.paint.Color;

public class WordStyle {
    private boolean isDescripteme;
    private boolean isAnnotation;
    private Color color;

    public WordStyle(boolean isDescripteme, boolean isAnnotation, Color color) {
        this.isDescripteme = isDescripteme;
        this.isAnnotation = isAnnotation;
        this.color = color;
    }

    public WordStyle(boolean isDescripteme) {
        this.isDescripteme = isDescripteme;
        this.isAnnotation = false;
        this.color = null;
    }

    @Override
    public String toString() {
        String str = "WordStyle: isDescripteme: " + isDescripteme;
        str += " isAnnotation: " + isAnnotation;
        if (color != null)
            str += " color: " + getCSSColor();
        return str;
    }

    public void removeAnnotation() {
        this.isAnnotation = false;
        this.color = null;
    }

    public void removeDescripteme() {
        this.isDescripteme = false;
    }

    public void becomeDescripteme() {
        this.isDescripteme = true;
    }

    public void becomeAnnotation(Color color) {
        this.isAnnotation = true;
        this.color = color;
    }

    public boolean getIsAnnotation() {
        return isAnnotation;
    }

    public boolean getIsDescripteme() {
        return isDescripteme;
    }

    public String getCSSColor() {
        return color.toString().replace("0x", "#");
    }

    public WordStyle mergeStyles(WordStyle style) {
        // given style is prioritise
        WordStyle merged = new WordStyle(isDescripteme, isAnnotation, color);
        if (style.color != null) {
            merged.color = style.color;
        }
        merged.isAnnotation = this.isAnnotation || style.isAnnotation;
        merged.isDescripteme = this.isDescripteme || style.isDescripteme;
        return merged;
    }
}
