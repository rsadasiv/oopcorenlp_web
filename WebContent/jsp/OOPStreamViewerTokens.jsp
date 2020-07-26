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
<jsp:include page="include/d3v5.jsp" />
<script src="js/oopcorenlp_d3_viewer.js"></script>

<script>
	var docId = "<%=request.getParameter("Document")%>";
	var corpus = "<%=request.getParameter("Corpus")%>";
	var annotation = "<%=request.getParameter("Annotation")==null||request.getParameter("Annotation").equals("")?"OOPPeopleAnnotation":request.getParameter("Annotation")%>";


    $(document).ready(function() {
        setProperties();
    	drawTokenAnnotators(annotation);

      	makeTokenBarChart(annotation, "#AnnotationViz_1");
      	makeTokenScoreBarCharts(annotation, "#AnnotationViz");
		$('#annotators').change(
				function() {
			    	window.location.href = location.protocol + '//' + location.host + location.pathname + "?Analysis=StreamTokens&Corpus="+ properties.corpus+"&Document=" + properties.docId + "&Annotation=" + $('#annotators option:selected').val();
			    }
			); 
        

    });
</script>
<title>OOP Annotation Tokens Stream Viewer</title>	
</head>
<body>
	<jsp:include page="include/logo.jsp" />
	<div class="container">	
		<jsp:include page="include/documentMetadata.jsp" />
		<div class="row">
			<div class="col-md-4"></div>
			<div class="col-md-4" id="annotatorPicker">
				<select id="annotators"	class="form-control" title="Select OOPCoreNLP annotator">

				</select>
			</div>
			<div class="col-md-4"></div>
		</div>

	</div>
	<jsp:include page="include/spacerRow.jsp" />
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<p class="text-center font-italic" id="sentenceText">
					&nbsp;
				</p>
			</div>
		</div>
	</div>
	<div class="container-fluid">
		<div class="row">
			<div class="col-sm-2" id="viz_1">
				<svg width="300" height="2400" id="AnnotationViz_1"></svg>
			</div>
			<div class="col-sm-2" id="viz_2">
				<svg width="300" height="2400" id="AnnotationViz_2"></svg>
			</div>
			<div class="col-sm-2" id="viz_3">
				<svg width="300" height="2400" id="AnnotationViz_3"></svg>
			</div>
			<div class="col-sm-2" id="viz_4">
				<svg width="300" height="2400" id="AnnotationViz_4"></svg>
			</div>
			<div class="col-sm-2" id="viz_5">
				<svg width="300" height="2400" id="AnnotationViz_5"></svg>
			</div>
			<div class="col-sm-2" id="viz_6">
				<svg width="300" height="2400" id="AnnotationViz_6"></svg>
			</div>													
		</div>				
	</div>
</body>
</html>
