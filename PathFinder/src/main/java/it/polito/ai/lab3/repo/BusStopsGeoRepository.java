package it.polito.ai.lab3.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.polito.ai.lab3.repo.entities.BusStopGeographic;

@Repository
public interface BusStopsGeoRepository extends CrudRepository<BusStopGeographic, String> {
	
	/**
	 * Query for retrieving the set of bus stops that are not further than the specified
	 * distance from the specified point.
	 * 
	 * @param point - the source point
	 * @param distance - the distance in meters
	 * @return the set of found bus stops
	 */
	@Query("select t " +
	           "from BusStopGeographic t " +
	           "where ST_DWithin(t.location, ST_GeographyFromText(:point), :distance) = true")
	List<BusStopGeographic> findByDistance(@Param("point") String point, @Param("distance") Double distance);
}
