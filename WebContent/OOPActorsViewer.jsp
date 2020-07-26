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
<%@ page import="java.util.Iterator" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.math.RoundingMode" %>
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

<!-- Bootstrap CSS -->
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	crossorigin="anonymous">

<link rel="stylesheet" 
	href="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.css" 
	integrity="sha256-rByPlHULObEjJ6XQxW/flG2r+22R5dKiAoef+aXWfik=" 
	crossorigin="anonymous" />
<link rel="stylesheet" 
	href="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.theme.min.css" 
	integrity="sha256-AjyoyaRtnGVTywKH/Isxxu5PXI0s4CcE0BzPAX83Ppc=" 
	crossorigin="anonymous" />
	
<title>OOP Actors Viewer</title>


<!-- JQuery -->

<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.js" crossorigin="anonymous">
	</script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.js" crossorigin="anonymous">
	</script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
	integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
	crossorigin="anonymous">
	</script>
<script
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
	integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
	crossorigin="anonymous">
	</script>
	
	<script 
		src="https://cdnjs.cloudflare.com/ajax/libs/d3/3.5.17/d3.min.js"
		crossorigin="anonymous">
	</script>

	<script src="js/d3.layout.cloud.js"></script>
	<script src="js/d3.wordcloud.js"></script>
	<script>
		var docId = "<%=request.getParameter("Document")%>";
		var corpus = "<%=request.getParameter("Corpus")%>";
		var actor = "<%=request.getParameter("Actor")==null||request.getParameter("Actor").equals("")?"":request.getParameter("Actor")%>";
	</script>
	<% 
	JsonNode selectedActor = (JsonNode)request.getAttribute("SelectedActor");
	%>
	<script src="js/oopcorenlp_d3_viewer.js">
		
	</script>

	<script>
	function type(d) {
		  d.value = +d.value; // coerce to number
		  return d;
	}
	
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
