package it.polito.ai.lab3;

import java.nio.file.*;
import java.sql.*;
import java.util.*;

import it.polito.ai.lab3.objects.BusStopGeographic;

public class JdbcWriter {

	private Connection connection;
	private String createBusStopGeographic = "CREATE TABLE BusStopGeographic ( id varchar(20) not null, name varchar(255) not null, location geography(POINT, 4326), primary key (id))";
	private String dropBusStopGeographic = "DROP TABLE IF EXISTS BusStopGeographic";
	private String readBusStop = "SELECT id, name, lat, lng from BusStop";
	private String insertBusStopGeographic = "INSERT INTO BusStopGeographic (id, name, location) values (?, ?, ST_GeographyFromText(?))";

	private PreparedStatement busStopSelect;
	private PreparedStatement busStopGeographicInsertStmt;

	public JdbcWriter() {
		try {
			Class.forName("org.postgresql.Driver");

			String server = null;
			try {
				List<String> lines = Files.readAllLines(Paths.get(
						JdbcWriter.class.getClassLoader().getResource("db_ip.txt").toURI().toString().substring(6)));
				server = lines.get(0);
			} catch (Exception e) {
				server = "localhost";
			}

			connection = DriverManager.getConnection("jdbc:postgresql://" + server + ":5432/trasporti", "postgres",
					"ai-user-password");
			// set auto commit to false so that transactions are used
			connection.setAutoCommit(false);
			// drop the geographic table
			PreparedStatement dropStmt = connection.prepareStatement(dropBusStopGeographic);
			dropStmt.executeUpdate();
			dropStmt.close();
			// create the geographic table again
			PreparedStatement createStmt = connection.prepareStatement(createBusStopGeographic);
			createStmt.executeUpdate();
			createStmt.close();
			busStopSelect = connection.prepareStatement(readBusStop);
			busStopGeographicInsertStmt = connection.prepareStatement(insertBusStopGeographic);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public void copyToGeographicTable() {
		try {
			ResultSet rs = busStopSelect.executeQuery();
			try {
				while (rs.next()) {
					// copy from one table to the other
					BusStopGeographic busStop = new BusStopGeographic(rs.getString("id"), rs.getString("name"),
							rs.getDouble("lat"), rs.getDouble("lng"));
					insertBusStopGeographic(busStop);
				}
			} finally {
				rs.close();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void insertBusStopGeographic(BusStopGeographic busStop) {
		try {
			busStopGeographicInsertStmt.setString(1, busStop.getId());
			busStopGeographicInsertStmt.setString(2, busStop.getName());
			// the string parameter is put into the prepared statement
			busStopGeographicInsertStmt.setString(3,
					"SRID=4326;POINT(" + busStop.getLat() + " " + busStop.getLng() + ")");
			busStopGeographicInsertStmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("insert bus stop failed: " + e.getMessage());
		}
	}

	/**
	 * to save after some calls to the insert functions
	 */
	public void save() {
		try {
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			close();
		}
	}

	/**
	 * to close the connection to the db
	 */
	public void close() {
		try {
			busStopGeographicInsertStmt.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
