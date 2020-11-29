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
<%@ page import="com.fasterxml.jackson.databind.JsonNode" %>
<!doctype html>
<html lang="en">
<head>
<jsp:include page="include/icon.jsp" />
<jsp:include page="include/meta.jsp" />
<jsp:include page="include/bootstrap.jsp" />
<jsp:include page="include/vega-lite.jsp" />
<script src="js/oopcorenlp.js"></script>
<script src="js/OOPStoryArcViewer.js"></script>

<%
ObjectNode annotationDescriptions = (ObjectNode) request.getAttribute("AnnotationDescriptions");
ObjectNode stats = (ObjectNode) request.getAttribute("Stats");

/*
 * 
 * 100.00–90.00	5th grade	Very easy to read. Easily understood by an average 11-year-old student.
 * 90.0–80.0	6th grade	Easy to read. Conversational English for consumers.
 * 80.0–70.0	7th grade	Fairly easy to read.
 * 70.0–60.0	8th & 9th grade	Plain English. Easily understood by 13- to 15-year-old students.
 * 60.0–50.0	10th to 12th grade	Fairly difficult to read.
 * 50.0–30.0	College	Difficult to read.
 * 30.0–0.0		College graduate	Very difficult to read. Best understood by university graduates.
 */
%>

<script>
    $(document).ready(function() {
    	getProperties()["docId"] = "<%=request.getParameter("Document")%>";
    	getProperties()["corpus"] = "<%=request.getParameter("Corpus")%>";
    	makeDocumentAnnotationTop5DonutChart("OOPPeopleAnnotation", "#OOPPeopleAnnotationDonut", 1);
    	makeDocumentAnnotationDonutChart("OOPPronounAnnotation", "#OOPPronounAnnotationDonut");
    	makeDocumentAnnotationDonutChart("OOPPossessivesAnnotation", "#OOPPossessivesAnnotationDonut");    	
    	makeDocumentAnnotationDonutChart("OOPVerbTenseAnnotation", "#OOPVerbTenseAnnotationDonut");
		makeSentenceAnnotationBarChart("OOPFleschKincaidAnnotation", "#OOPFleschKincaidAnnotationViz");
		makeSentenceAnnotationBarChart("VaderSentimentAnnotation", "#VaderSentimentAnnotationViz");
		makeSentenceAnnotationBarChart("OOPPeopleAnnotation", "#OOPPeopleAnnotationViz");
		makeSentenceAnnotationSubannotationBarChart("OOPPunctuationMarkAnnotation", "Quotation", "#OOPPunctuationMarkAnnotationViz");
		makeSentenceAnnotationStackedBarChart(["OOPVerbsAnnotation", "OOPActionlessVerbsAnnotation"], "#OOPVerbsAnnotationViz", "Verbs");
		makeMyersBriggsPie(
				[
					{"id": 0, "name": "Introvert", "value": <%=request.getAttribute("introvert") %>},
					{"id": 1, "name": "Extrovert", "value": <%=request.getAttribute("extrovert") %>}					
				],
				"#IntrovertExtrovertPie"
		);
		makeMyersBriggsPie(
				[
					{"id": 0, "name": "Intuitive", "value": <%=request.getAttribute("intuitive") %>},
					{"id": 1, "name": "Sensing", "value": <%=request.getAttribute("sensing") %>}					
				],
				"#IntuitiveSensingPie"
		);
		makeMyersBriggsPie(
				[
					{"id": 0, "name": "Thinking", "value": <%=request.getAttribute("thinking") %>},
					{"id": 1, "name": "Feeling", "value": <%=request.getAttribute("feeling") %>}					
				],
				"#ThinkingFeelingPie"
		);
		makeMyersBriggsPie(
				[
					{"id": 0, "name": "Judging", "value": <%=request.getAttribute("judging") %>},
					{"id": 1, "name": "Perceiving", "value": <%=request.getAttribute("perceiving") %>}					
				],
				"#JudgingPerceivingPie"
		);		
    });
</script>

<title>OOP Story Arc Viewer</title>
</head>
<body>
	<jsp:include page="include/nav.jsp" />
	<div class="container-fluid">	
		<jsp:include page="include/spacerRow.jsp" />
		<div class="row justify-content-center">
			<div class="col" id="OOPPronounAnnotationDonut"></div>
			<div class="col" id="OOPPossessivesAnnotationDonut"></div>
			<div class="col" id="OOPPeopleAnnotationDonut"></div>
			<div class="col" id="OOPVerbTenseAnnotationDonut"></div>
		</div>
		
		<jsp:include page="include/spacerRow.jsp" />
		<div class="row justify-content-center">			

			<div class="col" id="OOPFleschKincaidAnnotationViz"></div>		

			<div class="col" id="VaderSentimentAnnotationViz"></div>		

			<div class="col" id="OOPPunctuationMarkAnnotationViz"></div>		

			<div class="col" id="OOPPeopleAnnotationViz"></div>		

			<div class="col" id="OOPVerbsAnnotationViz"></div>		
		</div>
		
		<jsp:include page="include/spacerRow.jsp" />
		<div class="row justify-content-center">
			<div class="col">
				<p class="h5">Myers-Briggs: <%=request.getAttribute("myersBriggs") %></p>
			</div>
		</div>

		<div class="row justify-content-center">
			<div class="col" id="IntrovertExtrovertPie"></div>
			<div class="col" id="IntuitiveSensingPie"></div>
			<div class="col" id="ThinkingFeelingPie"></div>
			<div class="col" id="JudgingPerceivingPie"></div>
		</div>		
	</div>
	<jsp:include page="include/footer.jsp" />
</body>
</html>
