<!-- *******************************************************************************
 * Copyright (C) 2020 Ram Sadasiv
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ****************************************************************************** -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List" %>
<%@ page import="com.fasterxml.jackson.databind.JsonNode" %>
<%@ page import="com.fasterxml.jackson.databind.node.ArrayNode" %>
<%@ page import="io.outofprintmagazine.web.util.JsonSort" %>	
<!doctype html>
<html lang="en">
<head>
<link rel="apple-touch-icon" sizes="180x180"
	href="images/apple-touch-icon.png">
<link rel="icon" type="image/png" sizes="32x32"
	href="images/favicon-32x32.png">
<link rel="icon" type="image/png" sizes="16x16"
	href="images/favicon-16x16.png">
<link rel="manifest" href="images/site.webmanifest">
<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">

<!-- Bootstrap 4 CSS -->
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	crossorigin="anonymous">

<title>OOP Cover Viewer</title>

<!-- JQuery -->
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.js"
	crossorigin="anonymous">
</script>

<!-- Bootstrap 4 -->
<script
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
	integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
	crossorigin="anonymous">
</script>

<link href="https://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet">
<style>
.logo {
	width: 311px;
	height: 68px;
	display: block;
	text-indent: -6000px;
	margin: 25px auto 59px auto;
	cursor: pointer;
}

.bar {
	fill: steelblue;
}

.chart text {
	fill: white;
	font: 10px sans-serif;
	text-anchor: end;
}

.bld {
	font-weight: bold;
}
</style>
</head>
<body>
	<a href="index.html"><img src="images/OOP/logo.png" class="logo" /></a>
	<div class="container">
		<div class="row">
			<div class="col-md-4"></div>
			<div class="col-md-4 text-center">
				<h1 class="text-capitalize"><%=request.getAttribute("Title").toString().toLowerCase()%></h1>
				<h3 class="text-capitalize">
					by
					<%=request.getAttribute("Author").toString().toLowerCase()%></h3>
				<h5 class="text-capitalize"><%=request.getAttribute("Date").toString().toLowerCase()%></h5>
			</div>
			<div class="col-md-4"></div>
		</div>
	</div>
	<div class="container-fluid">
		<div class="row">&nbsp;</div>
	</div>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-2"></div>
			<div id="carouselExampleIndicators" class="carousel slide col-md-8 text-center" data-ride="carousel">
				<ol class="carousel-indicators">
					<li data-target="#carouselExampleIndicators" data-slide-to="0" class="active"></li>
					<li data-target="#carouselExampleIndicators" data-slide-to="1"></li>
					<li data-target="#carouselExampleIndicators" data-slide-to="2"></li>
					<li data-target="#carouselExampleIndicators" data-slide-to="3"></li>
					<li data-target="#carouselExampleIndicators" data-slide-to="4"></li>
					<li data-target="#carouselExampleIndicators" data-slide-to="5"></li>					
				</ol>
				<div class="carousel-inner">
					<%
					JsonNode stats = (JsonNode) request.getAttribute("Stats");
					ArrayNode locations = (ArrayNode) stats.get("OOPSettingsAnnotation");
					System.err.println(locations.size());
					JsonSort.sortActors(locations);
					System.err.println(locations.size());
					int locationCount = 3;
					for (int i=0;i<locationCount;i++) {
						ArrayNode thumbnails = (ArrayNode) locations.get(i).get("thumbnails");
						if (thumbnails.size() == 0) {
							locationCount++;
						}
						else {
						%>		
							<div class="carousel-item <%=i==0?"active":""%>">
								<img class="d-block w-100" src="<%=thumbnails.get(0).asText() %>">
							</div>
							<div class="carousel-item">
								<img class="d-block w-100" src="<%=thumbnails.get(thumbnails.size()-1).asText()%>" alt="Second slide">				
							</div>
						<%
						}
						
					}
					%>
				</div>
				<a class="carousel-control-prev" href="#carouselExampleIndicators" role="button" data-slide="prev"> 
					<span class="carousel-control-prev-icon" aria-hidden="true"></span> 
					<span class="sr-only">Previous</span>
				</a> 
				<a class="carousel-control-next" href="#carouselExampleIndicators" role="button" data-slide="next"> 
					<span class="carousel-control-next-icon" aria-hidden="true"></span> 
					<span class="sr-only">Next</span>
				</a>
			</div>
			<div class="col-md-2"></div>		
		</div>
	</div>
	<div class="container-fluid">
		<div class="row">	
			<div class="col-md-4"></div>
			<div class="col-md-4 text-center">
				<%
					for (String q : ((List<String>)request.getAttribute("PullQuotes"))) {
						%>
						<p><%=q%></p>
					<% }
				%>

			</div>
			<div class="col-md-4"></div>
		</div>
	</div>	


</body>
</html>
