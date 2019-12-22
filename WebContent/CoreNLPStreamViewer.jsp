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
<title>OOP Annotation Stream Viewer</title>


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
		src="https://d3js.org/d3.v5.min.js"
		crossorigin="anonymous">
	</script>
	<script>
		var docId = "<%=request.getParameter("Document")%>";
		var corpus = "<%=request.getParameter("Corpus")%>";
		var annotation = "<%=request.getParameter("Annotation")==null||request.getParameter("Annotation").equals("")?"io.outofprintmagazine.nlp.pipeline.OOPAnnotations$VaderSentimentAnnotation":request.getParameter("Annotation")%>";
		var normalized = <%=request.getParameter("Aggregate")!=null&&request.getParameter("Aggregate").equals("Normalized") %>;
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

		drawSentenceAnnotators(annotation);

		if (normalized) {
			$("#normalized").prop('checked', normalized);
		}
		makeSentenceBarChart(annotation, "#sentenceAnnotationViz");
		//makeTokenBarChart(annotation, "#tokenAnnotationViz");
		
		$('#annotators').change(
				function() {
			    	window.location.href = location.protocol + '//' + location.host + location.pathname + "?Analysis=Stream&Corpus="+ properties.corpus+"&Document=" + properties.docId + "&Annotation=" + $('#annotators option:selected').val() + ($('#normalized').prop('checked')?"&Aggregate=Normalized":"");
			    }
			);
		$('#normalized').change(
				function() {
			    	window.location.href = location.protocol + '//' + location.host + location.pathname + "?Analysis=Stream&Corpus="+ properties.corpus+"&Document=" + properties.docId + "&Annotation=" + $('#annotators option:selected').val() + ($('#normalized').prop('checked')?"&Aggregate=Normalized":"");
			    }
			); 
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
			<div class="col-md-4" id="annotatorPicker">
				<select id="annotators"	class="form-control" title="Select OOPCoreNLP annotator">
				</select>
			</div>
			<div class="col-md-4 form-check">
			    <input type="checkbox" class="form-check-input" id="normalized">
    			<label class="form-check-label" for="normalize">Normalize</label>
			</div>
		</div>

	</div>
	<div class="container-fluid">
		<div class="row">
		&nbsp;</div>
	</div>
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
			<div class="col-lg-12" id="sentenceViz">
				<svg width="1600" height="400" id="sentenceAnnotationViz"></svg>
			</div>
		</div>				
	</div>
</body>
</html>