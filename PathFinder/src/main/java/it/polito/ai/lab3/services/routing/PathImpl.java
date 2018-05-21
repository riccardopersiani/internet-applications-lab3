package it.polito.ai.lab3.services.routing;

import java.util.List;

public class PathImpl implements Path {
	private Point source;
	private Point destination;
	private List<PathSegment> pathSegments;
	
	
	public PathImpl(Point source, Point destination, List<PathSegment> segments) {
		this.source = source;
		this.destination = destination;
		pathSegments = segments;
	}
	
	public Point getSource() {
		return source;
	}

	public Point getDestination() {
		return destination;
	}

	public List<PathSegment> getPathSegments() {
		return pathSegments;
	}
}
