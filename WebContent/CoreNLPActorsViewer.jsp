<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!doctype html>
<html lang="en">
<head>
<title>OOP Actor Document Viewer</title>
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
<link 
	href="js/jquery-ui-themes-1.12.1/jquery-ui.css" 
	rel="stylesheet"
	crossorigin="anonymous">


	<!-- Optional JavaScript -->
	<!-- jQuery first, then Popper.js, then Bootstrap JS -->
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"
		crossorigin="anonymous">
	</script>
	<script 
		src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js"
		crossorigin="anonymous">
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

        getAnnotation(getProperties(),displayActorsAnnotation);
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
			<div class="col-md-8">
				<p>
					<!--  <em>teaser</em> -->
					&nbsp;
				</p>
			</div>

		</div>
	</div>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-4"><p class="display-4 text-center">Actors</p></div>
			<div class="col-md-8">
			</div>
		</div>
		<div class="row">
			<div class="col-lg-4" id="actors_div">					
			</div>
			<div class="col-lg-8" id="story_text"></div>
		</div>
	</div>
</body>
</html>