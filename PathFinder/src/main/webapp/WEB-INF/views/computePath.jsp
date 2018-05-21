<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<link rel="stylesheet" href="https://unpkg.com/leaflet@1.0.3/dist/leaflet.css" />
<script src="https://unpkg.com/leaflet@1.0.3/dist/leaflet.js"></script>
<meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

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
            <h1>Servizio di calcolo del percorso</h1>
            <p>Selezionare due punti sulla mappa come sorgente e destinazione del percorso da calcolare</p>
            
            <div id="mapid" style="width: 100%; height: 400px;"></div>
            
            <script type="text/javascript">
		        function addMarker(e) {
		            var srcLat = document.getElementById("srcLat");
		            var srcLng = document.getElementById("srcLng");
		            var dstLat = document.getElementById("dstLat");
		            var dstLng = document.getElementById("dstLng");
		
		            if (srcPointFilled == false) {
		                // set the form fields and set the filled var
		                srcLat.setAttribute("value", e.latlng.lat);
		                srcLng.setAttribute("value", e.latlng.lng);
		                srcPointFilled = true;
		
		                // show the marker
		                srcPointMarker = new L.marker(e.latlng).addTo(mymap);
		            }
		            else if (dstPointFilled == false) {
		                // set the form fields and set the filled var
		                dstLat.setAttribute("value", e.latlng.lat);
		                dstLng.setAttribute("value", e.latlng.lng);
		                dstPointFilled = true;
		
		                // show the marker
		                dstPointMarker = new L.marker(e.latlng).addTo(mymap);
		            }
		        }
		
		        function submitForm(form) {
		            if (srcPointFilled == true && dstPointFilled == true) {
		                form.submit();
		            }
		            else {
		                alert("Per calcolare il percorso selezionare prima due punti sulla mappa");
		                return false;
		            }
		        }
		
		        function resetForm() {
		            // reset form fields
		            srcLat.setAttribute("value", "");
		            srcLng.setAttribute("value", "");
		            dstLat.setAttribute("value", "");
		            dstLng.setAttribute("value", "");
		
		            // reset the filled var
		            srcPointFilled = false;
		            dstPointFilled = false;
		
		            // remove the markers
		            mymap.removeLayer(srcPointMarker);
		            mymap.removeLayer(dstPointMarker);
		
		            return false;
		        }
		    </script>

            <script>
	            var srcPointFilled = false;
	            var dstPointFilled = false;
	            var srcPointMarker;
	            var dstPointMarker;
            
                // Create the map and set the view on Turin
                var mymap = L.map('mapid').setView([45.064, 7.681], 13);
                L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
		    		maxZoom: 18,
		    		attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, ' +
		    			'<a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
		    			'Imagery © <a href="http://mapbox.com">Mapbox</a>',
		    		id: 'mapbox.streets'
		    	}).addTo(mymap);

                /* register the click event for
                 *  - setting the source and destination point coordinates
                 *  - showing the points markers
                */
                mymap.on('click', addMarker);
            </script>
        </div>
        
        <form:form action="computePath" method="post" modelAttribute="srcDstPoints">
        	<form:hidden id="srcLat" path="srcLat"/>
	        <form:hidden id="srcLng" path="srcLng"/>
	        <form:hidden id="dstLat" path="dstLat"/>
	        <form:hidden id="dstLng" path="dstLng"/>
	        <form:button class="btn btn-primary" onclick="return submitForm(this.form);">Calcola percorso</form:button>
	        <form:button class="btn" onclick="return resetForm();">Cancella</form:button>
        </form:form>
    </jsp:body>
</t:template>
