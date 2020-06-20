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
	crossorigin="anonymous"/>
<link rel="stylesheet" 
	href="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.css" 
	integrity="sha256-rByPlHULObEjJ6XQxW/flG2r+22R5dKiAoef+aXWfik=" 
	crossorigin="anonymous" />
<link rel="stylesheet" 
	href="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.theme.min.css" 
	integrity="sha256-AjyoyaRtnGVTywKH/Isxxu5PXI0s4CcE0BzPAX83Ppc=" 
	crossorigin="anonymous" />
<title>OOP Editor Document Viewer</title>
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
	<script 
		src="https://cdnjs.cloudflare.com/ajax/libs/mathjs/7.0.1/math.min.js" 
		integrity="sha256-vT3LwbZg753BzMyE25HfnWHVnMOcbuzde2XvX2wuD7U=" 
		crossorigin="anonymous">
	</script>
	<script src="js/oopcorenlp_viewer.js">
		
	</script>
	<script>
    $(document).ready(function() {
        setProperties(); 
        getAnnotation(getProperties(),displayEditorTextAnnotations);

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
    <div class="col-md-2"></div>
    <div class="col-md-8">
	<p>
		<h1 class="text-capitalize"><%= request.getAttribute("Title").toString().toLowerCase() %></h1>
		<h3 class="text-capitalize">by <%= request.getAttribute("Author").toString().toLowerCase() %></h3>
		<h5 class="text-capitalize"><%= request.getAttribute("Date").toString().toLowerCase() %></h5>
	</p>
	</div>
	</div class="col-md-2"></div>
  </div>
    <div class="row">
    <div class="col-md-2"></div>
    <div class="col-md-8" id="story_text"> </div>
    <div class="col-md-2"></div>
</div>
</body>
</html>