package utils;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Node;
import models.ConcreteCategory;
import models.ConcreteProperty;
import models.Moment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class MomentSearch {
    private String currentSearchWord;
    private ArrayList<Node> searchResult;
    private SimpleIntegerProperty resultCount;
    private SimpleIntegerProperty resultPosition;

    public int getResultPosition() {
        return resultPosition.get();
    }

    public SimpleIntegerProperty resultPositionProperty() {
        return resultPosition;
    }

    public int getResultCount() {
        return resultCount.get();
    }

    public SimpleIntegerProperty resultCountProperty() {
        return resultCount;
    }

    public void setResultCount(int resultCount) {
        this.resultCount.set(resultCount);
    }

    public MomentSearch() {
        this.searchResult = new ArrayList<>();
        this.resultCount = new SimpleIntegerProperty(0);
        this.resultPosition = new SimpleIntegerProperty(-1);
    }

    public void resetSearch() {
        this.resultPosition.set(-1);
        this.resultCount.set(0);
        this.searchResult.clear();
    }

    public void countOccurrences(String search, SearchProperties searchProperties) {
        search=search.trim().toUpperCase(Locale.ROOT);
        this.currentSearchWord = search;
        this.resetSearch();
        int count = 0;
        int lastIndex = 0;
        HashMap<Moment, Node> interviewNodes = GlobalVariables.nodeViews.getCurrentInterviewNodes().getInterviewNodesMap();
        search = search.trim();
        //by moment Name search
        if (searchProperties.isMoment_name_choice()) {
            for (Moment moment : interviewNodes.keySet()) {
                if (this.searchResult.contains(interviewNodes.get(moment))) {
                    continue;
                }
                if (moment.getName().trim().toUpperCase(Locale.ROOT).contains(search)) {
                    this.searchResult.add(interviewNodes.get(moment));
                    count++;
                }
            }
        }
        //By category name
        if (searchProperties.isCategory_name_choice()) {
            for (Moment moment : interviewNodes.keySet()) {
                if (this.searchResult.contains(interviewNodes.get(moment))) {
                    continue;
                }
                for (ConcreteCategory category : moment.getCategories()) {
                    if ((!this.searchResult.contains(interviewNodes.get(moment))) && category.getSchemaCategory().getName().trim().toUpperCase(Locale.ROOT).contains(search)) {
                        //this.searchResult.add(interviewNodes.get(moment));
                        this.searchResult.add(category.getController().getName());
                        count++;
                        //because we return all moment that have atleast one category with matching name
                        break;
                    }
                }
            }
        }

        //By property names
        if (searchProperties.isProperty_choice()) {
            for (Moment moment : interviewNodes.keySet()) {
                if (this.searchResult.contains(interviewNodes.get(moment))) {
                    continue;
                }
                for (ConcreteCategory category : moment.getCategories()) {
                    for (ConcreteProperty property : category.propertiesProperty()) {
                        if ((!this.searchResult.contains(interviewNodes.get(moment))) && property.getName().trim().toUpperCase(Locale.ROOT).contains(search)) {
                            //this.searchResult.add(interviewNodes.get(moment));
                            this.searchResult.add(property.getController().getName());
                            count++;
                            //because we return all moment that have atleast one category with matching name
                            break;
                        }
                    }

                }
            }
            //By property Values
            for (Moment moment : interviewNodes.keySet()) {
                if (this.searchResult.contains(interviewNodes.get(moment))) {
                    continue;
                }
                for (ConcreteCategory category : moment.getCategories()) {
                    for (ConcreteProperty property : category.propertiesProperty()) {
                        if ((!this.searchResult.contains(interviewNodes.get(moment))) && property.getValue().trim().toUpperCase(Locale.ROOT).contains(search)) {
                            //this.searchResult.add(interviewNodes.get(moment));
                            this.searchResult.add(property.getController().getValue());
                            count++;
                            //because we return all moment that have atleast one category with matching name
                            break;
                        }
                    }

                }
            }
        }


        this.resultCount.set(count);
    }

    public Node getNextResult() {
        this.resultPosition.set(this.resultPosition.get() + 1);
        return this.searchResult.get(resultPosition.get());
    }

    public Node getPreviousResult() {
        this.resultPosition.set(this.resultPosition.get() - 1);
        return this.searchResult.get(this.resultPosition.get());

    }

    public String getCurrentSearchWord() {
        return currentSearchWord;
    }

    public void setCurrentSearchWord(String currentSearchWord) {
        this.currentSearchWord = currentSearchWord;
    }

    public boolean hasNext() {
        return !(this.resultPosition.get() + 1 >= this.resultCount.get());
    }

    public boolean hasPrevious() {
        return this.resultPosition.get() > 0;
    }

    public boolean isEmpty() {
        return this.resultCount.get() == 0;
    }
    public int getCurrentSearchIndex(){
        return this.resultPosition.get();
    }
    public SimpleIntegerProperty getCurrentSearchIndexProperty(){
        return this.resultPosition;
    }
}
