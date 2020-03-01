package persistency;

import application.configuration.Configuration;
import application.project.models.Project;
import components.interviewSelector.models.Interview;
import components.modelisationSpace.category.model.ConcreteCategory;
import components.modelisationSpace.moment.model.Moment;
import components.modelisationSpace.moment.model.RootMoment;
import components.modelisationSpace.property.model.ConcreteProperty;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ProjectExporter {
    public static void exportToCSV(Project project, File saveFile) {
        List<Interview> interviews = project.interviewsProperty();
        List<CSVObject> csvObjects = new LinkedList<>();
        for (Interview interview :
                interviews) {
            for (int i = 0; i < interview.getRootMoment().momentsProperty().size(); i++) {
                RootMoment m = interview.getRootMoment().momentsProperty().get(i);
                addToCSVObjectList(interview.getTitle(), m, String.valueOf(i + 1), csvObjects);
            }
        }

        try {
            FileWriter csvWriter = new FileWriter(saveFile,false);
            String[] headers = new String[]{"INTERVIEW", "ID", "NAME", "DESCRIPTEME OF MOMENT",
                    "CATEGORY", "DESCRIPTEME OF CATEGORY", "PROPERTY", "VALUE", "DESCRIPTEME OF PROPERTY"};
            csvWriter.append(String.join(";",headers));
            csvWriter.append("\n");

            for (CSVObject csvObject : csvObjects) {
                csvWriter.append(csvObject.getInterviewName());
                csvWriter.append(';');
                csvWriter.append(csvObject.getMomentId());
                csvWriter.append(';');
                csvWriter.append(csvObject.getMomentName());
                csvWriter.append(';');
                csvWriter.append(csvObject.getMomentJustification());
                csvWriter.append(';');
                csvWriter.append(csvObject.getCategory());
                csvWriter.append(';');
                csvWriter.append(csvObject.getCategoryJustification());
                csvWriter.append(';');
                csvWriter.append(csvObject.getProperty());
                csvWriter.append(';');
                csvWriter.append(csvObject.getValue());
                csvWriter.append(';');
                csvWriter.append(csvObject.getPropertyJustification());
                csvWriter.append("\n");
            }

            csvWriter.flush();
            csvWriter.close();
            showExportResult(true);
        } catch (IOException e) {
            e.printStackTrace();
            showExportResult(false);
        }
    }

    private static void showExportResult(boolean succeed){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(Configuration.langBundle.getString("export_to_csv"));
        alert.setHeaderText(null);
        if (succeed) {
            alert.setContentText(Configuration.langBundle.getString("export_success"));
        }
        else{
            alert.setContentText(Configuration.langBundle.getString("export_failure"));

        }
        alert.showAndWait();
    }

    private static void addToCSVObjectList(String interviewName, RootMoment moment, String id, List<CSVObject> list) {
        List<CSVObject> csvObject = generateCsvObjList(interviewName, (Moment) moment, id);
        list.addAll(csvObject);
        if (moment.momentsProperty().size() > 0) {
            for (int j = 0; j < moment.momentsProperty().size(); j++) {
                Moment m = moment.momentsProperty().get(j);
                addToCSVObjectList(interviewName, m, id + "." + (j + 1), list);
            }
        }
    }

    //one moment could contain create many CSVObjects
    private static List<CSVObject> generateCsvObjList(String interviewName, Moment moment, String id) {
        List<CSVObject> oneMomentList = new LinkedList<>();
        if (moment.concreteCategoriesProperty().size() == 0) {
            CSVObject csvObject = new CSVObject(interviewName, id, moment.getName(), moment.getJustification().toString());
            oneMomentList.add(csvObject);
        }else {
            for (ConcreteCategory category : moment.concreteCategoriesProperty()
            ) {
                if (category.propertiesProperty().size() == 0) {
                    CSVObject csvObject = new CSVObject(interviewName, id, moment.getName(), moment.getJustification().toString(), category.getName(), category.getJustification().toString());
                    oneMomentList.add(csvObject);
                } else {
                    for (ConcreteProperty property : category.propertiesProperty()
                    ) {
                        CSVObject csvObject = new CSVObject(interviewName, id, moment.getName(), moment.getJustification().toString(), category.getName(), category.getJustification().toString(), property.getName(), property.getValue(), property.getJustification().toString());
                        oneMomentList.add(csvObject);
                    }
                }
            }
        }
        return oneMomentList;
    }


}

class CSVObject {
    private String interviewName;
    private String momentId;
    private String momentName;
    private String momentJustification;
    private String category;
    private String categoryJustification;
    private String property;
    private String value;
    private String propertyJustification;

    public CSVObject(String interviewName, String momentId, String momentName, String momentJustification) {
        this.interviewName = interviewName;
        this.momentId = momentId;
        this.momentName = momentName;
        this.momentJustification = momentJustification;
        this.category = "";
        this.categoryJustification = "";
        this.property = "";
        this.value = "";
        this.propertyJustification = "";
    }


    public CSVObject(String interviewName, String momentId, String momentName, String momentJustification, String category, String categoryJustification) {
        this.interviewName = interviewName;
        this.momentId = momentId;
        this.momentName = momentName;
        this.momentJustification = momentJustification;
        this.category = category;
        this.categoryJustification = categoryJustification;
        this.property = "";
        this.value = "";
        this.propertyJustification = "";
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