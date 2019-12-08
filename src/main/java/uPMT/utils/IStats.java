package utils;

import SchemaTree.Cell.Models.ICategoryAdapter;
import application.Main;

import java.util.Map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import model.DescriptionInterview;
import model.Folder;
import model.MomentExperience;
import model.Schema;
import model.Type;

public class IStats {
	
	/* Singleton */
    private IStats() {}
     
    private static IStats instance = null;
    private ArrayList<ICategoryAdapter> mCategories = null;
    private ArrayList<DescriptionInterview> mInterviews = null;
    private ArrayList<MomentExperience> mMomentsTmp = null;
    private Map<DescriptionInterview, ArrayList<MomentExperience>> mItwMoments = null;
    
    
    
    public static IStats getInstance() {           
        if (instance == null)
        {   instance = new IStats();
        	instance.mCategories = new ArrayList<ICategoryAdapter>();
        	instance.mInterviews = new ArrayList<DescriptionInterview>();
        	instance.mItwMoments = new HashMap<DescriptionInterview, ArrayList<MomentExperience>>();
        	instance.mMomentsTmp = new ArrayList<MomentExperience>();
        }
        return instance;
    }
    
   
    /***** REFRESHING METHODS *****/
    
    /**
     * finds the categories of the current scheme and stores it locally
     * @param t the current type of the scheme
     */
    private static void lookingForCategories(Type t) {
    	if(t.isSchema()) 
    		for(Folder f : ((Schema)t).getFolders()) 
    			instance.lookingForCategories(f);
    	else if(t.isFolder()) {
			for(ICategoryAdapter c : ((Folder)t).getCategories())
				instance.mCategories.add(c);
			for(Folder fs : ((Folder)t).getFolders())
				instance.lookingForCategories(fs);
    	}   	
    }
    
    
    /**
     * finds the interviews of the current project and stores it locally
     * @param interviews the list of all the interviews
     */
    private static void lookingForInterviews(LinkedList <DescriptionInterview> interviews) {
    	for (DescriptionInterview interview_it : interviews) {
    		instance.mInterviews.add(interview_it);
    		instance.mItwMoments.put(interview_it, null);
		}
    }
    
    
    /**
     * finds all the moments and submoments of one specific interview and stores it locally in a temporary variable
     * @param moment the current moment.
     */
    private static void lookingForMoments(MomentExperience moment) {
    	instance.mMomentsTmp.add(moment);
    	for (MomentExperience m_it : moment.getSubMoments()) {
			instance.lookingForMoments(m_it);
		}
    }
    
  
    
    /***** STATISTICS COMPUTING *****/ 
    
    
    /**
     * refresh the singleton's attributes
     * @param main
     */
    public static void update(Main main) {
    	instance.mCategories.clear();
    	instance.mInterviews.clear();
    	instance.mItwMoments.clear();
    	instance.lookingForCategories(main.getCurrentProject().getSchema());
    	instance.lookingForInterviews(main.getCurrentProject().getInterviews());
    }
    
    /**
     * Counts the number of occurences of appearance of a specific category in a specific interview
     * @param interview the current interview
     * @param category the current category of the schema 
     * @return the number of times the category has been used in one specific interview
     */
    public static int nbOccurrences(DescriptionInterview interview, ICategoryAdapter category) {
    	
    	int cpt = 0;
    	    	
    	/* if we are looking for a category in an interview that we have not met yet, we have to find all the moment*/
    	
    	if (instance.mItwMoments.get(interview) == null) {
    		
    		for (MomentExperience mom_it : interview.getMoments()) { // we start with the first-level moments
    			instance.lookingForMoments(mom_it); //then we find all the sub-moments employed in the model of the concerned interview
    		}
    		
    		instance.mItwMoments.put(interview, new ArrayList<MomentExperience>(instance.mMomentsTmp)); //we copy only the values of the moments found for 1 interview
    		instance.mMomentsTmp.clear(); //we clear it because we need it free for the next interview
    	
    	}
    	
    	for (MomentExperience moment : instance.mItwMoments.get(interview)) {
	    	if (moment.getCategories().contains(category)) { //then we compare if we find any category of the scheme in the model
				cpt++;
			}
    	}
    	
    	return cpt;
    }
    

    /***** GETTERS *****/ 
    
    public static ArrayList<ICategoryAdapter> getCategories() {
    	return instance.mCategories;
    }
   
    public static ArrayList<DescriptionInterview> getInterviews() {
    	return instance.mInterviews;
    }
    
    public static ICategoryAdapter getCategory(String s) {
    	for (ICategoryAdapter category : instance.mCategories) {
    		if (category.getName() == s) {
    			return category;
    		}
		}
    	return null;
    }
    
    public static DescriptionInterview getInterview(String s) {
    	for (DescriptionInterview interview : instance.mInterviews) {
    		if (interview.getName() == s) {
    			return interview;
    		}
    	}
    	return null;
    }
    
}

