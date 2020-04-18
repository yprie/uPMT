package persistency.Export;

public class CSVObject {
    private String interviewName;
    private String momentId;
    private String momentName;
    private String momentJustification;
    private String category;
    private String categoryJustification;
    private String property;
    private String value;
    private String propertyJustification;

    public String[] toStringArray(){
        return new String[]{interviewName,momentId,momentName,momentJustification,category,categoryJustification,
                property,value,propertyJustification};
    }

    public CSVObject(String interviewName, String momentId, String momentName, String momentJustification) {
        this.interviewName = interviewName;
        this.momentId = momentId;
        this.momentName = momentName;
        this.momentJustification = momentJustification;
    }


    public CSVObject(String interviewName, String momentId, String momentName, String momentJustification, String category, String categoryJustification) {
        this.interviewName = interviewName;
        this.momentId = momentId;
        this.momentName = momentName;
        this.momentJustification = momentJustification;
        this.category = category;
        this.categoryJustification = categoryJustification;
    }

    public CSVObject(String interviewName, String momentId, String momentName, String momentJustification, String category, String categoryJustification, String property, String value, String propertyJustification) {
        this.interviewName = interviewName;
        this.momentId = momentId;
        this.momentName = momentName;
        this.momentJustification = momentJustification;
        this.category = category;
        this.categoryJustification = categoryJustification;
        this.property = property;
        this.value = value;
        this.propertyJustification = propertyJustification;
    }

    public String getInterviewName() {
        return interviewName;
    }

    public String getMomentId() {
        return momentId;
    }

    public String getMomentName() {
        return momentName;
    }

    public String getMomentJustification() {
        return momentJustification;
    }

    public String getCategory() {
        return category;
    }

    public String getCategoryJustification() {
        return categoryJustification;
    }

    public String getProperty() {
        return property;
    }

    public String getValue() {
        return value;
    }

    public String getPropertyJustification() {
        return propertyJustification;
    }
}