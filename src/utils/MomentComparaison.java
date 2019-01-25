package utils;

import java.awt.List;
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
    private static ArrayList<MomentExperience> mMoments = null;
    private Map<DescriptionInterview, ArrayList<MomentExperience>> mItwMoments = null;
    
    public static MomentComparaison getInstance() {           
        if (instance == null){   
        	instance = new MomentComparaison();
        	instance.mCategories = new ArrayList<Category>();
        	instance.mInterviews = new ArrayList<DescriptionInterview>();
        	instance.mItwMoments = new HashMap<DescriptionInterview, ArrayList<MomentExperience>>();
        	instance.mMoments = new ArrayList<MomentExperience>();
        }
        return instance;
    }
    
   /**
   * finds all the moment in interwiew
   * @param p the current project
   */
    private static void lookingForMoment(Project p) {
    	
    	
    	System.out.println("name  " + p.getInterviews().toString());
    	
    	
    	for(DescriptionInterview interview : p.getInterviews()){
    		//get all moment in interview
		    for (int i = 0; i < interview.getMoments().size(); i++) {
		    	
				MomentExperience moment = interview.getMoments().get(i);
				mMoments.add(moment);
				for(Category c : moment.getCategories()){
				}
				//System.out.println("hhhh1" + moment.toString());
				System.out.println("size 1 " + moment.getSubMoments().size());
				for (int j = 0; j < moment.getSubMoments().size(); j++) {
					MomentExperience subMoment = moment.getSubMoments().get(j);
					mMoments.add(subMoment);
					//System.out.println("hhhh2" + subMoment.toString());
					for(Category c : subMoment.getCategories()){
					}
					System.out.println("size 2 " + moment.getSubMoments().size());
					if(subMoment.getSubMoments().size()==0) {
						System.out.println("taille sous moment = 0");
					}
					if(subMoment.getSubMoments().size()>0) {
						lookingForSubMoments(subMoment);
					}
				}
		    }
		    
    	}
    }

	 
    /**
	* finds all the sub moment for a moment given
	* @param the current moment.
	*/
    private static void lookingForSubMoments(MomentExperience moment) {
    	System.out.println("size 3 " + moment.getSubMoments().size());
    	for (int j = 0; j < moment.getSubMoments().size(); j++) {
    		MomentExperience subMoment = moment.getSubMoments().get(j);
    		//System.out.println("hhhh3" + subMoment.toString());

    		mMoments.add(subMoment);
			for(Category c : subMoment.getCategories()){
			}
			if(subMoment.getSubMoments().size()==0) {
				System.out.println("taille sous moment = 0");
			}
			if(subMoment.getSubMoments().size()>0) {
				lookingForSubMoments(subMoment);
			}
		}
    }
	
	
    public static ArrayList<MomentExperience> getmMoments() {
		return mMoments;
	}

	/**
     * refresh the singleton's attributes
     * @param main
     */
    public static void update(Main main) {
    	instance.lookingForMoment(main.getCurrentProject());
    }
    
}
