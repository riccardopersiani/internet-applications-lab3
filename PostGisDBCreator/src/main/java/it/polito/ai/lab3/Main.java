package it.polito.ai.lab3;

import it.polito.ai.lab3.JdbcWriter;

public class Main {
	/**
	 * steps:
	 * <ul>
	 * <li>connect to postgis db (lab2)</li>
	 * <li>drop table BusStopGeographic if exists</li>
	 * <li>create table BusStopGeographic with proper schema (geographic points
	 * instead of lat+lng)</li>
	 * <li>insert each row from BusStop to BusStopGeographic</li>
	 * </ul>
	 * 
	 **/
	public static void main(String[] args) {

		JdbcWriter jdbcWriter = new JdbcWriter();

		System.out.println("Connected to db");

		jdbcWriter.copyToGeographicTable();

		jdbcWriter.save();

		jdbcWriter.close();

		System.out.println("done");
	}
}
