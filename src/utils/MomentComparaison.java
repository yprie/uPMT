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
    private Map<DescriptionInterview, ArrayList<MomentExperience>> mItwMoments = null;
    private static HashMap<Integer, ArrayList<MomentExperience>> mRowMoments = null;
    static ArrayList<MomentExperience> lignee = null;
    
    public static MomentComparaison getInstance() {           
        if (instance == null){   
        	instance = new MomentComparaison();
        	instance.mCategories = new ArrayList<Category>();
        	instance.mInterviews = new ArrayList<DescriptionInterview>();
        	instance.mItwMoments = new HashMap<DescriptionInterview, ArrayList<MomentExperience>>();
        	instance.mMoments = new ArrayList<ArrayList<MomentExperience>>();
        	instance.lignee = new ArrayList<MomentExperience>();
        	instance.mRowMoments = new HashMap<Integer, ArrayList<MomentExperience>>();
        }
        return instance;
    }
    
   /**
   * finds all the moment in interwiew
   * @param p the current project
   */
    private static void lookingForMoment(Project p) {
    	
    	
    	//System.out.println("name  " + p.getInterviews().toString());
    	
    	//ArrayList<MomentExperience> mMoments = new ArrayList<MomentExperience>();
    	for(DescriptionInterview interview : p.getInterviews()){
    		
    		//get all moment in interview
		    for (int i = 0; i < interview.getMoments().size(); i++) {
		    	ArrayList<MomentExperience> lignee = new ArrayList<MomentExperience>();
		    	//System.out.println(interview.getMoments().size());
				MomentExperience moment = interview.getMoments().get(i);
				//System.out.println("ligne 1 " + moment.getName() + "       " + moment.getParentID());
				//System.out.println("aaaaaa " + moment.getName() + " "+ moment.getParentCol());
				lignee.add(moment);
				
				//ici
				MomentComparaison.addMoments(moment);
				
				//mMoments.add(lignee);
				for(Category c : moment.getCategories()){
				}
				//System.out.println("hhhh1" + moment.toString());
				//System.out.println("size 1 " + moment.getSubMoments().size());
				for (int j = 0; j < moment.getSubMoments().size(); j++) {
					
					//ici
					//MomentComparaison.addMoments(moment);
					
					MomentExperience subMoment = moment.getSubMoments().get(j);
					lignee.add(subMoment);
					//System.out.println("aaaaaa " + subMoment.getName() + " "+ subMoment.getParentCol());
					//System.out.println("ligne 2 " + subMoment.getName() + "       " + subMoment.getParentID());
					//mMoments.add(subMoment);
					//System.out.println("ligne 2 " + subMoment.toString());
					for(Category c : subMoment.getCategories()){
					}
					//System.out.println("size 2 " + moment.getSubMoments().size());

					if((subMoment.getSubMoments().size()>0)) {
							lookingForSubMoments(subMoment, lignee);
							
						
						
					}

					if(subMoment.getSubMoments().size()==0) {		
						/*
						System.out.println("LELLELELEL " + subMoment.getName());
						MomentExperience nullMoment = new MomentExperience();
						nullMoment.setName("nullMomentOOOOOOO");
						subMoment.addSousMoment(nullMoment);
						*/
					}
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
    		
    		//ici
			//MomentComparaison.addMoments(moment);
			
    		MomentExperience subMoment = moment.getSubMoments().get(j);
    		//System.out.println("ligne 3 " + subMoment.toString());

    		lignee.add(subMoment);
			for(Category c : subMoment.getCategories()){
			}
			//System.out.println("ligne 3 " + subMoment.getName() + subMoment.getParentID());
			//System.out.println("aaaaaa " + subMoment.getName() + " "+ subMoment.getParentCol());
			
			
			if(subMoment.getSubMoments().size()>0) {
				//subMoment.getParent(main);
				//System.out.println(subMoment.getParentCol());

					lookingForSubMoments(subMoment, lignee);
				
			}
			if(subMoment.getSubMoments().size()==0) {
				/*
				System.out.println("LELLELELEL " + subMoment.getName());
				MomentExperience nullMoment = new MomentExperience();
				//System.out.println("taille sous moment = 0");
				nullMoment.setName("nullMomentAAAAA");
				subMoment.addSousMoment(nullMoment);
				*/
			}
		}
    	
    }
	
	public static void addMoments(MomentExperience moment) {
		ArrayList<MomentExperience> moments = new ArrayList<MomentExperience>();
		if(mRowMoments.containsKey(moment.getParentID())) {
			//System.out.println("contain " + moment);
			moments = mRowMoments.get(moment.getParentID());
			moments.add(moment);
			mRowMoments.put(moment.getParentID(), moments);
		} else {
			//System.out.println("not contain " + moment);
			moments.add(moment);
			mRowMoments.put(moment.getParentID(), moments);
		}
		
	}
	
	public static void printRowMoments() {
		//System.out.println("row " + mRowMoments.size());

		 //mRowMoments.forEach((k,v) -> System.out.println("key: "+k+" value:"+v));
	}
	
	
	
    public static ArrayList<ArrayList<MomentExperience>> getmMoments() {
		return mMoments;
	}

	/**
     * refresh the singleton's attributes
     * @param main
     */
    public static void update(Main main) {
    	
    	//System.out.println("SIZES " + mRowMoments.size());
    	
    	instance.mMoments.clear();
    	instance.lookingForMoment(main.getCurrentProject());
    	//System.out.println("SIZE " + mRowMoments.size());
    	MomentComparaison.printRowMoments();
    }
    
}
