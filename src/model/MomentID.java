package model;

import java.util.ArrayList;
import java.util.Random;

public class MomentID {

	private static ArrayList<Integer> idUsed = new ArrayList<Integer>();
	private static Random rand = new Random();
	static int generateID() {
		int id = rand.nextInt(9999999) + 1;
		while(idUsed.contains(id))
			id = rand.nextInt(9999999) + 1;
		idUsed.add(id);
		return id;
	}
}
