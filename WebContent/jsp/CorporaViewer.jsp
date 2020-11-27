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
<title>Corpora Viewer</title>
</head>
<body>
	<nav class="navbar navbar-expand-lg navbar-light bg-light" style="padding-right: 100px">
		<a class="navbar-brand" href="index.html">
			<img src="images/favicon-32x32.png" width="32" height="32" alt="Home">
		</a>
		<div class="collapse navbar-collapse" id="navbarNavDropdown">
			<ul class="navbar-nav nav-pills">
	            <li class="nav-item">
					<span class="navbar-brand navbar-text text-capitalize">
				   		Corpora
				   	</span>
	            </li>
	        </ul>
		</div>
	</nav>
	<jsp:include page="include/spacerRow.jsp" />	
	<div class="container">	
		<ul class="list-group-flush">
	<%
	ArrayNode corporaNode = (ArrayNode)request.getAttribute("corpora");
	ObjectNode batchesNode = (ObjectNode)request.getAttribute("batches");
	Iterator<JsonNode> corporaDocumentIter = corporaNode.elements();
	while (corporaDocumentIter.hasNext()) {
		String corpus = corporaDocumentIter.next().asText();
		ObjectNode batch = (ObjectNode) batchesNode.get(corpus);
		if (batch != null) {
		%>
			<li class="list-group-item d-flex justify-content-between align-items-center">
				<a href="rest/browse/Corpora/<%=corpus %>/Batch"><%=corpus%></a>
				<a href="CorpusDocumentsViewer?Corpus=<%=corpus %>">
				    <span class="badge badge-primary badge-pill">
    					<%=batch.get("corpusBatchSteps").get(batch.get("corpusBatchSteps").size()-1).get("output").size() %>
    				</span>
    			</a>
  			</li>
		<%
		}
	}
	%>
		</ul>
	</div>
</body>
</html>
