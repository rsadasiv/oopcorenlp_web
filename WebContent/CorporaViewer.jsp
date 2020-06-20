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
<%@ page import="com.fasterxml.jackson.databind.node.*" %>
<%@ page import="com.fasterxml.jackson.databind.*" %>
<%@ page import="java.util.*" %>
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
	</style>
	<title>OOP Corpora</title>
</head>
<body>
	<a href="index.html"><img src="images/OOP/logo.png" class="logo" /></a>
	<div class="container">	
		<div class="row">
			<div class="col-md-4"></div>
			<div class="col-md-4 text-center">
			<%
			ObjectNode json = (ObjectNode) request.getAttribute("corpora");
			ArrayNode corporaNode = (ArrayNode) json.get("Corpora");
			Iterator<JsonNode> corporaDocumentIter = corporaNode.elements();
			while (corporaDocumentIter.hasNext()) {
				String corpusId = corporaDocumentIter.next().asText();
				%>
				<p">
					<a href="CorpusDocumentsViewer?Corpus=<%=corpusId%>"><%=corpusId %></a>
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
