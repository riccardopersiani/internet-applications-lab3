<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page isELIgnored="false"%>

<link rel="stylesheet"
	href="https://unpkg.com/leaflet@1.0.3/dist/leaflet.css" />
<script src="https://unpkg.com/leaflet@1.0.3/dist/leaflet.js"></script>

<t:template>

	<jsp:attribute name="header">
        <%@include file="../components/navbar.jsp"%>
    </jsp:attribute>
	<jsp:attribute name="footer">
        <div id="pagefooter" class="row">
            <%@include file="../components/footer.jsp"%>
        </div>
    </jsp:attribute>

	<jsp:body>
	    <div>
	    	<h1>Miglior percorso trovato</h1>
	    	
	    	<h4>Cliccare sui marker per uteriori informazioni sul percorso</h4>
	    	<div id="mapid" style="width: 100%; height: 400px;"></div>

            <c:choose>
		    <c:when test="${empty path}">
				<script type="text/javascript">
					var srcDstPoints;
		           	var path;
				</script>
		    </c:when>
		    <c:otherwise>
	           	<script type="text/javascript">
					var srcDstPoints = ${srcDstPoints};
		           	var path = ${path};
				</script>
           	</c:otherwise>
           	</c:choose>
           	
            <script>
            	var lineStyle = {
            		    "color": "#ff7800",
            		    "weight": 5,
            		    "opacity": 0.65
            		};
            	
            	function onEachFeature(feature, layer) {
                    // does this feature have a property named popupContent?
                    if (feature.properties) {
                        var content = "<p>" + feature.properties.popupContent + "</p>" ;
                        layer.bindPopup(content);
                    }
                }

                // Create the map and set the view on Turin
                var mymap = L.map('mapid').setView([45.064, 7.681], 13);

				L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
		    		maxZoom: 18,
		    		attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, ' +
		    			'<a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
		    			'Imagery © <a href="http://mapbox.com">Mapbox</a>',
		    		id: 'mapbox.streets'
		    	}).addTo(mymap);

                // Create the points
                var stopsLayer = L.geoJSON(srcDstPoints, {
                    onEachFeature: onEachFeature
                }).addTo(mymap);

                
                if (path != null) {
	                // Draw the paths if there is one
	                var pathLayer = L.geoJSON(path, {
	                    style: function(feature) {
	                    	return feature.style;
	                    },
	                    onEachFeature: onEachFeature
	                }).addTo(mymap);
	                
	                mymap.fitBounds(pathLayer.getBounds());
                }
            </script>
        </div>
        
        <h3>Dettaglio del percorso</h3>
        
        <c:choose>
		    <c:when test="${empty path}">
				<h4>Nessun percorso trovato!</h4>
		    </c:when>
		    <c:otherwise>
		        <div class="container">
				  <table class="table">
				    <thead>
						<tr>
							<th>Mezzo di trasporto</th>
							<th>Da</th>
							<th>A</th>
						</tr>
				    </thead>
				    <tbody>
				    	<!-- Add start point details -->
				    	<tr>
							<td>A piedi</td>
							<td>Punto di partenza</td>
							<td>${fullPathInfo.pathSegments[0].source.name}</td>
						</tr>
				    
				    	<!-- Add intermediate stops details -->
				    	<c:forEach items="${fullPathInfo.pathSegments}" var="segment">
							<tr>
								<c:choose>
								    <c:when test="${segment.line != null}">
										<td>${segment.line}</td>
								    </c:when>    
								    <c:otherwise>
										<td>A piedi</td>
								    </c:otherwise>
								</c:choose>
								
								<td>${segment.source.name}</td>
								<td>${segment.destination.name}</td>
							</tr>
						</c:forEach>
						
						<!-- Add end point details -->
				    	<tr>
							<td>A piedi</td>
							<td>${fullPathInfo.pathSegments[fn:length(fullPathInfo.pathSegments) - 1].destination.name}</td>
							<td>Punto di arrivo</td>
						</tr>
				    </tbody>
				  </table>
				</div>
			</c:otherwise>
		</c:choose>
    </jsp:body>

</t:template>
