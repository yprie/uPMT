package utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import model.DescriptionInterview;
import model.MomentExperience;

public class MomentID {

	private static ArrayList<Integer> idUsed = new ArrayList<Integer>();
	private static Random rand = new Random();
	
	public static void initID(LinkedList<MomentExperience> list) {
		idUsed.clear();
		for(MomentExperience moment : list) {
			idUsed.add(moment.getID());
			if(moment.getSubMoments().size()!=0) initID(moment.getSubMoments());
		}
	}
	
	public static int generateID() {
		int id = rand.nextInt(9999999) + 1;
		while(idUsed.contains(id))
			id = rand.nextInt(9999999) + 1;
		idUsed.add(id);
		return id;
	}
}
