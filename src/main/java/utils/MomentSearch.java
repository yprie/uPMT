package utils;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Node;
import models.Moment;

import java.util.ArrayList;
import java.util.HashMap;

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

    public void countOccurrences(String search) {
        this.currentSearchWord = search;
        this.resetSearch();
        int count = 0;
        int lastIndex = 0;
        HashMap<Moment, Node> interviewNodes = GlobalVariables.nodeViews.getCurrentInterviewNodes().getInterviewNodesMap();

        for (Moment moment : interviewNodes.keySet()) {
            if(moment.getName().contains(search)){
                this.searchResult.add(interviewNodes.get(moment)) ;
                count++;
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

}
