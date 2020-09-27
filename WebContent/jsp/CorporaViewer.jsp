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
<title>OOP Corpora</title>
</head>
<body>
	<jsp:include page="include/logo.jsp" />
	<div class="container">	
		<div class="row">
			<div class="col-md-4"></div>
			<div class="col-md-4 text-center">
			<%
			ArrayNode corporaNode = (ArrayNode)request.getAttribute("corpora");
			Iterator<JsonNode> corporaDocumentIter = corporaNode.elements();
			while (corporaDocumentIter.hasNext()) {
				String corpus = corporaDocumentIter.next().asText();
				%>
				<p>
					<a href="CorpusDocumentsViewer?Corpus=<%=corpus%>"><%=corpus %></a>
				</p>
				<%
			}
			%>
			</div>
			<div class="col-md-4"></div>
		</div>
	</div>
</body>
</html>
