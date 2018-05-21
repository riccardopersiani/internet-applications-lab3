package it.polito.ai.lab3.repo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.vividsolutions.jts.geom.Geometry;

@Entity
public class BusStopGeographic {
	
	@Id
	private String id;
	private String name;
	@Column(columnDefinition = "geography(Point,4326)")
	private Geometry location;
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public Geometry getLocation() {
		return location;
	}
}
