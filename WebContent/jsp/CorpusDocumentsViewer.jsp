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
<%@ page import="io.outofprintmagazine.web.util.JsonSort" %>
<!doctype html>
<html lang="en">
<head>
<jsp:include page="include/icon.jsp" />
<jsp:include page="include/meta.jsp" />
<jsp:include page="include/bootstrap.jsp" />

<title>Corpus Documents Viewer</title>
</head>
<body>
	<jsp:include page="include/navCorpus.jsp" />
	<jsp:include page="include/spacerRow.jsp" />
	<div class="container">	
		<ul class="list-group-flush">
		<%
		String corpus = request.getParameter("Corpus");
		ArrayNode documentsNode = (ArrayNode) request.getAttribute("documents");
		JsonSort.sortSimilarity(documentsNode);
		Iterator<JsonNode> documentsIter = documentsNode.elements();
		while (documentsIter.hasNext()) {
			JsonNode d = documentsIter.next();
			String corpusDocumentId = d.get("DocIDAnnotation").asText();
		%>
			<li class="list-group-item d-flex justify-content-between align-items-center">
		<%
			if (d.has("OOPThumbnailAnnotation")) {
				%>
				<img src="<%=d.get("OOPThumbnailAnnotation") %>" />
				<% 
			}
		%>
				<a href="rest/browse/Corpora/<%=request.getParameter("Corpus") %>/<%=corpusDocumentId %>/PIPELINE"><%=d.get("DocTitleAnnotation").asText() %></a>
    			<a href="TextViewer?Corpus=<%=corpus %>&Document=<%= corpusDocumentId %>">
    				<span class="badge badge-primary badge-pill">
    					<%= d.get("CorpusSimilarity").asText()%>
    				</span>
    			</a>
 			</li>
		<% 
		}
		%>
		</ul>
	</div>
	<jsp:include page="include/footerCorpus.jsp" />	
</body>
</html>
