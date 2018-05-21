package it.polito.ai.lab3;

import java.nio.file.*;
import java.sql.*;
import java.util.*;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import it.polito.ai.lab3.mongoClasses.Edge;
import it.polito.ai.lab3.model.Node;

public class DbReader {

	private final static int RIDE_PENALTY = 1000; // going on a bus has penalty
	private final static int WALK_WEIGHT = 10;
	private final static int BUS_WEIGHT = 2;
	private final static int METRO_WEIGHT = 1;

	/**
	 * We are using a connection pool to be able to use 100% CPU
	 */
	private ComboPooledDataSource cpds;
	private Connection connection;

	private final static String getBusStops = "SELECT id, name, ST_Y(location::geometry) AS lng, ST_X(location::geometry) AS lat "
			+ "from busStopGeographic";
	private final static String getReachableStops = "SELECT bsg.id, dst.lineId "
			+ "FROM BusLineStop src, BusLineStop dst, BusStopGeographic bsg "
			+ "WHERE src.lineId=dst.lineId AND src.stopId=? AND dst.stopId=bsg.id AND src.sequenceNumber<dst.sequenceNumber";
	private final static String getNearbyStops = "SELECT id, ST_Distance(location, ST_GeographyFromText(?)) as distance "
			+ "FROM busStopGeographic " + "WHERE ST_Distance(location, ST_GeographyFromText(?)) < 250";
	private final static String getBusLinesStops = "SELECT lineId, stopId, sequenceNumber " + "FROM BusLineStop "
			+ "ORDER BY lineId, sequenceNumber";
	private final static String getSequenceLength = "SELECT st_Length(ST_MakeLine(bsg.location::geometry ORDER BY bls.sequenceNumber)::geography) AS length "
			+ "FROM BusStopGeographic bsg, BusLineStop bls "
			+ "WHERE bls.lineId=? AND bls.sequenceNumber>=? AND BLS.sequenceNumber<=? AND bsg.id=bls.stopId";
	private final static String getSequenceStopsId = "SELECT stopId " + "FROM BusLineStop "
			+ "WHERE lineId=? AND sequenceNumber>=? AND sequenceNumber<=?";

	private PreparedStatement getBusStopsStmt;
	private PreparedStatement getBusLinesStopsStmt;

	public DbReader() {
		try {
			cpds = new ComboPooledDataSource();
			cpds.setDriverClass("org.postgresql.Driver");

			String server = null;
			try {
				List<String> lines = Files.readAllLines(Paths
						.get(DbReader.class.getClassLoader().getResource("db_ip.txt").toURI().toString().substring(6)));
				server = lines.get(0);
			} catch (Exception e) {
				server = "localhost";
			}

			cpds.setJdbcUrl("jdbc:postgresql://" + server + ":5432/trasporti");
			cpds.setUser("postgres");
			cpds.setPassword("ai-user-password");
			cpds.setMaxStatements(Runtime.getRuntime().availableProcessors());
			connection = cpds.getConnection();
			// read-only, so don't need transactions
			getBusStopsStmt = connection.prepareStatement(getBusStops);
			getBusLinesStopsStmt = connection.prepareStatement(getBusLinesStops);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Get the list of bus stops with their position
	 * 
	 * @return
	 */
	public Set<Node> getBusStops() {
		Set<Node> result = new HashSet<Node>();
		try {
			ResultSet rs = getBusStopsStmt.executeQuery();
			try {
				while (rs.next()) {
					String id = rs.getString("id");
					String name = rs.getString("name");
					Double lat = rs.getDouble("lat");
					Double lng = rs.getDouble("lng");
					result.add(new Node(id, name, lat, lng));
				}
			} finally {
				rs.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * gets the sequence numbers corresponding to stops
	 * 
	 * @return a Map that for each lineId gives a Map that for each stopId
	 *         contains the list of the corresponding sequence numbers
	 */
	public Map<String, Map<String, List<Integer>>> getBusLinesStops() {
		Map<String, Map<String, List<Integer>>> result = new HashMap<String, Map<String, List<Integer>>>();
		try {
			ResultSet rs = getBusLinesStopsStmt.executeQuery();
			try {
				while (rs.next()) {
					String lineId = rs.getString("lineId");
					String stopId = rs.getString("stopId");
					int sequenceNumber = rs.getInt("sequenceNumber");
					result.putIfAbsent(lineId, new HashMap<String, List<Integer>>());
					Map<String, List<Integer>> lineMap = result.get(lineId);
					lineMap.putIfAbsent(stopId, new ArrayList<Integer>());
					List<Integer> stopSequenceNumbers = lineMap.get(stopId);
					stopSequenceNumbers.add(sequenceNumber);
				}
			} finally {
				rs.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * Get the list of reachable stops in proximity of the source (250m)
	 * 
	 * @param source
	 * @return
	 */
	public Set<Edge> getReachableStopsByWalk(Connection connection, Node srcNode) {
		PreparedStatement getNearbyStopsStmt;
		Set<Edge> result = new HashSet<Edge>();
		try {
			getNearbyStopsStmt = connection.prepareStatement(getNearbyStops);
			String position = "SRID=4326;POINT(" + srcNode.getLat() + " " + srcNode.getLng() + ")";
			getNearbyStopsStmt.setString(1, position);
			getNearbyStopsStmt.setString(2, position);
			ResultSet rs = getNearbyStopsStmt.executeQuery();
			try {
				while (rs.next()) {
					String id = rs.getString("id");
					double distance = rs.getDouble("distance");
					result.add(new Edge(srcNode.getId(), id, null, true, (int) distance * WALK_WEIGHT));
				}
			} finally {
				rs.close();
				getNearbyStopsStmt.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * Get the list of reachable stops using a single run on a bus
	 * 
	 * @param source
	 * @return
	 */
	public Set<Edge> getReachableStopsByBus(Connection connection, Node srcNode) {
		PreparedStatement getReachableStopsStmt;
		Set<Edge> result = new HashSet<Edge>();
		try {
			getReachableStopsStmt = connection.prepareStatement(getReachableStops);
			getReachableStopsStmt.setString(1, srcNode.getId());
			ResultSet rs = getReachableStopsStmt.executeQuery();
			try {
				while (rs.next()) {
					String id = rs.getString("id");
					String lineId = rs.getString("lineId");
					double distance = 0; // will fill it later
					result.add(new Edge(srcNode.getId(), id, lineId, false, (int) distance * BUS_WEIGHT));
				}
			} finally {
				rs.close();
				getReachableStopsStmt.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * evaluates the length of the line that goes from source to destination
	 * sequence numbers in a specific line and passes for all the intermediate
	 * stops
	 * 
	 * @param lineId
	 * @param srcSequenceNumber
	 * @param dstSequenceNumber
	 * @return
	 */
	public double getSequenceCost(Connection connection, String lineId, int srcSequenceNumber, int dstSequenceNumber) {
		double length;
		try {
			PreparedStatement getSequenceLengthStmt = connection.prepareStatement(getSequenceLength);

			getSequenceLengthStmt.setString(1, lineId);
			getSequenceLengthStmt.setInt(2, srcSequenceNumber);
			getSequenceLengthStmt.setInt(3, dstSequenceNumber);
			ResultSet rs = getSequenceLengthStmt.executeQuery();
			try {
				rs.next();
				length = rs.getDouble("length");
			} finally {
				rs.close();
				getSequenceLengthStmt.close();
			}
			getSequenceLengthStmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		if (lineId.equals("METRO")) {
			// the metro is special
			return length * METRO_WEIGHT + RIDE_PENALTY;
		} else {
			return length * BUS_WEIGHT + RIDE_PENALTY;
		}
	}

	public List<String> getBusLineStopsIdBetween(Connection connection, String lineId, int bestSrcSeqNumber,
			int bestDstSeqNumber) {
		List<String> result = new ArrayList<String>();
		try {
			PreparedStatement getSequenceStopsIdStmt = connection.prepareStatement(getSequenceStopsId);

			getSequenceStopsIdStmt.setString(1, lineId);
			getSequenceStopsIdStmt.setInt(2, bestSrcSeqNumber);
			getSequenceStopsIdStmt.setInt(3, bestDstSeqNumber);
			ResultSet rs = getSequenceStopsIdStmt.executeQuery();
			try {
				while (rs.next()) {
					String stopId = rs.getString("stopId");
					result.add(stopId);
				}
			} finally {
				rs.close();
				getSequenceStopsIdStmt.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public Connection getConnection() {
		try {
			return cpds.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void closeConnection(Connection c) {
		try {
			c.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * release resources
	 */
	public void close() {
		try {
			getBusStopsStmt.close();
			connection.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}
}
