package models;

import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;

public class SearchResult {
    private String textContent;
    private String currentSearchWord;
    private ArrayList<Integer> searchResult;
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

    public SearchResult(String textContent) {
        this.textContent = textContent;
        this.searchResult = new ArrayList<>();
        this.resultCount = new SimpleIntegerProperty(0);
        this.resultPosition = new SimpleIntegerProperty(-1);
    }

    public void resetSearch(){
        this.resultPosition.set(-1);
        this.resultCount.set(0);
        this.searchResult.clear();
    }
    public void countOccurrences(String text, String search) {
        this.currentSearchWord = search;
        this.resetSearch();
        int count = 0;
        int lastIndex = 0;
        while (lastIndex != -1) {
            lastIndex = text.indexOf(search, lastIndex);
            if (lastIndex != -1) {
                this.searchResult.add(text.indexOf(search, lastIndex));
                count++;
                lastIndex += search.length();
            }
        }
        this.resultCount.set(count);
    }

    public int getNextResult() {
        this.resultPosition.set(this.resultPosition.get() + 1);
        return this.searchResult.get(resultPosition.get());
    }

    public int getPreviousResult() {
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
        System.out.println(this.resultPosition.get() + " >= " + this.resultCount.get());
        return !(this.resultPosition.get() + 1 >= this.resultCount.get());
    }

    public boolean hasPrevious() {
        return this.resultPosition.get() > 0;
    }

    public boolean isEmpty() {
        return this.resultCount.get() == 0;
    }

}
