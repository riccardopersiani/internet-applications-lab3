package it.polito.ai.lab3;

import java.util.*;

import it.polito.ai.lab3.model.Graph;
import it.polito.ai.lab3.mongoClasses.*;

public class Dijsktra {
	private Graph graph;

	public Dijsktra(Graph graph) {
		this.graph = graph;
	}

	public Map<String, MinPath> shortestPath(String sourceId) {

		// key identifies the destination node
		Map<String, Edge> previousEdge = new HashMap<String, Edge>();

		Map<String, Integer> distance = new HashMap<String, Integer>();

		Set<String> openSet = new TreeSet<String>();
		for (String vertex : graph.getNodes()) {
			if (sourceId.equals(vertex))
				distance.put(vertex, 0);
			else {
				distance.put(vertex, Integer.MAX_VALUE);
				openSet.add(vertex);
			}
		}

		// initialize the adjacencies of the source (distances of the directly
		// linked nodes)
		for (Edge edge : graph.getEdges(sourceId)) {
			if (distance.get(edge.getIdDestination()) > edge.getCost()) {
				// check the initialization because multiple edges with same src
				// and dst are possible in case the stops are in the range of
				// 250m and also belong to the same bus line
				distance.put(edge.getIdDestination(), edge.getCost());
				previousEdge.put(edge.getIdDestination(), edge);
			}
		}

		while (!openSet.isEmpty()) {

			String min = sourceId;
			int minDis = Integer.MAX_VALUE;

			// find the nearest vertex
			for (String vertex : openSet) {
				int tmp = distance.get(vertex);
				if (minDis > tmp) {
					minDis = tmp;
					min = vertex;
				}
			}
			if (minDis == Integer.MAX_VALUE) {
				// other vertexes are not linked
				break;
			}

			openSet.remove(min);

			// iterate on the neighbors to recompute distances
			for (Edge edge : graph.getEdges(min)) {

				int newPath = distance.get(min) + edge.getCost();
				if (distance.get(edge.getIdDestination()) > newPath) {
					distance.put(edge.getIdDestination(), newPath);

					// relaxation
					previousEdge.put(edge.getIdDestination(), edge);
				}
			}

		}
		Map<String, MinPath> result = new HashMap<String, MinPath>();
		// reconstruct the list<Edge> from the previousEdge
		for (String destinationId : previousEdge.keySet()) {
			MinPath minPath = new MinPath(sourceId, destinationId, new LinkedList<Edge>());
			String tmp = destinationId;
			int totalCost = 0;
			// go backward until reaching the sourceId
			while (!tmp.equals(sourceId)) {
				Edge previous = previousEdge.get(tmp);
				minPath.getEdges().add(0, previous);
				tmp = previous.getIdSource();
				totalCost += previous.getCost();
			}
			minPath.setTotalCost(totalCost);
			result.put(destinationId, minPath);
		}

		return result;

	}

}
