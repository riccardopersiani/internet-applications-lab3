package it.polito.ai.lab3.mongoClasses;

import java.util.*;

/**
 * Questa classe non rappresenta l'arco di un grafo, ma una tratta di un
 * percorso.
 */
public class Edge {

	/**
	 * Id della fermata (BusStop) di partenza
	 */
	private String idSource;

	/**
	 * Id della fermata (BusStop) di arrivo
	 */
	private String idDestination;

	private String lineId;

	/**
	 * Modo di spostamento 0 -> bus 1 -> a piedi
	 */
	private boolean mode;

	/**
	 * Costo dell'arco
	 */
	private int cost;

	/**
	 * a set of bus stopId belonging to the edge, to represent better on the map
	 * the edge (not as a single line)
	 */
	private List<String> stopsId;

	public Edge(String idSource, String idDestination, String lineId, boolean mode, int cost) {
		this.idSource = idSource;
		this.idDestination = idDestination;
		this.lineId = lineId;
		this.mode = mode;
		this.cost = cost;
		this.stopsId = new ArrayList<String>();
	}

	public String getIdSource() {
		return idSource;
	}

	public void setIdSource(String idSource) {
		this.idSource = idSource;
	}

	public String getIdDestination() {
		return idDestination;
	}

	public void setIdDestination(String idDestination) {
		this.idDestination = idDestination;
	}

	public boolean isMode() {
		return mode;
	}

	public void setMode(boolean mode) {
		this.mode = mode;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public String getLineId() {
		return lineId;
	}
	
	public void setStopsId(List<String> stopsId) {
		this.stopsId = stopsId;
	}
	
	public List<String> getStopsId() {
		return stopsId;
	}
}
