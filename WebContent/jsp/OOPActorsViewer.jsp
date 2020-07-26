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
<%@ page import="java.util.Iterator" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.math.RoundingMode" %>
<%@ page import="com.fasterxml.jackson.databind.JsonNode" %>
<%@ page import="com.fasterxml.jackson.databind.node.ArrayNode" %>
<%@ page import="io.outofprintmagazine.web.util.JsonSort" %>
<!doctype html>
<html lang="en">
<head>
<jsp:include page="include/icon.jsp" />
<jsp:include page="include/meta.jsp" />
<jsp:include page="include/bootstrap.jsp" />
<jsp:include page="include/d3v3.jsp" />

<script src="js/d3.layout.cloud.js"></script>
<script src="js/d3.wordcloud.js"></script>
<script src="js/oopcorenlp_d3_viewer.js"></script>

<script>
	var docId = "<%=request.getParameter("Document")%>";
	var corpus = "<%=request.getParameter("Corpus")%>";
	var actor = "<%=request.getParameter("Actor")==null||request.getParameter("Actor").equals("")?"":request.getParameter("Actor")%>";
</script>

<script>
$(document).ready(function() {
	setProperties();

	if (actor != "") {
		makeActorCloud(actor, "OOPNounsAnnotation", "#nounsViz", "#nounsTable");
		makeActorCloud(actor, "OOPNounGroupsAnnotation", "#nounGroupsViz", "#nounGroupsTable");
		makeActorCloud(actor, "OOPVerbsAnnotation", "#verbsViz", "#verbsTable");
		makeActorCloud(actor, "OOPVerbGroupsAnnotation", "#verbGroupsViz", "#verbGroupsTable");		
		makeActorCloud(actor, "OOPAdjectivesAnnotation", "#adjectivesViz", "#adjectivesTable");
		makeActorCloud(actor, "OOPAdverbsAnnotation", "#adverbsViz", "#adverbGroupsTable");
	}
	
	$('#actors').change(
			function() {
		    	window.location.href = location.protocol + '//' + location.host + location.pathname + "?Corpus="+corpus+"&Document="+docId+"&Actor=" + $('#actors option:selected').val();
		    }
	);
});
</script>

<% 
JsonNode selectedActor = (JsonNode)request.getAttribute("SelectedActor");
%>
	
<title>OOP Actors Viewer</title>
</head>
<body>
	<jsp:include page="include/logo.jsp" />
	
	<div class="container">	
		<div class="row">
			<div class="col-md-4"></div>
			<div class="col-md-4 text-center">
					<h1 class="text-capitalize"><%=request.getAttribute("Title").toString().toLowerCase()%></h1>
					<h3 class="text-capitalize">by <%=request.getAttribute("Author").toString().toLowerCase()%></h3>
					<h5 class="text-capitalize"><%=request.getAttribute("Date").toString().toLowerCase()%></h5>
			</div>
			<div class="col-md-4"></div>
		</div>
		<div class="row">
			<div class="col-md-4"></div>
			<div class="col-md-4" id="actorPicker">
				<select id="actors"	class="form-control" title="Select Actor">
				<% 
					JsonNode stats = (JsonNode) request.getAttribute("Stats");
					ArrayNode actors = (ArrayNode) stats.get("OOPActorsAnnotation");
					JsonSort.sortActors(actors);
					for (JsonNode actorNode : actors) {
						if (selectedActor.get("canonicalName") != null) {
						%>
						<option value="<%=actorNode.get("canonicalName").asText() %>" <%=actorNode.get("canonicalName").asText().equals(selectedActor.get("canonicalName").asText())?"selected":"" %>><%=actorNode.get("canonicalName").asText() %></option>
						<%
						}
						else {
						%>
						<option value="<%=actorNode.get("canonicalName").asText() %>"><%=actorNode.get("canonicalName").asText() %></option>
						<% 
						}
					}
				%>
				</select>
			</div>
			<div class="col-md-4 form-check">
			</div>
		</div>

		<div class="row">&nbsp;</div>
<%
if (selectedActor.get("canonicalName") != null) {
%>
		<div class="row bg-light">
			<div class="col">
				<label for="canonicalName">Canonical Name:</label> <span id="canonicalName"><%=selectedActor.get("canonicalName").asText("")%></span>
			</div>
			<div class="col">
				<label for="importance">Importance:</label> <span id="importance"><%=selectedActor.get("importance").asText("")%></span>
			</div>
			<div class="col">
				<label for="firstAppearance">First Appearance:</label> <span id="firstAppearance"><%=selectedActor.get("firstAppearance").asText("")%></span>
			</div>
			<div class="col">
				<label for="lastAppearance">Last Appearance:</label> <span id="lastAppearance"><%=selectedActor.get("lastAppearance").asText("")%></span>
			</div>							
		</div>

		<div class="row">&nbsp;</div>
		
		<div class="row bg-light">
			<div class="col">
				<label for="coreNlpGender">CoreNlp Gender:</label> <span id="canonicalName"><%=selectedActor.get("coreNlpGender").asText("NA")%></span>
			</div>
			<div class="col">
				<label for="oopgender">OOP Gender:</label> <span id="oopgender"><%=selectedActor.get("oopgender").asText("NA")%></span>
			</div>
			<div class="col">
				<label for="coreNlpSentimentAvg">CoreNlp Sentiment:</label> <span id="coreNlpSentimentAvg"><%=String.format("%.0f", selectedActor.get("coreNlpSentimentAvg").asDouble(0)*100)%></span>
			</div>
			<div class="col">
				<label for="vaderSentimentAvg">Vader Sentiment:</label> <span id="vaderSentimentAvg"><%=String.format("%.0f", selectedActor.get("vaderSentimentAvg").asDouble(0)*100)%></span>
			</div>							
		</div>

		<div class="row">&nbsp;</div>
	
		<div class="row bg-light">
			<div class="col">
				<label for="introvert">Introvert:</label>
					<span id="introvert"><%=request.getAttribute("introvert") %></span>
			</div>
			<div class="col">
				<label for="intuitive">Intuitive:</label> 				
				<span id="intuitive"><%=request.getAttribute("intuitive") %></span>
			</div>
			<div class="col">
				<label for="thinking">Thinking:</label>			
				<span id="thinking"><%=request.getAttribute("thinking") %></span>
			</div>
			<div class="col">
				<label for="judging">Judging:</label>		 
				<span id="judging"><%=request.getAttribute("judging") %></span>
			</div>							
		</div>	

		<div class="row">&nbsp;</div>
		
		<div class="row bg-light">
			<div class="col">
				<label for="extrovert">Extrovert:</label> 
				<span id="extrovert"><%=request.getAttribute("extrovert") %></span>
			</div>
			<div class="col">
				<label for="sensing">Sensing:</label> 
				<span id="sensing"><%=request.getAttribute("sensing") %></span>
			</div>
			<div class="col">
				<label for="feeling">Feeling:</label> 
				<span id="feeling"><%=request.getAttribute("feeling") %></span>
			</div>
			<div class="col">
				<label for="perceiving">Perceiving:</label> 
				<span id="perceiving"><%=request.getAttribute("perceiving") %></span>
			</div>							
		</div>
		
		<div class="row">&nbsp;</div>
		<%
		Iterator<JsonNode> glossesIter = ((ArrayNode)selectedActor.get("wikipediaGlosses")).iterator();
		for (int i=0;glossesIter.hasNext();i++) {
			JsonNode gloss = glossesIter.next();
		%>
		<div class="row bg-light">
			<div class="col">
				<label for="wikipedia_<%=i%>">Wikipedia:</label> <span id="wikipedia_<%=i%>"><%=gloss.asText("NA")%></span>
			</div>
		</div>
		<%
		}
		%>
		<div class="row">&nbsp;</div>		
		<%
		Iterator<JsonNode> quotesIter = ((ArrayNode)selectedActor.get("quotes")).iterator();
		for (int i=0;quotesIter.hasNext();i++) {
			JsonNode quote = quotesIter.next();
		%>
		<div class="row bg-light">
			<div class="col">
				<span><%=quote.asText("")%></span>
			</div>
		</div>
		<%
		}
		%>
		<div class="row">&nbsp;</div>

		
		<div class="row bg-light">
			<div class="col-md-6 text-center"><label>Nouns</label></div>
			<div class="col-md-6 text-center"><label>Noun Groups</label></div>
		</div>
		<div class="row">&nbsp;</div>

		<div class="row">
			<div class="col-md-6 text-center" id="nounsViz"></div>
			<div class="col-md-6 text-center" id="nounGroupsViz"></div>
		</div>				

		<div class="row">
			<div class="col-md-6 text-center" id="nounsTable"></div>
			<div class="col-md-6 text-center" id="nounGroupsTable"></div>			
		</div>
		
		<div class="row">&nbsp;</div>
		
		<div class="row bg-light">
			<div class="col-md-6 text-center"><label>Verbs</label></div>
			<div class="col-md-6 text-center"><label>Verb Groups</label></div>
		</div>

		<div class="row">&nbsp;</div>

		<div class="row">
			<div class="col-md-6 text-center" id="verbsViz"></div>
			<div class="col-md-6 text-center" id="verbGroupsViz"></div>
		</div>				

		<div class="row">
			<div class="col-md-6 text-center" id="verbsTable"></div>
			<div class="col-md-6 text-center" id="verbGroupsTable"></div>			
		</div>		

		<div class="row">&nbsp;</div>
		
		<div class="row bg-light">
			<div class="col-md-6 text-center"><label>Adjectives</label></div>
			<div class="col-md-6 text-center"><label>Adverbs</label></div>
		</div>

		<div class="row">&nbsp;</div>

		<div class="row">
			<div class="col-md-6 text-center" id="adjectivesViz"></div>
			<div class="col-md-6 text-center" id="adverbsViz"></div>
		</div>				

		<div class="row">
			<div class="col-md-6 text-center" id="adjectivesTable"></div>
			<div class="col-md-6 text-center" id="adverbsTable"></div>			
		</div>		

		<div class="row">&nbsp;</div>
<%
}
%>			
	</div>
</body>
</html>
