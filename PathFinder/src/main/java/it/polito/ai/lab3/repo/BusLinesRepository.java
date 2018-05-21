package it.polito.ai.lab3.repo;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import it.polito.ai.lab3.repo.entities.BusLine;

@Repository
public interface BusLinesRepository  extends PagingAndSortingRepository<BusLine, String> {

}
