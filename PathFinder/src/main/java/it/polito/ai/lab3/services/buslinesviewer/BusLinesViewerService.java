package it.polito.ai.lab3.services.buslinesviewer;

import java.util.List;

import it.polito.ai.lab3.repo.entities.BusLine;

/**
 * Service interface for retrieving the list of all
 * available transport lines, i.e. bus, tram, metro.
 * 
 * @author alessio
 *
 */
public interface BusLinesViewerService {

	/**
	 * Returns all the lines available in the transport system.
	 * 
	 * @return a list of BusLine objects
	 */
	public List<BusLine> getAllLines();
	
	/**
	 * Returns all the information of the requested line.
	 * 
	 * @param lineId - The ID of the line
	 * @return a BusLine objects
	 */
	public BusLine getLineById(String lineId);
}
