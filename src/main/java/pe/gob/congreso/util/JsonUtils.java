package pe.gob.congreso.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pe.gob.congreso.model.Deriva;

public final class JsonUtils {
	
	private JsonUtils() {
	}

	public static boolean isJsonValid(String jsonInString) {
		try {
			Gson gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
			Object response = gson.fromJson(jsonInString, Object.class);
			return true;
		} catch (com.google.gson.JsonSyntaxException ex) {
			return false;
		}
	}
	
	public static boolean isJsonValidDeriva(String jsonInString) {
		try {
			Gson gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
			Deriva response = gson.fromJson(jsonInString, Deriva.class);
			return true;
		} catch (com.google.gson.JsonSyntaxException ex) {
			return false;
		}
	}

	public static SignIn parseJsonSignIn(String jsonInSignIn) {
		try {
			Gson g = new Gson();
			return g.fromJson(jsonInSignIn, SignIn.class);
		} catch (com.google.gson.JsonSyntaxException ex) {
			return new SignIn();
		}
	}
	
}