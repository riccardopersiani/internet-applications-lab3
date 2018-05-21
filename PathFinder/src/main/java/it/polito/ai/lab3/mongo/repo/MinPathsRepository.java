package it.polito.ai.lab3.mongo.repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import it.polito.ai.lab3.mongo.repo.entities.Key;
import it.polito.ai.lab3.mongo.repo.entities.MinPath;

@Repository
public class MinPathsRepository {

	@Autowired MongoTemplate mongoTemplate;
	
	public MinPath findMinPath(String idSource, String idDestination) {
		Key key = new Key();
		key.setSrc(idSource);
		key.setDst(idDestination);
		Query q = new Query(Criteria.where("_id").is(key));
		List<MinPath> l = mongoTemplate.find(q, MinPath.class);
		if (l.isEmpty()) {
			return null;
		} else {
			return l.get(0);
		}
	}
}
