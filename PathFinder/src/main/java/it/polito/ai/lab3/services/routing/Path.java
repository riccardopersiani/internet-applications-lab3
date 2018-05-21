package it.polito.ai.lab3.services.routing;

import java.util.List;


public interface Path {
	
	/**
	 * The user-defined source point clicked on the map
	 * @return
	 */
	public Point getSource();
	
	/**
	 * The user-defined destination point clicked on the map
	 * @return
	 */
	public Point getDestination();
	
	/**
	 * Returns the list of all the segments that compose the whole path.
	 * Each segment represents a path that is traveled either on foot or by bus/metro.
	 * 
	 * @return list of PathSegment object
	 */
	public List<PathSegment> getPathSegments();
}
