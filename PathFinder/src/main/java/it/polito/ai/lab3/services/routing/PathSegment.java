package it.polito.ai.lab3.services.routing;

import java.util.List;

import it.polito.ai.lab3.repo.entities.BusStop;

public interface PathSegment {
	
	public BusStop getSource();
	
	public BusStop getDestination();
	
	public String getLine();
	
	// Lista dei bus stop in cui non è prevista la discesa
	public List<BusStop> getIntermediateStops();

}

