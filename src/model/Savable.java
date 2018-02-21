package model;

import java.util.HashMap;

public abstract class Savable {
		

		protected static final String NAME = "name";
		protected static final String SCHEMA = "schema";
		protected static final String TYPES = "types";
		protected static final String DESCRIPTION = "description";
		protected static final String COLOR = "couleur";
		protected static final String INTERVIEWS = "interviews";
		
		public abstract HashMap<String, Object> saveData();
		public abstract void loadData(HashMap<String, Object> data);
}
