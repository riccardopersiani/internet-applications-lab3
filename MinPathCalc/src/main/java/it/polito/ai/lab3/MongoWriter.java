package it.polito.ai.lab3;

import java.util.*;

import org.bson.Document;

import com.mongodb.*;
import com.mongodb.client.*;

import it.polito.ai.lab3.mongoClasses.Edge;
import it.polito.ai.lab3.mongoClasses.MinPath;

public class MongoWriter {

	private MongoClient mongoClient;
	private MongoCollection<Document> minPathsCollection;

	/**
	 * sets up connection to mongoDB, DELETING EVERYTHING in the min_paths
	 * collection
	 */
	public MongoWriter() {
		mongoClient = new MongoClient("localhost");
		MongoDatabase database = mongoClient.getDatabase("ai");
		minPathsCollection = database.getCollection("min_paths");
		// clear before starting
		minPathsCollection.drop();
	}

	public void addMinPath(MinPath minPath) {
		List<Document> edges = new ArrayList<Document>();

		for (Edge edge : minPath.getEdges()) {
			/*List<String> stopsId = new ArrayList<String>();
			for (String stopId : edge.getStopsId()) {
				stopsId.add(stopId);
			}*/
			edges.add(new Document("idSource", edge.getIdSource()).append("idDestination", edge.getIdDestination())
					.append("mode", edge.isMode()).append("cost", edge.getCost())
					// also store the lineId useful for displaying solution
					.append("lineId", edge.getLineId())
					.append("stopsId", edge.getStopsId()));
		}

		Document document = new Document("idSource", minPath.getIdSource())
				.append("idDestination", minPath.getIdDestination())
				.append("edges", edges)
				.append("totalCost", minPath.getTotalCost())
				// enable faster lookup by defining the primary key as
				// combination of src and dst ids
				.append("_id", new Document("src", minPath.getIdSource()).append("dst", minPath.getIdDestination()));

		minPathsCollection.insertOne(document);
	}

	public void close() {
		mongoClient.close();
	}
}
