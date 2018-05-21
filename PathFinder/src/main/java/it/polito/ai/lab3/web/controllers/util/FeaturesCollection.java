package it.polito.ai.lab3.web.controllers.util;

import org.json.JSONArray;
import org.json.JSONObject;

public class FeaturesCollection {
	private JSONObject featuresCollection = new JSONObject();
	private JSONArray features = new JSONArray();
	
	
	public FeaturesCollection() {
		featuresCollection.put("type", "FeatureCollection");
		featuresCollection.put("features", features);
	}
	
	public static FeaturesCollection newFeaturesCollection() {
		return new FeaturesCollection();
	}
	
	public void addFeature(JSONObject feature) {
		features.put(feature);
	}
	
	public String getGeoJson() {
		return featuresCollection.toString(4);
	}
}
