package components.comparison;

import javafx.util.Pair;
import models.SchemaFolder;
import persistency.newSaveSystem.SConcreteCategory;
import persistency.newSaveSystem.SInterview;
import persistency.newSaveSystem.SMoment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ComparisonTable {
    private ArrayList<block> blocks;  //the table is made from the blocks (1 block = 1 interview)
    private ComparisonParser rawDatas; //datas to fill the table

    public ComparisonTable (String path) throws IOException {
        this.blocks = new ArrayList<>();
        this.rawDatas = new ComparisonParser(path);
        createTable();
    }

    public int getMaxTableLength(){
        int length = 0;
        for(HashMap<SMoment, ArrayList<Pair<SchemaFolder, SConcreteCategory>>> listM : rawDatas.getDatas().values()){
            int momentsLength = listM.size();
            if (length < momentsLength){ length = momentsLength; }
        }
        return length;
    }
    private line<SMoment> createFirstLine(SInterview interview){
        int size = getMaxTableLength();
        String title = "Moments";
        ArrayList<SMoment> values = new ArrayList<>(interview.rootMoment.submoments);
        return new line<>(title, size, values);
    }
    private line<ArrayList<SConcreteCategory>> createDimensionLine(SInterview interview, SchemaFolder dimension){
        HashMap<SMoment, ArrayList<Pair<SchemaFolder, SConcreteCategory>>> datas = rawDatas.getDatas().get(interview);
        int size = getMaxTableLength();
        String title = dimension.getName();
        ArrayList<ArrayList<SConcreteCategory>> values = new ArrayList<>(size);
        for(ArrayList<Pair<SchemaFolder, SConcreteCategory>> pairList : datas.values()){
            ArrayList<SConcreteCategory> tmpList = new ArrayList<>();
            for(Pair<SchemaFolder, SConcreteCategory> pair: pairList){
                if(pair.getKey() == dimension){
                    tmpList.add(pair.getValue());
                }
            }
            values.add(tmpList);
        }
        return new line<>(title, size, values);
    }

    private block createBlock(SInterview interview){
        String title = interview.convertToModel().getTitle();
        line<SMoment> firstLine = createFirstLine(interview);
        ArrayList<line<ArrayList<SConcreteCategory>>> dimensionLines = new ArrayList<>();
        ArrayList<SchemaFolder> dimensions = new ArrayList<>(rawDatas.getDimensions().keySet());

        for (SchemaFolder d : dimensions){
            dimensionLines.add(createDimensionLine(interview, d));
        }
        return new block(title, firstLine, dimensionLines);
    }

    private void createTable(){
        ArrayList<block> blocks = new ArrayList<>();
        for (SInterview interview : rawDatas.getDatas().keySet()){
            blocks.add(createBlock(interview));
        }
        this.blocks = blocks;
    }

    public void readTable(){
        for(block b: blocks){
            System.out.println(b.getTitle()+ " ->  ");
            line<SMoment> firstLine = b.getFirstLine();
            System.out.print(firstLine.getTitle() + " : ");
            for (SMoment m : firstLine.getValues()){
                System.out.print("  |  "+m.name);
            }
            ArrayList<line<ArrayList<SConcreteCategory>>> dimensionLines = b.getDimensionLines();
            for (line<ArrayList<SConcreteCategory>> line : dimensionLines){
                System.out.println();
                System.out.print(line.getTitle() + " : ");
                for (ArrayList<SConcreteCategory> ccList : line.getValues()){
                    System.out.print("  |  ");
                    for (SConcreteCategory cc : ccList){
                        //test si c'est le dernier de la liste
                        if (ccList.indexOf(cc) == ccList.size()-1){
                            System.out.print(cc.schemaCategory.name);
                        }else {
                            System.out.print(cc.schemaCategory.name+"; ");
                        }
                    }
                }
            }
            System.out.println();
        }
    }

    public ArrayList<block> getBlocks() {
        return blocks;
    }

}

