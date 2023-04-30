package utils;

import javafx.scene.Node;
import models.Moment;

import java.util.HashMap;

public class InterviewNodes {
    private HashMap<Moment, Node> interviewNodesMap = new HashMap<>();
    public InterviewNodes() {
        this.interviewNodesMap = new HashMap<>();
    }

    public InterviewNodes(Moment moment, Node node) {
        this.interviewNodesMap = new HashMap<>();
        this.interviewNodesMap.put(moment, node);
    }

    public void add(Moment moment, Node node) {
        this.interviewNodesMap.put(moment, node);
    }

    public Node getNodeByMomentName(String name) {
        for (Moment moment : this.interviewNodesMap.keySet()) {
            if(moment.getName().equals(name)){
                return this.interviewNodesMap.get(moment);
            }
        }
        return null;
    }
    //test
    public Moment getMomentByMomentName(String name) {
        for (Moment moment : this.interviewNodesMap.keySet()) {
            if(moment.getName().equals(name)){
                return moment;
            }
        }
        return null;
    }
    public Node findNodeByNameLike(String name) {

        for (Moment moment : this.interviewNodesMap.keySet()) {
            if(moment.getName().contains(name)){
                return this.interviewNodesMap.get(moment);
            }
        }
        return null;
    }

    public HashMap<Moment, Node> getInterviewNodesMap() {
        return interviewNodesMap;
    }

    public void setInterviewNodesMap(HashMap<Moment, Node> interviewNodesMap) {
        this.interviewNodesMap = interviewNodesMap;
    }

    public void remove(Moment moment) {
        this.interviewNodesMap.remove(moment);
    }
}
