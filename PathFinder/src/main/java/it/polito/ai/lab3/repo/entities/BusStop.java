package it.polito.ai.lab3.repo.entities;

import java.util.*;

import javax.persistence.*;

@Entity
public class BusStop {
	
	@Id
	private String id;
	private String name;
	@Column(name="lat")
	private double latitude;
	@Column(name="lng")
	private double longitude;
	@ManyToMany(mappedBy="stops")
	private Set<BusLine> lines = new HashSet<BusLine>();
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public Set<BusLine> getLines() {
		return lines;
	}

}

