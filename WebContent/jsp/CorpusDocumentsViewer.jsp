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
<%@ page import="com.fasterxml.jackson.databind.node.*" %>
<%@ page import="com.fasterxml.jackson.databind.*" %>
<%@ page import="java.util.*" %>
<!doctype html>
<html lang="en">
<head>
<jsp:include page="include/icon.jsp" />
<jsp:include page="include/meta.jsp" />
<jsp:include page="include/bootstrap.jsp" />

<title>Corpus Documents Viewer</title>
</head>
<body>
	<jsp:include page="include/logo.jsp" />
		
<div class="container-fluid">
	<table class="table table-hover">
		<thead>
			<tr>
				<th scope="col"></th>
				<th scope="col">Metadata</th>
				<th scope="col">Read</th>
				<th scope="col">Interact</th>
				<th scope="col">Visualize</th>
				<th scope="col">Analyze</th>
			</tr>
		</thead>
		<tbody>
<%
String corpus = request.getParameter("Corpus");
ObjectNode json = (ObjectNode) request.getAttribute("corpora");
ArrayNode corporaNode = (ArrayNode) json.get("Documents");
Iterator<JsonNode> corporaDocumentIter = corporaNode.elements();
while (corporaDocumentIter.hasNext()) {
	JsonNode d = corporaDocumentIter.next();
	String corpusDocumentId = d.get("DocID").asText();
%>
   	<tr>
   		<td scope="row">
   			<%
   				if (d.has("Thumbnail")) {
   					%>
   					<img src="<%=d.get("Thumbnail") %>" />
   					<% 
   				}
   			%>
   		</td>
   		<th scope="row">
   			<p>DocID: <%= corpusDocumentId %></p>
   			<p>Title: <%= d.get("Title").asText()%></p>
   			<p>Author: <%= d.get("Author").asText()%></p>
   			<p>Date: <%= d.get("Date").asText()%></p> 
 			<p><a href="Corpora/<%= corpus %>/PIPELINE_<%= corpusDocumentId %>.json">Pipeline info</a></p>  			
   		</th>
   		<td>
   			<p><a href="Corpora/<%= corpus %>/TXT_<%= corpusDocumentId %>.txt">Plain Text</a></p>
			<p><a href="TextOutOfPrintViewer?Corpus=<%= corpus %>&Document=<%= corpusDocumentId %>">Out Of Print</a></p>
			<p><a href="TextMediumViewer?Corpus=<%= corpus %>&Document=<%= corpusDocumentId %>">Medium</a></p>
			<p><a href="Corpora/<%= corpus %>/POLLY_<%= corpusDocumentId %>.mp3">Audio</a></p>
		</td>
		<td>
			<p><a href="OOPStoryArcViewer?Corpus=<%= corpus %>&Document=<%= corpusDocumentId %>">Story Arc</a></p>
			<p><a href="OOPPullQuotesViewer?Corpus=<%= corpus %>&Document=<%= corpusDocumentId %>">Pull Quotes</a></p>			
			<p><a href="OOPFactCheckerViewer?Corpus=<%= corpus %>&Document=<%= corpusDocumentId %>">Fact Checker</a></p>
			<p><a href="OOPEditorViewer?Corpus=<%= corpus %>&Document=<%= corpusDocumentId %>">Editor</a></p>
		</td>
		<td>
			<p><a href="OOPActorsViewer?Corpus=<%= corpus %>&Document=<%= corpusDocumentId %>">Actors</a></p>
			<p><a href="OOPCloudViewer?Corpus=<%= corpus %>&Document=<%= corpusDocumentId %>">Word Clouds</a></p>
			<p><a href="OOPStreamViewer?Corpus=<%= corpus %>&Document=<%= corpusDocumentId %>">Sentences</a></p>
			<p><a href="OOPStreamViewerTokens?Corpus=<%= corpus %>&Document=<%= corpusDocumentId %>">Tokens</a></p>
		</td>
		<td>
			<p><a href="OOPDocumentViewer?Corpus=<%= corpus %>&Document=<%= corpusDocumentId %>">OOPCoreNLP</a></p>
			<p><a href="Corpora/<%= corpus %>/OOP_<%= corpusDocumentId %>.json">OOPCoreNLP json</a></p>
			<p><a href="StanfordBratViewer?Corpus=<%= corpus %>&Document=<%= corpusDocumentId %>">StanfordCoreNLP</a></p>
			<p><a href="Corpora/<%= corpus %>/STANFORD_<%= corpusDocumentId %>.json">StanfordCoreNLP json</a></p>			
		</td>
	</tr>
<%

}
%>
		</tbody>
	</table>
</div>
</body>
</html>
