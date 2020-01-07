<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!doctype html>
<html lang="en">
<head>
<link rel="apple-touch-icon" sizes="180x180"
	href="images/apple-touch-icon.png">
<link rel="icon" type="image/png" sizes="32x32"
	href="images/favicon-32x32.png">
<link rel="icon" type="image/png" sizes="16x16"
	href="images/favicon-16x16.png">
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
<link href="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.theme.css" rel="stylesheet" crossorigin="anonymous">
<title>OOP Annotation Cloud Viewer</title>


<!-- JQuery -->

<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.js" crossorigin="anonymous">
	</script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.js" crossorigin="anonymous">
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
	
	<script 
		src="https://cdnjs.cloudflare.com/ajax/libs/d3/3.5.17/d3.min.js"
		crossorigin="anonymous">
	</script>

	<script src="js/d3.layout.cloud.js"></script>
	<script src="js/d3.wordcloud.js"></script>
	<script>
		var docId = "<%=request.getParameter("Document")%>";
		var corpus = "<%=request.getParameter("Corpus")%>";
		var annotation = "<%=request.getParameter("Annotation")==null||request.getParameter("Annotation").equals("")?"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$VaderSentimentAnnotation":request.getParameter("Annotation")%>";
	</script>
	<script src="js/oopcorenlp_d3_viewer.js">
		
	</script>

	<script>
	function type(d) {
		  d.value = +d.value; // coerce to number
		  return d;
	}
	
    $(document).ready(function() {
		setProperties();
		makeCloudNoTable(annotation, "#cloudViz");
    });
	</script>
	<link href="https://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet">
	<style>
		.logo {
			width: 311px;
			height: 68px;
			display: block;
			text-indent: -6000px;
			margin: 25px auto 59px auto;
			cursor: pointer;
		}
		.bar {
		  fill: steelblue;
		}
		
		.chart text {
		  fill: white;
		  font: 10px sans-serif;
		  text-anchor: end;
		}
		
		.bld {
        	font-weight: bold;
    	}
	</style>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-2"></div>
			<div class="col-lg-8 text-center" id="cloudViz">

			</div>
			<div class="col-md-2"></div>
		</div>				
	</div>
</body>
</html>