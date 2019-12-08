package utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public abstract class UpdateVersions {

	
	/**
	 * ################################################################################
	 * 
	 * VERSION 2 TO 3
	 * 
	 * ################################################################################
	 */
	
	public static String version2To3(String json) {
		String ret = json;
		try {
			//On convertit le fichier en Objet Json
			JsonParser parser = new JsonParser();
			JsonObject rootObj = parser.parse(json).getAsJsonObject();
			
			//Pour chaque interview on regarde chaque moment pour convertir également chaque type
			JsonArray interviews = rootObj.getAsJsonArray("mInterviews");
			for(JsonElement i : interviews) {
				JsonArray moments = i.getAsJsonObject().getAsJsonArray("mMoments");
				for(JsonElement moment : moments) {
					moment = UpdateVersions.version3Moment(moment.getAsJsonObject());
					JsonArray categories = moment.getAsJsonObject().getAsJsonArray("mCategories");
					for(JsonElement category : categories) {
						JsonArray properties = category.getAsJsonObject().getAsJsonArray("mProperties");
						for(JsonElement property : properties) {
							property = UpdateVersions.version3Properties(property.getAsJsonObject());
						}
					}
				}
			}
			ret = rootObj.toString();
			
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	
	protected static JsonElement version3Moment(JsonObject moment) {
		System.out.println("Pour le moment: "+moment.get("mName"));
		JsonArray descrArray = new JsonArray();
		JsonPrimitive descrValue = moment.getAsJsonPrimitive("mDescripteme");
		if(descrValue.getAsString()!=null)
			if(descrValue.getAsString()!="") {
				JsonObject jo = new JsonObject();
				jo.add("mText", descrValue);
				descrArray.add(jo);
			}
				
		moment.add("mDescriptemes", descrArray);
		moment.remove("mDescripteme");
		moment.remove("mCurrentProperty");
		JsonArray moments = moment.getAsJsonArray("mSubMoments");
		for(JsonElement sub : moments) {
			sub = UpdateVersions.version3Moment(sub.getAsJsonObject());
			JsonArray categories = sub.getAsJsonObject().getAsJsonArray("mCategories");
			for(JsonElement category : categories) {
				JsonArray properties = category.getAsJsonObject().getAsJsonArray("mProperties");
				for(JsonElement property : properties) {
					property = UpdateVersions.version3Properties(property.getAsJsonObject());
				}
			}
		}
		
		return moment;
	}
	
	
	protected static JsonElement version3Properties(JsonObject property) {
		JsonArray descrArray = new JsonArray();
		JsonObject descr = new JsonObject();
		descr.add("mText", property.getAsJsonObject("mExtract").getAsJsonPrimitive("mText"));
		descrArray.add(descr);
		property.remove("mExtract");
		property.add("mDescriptemes", descrArray);
		return property;
	}
	
	
	
	
	

	/**
	 * ################################################################################
	 * 
	 * VERSION 1 TO 2
	 * 
	 * ################################################################################
	 */
	
	public static String version1To2(String json) {
		String ret = json;
		try {
			//On convertit le fichier en Objet Json
			JsonParser parser = new JsonParser();
			JsonObject rootObj = parser.parse(json).getAsJsonObject();
			//On récupère le schéma pour convertir les anciens type en nouveau
			JsonObject schema = rootObj.getAsJsonObject("mSchema");
			/*rootObj.remove("SAVE_VERSION");
			rootObj.addProperty("SAVE_VERSION", 2);*/
			//On change mTypes en mFolders
			schema.add("mFolders", schema.get("mTypes"));
			schema.remove("mTypes");
			//Pour chaque dossier du schema on applique la conversion
			JsonArray folders = schema.getAsJsonArray("mFolders");
			for(JsonElement f : folders) {
				f = UpdateVersions.version2Folder(f.getAsJsonObject());
			}
			
			//Pour chaque interview on regarde chaque moment pour convertir également chaque type
			JsonArray interviews = rootObj.getAsJsonArray("mInterviews");
			for(JsonElement i : interviews) {
				JsonArray moments = i.getAsJsonObject().getAsJsonArray("mMoments");
				for(JsonElement moment : moments) {
					moment = UpdateVersions.version2Moment(moment.getAsJsonObject());
				}
			}
			
			
			ret = rootObj.toString();
			
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	protected static JsonElement version2Moment(JsonObject moment) {
		System.out.println("Pour le moment: "+moment.get("mName"));
		JsonArray types = moment.getAsJsonArray("mTypes");
		System.out.println("Les types: "+types);
		JsonArray cat = new JsonArray();
		for(JsonElement type : types) {
			JsonObject o = type.getAsJsonObject();
			JsonObject c = o.get("data").getAsJsonObject();
			//On convertit aussi les propriétés de la catégorie
			JsonArray prop = new JsonArray();
			for(JsonElement p : c.get("mTypes").getAsJsonArray()) {
				prop.add(p.getAsJsonObject().get("data"));
			}
			c.add("mProperties", prop);
			cat.add(c);
		}
		moment.add("mCategories", cat);
		moment.remove("mTypes");
		JsonArray moments = moment.getAsJsonArray("mSubMoments");
		for(JsonElement sub : moments) {
			sub = UpdateVersions.version2Moment(sub.getAsJsonObject());
		}
		
		return moment;
	}
	
	
	protected static JsonElement version2Folder(JsonObject folder) {
		//On recupère les données du dossier pour le mettre au bon endroit
		folder.remove("type");
		folder.add("tmp", folder.get("data").getAsJsonObject().get("mTypes"));
		folder.add("mName", folder.get("data").getAsJsonObject().get("mName"));
		folder.add("mDescription", folder.get("data").getAsJsonObject().get("mDescription"));
		folder.add("mColor", folder.get("data").getAsJsonObject().get("mColor"));
		folder.remove("data");
		//On regarde tous les types pour séparer les catégories des sous dossiers
		JsonArray types = folder.getAsJsonArray("tmp");
		JsonArray cat = new JsonArray();
		JsonArray fold = new JsonArray();
		for(JsonElement type : types) {
			JsonObject o = type.getAsJsonObject();
			//Si c'est une catégorie
			if(o.get("type").getAsString().equals("model.Category")) {
				JsonObject c = o.get("data").getAsJsonObject();
				//On convertit aussi les propriétés de la catégorie
				JsonArray prop = new JsonArray();
				for(JsonElement p : c.get("mTypes").getAsJsonArray()) {
					prop.add(p.getAsJsonObject().get("data"));
				}
				c.add("mProperties", prop);
				cat.add(c);
			}
			//Si c'est un dossier
			else {
				//On convertit le dossier
				fold.add(UpdateVersions.version2Folder(o.getAsJsonObject()));
			}
		}
		folder.add("mCategories", cat);
		folder.add("mFolders", fold);
		folder.remove("tmp");
		
		return folder;
	}
	
	
	
	
	
	
	/**
	 * ################################################################################
	 * 
	 * VERSION 0 TO 1
	 * 
	 * ################################################################################
	 */
	
	public static String version0To1(String json) {
		String ret = json;
		//Renommage des classes
		ret = ret.replaceAll("model.Dossier", "model.Folder");
		ret = ret.replaceAll("model.Classe", "model.Category");
		ret = ret.replaceAll("model.Propriete", "model.Property");
		
		//Project
		ret = ret.replaceAll("\"nom\"", "\"mName\"");
		ret = ret.replaceAll("\"schemaProjet\"", "\"mSchema\"");
		ret = ret.replaceAll("\"entretiens\"", "\"mInterviews\"");
		
		//DescriptionInterview
		ret = ret.replaceAll("\"descripteme\"", "\"mDescripteme\"");
		ret = ret.replaceAll("\"moments\"", "\"mMoments\"");
		ret = ret.replaceAll("\"dateEntretien\"", "\"mDateInterview\"");
		ret = ret.replaceAll("\"participant\"", "\"mParticipant\"");
		ret = ret.replaceAll("\"commentaire\"", "\"mComment\"");
		
		//MomentExperience
		ret = ret.replaceAll("\"date\"", "\"mDate\"");
		ret = ret.replaceAll("\"types\"", "\"mTypes\"");
		ret = ret.replaceAll("\"enregistrements\"", "\"mRecords\"");
		ret = ret.replaceAll("\"sousMoments\"", "\"mSubMoments\"");
		ret = ret.replaceAll("\"parentMomentID\"", "\"mParentMomentID\"");
		ret = ret.replaceAll("\"parentCol\"", "\"mParentCol\"");
		ret = ret.replaceAll("\"gridCol\"", "\"mGridCol\"");
		ret = ret.replaceAll("\"couleur\"", "\"mColor\"");
		ret = ret.replaceAll("\"id\"", "\"mID\"");
		ret = ret.replaceAll("\"row\"", "\"mRow\"");
		ret = ret.replaceAll("\"currentProperty\"", "\"mCurrentProperty\"");
		ret = ret.replaceAll("\"morceauDescripteme\"", "\"mDescripteme\"");
		
		//Type
		ret = ret.replaceAll("\"description\"", "\"mDescription\"");
		ret = ret.replaceAll("\"valeur\"", "\"mValue\"");
		ret = ret.replaceAll("\"extract\"", "\"mExtract\"");
		
		//Descripteme
		ret = ret.replaceAll("\"texte\"", "\"mText\"");
		
		//Record
		ret = ret.replaceAll("\"link\"", "\"mLink\"");
		
		//System.out.println(ret);
		return ret;
	}
	
}
