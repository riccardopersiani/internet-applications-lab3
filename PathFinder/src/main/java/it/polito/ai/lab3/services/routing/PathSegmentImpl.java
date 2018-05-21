package it.polito.ai.lab3.services.routing;

import java.util.List;

import it.polito.ai.lab3.repo.entities.BusStop;

public class PathSegmentImpl implements PathSegment {
	private BusStop source;
	private BusStop destination;
	private String line;
	private List<BusStop> intermediateStops;
	
	
	public PathSegmentImpl(BusStop source, BusStop destination, String line, List<BusStop> intermediateStops) {
		this.source = source;
		this.destination = destination;
		this.line = line;
		this.intermediateStops = intermediateStops;
	}

	public BusStop getSource() {
		return source;
	}

	public BusStop getDestination() {
		return destination;
	}

	public String getLine() {
		return line;
	}

	public List<BusStop> getIntermediateStops() {
		return intermediateStops;
	}
}
