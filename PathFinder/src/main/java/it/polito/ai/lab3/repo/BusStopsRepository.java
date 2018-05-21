package it.polito.ai.lab3.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.polito.ai.lab3.repo.entities.BusStop;

@Repository
public interface BusStopsRepository extends CrudRepository<BusStop, String> {

}
