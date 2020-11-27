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
<jsp:include page="include/vega-lite.jsp" />
<script src="js/oopcorenlp.js"></script>
<script src="js/OOPCorpusAnnotationCVScoresViewer.js"></script>

<%
ObjectNode stats = (ObjectNode) request.getAttribute("Stats");
ObjectNode annotationDescriptions = (ObjectNode) request.getAttribute("AnnotationDescriptions");
ArrayNode corpora = (ArrayNode) request.getAttribute("corpora");
String selectedCorpus = request.getParameter("Corpus");
String selectedAnnotation = request.getParameter("Annotation")==null||request.getParameter("Annotation").equals("")?"OOPBiberDimensionsAnnotation":request.getParameter("Annotation");
request.setAttribute("Annotation", selectedAnnotation);
String selectedAggregate = (request.getParameter("AggregateName")==null||request.getParameter("AggregateName").equals(""))?"normalized":request.getParameter("AggregateName");
%>

<script>
$(document).ready(function() {
	getProperties()["docId"] = "<%=request.getParameter("Document")%>";
	getProperties()["corpus"] = "<%=request.getParameter("Corpus")%>";
	
	makeAnnotationsCVScoresBarChart("<%=selectedCorpus%>", "<%=selectedAnnotation%>", "<%=selectedAggregate%>",  "#corpusAnnotationCVScoresViz");

	$('#corpus').change(
		function() {
	    	window.location.href = location.protocol + '//' + location.host + location.pathname + "?Corpus="+ $('#corpus option:selected').val()+"&Annotation=" + $('#annotators option:selected').val()+"&AggregateName=" + $('#aggregateName option:selected').val();
	    }
	);
	
	$('#annotators').change(
			function() {
		    	window.location.href = location.protocol + '//' + location.host + location.pathname + "?Corpus="+ $('#corpus option:selected').val()+"&Annotation=" + $('#annotators option:selected').val()+"&AggregateName=" + $('#aggregateName option:selected').val();
		    }
		);
	$('#aggregateName').change(
			function() {
		    	window.location.href = location.protocol + '//' + location.host + location.pathname + "?Corpus="+ $('#corpus option:selected').val()+"&Annotation=" + $('#annotators option:selected').val()+"&AggregateName=" + $('#aggregateName option:selected').val();
		    }
		);
});
</script>

<title>OOP Corpus Annotation CVScores Viewer</title>	
</head>
<body>
	<jsp:include page="include/navCorpus.jsp" />
	<div class="container">	
		<jsp:include page="include/spacerRow.jsp" />
		<div class="row justify-content-center">
			<div class="col-md-4" id="targetCorpusPicker">
				<select id="corpus" class="form-control" title="Select target corpus">
					<%
					
					ArrayNode corporaNode = (ArrayNode)request.getAttribute("corpora");
					Iterator<JsonNode> corporaDocumentIter = corporaNode.elements();
					while (corporaDocumentIter.hasNext()) {
						String corpus = corporaDocumentIter.next().asText();
					%>
					<option value="<%=corpus %>" <%=corpus.equals(selectedCorpus)?"selected":"" %>><%=corpus %></option>
					<%
					}
					%>
				</select>
			</div>
		</div>
	</div>
	<div class="container">	
		<jsp:include page="include/spacerRow.jsp" />
		<div class="row justify-content-center">
			<div class="col-md-4" id="annotatorPicker">
				<select id="annotators"	class="form-control" title="Select OOPCoreNLP annotator">
					<jsp:include page="include/optionAnnotators.jsp" />
				</select>
			</div>
		</div>
	</div>
	<div class="container">	
		<jsp:include page="include/spacerRow.jsp" />
		<div class="row justify-content-center">
			<div class="col-md-4" id="aggregatePicker">
				<select id="aggregateName" class="form-control" title="Select aggregate name">
					<option value="normalized" <%="normalized".equals(selectedAggregate)?"selected":"" %>>normalized</option>
					<option value="count" <%="count".equals(selectedAggregate)?"selected":"" %>>count</option>
					<option value="raw" <%="raw".equals(selectedAggregate)?"selected":"" %>>raw</option>
					<option value="rank" <%="rank".equals(selectedAggregate)?"selected":"" %>>rank</option>
					<option value="percentage" <%="percentage".equals(selectedAggregate)?"selected":"" %>>percentage</option>
					<option value="percentile" <%="percentile".equals(selectedAggregate)?"selected":"" %>>percentile</option>										
				</select>
			</div>
		</div>
	</div>

	<jsp:include page="include/spacerRow.jsp" />

	<div class="container">
		<div class="row justify-content-center">
			<div class="col-md-12" id="corpusAnnotationCVScoresViz">

			</div>
		</div>
	</div>		
	<jsp:include page="include/footerCorpus.jsp" />
</body>
</html>
