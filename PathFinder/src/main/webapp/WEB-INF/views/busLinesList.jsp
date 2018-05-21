<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.net.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false"%>

<!DOCTYPE html>
<html>
<head>
<base
	href="<%=new URL(request.getScheme(), request.getServerName(), request.getServerPort(),
					request.getContextPath())%>/">
<title>${title}</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"/>
<link rel="stylesheet" href="css/style.css"/>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

</head>

<body>
	<div id="pageheader" class="navbar navbar-inverse navbar-fixed-top">
		<div class="container-fluid">
			<%@include file="../components/navbar.jsp"%>
		</div>
	</div>
	<div class="container" style="padding-top: 50px;">
		<div id="body" class="row">
			<div style="padding-top: 50px;">
				<h1>List of Bus Lines</h1>

				<ul class="map_redirections">
					<c:forEach items="${busLines}" var="element">
						<li><a href="showbusLineStops?lineId=${element.line}">${element.line} - ${element.description}</a></li>
					</c:forEach>
				</ul>

				<br> <br> <br> <br>

			</div>
		</div>
	</div>
</body>
</html>