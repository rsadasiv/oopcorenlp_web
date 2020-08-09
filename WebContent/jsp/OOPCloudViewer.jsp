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
<!doctype html>
<html lang="en">
<head>
<jsp:include page="include/icon.jsp" />
<jsp:include page="include/meta.jsp" />
<jsp:include page="include/bootstrap.jsp" />

<jsp:include page="include/d3v3.jsp" />
<script src="js/d3.layout.cloud.js"></script>
<script src="js/d3.wordcloud.js"></script>

<script src="js/oopcorenlp.js"></script>
<script src="js/OOPCloudViewer.js"></script>

<script>
$(document).ready(function() {
	getProperties()["docId"] = "<%=request.getParameter("Document")%>";
	getProperties()["corpus"] = "<%=request.getParameter("Corpus")%>";
	
	let annotation = "<%=request.getParameter("Annotation")==null||request.getParameter("Annotation").equals("")?"OOPNounsAnnotation":request.getParameter("Annotation")%>";
	$("#annotators").val(annotation);
	
	makeCloud(annotation, "#cloudViz");
	makeTable(annotation, "#tableViz");
	
	$('#annotators').change(
			function() {
		    	window.location.href = location.protocol + '//' + location.host + location.pathname + "?Corpus="+getProperties()["corpus"]+"&Document="+getProperties()["docId"]+"&Annotation=" + $('#annotators option:selected').val();
		    }
	);
});
</script>

<title>OOP Cloud Viewer</title>
</head>
<body>
	<jsp:include page="include/logo.jsp" />
	<div class="container">	
		<jsp:include page="include/divRowDocumentMetadata.jsp" />
		<div class="row">
			<div class="col-md-4"></div>
			<div class="col-md-4" id="annotatorPicker">
				<select id="annotators"	class="form-control" title="Select OOPCoreNLP annotator">
					<jsp:include page="include/optionAnnotators.jsp" />
				</select>
			</div>
			<div class="col-md-4 form-check">
			</div>
		</div>

	</div>
	
	<jsp:include page="include/spacerRow.jsp" />
	
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-2"></div>
			<div class="col-lg-8 text-center" id="cloudViz">

			</div>
			<div class="col-md-2"></div>
		</div>				
	</div>
		<div class="container-fluid">
		<div class="row">
			<div class="col-md-4"></div>
			<div class="col-md-4" id="tableViz">

			</div>
			<div class="col-md-4"></div>
		</div>				
	</div>
</body>
</html>
