# Mininimum Path calculation

This document describes the main steps to find the minimum paths and store them into mongoDB.

## Data extraction from postgis

Skeleton:

```
busStops = read list from BusStop
busLinesStops = read list from BusLineStop
foreach busStop in busStops:
  destinationsByWalk = get stations in a range of 250m from busStop
  foreach destinationByWalk in destinationsByWalk:
    destinationByWalk.cost = get distance between busStop and destinationByWalk and multiply for WALK_WEIGHT
  destinationsByOneBus = get the stations reachable from busStop using a single bus
  foreach destinationByOneBus in destinationsByOneBus:
    busLineStops = busLinesStops.get(destinationByOneBus.line)
    sequences = find wanted sequences from busLineStops, corresponding to pairs sqNsrc,sqNdst
    foreach sequence in sequences:
      sequence.cost = evaluate the length of path from sqNsrc to sqNdst
    best_sequence = sequence in sequences with smallest cost
    destinationByOneBus.cost = best_sequence.cost multiplied by BUS_WEIGHT

  save in graph destinationsByWalk
  save in graph destinationsByOneBus

foreach busStop in busStops:
  min_paths = dijkstra from busStop
  save min_paths in mongo
```

### Read the list of BusStop

```SQL
SELECT id, name, ST_Y(location::geometry) AS lng, ST_X(location::geometry) AS lat
FROM busStopGeographic
```

### Read the stopId and sequenceNumber

```SQL
SELECT lineIstopId, sequenceNumber
FROM BusLineStop
```

### Get stations in range of 250m from a given BusStop

```SQL
SELECT id, ST_Distance(location, ST_GeographyFromText(?)) AS distance
FROM busStopGeographic
WHERE ST_Distance(location, ST_GeographyFromText(?)) < 250
```

### Get stations reachable by using one bus from a given BusStop

```SQL
SELECT bsg.id, dst.lineId
FROM BusLineStop src, BusLineStop dst, BusStopGeographic bsg
WHERE src.lineId=dst.lineId AND src.stopId=? AND dst.stopId=bsg.id AND src.sequenceNumber>dst.sequenceNumber
```

### Evaluate length of a sequence given start seq number and end seq number and line number

```SQL
SELECT st_Length(ST_MakeLine(bsg.location::geometry ORDER BY bls.sequenceNumber)::geography) AS length
FROM BusStopGeographic bsg, BusLineStop bls
WHERE bls.lineId=? AND bls.sequenceNumber>=? AND bls.sequenceNumber<=? AND bsg.id=bls.stopId
```

### Get the stops id belonging to a line between two stops identified by sequence number

```SQL
SELECT stopId
FROM BusLineStop
WHERE lineId=? AND sequenceNumber>=? AND sequenceNumber<=?
```

## Dijkstra

Dijkstra runs on the set of edges found with the two strategies. The walking edges are edges identified by source and destination, while the bus edges begin from the station where users take the bus to the station where they go down: they contain also informations on the intermediate stops that occurr between the two ends.

Dijkstra then for each node computes the minimum path towards every destination node.

## Inserting in mongoDB

The documents contain the following informations:

```json
{
  "_id" : {
    "src" : "557",
    "dst" : "476"
  },
  "idSource" : "557",
  "idDestination" : "476",
  "edges" : [{
    "idSource" : "557",
    "idDestination" : "476",
    "mode" : false,
    "cost" : 4456,
    "lineId" : "55",
    "stopsId": ["557","559","3275","2006","2008","1295","2010","249","1642","471","474","476"]
  }],
  "totalCost" : 4456
}
```

The `_id` field is added for faster searches.
