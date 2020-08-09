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
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.fasterxml.jackson.databind.JsonNode" %>
<%@ page import="com.fasterxml.jackson.databind.node.ArrayNode" %>
<%@ page import="io.outofprintmagazine.web.util.JsonSort" %>	
<!doctype html>
<html lang="en">
<head>
<jsp:include page="include/icon.jsp" />
<jsp:include page="include/meta.jsp" />
<jsp:include page="include/bootstrap.jsp" />

<title>OOP Cover Viewer</title>
</head>
<body>
	<jsp:include page="include/logo.jsp" />
	<div class="container">
		<jsp:include page="include/divRowDocumentMetadata.jsp" />
	</div>
		<jsp:include page="include/spacerRow.jsp" />
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
