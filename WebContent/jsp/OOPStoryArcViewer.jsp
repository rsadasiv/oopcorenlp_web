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
<%@ page import="com.fasterxml.jackson.databind.node.ObjectNode" %>
<%@ page import="com.fasterxml.jackson.databind.JsonNode" %>
<!doctype html>
<html lang="en">
<head>
<jsp:include page="include/icon.jsp" />
<jsp:include page="include/meta.jsp" />
<jsp:include page="include/bootstrap.jsp" />
<jsp:include page="include/d3v5.jsp" />
<script src="js/oopcorenlp.js"></script>
<script src="js/OOPStoryArcViewer.js"></script>

<%
ObjectNode annotationDescriptions = (ObjectNode) request.getAttribute("AnnotationDescriptions");
ObjectNode stats = (ObjectNode) request.getAttribute("Stats");
%>

<script>
    $(document).ready(function() {
    	getProperties()["docId"] = "<%=request.getParameter("Document")%>";
    	getProperties()["corpus"] = "<%=request.getParameter("Corpus")%>";

		makeSentenceBarChart("OOPFleschKincaidAnnotation", "#OOPFleschKincaidAnnotationViz", 0);
		makeSentenceBarChart("VaderSentimentAnnotation", "#VaderSentimentAnnotationViz", 0);
		makeSentenceBarChart("OOPVerbsAnnotation", "#OOPVerbsAnnotationViz", 0);
		makeSentenceBarChart("OOPActionlessVerbsAnnotation", "#OOPActionlessVerbsAnnotationViz", 0);
		makeSentenceBarChart("OOPPeopleAnnotation", "#OOPPeopleAnnotationViz", 0);		
		//makeSentenceBarChart("OOPLocationsAnnotation", "#OOPLocationsAnnotationViz", 0);
		makeSentenceScoreBarChart("OOPPunctuationMarkAnnotation", "Quotation", "#OOPPunctuationMarkAnnotationViz", 0);
		makeSentenceBarChart("OOPAdjectivesAnnotation", "#OOPAdjectivesAnnotationViz", 0);
		makeSentenceBarChart("OOPAdverbsAnnotation", "#OOPAdverbsAnnotationViz", 0);
    });
</script>

<title>OOP Story Arc Viewer</title>
</head>
<body>
	<jsp:include page="include/nav.jsp" />
	<div class="container">
		<div class="row">
			<div class="col">
				<p>
					&nbsp;
				</p>
			</div>
		</div>
	</div>
	<div class="container-fluid">
		<div class="row">
			<div class="col">
				<p>OOPFleschKincaidAnnotation</p>
				<p><%=annotationDescriptions.get("OOPFleschKincaidAnnotation").asText() %></p>
				<p>
					<a target="_blank"id="OOPFleschKincaidAnnotationVizDataLink">
						Data
					</a>
				</p>
				<svg width="<%=Integer.toString(stats.get("OOPSentenceCountAnnotation").asInt()+100) %>" height="200" id="OOPFleschKincaidAnnotationViz"></svg>
			</div>
			
			<div class="col">
				<p>VaderSentimentAnnotation</p>
				<p><%=annotationDescriptions.get("VaderSentimentAnnotation").asText() %></p>
				<p>
					<a target="_blank" id="VaderSentimentAnnotationVizDataLink">
						Data
					</a>
				</p>
				<svg width="<%=Integer.toString(stats.get("OOPSentenceCountAnnotation").asInt()+100) %>" height="200" id="VaderSentimentAnnotationViz"></svg>
			</div>
		</div>
		<div class="row">
			<div class="col">
				<p>OOPVerbsAnnotation</p>
				<p><%=annotationDescriptions.get("OOPVerbsAnnotation").asText() %></p>
				<p>
					<a target="_blank" id="OOPVerbsAnnotationVizDataLink">
						Data
					</a>
				</p>
				<svg width="<%=Integer.toString(stats.get("OOPSentenceCountAnnotation").asInt()+100) %>" height="200" id="OOPVerbsAnnotationViz"></svg>
			</div>
			
			<div class="col">
				<p>OOPActionlessVerbsAnnotation</p>
				<p><%=annotationDescriptions.get("OOPActionlessVerbsAnnotation").asText() %></p>
				<p>
					<a target="_blank" id="OOPActionlessVerbsAnnotationVizDataLink">
						Data
					</a>
				</p>
				<svg width="<%=Integer.toString(stats.get("OOPSentenceCountAnnotation").asInt()+100) %>" height="200" id="OOPActionlessVerbsAnnotationViz"></svg>
			</div>
		</div>
		<div class="row">
			<div class="col">
				<p>OOPPeopleAnnotation</p>
				<p><%=annotationDescriptions.get("OOPPeopleAnnotation").asText() %></p>
				<p>
					<a target="_blank" id="OOPPeopleAnnotationVizDataLink">
						Data
					</a>
				</p>
				<svg width="<%=Integer.toString(stats.get("OOPSentenceCountAnnotation").asInt()+100) %>" height="200" id="OOPPeopleAnnotationViz"></svg>
			</div>
			

			<div class="col">
				<p>Quotation</p>
				<p><%=annotationDescriptions.get("OOPPunctuationMarkAnnotation").asText() %></p>
				<p>
					<a target="_blank" id="OOPPunctuationMarkAnnotationVizDataLink">
						Data
					</a>
				</p>
				<svg width="<%=Integer.toString(stats.get("OOPSentenceCountAnnotation").asInt()+100) %>" height="200" id="OOPPunctuationMarkAnnotationViz"></svg>
			</div>
		</div>
		<div class="row">
			<div class="col">
				<p>OOPAdjectivesAnnotation</p>
				<p><%=annotationDescriptions.get("OOPAdjectivesAnnotation").asText() %></p>
				<p>
					<a target="_blank" id="OOPAdjectivesAnnotationVizDataLink">
						Data
					</a>
				</p>
				<svg width="<%=Integer.toString(stats.get("OOPSentenceCountAnnotation").asInt()+100) %>" height="200" id="OOPAdjectivesAnnotationViz"></svg>
			</div>
			

			<div class="col">
				<p>OOPAdverbsAnnotation</p>
				<p><%=annotationDescriptions.get("OOPAdverbsAnnotation").asText() %></p>
				<p>
					<a target="_blank" id="OOPAdverbsAnnotationVizDataLink">
						Data
					</a>
				</p>
				<svg width="<%=Integer.toString(stats.get("OOPSentenceCountAnnotation").asInt()+100) %>" height="200" id="OOPAdverbsAnnotationViz"></svg>
			</div>
		</div>							
	</div>	
	<jsp:include page="include/footer.jsp" />
</body>
</html>
