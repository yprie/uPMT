package utils;

public class SearchProperties {
    private boolean moment_name_choice;
    private boolean category_name_choice;
    private boolean property_name_choice;
    private boolean property_value_choice;
    private boolean all_choice;

    public SearchProperties() {
        moment_name_choice = false;
        category_name_choice = false;
        property_name_choice = false;
        property_value_choice = false;
        all_choice = false;
    }

    public void setChoiceMomentName() {
        moment_name_choice = true;
        category_name_choice = false;
        property_name_choice = false;
        property_value_choice = false;
        all_choice = false;
    }

    public void setChoiceCategoryName() {
        moment_name_choice = false;
        category_name_choice = true;
        property_name_choice = false;
        property_value_choice = false;
        all_choice = false;
    }

    public void setChoicePropertyName() {
        moment_name_choice = false;
        category_name_choice = false;
        property_name_choice = true;
        property_value_choice = false;
        all_choice = false;
    }

    public void setChoicePropertyValue() {
        moment_name_choice = false;
        category_name_choice = false;
        property_name_choice = false;
        property_value_choice = true;
        all_choice = false;
    }

    public void setChoiceAll() {
        moment_name_choice = true;
        category_name_choice = true;
        property_name_choice = true;
        property_value_choice = true;
        all_choice = true;
    }

    public boolean isMoment_name_choice() {
        return moment_name_choice;
    }

    public boolean isCategory_name_choice() {
        return category_name_choice;
    }

    public boolean isProperty_name_choice() {
        return property_name_choice;
    }

    public boolean isProperty_value_choice() {
        return property_value_choice;
    }

    public boolean isAll_choice() {
        return all_choice;
    }
}
