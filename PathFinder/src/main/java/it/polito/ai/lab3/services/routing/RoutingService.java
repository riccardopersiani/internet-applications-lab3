package it.polito.ai.lab3.services.routing;

public interface RoutingService {

	/**
	 * Computes the path that connects the source and destination points.
	 * 
	 * @param srcLat - The latitude of the start point
	 * @param srcLng - The longitude of the start point
	 * @param dstLat - The latitude of the destination point
	 * @param dstLng - The longitude of the destination point
	 * @returns a Path object
	 */
	public Path getPath(String srcLat, String srcLng, String dstLat, String dstLng);
}
