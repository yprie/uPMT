package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import utils.IStats;

public class AutoCompletionService {
	
	private Project currentProject;
	private Property property;
	private Category cat; 
	private MomentExperience moment;
	private LinkedList<String> suggestedValuesProperties = new LinkedList<String>();
	private LinkedList<String> suggestedMoments = new LinkedList<String>();

	
	public AutoCompletionService(Project pro,Property p, Category cat){
		this.currentProject = pro;
		this.property = p ;
		this.cat = cat;
	}
	
	public AutoCompletionService(Project pro,MomentExperience m) {
		this.currentProject = pro;
		this.moment = m;
	}
	
	public Property getProperty() {
		return property;
	}
	
	public MomentExperience getMoment() {
		return moment;
	}
	
	public LinkedList<String> getSuggestedValues(Property p) {
		suggestPropValues(p);
		//this.suggestedValuesProperties.sort((o1,o2)-> o1.compareTo(o2));
		return this.suggestedValuesProperties;
	}
	
	public LinkedList<String> getSuggestedMoments(MomentExperience m) {
		suggestMoments(m);
		//this.suggestedMoments.sort((o1,o2)-> o1.compareTo(o2));
		return this.suggestedMoments ;
	}
	
	/**
	 * puts all values of the same property in a list 
	 * @param p
	 * @return
	 */
	public LinkedList<String> suggestPropValues(Property p) {

		for (DescriptionInterview di : this.currentProject.getInterviews()) {
			for(MomentExperience me :di.getMoments()) {
					for(Category c : me.getCategories()) { 
						if ((c.getName().equals(this.cat.getName()))) {
							for (Property prop : c.getProperties()) {
								if(prop.getName().equals(p.getName())) {
									if (!(suggestedValuesProperties.contains(prop.getValue()))){
										suggestedValuesProperties.add(prop.getValue());
									}
									
								}
							}
						}	
				
				}
					if(me.getSubMoments().size()>0) {
						suggestPropSubValues(suggestedValuesProperties,me, p);
						
					}
			}
		}
		suggestedValuesProperties.remove(p.getValue());
		return suggestedValuesProperties;	
	}

	public void suggestPropSubValues(LinkedList<String> suggestedList, MomentExperience m, Property p) {
		for(MomentExperience sm: m.getSubMoments()) {
			for(Category c : sm.getCategories()) { 
				if ((c.getName().equals(this.cat.getName()))) {
					for (Property prop : c.getProperties()) {
						if(prop.getName().equals(p.getName())) {
							if (!(suggestedValuesProperties.contains(prop.getValue()))){
								suggestedValuesProperties.add(prop.getValue());
							}
							
						}
					}
				}	
			}
			if(sm.getSubMoments().size()>0) {
				suggestPropSubValues(suggestedValuesProperties,sm, p);
			}
		}
	}
	
	public LinkedList<String> suggestMoments(MomentExperience m) {

		for (DescriptionInterview di : this.currentProject.getInterviews()) {
			for(MomentExperience me :di.getMoments()) {
				if(!(suggestedMoments.contains(me.getName()))) {
					suggestedMoments.add(me.getName());
				}
				if(me.getSubMoments().size()>0) {
					suggestMomentNames(suggestedMoments,me);
					
				}
			}
		}
		suggestedMoments.remove(m.getName());
		return suggestedMoments;	
	}


	public void suggestMomentNames(LinkedList<String> suggestedList, MomentExperience m) {
		for(MomentExperience sm: m.getSubMoments()) {
			System.out.println(sm.getName());
			suggestedMoments.add(sm.getName());
			
			if(sm.getSubMoments().size()>0) {
				System.out.println(sm.getName());
				suggestMomentNames(suggestedMoments,sm);
			}
		}
	}

	

}