package it.polito.ai.lab3.web.controllers;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import it.polito.ai.lab3.repo.entities.BusLine;
import it.polito.ai.lab3.repo.entities.BusStop;
import it.polito.ai.lab3.services.buslinesviewer.BusLinesViewerService;

@Controller
@RequestMapping("/showbusLineStops")
public class MapPageController {
	@Autowired
	private BusLinesViewerService busLinesViewerService;
	
	
	@RequestMapping(method = RequestMethod.GET)
	public String showBusStopsOnMap(@RequestParam("lineId") String lineId, ModelMap model){
		BusLine busLine = busLinesViewerService.getLineById(lineId);
		
		// Check if the requested line exists
		if (busLine == null) {
			return "redirect:busLinesList";
		}
			
		List<BusStop> busStopsList = busLine.getStops();
		
		// Conversion of stops and path to GeoJson
		JSONObject geoJsonStops = busStopsToGeoJson(busStopsList);
		JSONArray geoJsonPath = busLinePathToGeoJson(busStopsList);
		
		// Set the GeoJson strings to the model
		model.addAttribute("lineName", busLine.getLine());
		model.addAttribute("busStops", geoJsonStops);
		model.addAttribute("busPath", geoJsonPath);
		
		return "showbusLineStops";
	}
	
	private JSONObject busStopsToGeoJson(List<BusStop> busStopList) {
		JSONObject root = new JSONObject();
		root.put("type", "FeatureCollection");
		
		JSONArray features = new JSONArray();
		for (BusStop stop : busStopList) {
			// Create coordinate object and set latitude and longitude
			JSONArray coordinates = new JSONArray();
			coordinates.put(stop.getLongitude());
			coordinates.put(stop.getLatitude());
			
			// Create and fill up the geometry object with type and coordinates
			JSONObject geometry = new JSONObject();
			geometry.put("type", "Point");
			geometry.put("coordinates", coordinates);
			
			// Add the list of lines
			JSONArray lines = new JSONArray();
			for (BusLine bl: stop.getLines()){
				lines.put(bl.getLine());
			}
			
			// Create and fill up the properties object
			JSONObject properties = new JSONObject();
			properties.put("busStopId", stop.getId());
			properties.put("busStopName", stop.getName());
			properties.put("lines", lines);
			
			// Create and fill up the bus stop object with type, geometry, id, properties and lines
			JSONObject feature = new JSONObject();
			feature.put("type", "Feature");
			feature.put("geometry", geometry);
			feature.put("properties", properties);
			
			// Add the single feature to the feature collection
			features.put(feature);
		}
		
		// Add every feature to the root
		root.put("features", features);
		return root;
	}

	public JSONArray busLinePathToGeoJson(List<BusStop> busStopList) {
		JSONArray root = new JSONArray();
		
		JSONArray coordinates = new JSONArray();
		for (BusStop stop : busStopList) {
			// Create coordinates array and fill in with the list of bust stops
			JSONArray latLong = new JSONArray();
			latLong.put(stop.getLongitude());
			latLong.put(stop.getLatitude());
			
			// Add the line segment to the coordinates array
			coordinates.put(latLong);
		}
		
		JSONObject geometry = new JSONObject();
		geometry.put("type", "LineString");
		geometry.put("coordinates", coordinates);
		
		root.put(geometry);
		return root;
	}
}
