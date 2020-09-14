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
<%@ page import="java.util.Map" %>
<%@ page import="java.util.concurrent.ThreadLocalRandom" %>
<%@ page import="com.fasterxml.jackson.databind.JsonNode" %>
<%@ page import="com.fasterxml.jackson.databind.node.ArrayNode" %>
<%@ page import="io.outofprintmagazine.web.util.JsonSort" %>	
<!doctype html>
<html lang="en">
<head>
<jsp:include page="include/icon.jsp" />
<jsp:include page="include/meta.jsp" />
<jsp:include page="include/bootstrap.jsp" />

<style> 
	.carousel-inner > .carousel-item > img { 
	height:400px; 
	object-fit: scale-down;	
} 
</style>
<%
Map<String, List<String>> pullQuotes = (Map<String, List<String>>)request.getAttribute("PullQuotes");
%>
<title>OOP Pull Quotes</title>
</head>
<body>
	<jsp:include page="include/nav.jsp" />
	<div class="container">	
		<jsp:include page="include/spacerRow.jsp" />
		<div class="row">
			<div class="col-md-2"></div>
			<div id="carouselExampleIndicators" class="carousel slide col-md-8 text-center bg-dark" data-ride="carousel">
				<div class="carousel-inner">
					<%
					JsonNode stats = (JsonNode) request.getAttribute("Stats");
					ArrayNode locations = (ArrayNode) stats.get("OOPSettingsAnnotation");
					JsonSort.sortActors(locations);
					int locationCount = 3;
					int thumbnailCount = 5;
					int imageCount = 0;
					boolean markActive = true;
					for (int i=0;i<locationCount&&i<locations.size();i++) {
						ArrayNode thumbnails = (ArrayNode) locations.get(i).get("thumbnails");
						if (thumbnails.size() == 0) {
							locationCount++;
						}
						else {
							ThreadLocalRandom random = ThreadLocalRandom.current();
							for (int j=0;j<thumbnailCount&&j<thumbnails.size();j++) {
								int idx = random.nextInt(0, thumbnails.size());
						%>		
								<div class="carousel-item<%=markActive?" active":""%>">
									<img class="d-block w-100" src="<%=thumbnails.get(idx).asText() %>" alt="<%=thumbnails.get(idx).asText() %>">
										<div class="carousel-caption d-none d-md-block">
											<p><%=locations.get(i).get("canonicalName").asText()%></p>
										</div>
								</div>
						<%
								imageCount++;
								markActive = false;
							}
						}
						
					}
					%>
				</div>
				<ol class="carousel-indicators">
					<li data-target="#carouselExampleIndicators" data-slide-to="0" class="active"></li>
					<%
					for (int k=1;k<imageCount;k++) {
					%>
						<li data-target="#carouselExampleIndicators" data-slide-to="<%=k%>"></li>
					<%
					}	
					%>					
				</ol>
				<a class="carousel-control-prev" href="#carouselExampleIndicators" role="button" data-slide="prev"> 
					<span class="carousel-control-prev-icon" aria-hidden="false"></span> 
					<span class="sr-only">Previous</span>
				</a> 
				<a class="carousel-control-next" href="#carouselExampleIndicators" role="button" data-slide="next"> 
					<span class="carousel-control-next-icon" aria-hidden="false"></span> 
					<span class="sr-only">Next</span>
				</a>
			</div>
			<div class="col-md-2"></div>		
		</div>
	</div>
	<jsp:include page="include/spacerRow.jsp" />	
	<div class="container-fluid">
		<div class="row">
			<div class="col">
				<div class="card">
					<div class="card-header">
		    			<h5 class="card-title">First/Last</h5>
		    		</div>
					<div class="card-body">
		    			<%
		    			for (String s : pullQuotes.get("FirstLast")) {
		    			%>
		    				<p class="card-text"><%=s %></p>
		    			<%
		    			}
		    			%>
					</div>
				</div>
			</div>
			<div class="col">
				<div class="card">
					<div class="card-header">
		    			<h5 class="card-title">Best</h5>
		    		</div>
					<div class="card-body">
		    			<%
		    			for (String s : pullQuotes.get("Best")) {
		    			%>
		    				<p class="card-text"><%=s %></p>
		    			<%
		    			}
		    			%>
					</div>
				</div>
			</div>
			<div class="col">
				<div class="card">
					<div class="card-header">
		    			<h5 class="card-title">Worst</h5>
		    		</div>	
					<div class="card-body">
		    			<%
		    			for (String s : pullQuotes.get("Worst")) {
		    			%>
		    				<p class="card-text"><%=s %></p>
		    			<%
		    			}
		    			%>
					</div>
				</div>
			</div>			
		</div>
		<jsp:include page="include/spacerRow.jsp" />		
		<div class="row">
			<div class="col">
				<div class="card">
					<div class="card-header">
		    			<h5 class="card-title">Lede</h5>
		    		</div>
					<div class="card-body">		    			
		    			<%
		    			for (String s : pullQuotes.get("Lede")) {
		    			%>
		    				<p class="card-text"><%=s %></p>
		    			<%
		    			}
		    			%>
					</div>
				</div>
			</div>
			<div class="col">
				<div class="card">
					<div class="card-header">
		    			<h5 class="card-title">Dialog</h5>
		    		</div>
					<div class="card-body">		    			
		    			<%
		    			for (String s : pullQuotes.get("Dialog")) {
		    			%>
		    				<p class="card-text"><%=s %></p>
		    			<%
		    			}
		    			%>
					</div>
				</div>
			</div>
			<div class="col">
				<div class="card">
					<div class="card-header">
				  		<h5 class="card-title">In Medias Res</h5>
				  	</div>
					<div class="card-body">
		    			
		    			<%
		    			for (String s : pullQuotes.get("InMediasRes")) {
		    			%>
		    				<p class="card-text"><%=s %></p>
		    			<%
		    			}
		    			%>
					</div>
				</div>
			</div>					
		</div>
	</div>
	<jsp:include page="include/footer.jsp" />
</body>
</html>
