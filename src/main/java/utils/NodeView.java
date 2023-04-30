package utils;

import javafx.scene.Node;
import models.Interview;
import models.Moment;
import models.SchemaCategory;

import java.util.HashMap;

public class NodeView {
    private HashMap<Interview, InterviewNodes> nodeList;
    private GlobalVariables globalVariables = GlobalVariables.getGlobalVariables();

    public NodeView() {
        this.nodeList = new HashMap<>();
        this.globalVariables = GlobalVariables.getGlobalVariables();
    }

    public void addMoment(Moment moment, Node node) {
        Interview currentInterview = globalVariables.getProject().getSelectedInterview();
        if (this.nodeList.containsKey(currentInterview)) {
            this.nodeList.get(currentInterview).add(moment, node);
        } else {
            this.nodeList.put(currentInterview, new InterviewNodes(moment, node));
        }
    }

    public Node getNodeByMomentName(String name) {
        return this.nodeList.get(globalVariables.getProject().getSelectedInterview()).getNodeByMomentName(name);
    }

    public Moment getMomentByMomentName(String name) {
        return this.nodeList.get(globalVariables.getProject().getSelectedInterview()).getMomentByMomentName(name);
    }

    public Moment getInstanceByCategory(SchemaCategory schemaCategory, int index) {
        return this.nodeList.get(this.globalVariables.getProject().getSelectedInterview())
                .getInstanceByCategory(schemaCategory, index);
    }

    public InterviewNodes getCurrentInterviewNodes() {
        return this.nodeList.get(globalVariables.getProject().getSelectedInterview());
    }

    public void removeMoment(Moment moment) {
        this.getCurrentInterviewNodes().remove(moment);
    }
}
