package it.polito.ai.lab3.objects;

public class BusStopGeographic {
	
	private String id;
	private String name;
	private double latitude;
	private double longitude;
	
	public BusStopGeographic(String id, String name, double latitude, double longitude) {
		this.id = id;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public double getLat() {
		return latitude;
	}
	
	public double getLng() {
		return longitude;
	}
	
}
