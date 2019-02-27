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
    private static ArrayList<ArrayList<MomentExperience>> mMoments = null;
    //private Map<DescriptionInterview, ArrayList<MomentExperience>> mItwMoments = null;
    private static HashMap<Integer, ArrayList<MomentExperience>> mRowMoments = null; 
    //static ArrayList<MomentExperience> mMoment = null;
    
    public static MomentComparaison getInstance() {           
        if (instance == null){   
        	instance = new MomentComparaison();
        	instance.mCategories = new ArrayList<Category>();
        	instance.mInterviews = new ArrayList<DescriptionInterview>();
        	//instance.mItwMoments = new HashMap<DescriptionInterview, ArrayList<MomentExperience>>();
        	instance.mMoments = new ArrayList<ArrayList<MomentExperience>>();
        	//instance.mMoment = new ArrayList<MomentExperience>();
        	instance.mRowMoments = new HashMap<Integer, ArrayList<MomentExperience>>();
        }
        return instance;
    }
    
   /**
   * finds all the moment in interwiew
   * @param p the current project
   */
    private static void lookingForMoment(Project p) {
    	for(DescriptionInterview interview : p.getInterviews()){
		    for (int i = 0; i < interview.getMoments().size(); i++) {
		    	ArrayList<MomentExperience> lignee = new ArrayList<MomentExperience>();
				MomentExperience moment = interview.getMoments().get(i);
				lignee.add(moment);
				MomentComparaison.addMoments(moment);
				for(Category c : moment.getCategories()){
				}
				//System.out.println("size 1 " + moment.getSubMoments().size());
				for (int j = 0; j < moment.getSubMoments().size(); j++) {
					MomentExperience subMoment = moment.getSubMoments().get(j);
					lignee.add(subMoment);
					for(Category c : subMoment.getCategories()){
					}
					//System.out.println("size 2 " + moment.getSubMoments().size());
					if((subMoment.getSubMoments().size()>0)) {
						lookingForSubMoments(subMoment, lignee);
					}
					/*
					if(subMoment.getSubMoments().size()==0) {		
						
						System.out.println("LELLELELEL " + subMoment.getName());
						MomentExperience nullMoment = new MomentExperience();
						nullMoment.setName("nullMomentOOOOOOO");
						subMoment.addSousMoment(nullMoment);
						
					}
					 */
				}
				System.out.println(lignee);
				mMoments.add(lignee);
		    }
		    
    	}
    }

	 
    /**
	* finds all the sub moment for a moment given
	* @param the current moment.
	*/
    private static void lookingForSubMoments(MomentExperience moment, ArrayList<MomentExperience> lignee) {
    	//System.out.println("size 3 " + moment.getSubMoments().size());
    	for (int j = 0; j < moment.getSubMoments().size(); j++) {
    		MomentExperience subMoment = moment.getSubMoments().get(j);
    		lignee.add(subMoment);
			for(Category c : subMoment.getCategories()){
			}
			if(subMoment.getSubMoments().size()>0) {
				lookingForSubMoments(subMoment, lignee);
				
			}
		}
    }
	
    /**
    * Add all subMoments in  of a moment in a list
    * @param main
    */
	public static void addMoments(MomentExperience moment) {
		ArrayList<MomentExperience> moments = new ArrayList<MomentExperience>();
		if(mRowMoments.containsKey(moment.getParentID())) {
			moments = mRowMoments.get(moment.getParentID());
			moments.add(moment);
			mRowMoments.put(moment.getParentID(), moments);
		} else {
			moments.add(moment);
			mRowMoments.put(moment.getParentID(), moments);
		}
	}
	
	
    public static ArrayList<ArrayList<MomentExperience>> getmMoments() {
		return mMoments;
	}

    /**
     * refresh the singleton's attributes
     * @param main
     */
    public static void update(Main main) {
    	instance.mMoments.clear();
    	instance.lookingForMoment(main.getCurrentProject());
    }
    
}
