package utils;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import models.ConcreteCategory;
import models.Moment;
import models.SchemaCategory;

import java.util.ArrayList;
import java.util.HashMap;

public class InterviewNodes {
    private HashMap<Moment, Node> interviewNodesMap = new HashMap<>();
    private HashMap<SchemaCategory, ArrayList<Moment>> categoryNodesMap;
    public InterviewNodes() {
        this.interviewNodesMap = new HashMap<>();
    }

    public InterviewNodes(Moment moment, Node node) {
        this.interviewNodesMap = new HashMap<>();
        this.categoryNodesMap= new HashMap<>();
        this.interviewNodesMap.put(moment, node);
    }

    public void add(Moment moment, Node node) {
        this.interviewNodesMap.put(moment, node);
        try{
            ObservableList<ConcreteCategory> momentCategories =  moment.getCategories();
            for (ConcreteCategory category : momentCategories){
                if(!this.categoryNodesMap.containsKey(category.getSchemaCategory())){
                    ArrayList<Moment> moments = new ArrayList<>();
                    moments.add(moment);
                    this.categoryNodesMap.put(category.getSchemaCategory(),moments);
                }else{
                    this.categoryNodesMap.get(category.getSchemaCategory()).add(moment);
                }
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }


    }

    public Node getNodeByMomentName(String name) {
        for (Moment moment : this.interviewNodesMap.keySet()) {
            if(moment.getName().equals(name)){
                return this.interviewNodesMap.get(moment);
            }
        }
        return null;
    }

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
        ObservableList<ConcreteCategory> momentCategories =  moment.getCategories();
        for (ConcreteCategory category : momentCategories){
            if(this.categoryNodesMap.containsKey(category.getSchemaCategory())){
                this.categoryNodesMap.get(category.getSchemaCategory()).remove(moment);
            }
        }

    }
    public Moment getInstanceByCategory(SchemaCategory schemaCategory,int index){
        if(this.categoryNodesMap.containsKey(schemaCategory)){
            return this.categoryNodesMap.get(schemaCategory).get(index);
        }
        return null;
    }
}
