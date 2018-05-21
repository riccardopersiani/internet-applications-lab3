package it.polito.ai.lab3;

import java.sql.Connection;
import java.util.*;

import com.mongodb.MongoWriteException;

import it.polito.ai.lab3.model.*;
import it.polito.ai.lab3.mongoClasses.*;

public class Main {

	public static void main(String[] args) {

		printTime("starting");

		final DbReader dbReader = new DbReader();
		Graph graph = new Graph();

		System.out.println("Reading stops from database..");
		Set<Node> busStops = dbReader.getBusStops();
		Map<String, Map<String, List<Integer>>> busLinesStops = dbReader.getBusLinesStops();

		System.out.println("Reading edges from database (should take around 45 minute)...");
		busStops.parallelStream().forEach(node -> {
			Connection connection = dbReader.getConnection();
			Set<Edge> byBus = dbReader.getReachableStopsByBus(connection, node);
			Set<Edge> byWalk = dbReader.getReachableStopsByWalk(connection, node);

			for (Edge edge : byBus) {
				Map<String, List<Integer>> busLineStops = busLinesStops.get(edge.getLineId());
				// search the interesting sub-sequences
				List<Integer> sourceStopSequences = busLineStops.get(edge.getIdSource());
				List<Integer> destinationStopSequences = busLineStops.get(edge.getIdDestination());
				double minCost = Double.MAX_VALUE;
				int bestSrcSeqNumber = -1;
				int bestDstSeqNumber = -1;
				for (int sourceSequenceNumber : sourceStopSequences) {
					OptionalInt destination = destinationStopSequences.stream().mapToInt(a -> a).sorted()
							.filter(a -> a > sourceSequenceNumber).findFirst();
					if (destination.isPresent()) {
						int destinationSequenceNumber = destination.getAsInt();
						// get the cost of this candidate sub-sequence
						double cost = dbReader.getSequenceCost(connection, edge.getLineId(), sourceSequenceNumber,
								destinationSequenceNumber);
						if (cost < minCost) {
							// this is the shortest
							minCost = cost;
							bestSrcSeqNumber = sourceSequenceNumber;
							bestDstSeqNumber = destinationSequenceNumber;
						}
					}
				}
				edge.setCost((int) minCost);
				// get the list of intermediate stops for a better representation of the edge
				List<String> intermediateStops = dbReader.getBusLineStopsIdBetween(connection, edge.getLineId(), bestSrcSeqNumber, bestDstSeqNumber);
				edge.setStopsId(intermediateStops);
			}

			dbReader.closeConnection(connection);

			graph.addNode(node);
			graph.addEdges(node.getId(), byBus);
			graph.addEdges(node.getId(), byWalk);
		});

		dbReader.close();
		printTime("done with postgis");

		System.out.println("Numero di nodi: " + graph.myNodes.size());
		System.out.println("numero di archi: " + graph.myAdjList.values().stream().flatMap(value -> value.stream()).count());

		Dijsktra dijkstra = new Dijsktra(graph);

		System.out.println("Going to run Dijkstra from each node (should take around 1 hour)...");

		MongoWriter mongoWriter = new MongoWriter();

		// work with the data collected
		graph.getNodes()
				// get a parallel stream to improve performances (automatic
				// usage of threads)
				.parallelStream()
				// for each node
				.map(node -> {
					// evaluate the minimum path for every stop starting from
					// this node
					return dijkstra.shortestPath(node);
				})
				// map from a Stream<Map<String,MinPath>> to a Stream<MinPath>
				.flatMap(map -> map.values().stream()).forEach(minPath -> {
					// store the path
					try {
						mongoWriter.addMinPath(minPath);
					} catch (MongoWriteException e) {
						System.err.println("An unwanted exception occurred with the path between " + minPath.getIdSource() + " and " + minPath.getIdDestination());
					}
				});

		mongoWriter.close();

		printTime("done");

		System.out.println("Done");
	}

	static void printTime(String msg) {
		System.out.println(new Date() + "\t" + msg);
	}

}
