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
<%@ page import="java.util.concurrent.ThreadLocalRandom" %>
<%@ page import="java.util.regex.Pattern" %>
<%@ page import="com.fasterxml.jackson.databind.JsonNode" %>
<%@ page import="com.fasterxml.jackson.databind.node.ArrayNode" %>
<%@ page import="io.outofprintmagazine.web.util.JsonSort" %>
<!doctype html>
<html lang="en">
<head>
<jsp:include page="include/icon.jsp" />
<jsp:include page="include/meta.jsp" />
<jsp:include page="include/bootstrap.jsp" />
<jsp:include page="include/vega-lite.jsp" />
<script src="js/oopcorenlp.js"></script>
<script src="js/OOPActorsViewer.js"></script>

<%
String actor = request.getParameter("Actor")==null||request.getParameter("Actor").equals("")?"":request.getParameter("Actor");
JsonNode selectedActor = (JsonNode)request.getAttribute("SelectedActor");
if (selectedActor != null && selectedActor.has("canonicalName")) {
	actor = selectedActor.get("canonicalName").asText();
}
%>

<script>
$(document).ready(function() {
	getProperties()["docId"] = "<%=request.getParameter("Document")%>";
	getProperties()["corpus"] = "<%=request.getParameter("Corpus")%>";
	let actor = "<%=actor%>";
	if (actor != "") {
		makeActorCloud(actor, "#cloudViz");
	}
	
	$('#actors').change(
			function() {
		    	window.location.href = location.protocol + '//' + location.host + location.pathname + "?Corpus="+getProperties()["corpus"]+"&Document="+getProperties()["docId"]+"&Actor=" + $('#actors option:selected').val();
		    }
	);
});
</script>

	
<title>OOP Actors Viewer</title>
</head>
<body>
	<jsp:include page="include/nav.jsp" />
	<div class="container">	
		<jsp:include page="include/spacerRow.jsp" />
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
	</div>

<%
if (selectedActor.get("canonicalName") != null) {
%>
	<div class="container-fluid">	
		<jsp:include page="include/spacerRow.jsp" />
		<div class="row">
			<div class="col-md-2"></div>
			<div id="carouselExampleIndicators" class="carousel slide col-md-8 text-center bg-dark" data-ride="carousel">
				<div class="carousel-inner">
					<%
						int thumbnailCount = 10;
						int imageCount = 0;
						boolean markActive = true;
						ArrayNode thumbnails = (ArrayNode) selectedActor.get("thumbnails");
						if (thumbnails != null && thumbnails.size() > 0) {
							ThreadLocalRandom random = ThreadLocalRandom.current();
							for (int j=0;j<thumbnailCount&&j<thumbnails.size();j++) {
								int idx = random.nextInt(0, thumbnails.size());
						%>		
								<div class="carousel-item<%=markActive?" active":""%>">
									<img class="d-block w-100" src="<%=thumbnails.get(idx).asText() %>" alt="<%=thumbnails.get(idx).asText() %>">
										<div class="carousel-caption d-none d-md-block">
											<p><%=selectedActor.get("canonicalName").asText()%></p>
										</div>
								</div>
						<%
								imageCount++;
								markActive = false;
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

		<jsp:include page="include/spacerRow.jsp" />	
		<div class="row">
			<div class="col">
				<div class="card">
					<div class="card-header">
		    			<h5 class="card-title">Info</h5>
		    		</div>
					<div class="card-body">
		    			<p class="card-text"><label for="canonicalName" class="h5">Canonical Name:</label> <span id="canonicalName"><%=selectedActor.get("canonicalName").asText("")%></span></p>
						<p class="card-text"><label for="importance" class="h5">Importance:</label> <span id="importance"><%=selectedActor.get("importance").asText("")%></span></p>
						<p class="card-text"><label for="firstAppearance" class="h5">First Appearance:</label> <span id="firstAppearance"><%=selectedActor.get("firstAppearance").asText("")%></span></p>
						<p class="card-text"><label for="lastAppearance" class="h5">Last Appearance:</label> <span id="lastAppearance"><%=selectedActor.get("lastAppearance").asText("")%></span></p>
					</div>
				</div>
			</div>

			<div class="col">
			<%		
			if ((!selectedActor.get("coreNlpGender").asText().equals("")) || (!selectedActor.get("oopgender").asText().equals(""))) {
			%>	
				<div class="card">
					<div class="card-header">
		    			<h5 class="card-title">Gender</h5>
		    		</div>
					<div class="card-body">
					<%
					if (!selectedActor.get("coreNlpGender").asText().equals("")) {
					%>
		    			<p class="card-text"><label for="coreNlpGender" class="h5">CoreNlp Gender:</label> <span id="canonicalName"><%=selectedActor.get("coreNlpGender").asText().equals("")?"N/A":selectedActor.get("coreNlpGender").asText()%></span></p>
		    		<%
		    		}
					if (!selectedActor.get("oopgender").asText().equals("")) {
		    		%>
						<p class="card-text"><label for="oopgender" class="h5">OOP Gender:</label> <span id="oopgender"><%=selectedActor.get("oopgender").asText().equals("")?"N/A":selectedActor.get("oopgender").asText()%></span></p>
					<%
					}
					%>
					</div>
				</div>
				<p>&nbsp;</p>
			<%
			}
			 %>
				<div class="card">
					<div class="card-header">
		    			<h5 class="card-title">Sentiment</h5>
		    		</div>
					<div class="card-body">
						<p class="card-text"><label for="coreNlpSentimentAvg" class="h5">CoreNlp Sentiment:</label> <span id="coreNlpSentimentAvg"><%=String.format("%.0f", selectedActor.get("coreNlpSentimentAvg").asDouble(0)*100)%></span></p>
						<p class="card-text"><label for="vaderSentimentAvg" class="h5">Vader Sentiment:</label> <span id="vaderSentimentAvg"><%=String.format("%.0f", selectedActor.get("vaderSentimentAvg").asDouble(0)*100)%></span></p>
					</div>
				</div>
			</div>

			<div class="col">
				<div class="card">
					<div class="card-header">
		    			<h5 class="card-title">Myers-Briggs Personality Type: <%=request.getAttribute("myersBriggs") %></h5>
		    		</div>
					<div class="card-body">
		    			<p class="card-text"><label for="introvertExtrovert" class="h5">Introvert/Extrovert:</label> <span id="introvertExtrovert"><%=request.getAttribute("introvert") %>/<%=request.getAttribute("extrovert") %></span></p>
						<p class="card-text"><label for="intuitiveSensing" class="h5">Intuitive/Sensing:</label> <span id="intuitive"><%=request.getAttribute("intuitive") %>/<%=request.getAttribute("sensing") %></span></p>
		    			<p class="card-text"><label for="thinkingFeeling" class="h5">Thinking/Feeling:</label> <span id="thinking"><%=request.getAttribute("thinking") %>/<%=request.getAttribute("feeling") %></span></p>
						<p class="card-text"><label for="judgingPerceiving" class="h5">Judging/Perceiving:</label> <span id="judging"><%=request.getAttribute("judging") %>/<%=request.getAttribute("perceiving") %></span></p>						
					</div>
				</div>
			</div>
		</div>


		<% 
		if (((ArrayNode)selectedActor.get("wikipediaGlosses")).size() > 0) {
		%>			
		<jsp:include page="include/spacerRow.jsp" />	
		<div class="row">
			<div class="col">
				<div class="card">
					<div class="card-header">
		    			<h5 class="card-title">Wikipedia</h5>
		    		</div>
					<div class="card-body">	
					<%
					Iterator<JsonNode> glossesIter = ((ArrayNode)selectedActor.get("wikipediaGlosses")).iterator();
					for (int i=0;glossesIter.hasNext();i++) {
						JsonNode gloss = glossesIter.next();
					%>
						<p class="card-text"><%=gloss.asText("NA")%></p>
					<%
					}
					%>
					</div>
				</div>
			</div>
		</div>			
		<%
		}
		%>
		
		<jsp:include page="include/spacerRow.jsp" />
		<div class="row">
			<div class="col">
				<div class="card">
					<div class="card-header">
		    			<h5 class="card-title">Nouns</h5>
		    		</div>
					<div class="card-body">
		    			<%
						for (JsonNode datum : (JsonNode)request.getAttribute("OOPNounsAnnotation")) {
		    			%>
							<p class="card-text"><label for="nouns_<%=datum.get("name").asText() %>" class="h5"><%=datum.get("name").asText() %>:</label> <span id="nouns_<%=datum.get("name").asText() %>"><%=datum.get("value").asText() %></span></p>
		    			<%
		    			}
		    			%>
					</div>
				</div>
			</div>
			<div class="col">
				<div class="card">
					<div class="card-header">
		    			<h5 class="card-title">Adjectives</h5>
		    		</div>
					<div class="card-body">
		    			<%
						for (JsonNode datum : (JsonNode)request.getAttribute("OOPAdjectivesAnnotation")) {
		    			%>
							<p class="card-text"><label for="adjectives_<%=datum.get("name").asText() %>" class="h5"><%=datum.get("name").asText() %>:</label> <span id="adjectives_<%=datum.get("name").asText() %>"><%=datum.get("value").asText() %></span></p>
		    			<%
		    			}
		    			%>
					</div>
				</div>
			</div>
			<div class="col">
				<div class="card">
					<div class="card-header">
		    			<h5 class="card-title">Adverbs</h5>
		    		</div>
					<div class="card-body">
		    			<%
						for (JsonNode datum : (JsonNode)request.getAttribute("OOPAdverbsAnnotation")) {
		    			%>
							<p class="card-text"><label for="adverbs_<%=datum.get("name").asText() %>" class="h5"><%=datum.get("name").asText() %>:</label> <span id="adverbs_<%=datum.get("name").asText() %>"><%=datum.get("value").asText() %></span></p>
		    			<%
		    			}
		    			%>
					</div>
				</div>
			</div>					
			<div class="col">
				<div class="card">
					<div class="card-header">
		    			<h5 class="card-title">Verbs</h5>
		    		</div>
					<div class="card-body">
		    			<%
						for (JsonNode datum : (JsonNode)request.getAttribute("OOPVerbsAnnotation")) {
		    			%>
							<p class="card-text"><label for="verbs_<%=datum.get("name").asText() %>" class="h5"><%=datum.get("name").asText() %>:</label> <span id="verbs_<%=datum.get("name").asText() %>"><%=datum.get("value").asText() %></span></p>
		    			<%
		    			}
		    			%>
					</div>
				</div>
			</div>		
		</div>

		<%
		Pattern extraQuote = Pattern.compile("^\"\\s*\"", Pattern.MULTILINE);
		if (((ArrayNode)selectedActor.get("quotes")).size() > 0) {
		%>
		<jsp:include page="include/spacerRow.jsp" />
		<div class="row">
			<div class="col">
				<div class="card">
					<div class="card-header">
		    			<h5 class="card-title">Quotes</h5>
		    		</div>
					<div class="card-body">	
				<%
				Iterator<JsonNode> quotesIter = ((ArrayNode)selectedActor.get("quotes")).iterator();
				for (int i=0;quotesIter.hasNext();i++) {
					JsonNode quote = quotesIter.next();
		 			String q = quote.asText();
		 			if (q!=null) {
		 				q = extraQuote.matcher(q).replaceFirst("\"");
				%>
						<p class="card-text"><%=q.trim()%></p>
	
				<%
		 			}
				}
				%>
					</div>
				</div>
			</div>
		</div>
		<%
		}
		%>
		
		<jsp:include page="include/spacerRow.jsp" />			
		<div class="row justify-content-center">
			<div class="col justify-content-center" id="cloudViz">
			</div>
		</div>

	</div>
	
	
<%
}
%>			

	<jsp:include page="include/footer.jsp" />
</body>
</html>
