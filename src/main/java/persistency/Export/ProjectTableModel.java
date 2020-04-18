package persistency.Export;

import models.*;


import java.util.LinkedList;
import java.util.List;

public class ProjectTableModel implements CSVTableModel {
    Project project;
    List<Interview> interviews;
    List<String[]> values;
    static String[] headers = new String[]{"INTERVIEW", "ID", "NAME", "DESCRIPTEME OF MOMENT",
            "CATEGORY", "DESCRIPTEME OF CATEGORY", "PROPERTY", "VALUE", "DESCRIPTEME OF PROPERTY"};



    public ProjectTableModel(Project project){
        this.project = project;
        this.interviews = project.interviewsProperty();
        this.values = new LinkedList<>();

        setValues();
    }



    private void setValues(){
        for (Interview interview :
                interviews) {
            for (int i = 0; i < interview.getRootMoment().momentsProperty().size(); i++) {
                RootMoment m = interview.getRootMoment().momentsProperty().get(i);
                recurSetValues(interview.getTitle(), m, String.valueOf(i + 1));
            }
        }
    }

    private void recurSetValues(String interviewName, RootMoment moment, String id){
        List<String[]> valueList = getValuesOfOneMoment(interviewName,(Moment)moment, id);
        this.values.addAll(valueList);
        if (moment.momentsProperty().size() > 0) {
            for (int j = 0; j < moment.momentsProperty().size(); j++) {
                Moment m = moment.momentsProperty().get(j);
                recurSetValues(interviewName, m, id + "." + (j + 1));
            }
        }
    }

    private List<String[]>getValuesOfOneMoment(String interviewName, Moment moment, String id){
        List<String[]> valueList = new LinkedList<>();
        if (moment.concreteCategoriesProperty().size() == 0) {
            CSVObject csvObject = new CSVObject(interviewName, id, moment.getName(), moment.getJustification().toString());
            valueList.add(csvObject.toStringArray());
        }else {
            for (ConcreteCategory category : moment.concreteCategoriesProperty()
            ) {
                if (category.propertiesProperty().size() == 0) {
                    CSVObject csvObject = new CSVObject(interviewName, id, moment.getName(), moment.getJustification().toString(), category.getName(), category.getJustification().toString());
                    valueList.add(csvObject.toStringArray());
                } else {
                    for (ConcreteProperty property : category.propertiesProperty()
                    ) {
                        CSVObject csvObject = new CSVObject(interviewName, id, moment.getName(), moment.getJustification().toString(), category.getName(), category.getJustification().toString(), property.getName(), property.getValue(), property.getJustification().toString());
                        valueList.add(csvObject.toStringArray());
                    }
                }
            }
        }
        return valueList;
    }


    @Override
    public String getValueAt(int row, int column) {
        return values.get(row)[column];
    }

    @Override
    public int getColumnCount() {
        return headers.length;
    }

    @Override
    public int getRowCount() {
        return values.size();
    }

    @Override
    public String getColumnName(int column) {
        return headers[column];
    }

    @Override
    public boolean areColumnsVisible() {
        return true;
    }
}


