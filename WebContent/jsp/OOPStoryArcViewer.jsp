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
	
    $(document).ready(function() {
		setProperties();

		makeSentenceBarChart("OOPFleschKincaidAnnotation", "#OOPFleschKincaidAnnotationViz", "#OOPFleschKincaidAnnotationSentenceText");
		makeSentenceBarChart("VaderSentimentAnnotation", "#VaderSentimentAnnotationViz", "#VaderSentimentAnnotationSentenceText");
		makeSentenceBarChart("OOPPeopleAnnotation", "#OOPPeopleAnnotationViz", "#OOPPeopleAnnotationSentenceText");		
		makeSentenceBarChart("OOPLocationsAnnotation", "#OOPLocationsAnnotationViz", "#OOPLocationsAnnotationSentenceText");
    });
</script>

<title>OOP Story Arc Viewer</title>
</head>
<body>
	<jsp:include page="include/logo.jsp" />
	<div class="container">	
		<jsp:include page="include/documentMetadata.jsp" />
	</div>
	<jsp:include page="include/spacerRow.jsp" />
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-4"></div>
			<div class="col-md-4 text-center">
				<h6>
					Estimated reading time:
					<%=request.getAttribute("ReadingTime").toString() %>
					minutes
				</h6>
				<h6>
					Estimated listening time:
					<%=request.getAttribute("ListeningTime").toString() %>
					minutes
				</h6>
				<h6>
					Reading level:
					<%=request.getAttribute("ReadingLevel").toString() %>
				</h6>
				<h6>
					Happy/Sad:
					<%=request.getAttribute("EmotionalLevel").toString() %>
				</h6>
			</div>
			<div class="col-md-4"></div>
		</div>
	</div>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<p class="text-center font-italic" id="OOPFleschKincaidAnnotationSentenceText">
					&nbsp;
				</p>
			</div>
		</div>
	</div>
	<div class="container-fluid">
			<div class="row">
				<div class="col-lg-12" id="pace">
				<svg width="1600" height="400" id="OOPFleschKincaidAnnotationViz"></svg>
			</div>
		</div>
	</div>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<p class="text-center font-italic" id="VaderSentimentAnnotationSentenceText">
					&nbsp;
				</p>
			</div>
		</div>
	</div>	
	<div class="container-fluid">
			<div class="row">
				<div class="col-lg-12" id="VaderEmotionalArc">
				<svg width="1600" height="400" id="VaderSentimentAnnotationViz"></svg>
			</div>
		</div>
	</div>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<p class="text-center font-italic" id="OOPPeopleAnnotationSentenceText">
					&nbsp;
				</p>
			</div>
		</div>
	</div>	
	<div class="container-fluid">
			<div class="row">
				<div class="col-lg-12" id="People">
				<svg width="1600" height="400" id="OOPPeopleAnnotationViz"></svg>
			</div>
		</div>
	</div>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<p class="text-center font-italic" id="OOPLocationsAnnotationSentenceText">
					&nbsp;
				</p>
			</div>
		</div>
	</div>		
	<div class="container-fluid">
			<div class="row">
				<div class="col-lg-12" id="Locations">
				<svg width="1600" height="400" id="OOPLocationsAnnotationViz"></svg>
			</div>
		</div>
	</div>
</body>
</html>
