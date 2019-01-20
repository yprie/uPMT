package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import application.Main;
import model.Category;
import model.DescriptionInterview;
import model.Folder;
import model.MomentExperience;
import model.Project;
import model.Property;
import model.Schema;
import model.Type;

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
    	for(DescriptionInterview ent : p.getInterviews()){
    		//get all moment in interview
		    for (int i = 0; i < ent.getMoments().size(); i++) {
				MomentExperience m = ent.getMoments().get(i);
				System.out.println("moment " + m.getName());
				//get category
				for(Category c : m.getCategories()){
					System.out.println("category: " + c.getName());
				}
				//get all sub moments of the moment
				for (int j = 0; j < m.getSubMoments().size(); j++) {
					MomentExperience sub = m.getSubMoments().get(j);
					System.out.println("sous moment: " + sub.getName());
					for(Category c : m.getCategories()){
						System.out.println("category: " + c.getName());
					}
					if(sub.getSubMoments().size()>0) {
						lookingForSubMoments(sub);
					}
				}
		    }
    	}
    }

	 
    /**
	* finds all the sub moment for a moment given
	* @param the current moment.
	*/
    private static void lookingForSubMoments(MomentExperience m) {
    	for (int j = 0; j < m.getSubMoments().size(); j++) {
    		MomentExperience sub = m.getSubMoments().get(j);
			System.out.println("sous moment: " + sub.getName());
			for(Category c : m.getCategories()){
				System.out.println("category: " + c.getName());
			}
			if(sub.getSubMoments().size()>0) {
				lookingForSubMoments(sub);
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
