package model;

import java.util.ArrayList;

public class MomentID {

	private static ArrayList<Integer> idUsed = new ArrayList<Integer>();
	
	public static int generateID() {
		idUsed.add(idUsed.size()+1);
		return idUsed.size();
	}
}
