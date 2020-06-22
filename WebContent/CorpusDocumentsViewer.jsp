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
<%@ page language="java" contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="com.fasterxml.jackson.databind.node.*" %>
<%@ page import="com.fasterxml.jackson.databind.*" %>
<%@ page import="java.util.*" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="description" content="">
<link rel="apple-touch-icon" sizes="180x180" href="images/apple-touch-icon.png">
<link rel="icon" type="image/png" sizes="32x32" href="images/favicon-32x32.png">
<link rel="icon" type="image/png" sizes="16x16" href="images/favicon-16x16.png">
<link rel="manifest" href="images/site.webmanifest">
<!-- Bootstrap CSS -->
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	crossorigin="anonymous">
<link 
	href="js/jquery-ui-themes-1.12.1/jquery-ui.css" 
	rel="stylesheet"
	crossorigin="anonymous">

<style>
.logo {
	width: 311px;
	height: 68px;
	display: block;
	text-indent:-6000px;
	margin: 25px auto 59px auto;
	cursor: pointer;
}
</style>
</head>
<body>
<a href="index.html"><img src="images/OOP/logo.png" class="logo" /></a>
		
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
String pCorpus = request.getParameter("Corpus");
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
 			<p><a href="Corpora/<%= pCorpus %>/PIPELINE_<%= corpusDocumentId %>.json">Pipeline info</a></p>  			
   		</th>
   		<td>
   			<p><a href="Corpora/<%= pCorpus %>/TXT_<%= corpusDocumentId %>.txt">Plain Text</a></p>
			<p><a href="TextOutOfPrintViewer?Corpus=<%= pCorpus %>&Document=<%= corpusDocumentId %>">Out Of Print</a></p>
			<p><a href="TextMediumViewer?Corpus=<%= pCorpus %>&Document=<%= corpusDocumentId %>">Medium</a></p>
			<p><a href="Corpora/<%= pCorpus %>/POLLY_<%= corpusDocumentId %>.mp3">Audio</a></p>
		</td>
		<td>
			<p><a href="OOPFactCheckerViewer?Corpus=<%= pCorpus %>&Document=<%= corpusDocumentId %>">Fact Checker</a></p>
			<p><a href="OOPEditorViewer?Corpus=<%= pCorpus %>&Document=<%= corpusDocumentId %>">Editor</a></p>
		</td>
		<td>
			<p><a href="OOPCloudViewer?Corpus=<%= pCorpus %>&Document=<%= corpusDocumentId %>">Word Clouds</a></p>
			<p><a href="OOPStreamViewer?Corpus=<%= pCorpus %>&Document=<%= corpusDocumentId %>">Sentences</a></p>
			<p><a href="OOPStreamViewerTokens?Corpus=<%= pCorpus %>&Document=<%= corpusDocumentId %>">Tokens</a></p>
		</td>
		<td>
			<p><a href="OOPDocumentViewer?Corpus=<%= pCorpus %>&Document=<%= corpusDocumentId %>">OOPCoreNLP</a></p>
			<p><a href="Corpora/<%= pCorpus %>/OOP_<%= corpusDocumentId %>.json">OOPCoreNLP json</a></p>
			<p><a href="StanfordBratViewer?Corpus=<%= pCorpus %>&Document=<%= corpusDocumentId %>">StanfordCoreNLP</a></p>
			<p><a href="Corpora/<%= pCorpus %>/STANFORD_<%= corpusDocumentId %>.json">StanfordCoreNLP json</a></p>			
		</td>
	</tr>
<%

}
%>
		</tbody>
	</table>
</div>
</body></html>
