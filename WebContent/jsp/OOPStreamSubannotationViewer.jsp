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
<%@ page import="com.fasterxml.jackson.databind.node.ObjectNode" %>
<!doctype html>
<html lang="en">
<head>
<jsp:include page="include/icon.jsp" />
<jsp:include page="include/meta.jsp" />
<jsp:include page="include/bootstrap.jsp" />
<jsp:include page="include/d3v5.jsp" />
<script src="js/oopcorenlp.js"></script>
<script src="js/OOPStreamSubannotationViewer.js"></script>

<%
ObjectNode stats = (ObjectNode) request.getAttribute("Stats");
ObjectNode annotationDescriptions = (ObjectNode) request.getAttribute("AnnotationDescriptions");
String selectedAnnotation = request.getParameter("Annotation")==null||request.getParameter("Annotation").equals("")?"VaderSentimentAnnotation":request.getParameter("Annotation");
int sentenceCount = (int)request.getAttribute("SentenceCount");
%>

<script>
$(document).ready(function() {
	getProperties()["docId"] = "<%=request.getParameter("Document")%>";
	getProperties()["corpus"] = "<%=request.getParameter("Corpus")%>";
	
	let annotation = "<%=request.getParameter("Annotation")==null||request.getParameter("Annotation").equals("")?"VaderSentimentAnnotation":request.getParameter("Annotation")%>";	
	makeSentenceBarChart(annotation, "<%=request.getParameter("Subannotation")%>", "#sentenceAnnotationViz", 0);

});
</script>

<title>OOP Subannotation Stream Viewer</title>	
</head>
<body>
	<jsp:include page="include/nav.jsp" />
	<jsp:include page="include/spacerRow.jsp" />
	<div class="container">
		<div class="row">
			<div class="col-md-4"></div>
			<div class="col-md-4 justify-content-center">
				<p><%=selectedAnnotation %></p>
				<p><%=annotationDescriptions.get(selectedAnnotation).asText() %></p>
				<p>
					<a id="sentenceAnnotationVizDataLink" target="_blank">
						Data
					</a>
				</p>
			</div>
			<div class="col-md-4"></div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<p class="text-center font-italic" id="sentenceAnnotationVizText">
					&nbsp;
				</p>
			</div>
		</div>
	</div>
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12" id="sentenceViz">
				<svg width="<%=sentenceCount>1600?new Integer(sentenceCount+100).toString():"1600"%>" height="400" id="sentenceAnnotationViz"></svg>
			</div>
		</div>
	</div>			
	<jsp:include page="include/footer.jsp" />
</body>
</html>
