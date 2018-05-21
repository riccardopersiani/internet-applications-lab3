package it.polito.ai.lab3.mongoClasses;

import java.util.*;

/**
 * Classe che rappresenta i percorsi a costo minimo da salvare all'interno di MongoDB.
 * */
public class MinPath {

	/**
	 * Id della fermata (BusStop) di partenza dell'intero percorso
	 * */
	private String idSource;
	
	/**
	 * Id della fermata (BusStop) di arrivo dell'intero percorso
	 * */
	private String idDestination;
	
	/**
	 * Lista di tratte che compongono il percorso
	 * */
	private List<Edge> edges;
	
	/**
	 * Costo totale dell'intero percorso.
	 * La somma dei costi delle singole tratte deve essere uguale 
	 * a questo valore
	 * */
	private int totalCost;
	
	public MinPath(String idSource, String idDestination, List<Edge> edges) {
		this.idSource = idSource;
		this.idDestination = idDestination;
		this.edges = edges;
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
	public List<Edge> getEdges() {
		return edges;
	}
	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}
	public int getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(int totalCost) {
		this.totalCost = totalCost;
	}
}