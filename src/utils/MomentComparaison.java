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
    private static ArrayList<ArrayList<ArrayList<MomentExperience>>> mMoments = null;
    private static double maxNbMoment=0;
    private static HashMap<Integer, ArrayList<MomentExperience>> mRowMoments = null; 
    
    public static MomentComparaison getInstance() {           
        if (instance == null){   
        	instance = new MomentComparaison();
        	instance.mCategories = new ArrayList<Category>();
        	instance.mInterviews = new ArrayList<DescriptionInterview>();
        	instance.mMoments = new ArrayList<ArrayList<ArrayList<MomentExperience>>>();
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
    		interview.initInterviewName();
    		ArrayList<ArrayList<MomentExperience>> allMoment = new ArrayList<ArrayList<MomentExperience>>();
		    for (int i = 0; i < interview.getMoments().size(); i++) {
		    	
				MomentExperience moment = interview.getMoments().get(i);
				ArrayList<MomentExperience> momentInterview = new ArrayList<MomentExperience>();
				momentInterview.add(moment);
				MomentComparaison.addMoments(moment);
				
				if(moment.getSubMoments().size()>maxNbMoment) {
					maxNbMoment=moment.getSubMoments().size();
				}
				
				for (int j = 0; j < moment.getSubMoments().size(); j++) {
					
					if(moment.getSubMoments().size()>maxNbMoment) {
						maxNbMoment=moment.getSubMoments().size();
					}
					
					MomentExperience subMoment = moment.getSubMoments().get(j);
					momentInterview.add(subMoment);		
					
					if((subMoment.getSubMoments().size()>0)) {
						lookingForSubMoments(subMoment, momentInterview);
					}
		
				}
				
				allMoment.add(momentInterview);
		    }
		    
		    mMoments.add(allMoment);
    	}
    }

	 
    /**
	* finds all the sub moment for a moment given
	* @param the current moment.
	*/
    private static void lookingForSubMoments(MomentExperience moment, ArrayList<MomentExperience> lignee) {

    	for (int j = 0; j < moment.getSubMoments().size(); j++) {
    		
    		if(moment.getSubMoments().size()>maxNbMoment) {
				maxNbMoment=moment.getSubMoments().size();
			}
    		
    		MomentExperience subMoment = moment.getSubMoments().get(j);
    		lignee.add(subMoment);
    		
			if(subMoment.getSubMoments().size()>0) {
				lookingForSubMoments(subMoment, lignee);
			}
			
		}
    }
	
    public static ArrayList<MomentExperience> searchMomentName(String name) {
    	//System.out.println("STRRRRRRRRRRRRRRRRRRRRR");
    	ArrayList<MomentExperience> res = new ArrayList();
    	for(ArrayList<ArrayList<MomentExperience>> allMoments : MomentComparaison.getmMoments()) {
	   		  for(ArrayList<MomentExperience> moments : allMoments) {
	   			  for(MomentExperience moment : moments) {
	   				 if(moment.getName().equalsIgnoreCase(name)) {
	   					 //System.out.println(moment.getName()+moment.getID());
	   					 res.add(moment);
	   				 }
	           		  if(moment!=null) {
	           			  if(moment.getSubMoments().size()>0) {
	           				searchSubMomentName(moment, name, res);
	           			  } 
	           		  }
	           	  }  
	   		  }
	   	  }
    	return res;
    }
    
    public static void searchSubMomentName(MomentExperience moment, String name, ArrayList<MomentExperience> res) {
   	  	for(MomentExperience m : moment.getSubMoments()) {
   	  		if(moment.getName().equalsIgnoreCase(name)) {
   	  		 //System.out.println(moment.getName()+moment.getID());
   	  			//System.out.println(moment.getName());
   	  			res.add(moment);
   	  		}
   	  		if(m!=null) {
   	  			if(m.getSubMoments().size()>0) {
   	  				searchSubMomentName(m, name, res);
     			} 
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
	
	
    public static ArrayList<ArrayList<ArrayList<MomentExperience>>> getmMoments() {
		return mMoments;
	}
    
    /**
  	  * 
  	  * Init width of moment
  	  */
     public static void initLengthMoment(double size) {
	   	  for(ArrayList<ArrayList<MomentExperience>> allMoments : MomentComparaison.getmMoments()) {
	   		  for(ArrayList<MomentExperience> moments : allMoments) {
	   			  for(MomentExperience moment : moments) {
	   				  moment.setmWidth(size);
	           		  if(moment!=null) {
	           			  if(moment.getSubMoments().size()>0) {
	           				  initLengthSubMoment(moment, size);
	           			  } 
	           		  }
	           	  }  
	   		  }
	   	  }
     }
     
     /**
     * 
     * Init width of sub moment
     */
     public static void initLengthSubMoment(MomentExperience moment, double size) {
   	  	for(MomentExperience m : moment.getSubMoments()) {
   	  		moment.setmWidth(size);
   	  		if(m!=null) {
   	  			if(m.getSubMoments().size()>0) {
   	  				initLengthSubMoment(m, size);
     			} 
   	  		}
   	  	}
     }
	 
    
    public static double getmaxNbMoment() {
    	return maxNbMoment;
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
