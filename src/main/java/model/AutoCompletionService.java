package model;

import java.util.Set;
import java.util.TreeSet;

import SchemaTree.Cell.Models.IPropertyAdapter;

public class AutoCompletionService {
	
	private Project currentProject;
	private IPropertyAdapter property;
	private Category cat; 
	private MomentExperience moment;
	private Set<String> suggestedValuesProperties = new TreeSet<String>();
	private Set<String> suggestedMoments = new TreeSet<String>();

	
	public AutoCompletionService(Project pro, IPropertyAdapter p, Category cat){
		this.currentProject = pro;
		this.property = p ;
		this.cat = cat;
	}
	
	public AutoCompletionService(Project pro,MomentExperience m) {
		this.currentProject = pro;
		this.moment = m;
	}
	
	public IPropertyAdapter getProperty() {
		return property;
	}
	
	public MomentExperience getMoment() {
		return moment;
	}
	
	public Set<String> getSuggestedValues(Property p) {
		suggestPropValues(p);
		//this.suggestedValuesProperties.sort((o1,o2)-> o1.compareTo(o2));
		/*List<String> empNames= new ArrayList<String>();
		empNames=(List<String>) this.suggestedValuesProperties;
		Collections.sort(empNames);
		this.suggestedValuesProperties=(Set<String>) empNames;*/
		System.out.println("yoo : "+this.suggestedValuesProperties);
		return this.suggestedValuesProperties;
	}
	
	public Set<String> getSuggestedMoments(MomentExperience m) {
		suggestMoments(m);
		//this.suggestedMoments.sort((o1,o2)-> o1.compareTo(o2));
		return this.suggestedMoments;
	}
	
	/**
	 * puts all values of the same property in a list 
	 * @param p
	 * @return
	 */
	public Set<String> suggestPropValues(Property p) {

		for (DescriptionInterview di : this.currentProject.getInterviews()) {
			for(MomentExperience me :di.getMoments()) {
					for(Category c : me.getCategories()) { 
						if ((c.getName().equals(this.cat.getName()))) {
							for (IPropertyAdapter prop : c.getProperties()) {
								if(prop.getValue().equals(p.getName())) {
									if (!(suggestedValuesProperties.contains(prop.getValue())) && !prop.getValue().equals("____")){
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

	public void suggestPropSubValues(Set<String> suggestedValuesProperties2, MomentExperience m, Property p) {
		for(MomentExperience sm: m.getSubMoments()) {
			for(Category c : sm.getCategories()) { 
				if ((c.getName().equals(this.cat.getName()))) {
					for (IPropertyAdapter prop : c.getProperties()) {
						if(prop.getValue().equals(p.getName())) {
							if (!(suggestedValuesProperties.contains(prop.getValue())) && !prop.getValue().equals("____")){
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
	
	public Set<String> suggestMoments(MomentExperience m) {

		for (DescriptionInterview di : this.currentProject.getInterviews()) {
			for(MomentExperience me :di.getMoments()) {
				if(!(suggestedMoments.contains(me.getName())) && !me.getName().equals("____")) {
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


	public void suggestMomentNames(Set<String> suggestedList, MomentExperience m) {
		for(MomentExperience sm: m.getSubMoments()) {
			//System.out.println(sm.getName());
			
			if(!(suggestedMoments.contains(sm.getName())) && !sm.getName().equals("____")) {
				suggestedMoments.add(sm.getName());
			}
			
			if(sm.getSubMoments().size()>0) {
				System.out.println(sm.getName());
				suggestMomentNames(suggestedMoments,sm);
			}
		}
	}

	

}