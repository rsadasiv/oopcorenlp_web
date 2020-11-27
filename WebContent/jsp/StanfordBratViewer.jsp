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

<!-- Brat -->
<link rel="stylesheet" type="text/css" href="css/brat/style-vis.css" />
<script type="text/javascript" src="brat-master/client/lib/head.load.min.js"></script>
<!-- CoreNLP -->
<link rel="stylesheet" type="text/css" href="css/brat/corenlp-brat.css" />

<script src="js/corenlp.js"></script>

<title>Stanford Brat Viewer</title>
<script>
	properties["docId"] = "<%=request.getParameter("Document")%>";
	properties["corpus"] = "<%=request.getParameter("Corpus")%>";
	var annotation = "<%=request.getParameter("Annotation")==null?"pos":request.getParameter("Annotation")%>";

    $(document).ready(function() {
        $("#annotators").val(annotation);
        if (annotation == "pos") {
        	displayPos("story_text");
        }
        if (annotation == "sentiment") {
          	displaySentiment("story_text");
        }
        if (annotation == "ner") {
        	displayNer("story_text");
        }
        if (annotation == "ner-person") {
        	displayNerPerson("story_text");
        }
        if (annotation== "parse") {
          displayParse("story_text");
        }
        if (annotation == "coref") {
        	displayCoref("story_text");
        } 
        if (annotation == "depparse") {
        	displayDep("story_text");
        }
        
        $('#annotators').change(
        		function() {
        	    	window.location.href = location.protocol + '//' + location.host + location.pathname + "?Corpus="+ properties["corpus"]+"&Document=" + properties["docId"] + "&Annotation=" + $('#annotators option:selected').val();
        	    }
        	);

});
</script>
</head>
<body>
	<jsp:include page="include/nav.jsp" />
	<div class="container">	
		<jsp:include page="include/spacerRow.jsp" />
		<div class="row">
			<div class="col-md-4"></div>
			<div class="col-md-4" id="annotatorPicker">
				<select id="annotators"	class="form-control" title="Select CoreNLP annotators">
					<option value="pos" selected>parts-of-speech</option>
					<option value="ner" >named entities</option>
					<option value="ner-person" >people-places</option>
					<option value="parse">constituency parse</option>
					<option value="depparse">dependency parse</option>
					<option value="coref">coreference</option>
					<option value="sentiment">sentiment</option>
				</select>
			</div>
			<div class="col-md-4"></div>
		</div>
		<div class="row">
			<div class="col-md-12" id="story_text"><p></p></div>
		</div>
	</div>
	<jsp:include page="include/footer.jsp" />
</body>
</html>
