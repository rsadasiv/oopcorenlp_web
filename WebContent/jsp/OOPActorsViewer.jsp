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
<jsp:include page="include/d3v3.jsp" />
<script src="js/oopcorenlp.js"></script>
<script src="js/d3.layout.cloud.js"></script>
<script src="js/d3.wordcloud.js"></script>
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
		makeActorCloud(actor, "OOPNounsAnnotation", "#nounsViz");
		makeActorCloud(actor, "OOPVerbsAnnotation", "#verbsViz");	
		makeActorCloud(actor, "OOPAdjectivesAnnotation", "#adjectivesViz");
		makeActorCloud(actor, "OOPAdverbsAnnotation", "#adverbsViz");
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

		<div class="row">&nbsp;</div>
<%
if (selectedActor.get("canonicalName") != null) {
%>

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
	</div>
	<jsp:include page="include/spacerRow.jsp" />	
	<div class="container-fluid">
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
				<div class="card">
					<div class="card-header">
		    			<h5 class="card-title">Gender</h5>
		    		</div>
					<div class="card-body">
		    			<p class="card-text"><label for="coreNlpGender" class="h5">CoreNlp Gender:</label> <span id="canonicalName"><%=selectedActor.get("coreNlpGender").asText().equals("")?"N/A":selectedActor.get("coreNlpGender").asText()%></span></p>
						<p class="card-text"><label for="oopgender" class="h5">OOP Gender:</label> <span id="oopgender"><%=selectedActor.get("oopgender").asText().equals("")?"N/A":selectedActor.get("oopgender").asText()%></span></p>
					</div>
				</div>
				<p>&nbsp;</p>
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
		    			<h5 class="card-title">Myers-Briggs Personality Type</h5>
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
	</div>
	<jsp:include page="include/spacerRow.jsp" />	
	<div class="container">
		<% 
		if (((ArrayNode)selectedActor.get("wikipediaGlosses")).size() > 0) {
		%>			
		<div class="row">&nbsp;</div>
		<div class="row bg-light">
			<div class="col">
				<h5>Wikipedia</h5>	
			<%

			Iterator<JsonNode> glossesIter = ((ArrayNode)selectedActor.get("wikipediaGlosses")).iterator();
			for (int i=0;glossesIter.hasNext();i++) {
				JsonNode gloss = glossesIter.next();
			%>
				<p><%=gloss.asText("NA")%></p>
			<%
			}
			%>
			</div>
		</div>			
		<%
		}
		%>
	
		<%
		Pattern extraQuote = Pattern.compile("^\"\\s*\"", Pattern.MULTILINE);
		if (((ArrayNode)selectedActor.get("quotes")).size() > 0) {
		%>
		<div class="row">&nbsp;</div>
		<div class="row bg-light">
			<div class="col">
				<h5>Quotes</h5>	
			<%
			Iterator<JsonNode> quotesIter = ((ArrayNode)selectedActor.get("quotes")).iterator();
			for (int i=0;quotesIter.hasNext();i++) {
				JsonNode quote = quotesIter.next();
	 			String q = quote.asText();
	 			if (q!=null) {
	 				q = extraQuote.matcher(q).replaceFirst("\"");
			%>
					<p><%=q.trim()%></p>

			<%
	 			}
			}
			%>
			</div>
		</div>
		<div class="row">&nbsp;</div>	
		<%
		}
		%>
		
		<div class="row bg-light">
			<div class="col-md-6 text-center"><label class="h5">Nouns</label></div>
			<div class="col-md-6 text-center"><label class="h5">Adjectives</label></div>
		</div>
		<div class="row">&nbsp;</div>

		<div class="row">
			<div class="col-md-6 text-center" id="nounsViz"></div>
			<div class="col-md-6 text-center" id="adjectivesViz"></div>
		</div>				
		
		<div class="row">&nbsp;</div>
		
		<div class="row bg-light">
			<div class="col-md-6 text-center"><label class="h5">Verbs</label></div>
			<div class="col-md-6 text-center"><label class="h5">Adverbs</label></div>
		</div>

		<div class="row">&nbsp;</div>

		<div class="row">
			<div class="col-md-6 text-center" id="verbsViz"></div>
			<div class="col-md-6 text-center" id="adverbsViz"></div>
		</div>				
		<div class="row">&nbsp;</div>
<%
}
%>			
	</div>
	<jsp:include page="include/footer.jsp" />
</body>
</html>
