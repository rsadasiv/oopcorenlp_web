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
<!doctype html>
<html lang="en">
<head>
<link rel="apple-touch-icon" sizes="180x180" href="images/apple-touch-icon.png">
<link rel="icon" type="image/png" sizes="32x32" href="images/favicon-32x32.png">
<link rel="icon" type="image/png" sizes="16x16" href="images/favicon-16x16.png">
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
<link rel="stylesheet" 
	href="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.css" 
	integrity="sha256-rByPlHULObEjJ6XQxW/flG2r+22R5dKiAoef+aXWfik=" 
	crossorigin="anonymous" />
<link rel="stylesheet" 
	href="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.theme.min.css" 
	integrity="sha256-AjyoyaRtnGVTywKH/Isxxu5PXI0s4CcE0BzPAX83Ppc=" 
	crossorigin="anonymous" />
	
<title>OOP Document Viewer</title>

	<!-- Optional JavaScript -->
	<!-- jQuery first, then Popper.js, then Bootstrap JS -->
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js">
	</script>
	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js">
	</script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
		integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
		crossorigin="anonymous">
	</script>
	<script
		src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
		integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
		crossorigin="anonymous">
	</script>
	<script>
		var docId = "<%=request.getParameter("Document")%>";
		var corpus = "<%=request.getParameter("Corpus")%>";
	</script>
	<script src="js/oopcorenlp_viewer.js">
		
	</script>

	<script>
    $(document).ready(function() {
        setProperties();
        getAnnotation(getProperties(),displayMetadataAnnotations);

        getAnnotation(getProperties(),displayDocumentAnnotations);

        getAnnotation(getProperties(),displayDictionAnnotations);
      
        getAnnotation(getProperties(),displayLedeAnnotations);

        getAnnotation(getProperties(),displayGenderNumberAnnotations);

        getAnnotation(getProperties(),displayDescriptionAnnotations);

        getAnnotation(getProperties(),displaySemanticsAnnotations);
        
        getAnnotation(getProperties(),displayUncategorizedAnnotations);

        //getAnnotation(getProperties(),displayActorsAnnotation);
        getAnnotation(getProperties(),displayTextAnnotation);
        //getText(getProperties(),displayText);
        //getAnnotation(getProperties(),displayRawAnnotation);
        //$( "#accordion" ).accordion({
        //    collapsible: true,
        //    heightStyle: "content"
        //  });
    	$('[data-toggle="popover"]').popover();
    	$('[data-toggle="tooltip"]').tooltip();
     });
	</script>
	<style>
		.logo {
			width: 311px;
			height: 68px;
			display: block;
			text-indent: -6000px;
			margin: 25px auto 59px auto;
			cursor: pointer;
		}
		.ui-colorpicker-swatch {
		  cursor: pointer;
		  float: right;
		  width: 22px;
		  height: 22px;
		  border-top: 1px solid black;
		  border-bottom: 1px solid black;
		  border-right: 1px solid black;
		  border-left: 1px solid black;
		}		
	</style>
</head>
<body>
	<a href="index.html"><img src="images/OOP/logo.png" class="logo" /></a>
	<div class="container">	
		<div class="row">
			<div class="col-md-4"></div>
			<div class="col-md-8">
				<p>
					<h1 class="text-capitalize"><%=request.getAttribute("Title").toString().toLowerCase()%></h1>
					<h3 class="text-capitalize">by <%=request.getAttribute("Author").toString().toLowerCase()%></h3>
					<h5 class="text-capitalize"><%=request.getAttribute("Date").toString().toLowerCase()%></h5>
				</p>
			</div>
		</div>
		<div class="row">
			<div class="col-md-4"></div>
			<div class="col-md-8" id="colorGuide">
				<p>
					&nbsp;
				</p>
			</div>
		</div>
	</div>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-4"><p class="display-4 text-center">Annotations</p></div>
			<div class="col-md-8">
			</div>
		</div>
		<div class="row">
			<div class="col-lg-4">
				<div class="container-fluid accordion">
					<div class="card">
    					<div class="card-header" id="metadata">
      						<h5 class="mb-0">
        						<button class="btn btn-link" type="button" data-toggle="collapse" data-target="#metadata_div" aria-expanded="true" aria-controls="collapseMetadata">
          							Metadata
        						</button>
      						</h5>
    					</div>
    				</div>	
					<div class="container-fluid collapse" id="metadata_div"></div>
				</div>		
				
				<div class="container-fluid accordion">
					<div class="card">
    					<div class="card-header" id="document">
      						<h5 class="mb-0">
        						<button class="btn btn-link" type="button" data-toggle="collapse" data-target="#document_div" aria-expanded="true" aria-controls="collapseDocument">
          							Document
        						</button>
      						</h5>
    					</div>
    				</div>

					<div class="container-fluid collapse" id="document_div"></div>
				</div>	
				
				<div class="container-fluid accordion">
					<div class="card">
    					<div class="card-header" id="diction">
      						<h5 class="mb-0">
        						<button class="btn btn-link" type="button" data-toggle="collapse" data-target="#diction_div" aria-expanded="true" aria-controls="collapseDiction">
          							Diction
        						</button>
      						</h5>
    					</div>
    				</div>

					<div class="container-fluid collapse" id="diction_div">
					
					</div>
				</div>
				
				<div class="container-fluid accordion">
					<div class="card">
    					<div class="card-header" id="lede">
      						<h5 class="mb-0">
        						<button class="btn btn-link" type="button" data-toggle="collapse" data-target="#lede_div" aria-expanded="true" aria-controls="collapseLede">
          							Lede
        						</button>
      						</h5>
    					</div>
    				</div>

					<div class="container-fluid collapse" id="lede_div">
					
					</div>
				</div>
				
				<div class="container-fluid accordion">
					<div class="card">
    					<div class="card-header" id="gendernumber">
      						<h5 class="mb-0">
        						<button class="btn btn-link" type="button" data-toggle="collapse" data-target="#gendernumber_div" aria-expanded="true" aria-controls="collapseGenderNumber">
          							Gender/Number
        						</button>
      						</h5>
    					</div>
    				</div>

					<div class="container-fluid collapse" id="gendernumber_div">
					
					</div>
				</div>
			
				<div class="container-fluid accordion">
					<div class="card">
    					<div class="card-header" id="description">
      						<h5 class="mb-0">
        						<button class="btn btn-link" type="button" data-toggle="collapse" data-target="#description_div" aria-expanded="true" aria-controls="collapseDescription">
          							Description
        						</button>
      						</h5>
    					</div>
    				</div>

					<div class="container-fluid collapse" id="description_div">
					
					</div>
				</div>
			
				<div class="container-fluid accordion">
					<div class="card">
    					<div class="card-header" id="semantics">
      						<h5 class="mb-0">
        						<button class="btn btn-link" type="button" data-toggle="collapse" data-target="#semantics_div" aria-expanded="true" aria-controls="collapseSemantics">
          							Semantics
        						</button>
      						</h5>
    					</div>
    				</div>

					<div class="container-fluid collapse" id="semantics_div">
					
					</div>
				</div>
				
				<div class="container-fluid accordion">
					<div class="card">
    					<div class="card-header" id="uncategorized">
      						<h5 class="mb-0">
        						<button class="btn btn-link" type="button" data-toggle="collapse" data-target="#uncategorized_div" aria-expanded="true" aria-controls="collapseUncategorized">
          							Uncategorized
        						</button>
      						</h5>
    					</div>
    				</div>

					<div class="container-fluid collapse" id="uncategorized_div">
					
					</div>
				</div>
			
			</div>
			<div class="col-lg-8" id="story_text"></div>
		</div>
	</div>
</body>
</html>
