package components.comparison;


import javafx.collections.ObservableList;
import javafx.util.Pair;
import models.*;
import org.json.JSONObject;
import persistency.newSaveSystem.*;
import persistency.newSaveSystem.serialization.Serializable;
import persistency.newSaveSystem.serialization.SerializationPool;
import persistency.newSaveSystem.serialization.json.JSONReadPool;
import persistency.newSaveSystem.serialization.json.JSONSerializer;
import persistency.newSaveSystem.serialization.json.JSONWritePool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ComparisonParser {

    private LinkedHashMap<SInterview, LinkedHashMap<SMoment, ArrayList<Pair<SchemaFolder, SConcreteCategory>>>> datas;

    private LinkedHashMap<SchemaFolder, ArrayList<String>> dimensions;        //hashMap of the dimensions, and their categories
    public ComparisonParser(String path) throws IOException {
        parse(path);
    }

    public void parse(String path) throws IOException {
        //load a project to get the datas we need (moments and categories)

        File fileToRead = new File(path);
        String fileContents;
        FileInputStream file_input = new FileInputStream(fileToRead);
        byte[] content = new byte[(int) fileToRead.length()];
        file_input.read(content);
        file_input.close();
        fileContents = new String(content, StandardCharsets.UTF_8);

        JSONObject obj = new JSONObject(fileContents);
        JSONReadPool pool = new JSONReadPool();
        SerializationPool<Integer, Object> modelsPool = new SerializationPool<>();
        JSONSerializer serializer = new JSONSerializer(obj, pool, modelsPool);

        SProject sp = new SProject(serializer);
        sp.initReading();

        ObservableList<SchemaFolder> folders = sp.schemaTreeRoot.convertToModel().foldersProperty();  //folders from the treeRoot
        ArrayList<SInterview> interviews = sp.interviews;              //list of the interviews

        setDimensionsToHM(folders);
        setDatas(interviews);
    }

    //fill the dimension hashmap with what we found in the folders
    public void setDimensionsToHM(ObservableList<SchemaFolder> folders){
        LinkedHashMap<SchemaFolder, ArrayList<String>> dimensions = new LinkedHashMap<>();
        for (SchemaFolder f : folders) {    //fill the dimension hashmap with what we found in the folders
            ArrayList<String> cctmp = new ArrayList<>();
            if(!Objects.equals(f.getName(), "moments")) {      //avoid the momentType folder
                for (SchemaCategory cc : f.categoriesProperty()){
                    cctmp.add(cc.getName());
                }
                dimensions.put(f, cctmp);
            }
        }
        this.dimensions = dimensions;
    }
    public LinkedHashMap<SchemaFolder, ArrayList<String>> getDimensions(){
        return dimensions;
    }

    public void setDatas(ArrayList<SInterview> interviews) {
        LinkedHashMap<SInterview, LinkedHashMap<SMoment, ArrayList<Pair<SchemaFolder, SConcreteCategory>>>> datas = new LinkedHashMap<>();
        for (SInterview i : interviews){
            LinkedHashMap<SMoment, ArrayList<Pair<SchemaFolder, SConcreteCategory>>> hmTmp = new LinkedHashMap<>();
            ArrayList<SMoment> moments = i.rootMoment.submoments;

            for (SMoment m : moments){
                ArrayList<Pair<SchemaFolder, SConcreteCategory>> lTmp = new ArrayList<>();
                for (SConcreteCategory cc : m.categories){    //check each category from a moment
                    for (SchemaFolder key : dimensions.keySet()) {      //we fuse the categories with their dimension
                        if (dimensions.get(key).contains(cc.schemaCategory.name)) {    //if it matchs we add the dimension key
                            lTmp.add(new Pair<>(key, cc));
                            break;  // break the loop if the category is found in any of the arraylists
                        }
                    }
                }
                hmTmp.put(m, lTmp);
            }
            datas.put(i, hmTmp);
        }
        this.datas = datas;
    }

    public LinkedHashMap<SInterview, LinkedHashMap<SMoment, ArrayList<Pair<SchemaFolder, SConcreteCategory>>>> getDatas(){ return datas; }

    public void readDatas(){
        for (SInterview i : datas.keySet()){
            System.out.println("Interview : " + i.getName());
            for (SMoment m : datas.get(i).keySet()){
                System.out.println("Moment : " + m.getName());
                for (Pair<SchemaFolder, SConcreteCategory> cc : datas.get(i).get(m)){
                    System.out.println("Category : " + cc.getValue().getName());
                }
            }
        }
    }
}