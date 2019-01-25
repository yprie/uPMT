package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import application.Main;
import model.Category;
import model.DescriptionInterview;
import model.MomentExperience;
import model.Project;

public class MomentComparaison {
	/* Singleton */
    private MomentComparaison() {}
     
    private static MomentComparaison instance = null;
    private ArrayList<Category> mCategories = null;
    private ArrayList<DescriptionInterview> mInterviews = null;
    private ArrayList<MomentExperience> mMomentsTmp = null;
    private Map<DescriptionInterview, ArrayList<MomentExperience>> mItwMoments = null;
    
    public static MomentComparaison getInstance() {           
        if (instance == null){   
        	instance = new MomentComparaison();
        	instance.mCategories = new ArrayList<Category>();
        	instance.mInterviews = new ArrayList<DescriptionInterview>();
        	instance.mItwMoments = new HashMap<DescriptionInterview, ArrayList<MomentExperience>>();
        	instance.mMomentsTmp = new ArrayList<MomentExperience>();
        }
        return instance;
    }
    
   /**
   * finds all the moment in interwiew
   * @param p the current project
   */
    private static void lookingForMoment(Project p) {
    	
    	
    	System.out.println("name  " + p.getInterviews().toString());
    	HashMap<String, Integer> mapMoments = new HashMap<String, Integer>();
    	
    	for(DescriptionInterview interview : p.getInterviews()){
    		//get all moment in interview
		    for (int i = 0; i < interview.getMoments().size(); i++) {
		    	System.out.println("le 1er i "+i);
				MomentExperience moment = interview.getMoments().get(i);
				System.out.println(moment.toString());
				
				/*
				System.out.println(moment.getName() + " nb Fils " + moment.getSubMoments().size());
				//System.out.println("moi " + moment.getID() + " parent " +  moment.getParentID());
				//get category
				for(Category c : moment.getCategories()){
					//System.out.println("category: " + c.getName());
				}
				//get all sub moments of the moment
				for (int j = 0; j < moment.getSubMoments().size(); j++) {
					System.out.println("le 1er j "+j);
					MomentExperience subMoment = moment.getSubMoments().get(j);
					System.out.println(subMoment.getName() + " nb Fils " + subMoment.getSubMoments().size());
					for(Category c : subMoment.getCategories()){
						//System.out.println("category: " + c.getName());
						
					}
					if(subMoment.getSubMoments().size()>0) {
						lookingForSubMoments(subMoment);
					}
				}
				//System.out.println("fin boucle");
				 *
				 */
		    }
		    
    	}
    }

	 
    /**
	* finds all the sub moment for a moment given
	* @param the current moment.
	*/
    private static void lookingForSubMoments(MomentExperience moment) {
    	for (int j = 0; j < moment.getSubMoments().size(); j++) {
    		
    		//System.out.println("parent " +  moment.getParentID());
    		MomentExperience subMoment = moment.getSubMoments().get(j);
			System.out.println(subMoment.getName() + " nb Fils " + subMoment.getSubMoments().size());
			for(Category c : subMoment.getCategories()){
				//System.out.println("le 2er j "+j + " " + moment.getParentCol());
				//System.out.println("category: " + c.getName());
			}
			if(subMoment.getSubMoments().size()>0) {
				lookingForSubMoments(subMoment);
			}
		}
    }
	
	
    /**
     * refresh the singleton's attributes
     * @param main
     */
    public static void update(Main main) {
    	instance.mCategories.clear();
    	instance.mInterviews.clear();
    	instance.mItwMoments.clear();
    	instance.lookingForMoment(main.getCurrentProject());
    }
    
}
