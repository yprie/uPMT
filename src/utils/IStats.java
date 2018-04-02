package utils;

import application.Main;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

import model.Category;
import model.DescriptionInterview;
import model.Folder;
import model.MomentExperience;
import model.Type;

public class IStats {
	
	/* Singleton */
    private IStats() {}
     
    private static IStats instance = null;
    private Main main = null;
    private ArrayList<Category> mCategories = null;
    private ArrayList<DescriptionInterview> mInterviews = null;
    
    public static IStats getInstance() {           
        if (instance == null)
        {   instance = new IStats();
        	instance.mCategories = new ArrayList<Category>();
        	instance.mInterviews = new ArrayList<DescriptionInterview>();
        }
        return instance;
    }
    
    public static void setParameters(Main main) {
    	instance.main = main;
    }
    
    /**
     * Counts the number of occurences of appar
     * @param interview the current interview
     * @param category the current category of the schema 
     * @return the number of times the category has been used in one specific interview
     */
    public static int nbOccurences(DescriptionInterview interview, Category category) {
    	int cpt = 0;
    	for (MomentExperience moment : interview.getMoments()) {
    			if (moment.getTypes().contains(category)) {
    			cpt++;
    		}
		}
    	return cpt;
    }
    
    private static void lookingForCategories(Type t) {
    	
    	for (Type t_it : t.getTypes()) {
    					
		   	if(t_it.isCategory()) {
		   		instance.mCategories.add((Category) t_it);
		   	}
    	
	    	else {
	    		instance.lookingForCategories(t_it);
	    	}
    	}	   	
    }
    
    private static void lookingForInterviews(LinkedList <DescriptionInterview> interviews) {
    	for (DescriptionInterview interview_it : interviews) {
    		instance.mInterviews.add(interview_it);
		}
    }
    
    
    public static void update() {
    	instance.lookingForCategories(instance.main.getCurrentProject().getSchema());
    	instance.lookingForInterviews(instance.main.getCurrentProject().getInterviews());
    }
    
    public static ArrayList<Category> getCategories() {
    	return instance.mCategories;
    }
   
    public static ArrayList<DescriptionInterview> getInterviews() {
    	return instance.mInterviews;
    }
}

