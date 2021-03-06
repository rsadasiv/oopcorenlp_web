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
<jsp:include page="include/bootstrapPopper.jsp" />

<script 
	src="https://cdnjs.cloudflare.com/ajax/libs/mathjs/7.0.1/math.min.js" 
	integrity="sha256-vT3LwbZg753BzMyE25HfnWHVnMOcbuzde2XvX2wuD7U=" 
	crossorigin="anonymous">
</script>
	
<script src="js/oopcorenlp.js"></script>
<script src="js/OOPEditorViewer.js"></script>

<script>
    $(document).ready(function() {
    	getProperties()["docId"] = "<%=request.getParameter("Document")%>";
    	getProperties()["corpus"] = "<%=request.getParameter("Corpus")%>";
        displayEditorTextAnnotations("#story_text");	        
     });
</script>

<title>OOP Editor Viewer</title>
</head>
<body>
	<jsp:include page="include/nav.jsp" />
	<div class="container">	
		<jsp:include page="include/spacerRow.jsp" />
		<div class="row">
	    	<div class="col-md-2"></div>
	    	<div class="col-md-8" id="story_text"> <%=request.getAttribute("Pst").toString() %> </div>
	    	<div class="col-md-2"></div>
		</div>
	</div>
	<jsp:include page="include/footer.jsp" />
</body>
</html>
