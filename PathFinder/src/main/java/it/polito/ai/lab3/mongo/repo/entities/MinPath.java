package it.polito.ai.lab3.mongo.repo.entities;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "min_paths")
public class MinPath {
	@Id
	private Key id;
	private String idSource;
	private String idDestination;
	private List<Edge> edges;
	private int totalCost;
	
	
	public Key getId() {
		return id;
	}
	public void setId(Key id) {
		this.id = id;
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
