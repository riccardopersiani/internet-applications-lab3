package it.polito.ai.lab3.repo.entities;

import java.util.*;

import javax.persistence.*;

@Entity
public class BusLine {

	@Id
	private String line;
	private String description;
	@ManyToMany
	@JoinTable(name = "BusLineStop",
		joinColumns = { @JoinColumn(name = "lineId") },
		inverseJoinColumns = {@JoinColumn(name = "stopId")
	})
	private List<BusStop> stops = new ArrayList<BusStop>();

	public String getLine() {
		return line;
	}

	public String getDescription() {
		return description;
	}
	
	public List<BusStop> getStops() {
		return stops;
	}

}
