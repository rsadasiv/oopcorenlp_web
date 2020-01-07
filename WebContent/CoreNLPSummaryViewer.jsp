<%@page import="java.util.logging.Logger"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="com.fasterxml.jackson.databind.*, java.util.*, java.util.Map.Entry, java.io.*, io.outofprintmagazine.web.util.*" %>

<% Logger logger = java.util.logging.Logger.getLogger("wtf"); %>
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
<link href="js/jquery-ui-themes-1.12.1/jquery-ui.css" rel="stylesheet">
<title>OOP CoreNlp Summary Viewer</title>

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
		src="https://d3js.org/d3.v5.min.js"
		crossorigin="anonymous">
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

		//makeCloudNoTable("io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPNounsAnnotation", "#cloudViz_nouns");
		//makeCloudNoTable("io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPVerbsAnnotation", "#cloudViz_verbs");
		//makeCloudNoTable("io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPAdjectivesAnnotation", "#cloudViz_adjectives");
		//makeCloudNoTable("io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPAdverbsAnnotation", "#cloudViz_adverbs");
		makeSentenceBarChart("io.outofprintmagazine.nlp.pipeline.OOPAnnotations$VaderRollingSentimentAnnotation", "#sentenceAnnotationViz");		
      	makeTokenBarChart("io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPPeopleAnnotation", "#AnnotationViz_1");
      	makeTokenScoreBarCharts("io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPPeopleAnnotation", "#AnnotationViz");
		
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
<%
//Calculate MyersBriggs for corpus, document and top 5 characters
//Get character image

	List<String> characterName = new ArrayList<String>();
	List<String> characterSentiment = new ArrayList<String>();
	List<String> characterMyersBriggs = new ArrayList<String>();
	List<String> characterImageFlickr = new ArrayList<String>();
	List<String> characterImageBing = new ArrayList<String>();
	
	List<JsonNode> sortedActors = new ArrayList<JsonNode>();
	Iterator<Entry<String, JsonNode>> actorIter = ((JsonNode)request.getAttribute("Stats")).get("io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPActorsAnnotation").fields();
	while (actorIter.hasNext()) {
		sortedActors.add(actorIter.next().getValue());
	}
	Collections.sort(sortedActors, new Comparator<JsonNode>() {
		@Override
	    public int compare(JsonNode a, JsonNode b) {
			return Integer.compare(b.get("importance").asInt(), a.get("importance").asInt());
		}
	});

          
	BufferedReader cr = new BufferedReader(
              	new InputStreamReader(
              		request.getSession().getServletContext().getResourceAsStream(
              			"/Corpora/"+request.getParameter("Corpus")+"/Annotations/CORPUS/io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPVerbGroupsAnnotationAggregate.json"
              		)
              	)	
              );
          ObjectMapper cMapper = new ObjectMapper();
          JsonNode cstats = cMapper.readTree(cr);
          cr.close();
	
	List<String> extrovert = Arrays.asList("communication", "social");
	List<String> introvert = Arrays.asList("perception");
	List<String> sensing = Arrays.asList("stative");
	List<String> intuition = Arrays.asList("change");
	List<String> thinking = Arrays.asList("cognition");
	List<String> feeling = Arrays.asList("emotion");
	
	double cExtrovert = 0.0;
	double cIntrovert = 0.0;
	double cSensing = 0.0;
	double cIntuition = 0.0;
	double cThinking = 0.0;
	double cFeeling = 0.0;
	
	Iterator<JsonNode> cstatsIter = cstats.get("detailScores").elements();
	while (cstatsIter.hasNext()) {
		JsonNode cstat = cstatsIter.next();
		double cScore = cstat.get("normalized").get("scoreStats").get("score").get("normalized").asDouble();
		if (extrovert.contains(cstat.get("name").asText())) {
			cExtrovert += cScore;
		}
		else if (introvert.contains(cstat.get("name").asText())) {
			cIntrovert += cScore;
		}
		else if (sensing.contains(cstat.get("name").asText())) {
			cSensing += cScore;
		}
		else if (intuition.contains(cstat.get("name").asText())) {
			cIntuition += cScore;
		}
		else if (thinking.contains(cstat.get("name").asText())) {
			cThinking += cScore;
		}
		else if (feeling.contains(cstat.get("name").asText())) {
			cFeeling += cScore;
		}				
	}
	
	double dExtrovert = 0.0;
	double dIntrovert = 0.0;
	double dSensing = 0.0;
	double dIntuition = 0.0;
	double dThinking = 0.0;
	double dFeeling = 0.0;
	
	Iterator<JsonNode> dstatsIter = ((JsonNode)request.getAttribute("Stats")).get("io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPVerbGroupsAnnotationAggregate").get("aggregatedScores").elements();
	while (dstatsIter.hasNext()) {
		JsonNode dstat = dstatsIter.next();
		double dScore = dstat.get("score").get("raw").asDouble();
		if (extrovert.contains(dstat.get("name").asText())) {
			dExtrovert += dScore;
		}
		else if (introvert.contains(dstat.get("name").asText())) {
			dIntrovert += dScore;
		}
		else if (sensing.contains(dstat.get("name").asText())) {
			dSensing += dScore;
		}
		else if (intuition.contains(dstat.get("name").asText())) {
			dIntuition += dScore;
		}
		else if (thinking.contains(dstat.get("name").asText())) {
			dThinking += dScore;
		}
		else if (feeling.contains(dstat.get("name").asText())) {
			dFeeling += dScore;
		}				
	}
          
	for (int i=0;i<sortedActors.size()&&i<5;i++) {
		JsonNode actor = sortedActors.get(i);

		double sentimentTotal = 0;
		double sentimentAvg = 0.0;
		Iterator<JsonNode> sentimentIter = actor.get("vaderSentiment").elements();
		int j=0;
		for (j=0;sentimentIter.hasNext();j++) {
			sentimentTotal += sentimentIter.next().asDouble();
		}
		if (j != 0) {
			sentimentAvg = (sentimentTotal/j);
		}

		double aExtrovert = 0.0;
		double aIntrovert = 0.0;
		double aSensing = 0.0;
		double aIntuition = 0.0;
		double aThinking = 0.0;
		double aFeeling = 0.0;
		if (actor.hasNonNull("attributes") && actor.get("attributes").hasNonNull("io.outofprintmagazine.nlp.pipeline.OOPAnnotations.OOPVerbGroupsAnnotation")) {
			Iterator<Entry<String, JsonNode>> astatsIter = actor.get("attributes").get("io.outofprintmagazine.nlp.pipeline.OOPAnnotations.OOPVerbGroupsAnnotation").fields();
			while (astatsIter.hasNext()) {
				Entry<String, JsonNode> astat = astatsIter.next();
				String aName = astat.getKey();
				double aScore = astat.getValue().asDouble();
				if (extrovert.contains(aName)) {
					aExtrovert += aScore;
				}
				else if (introvert.contains(aName)) {
					aIntrovert += aScore;
				}
				else if (sensing.contains(aName)) {
					aSensing += aScore;
				}
				else if (intuition.contains(aName)) {
					aIntuition += aScore;
				}
				else if (thinking.contains(aName)) {
					aThinking += aScore;
				}
				else if (feeling.contains(aName)) {
					aFeeling += aScore;
				}						
			}
			String tmpCharacterName = actor.get("canonicalName").asText();
			if (tmpCharacterName.equalsIgnoreCase("I")) {
				characterName.add("The narrator");
				characterImageFlickr.add("images/narrator.png");
				characterImageBing.add("images/narrator.png");
			}
			else {
				characterName.add(tmpCharacterName);
				String bestImageUrl = "images/blank.png";
				try {
					String[] names = tmpCharacterName.split(" ");
					if (names.length > 1) {
						if (names[0].startsWith("Mr") || names[0].startsWith("Mrs") || names[0].startsWith("Dr")) {
							tmpCharacterName = names[1];
						}
						else {
							tmpCharacterName = names[0];
						}
					}
					//logger.info("image search: " + tmpCharacterName);
					List<String> possibleImages = FlickrUtils.getInstance().getImagesByText(tmpCharacterName);
					for (String imageUrl : possibleImages) {
						//logger.info("flickr match: " + imageUrl);
						if (FacePlusPlusUtils.getInstance().imageHasOneFace(imageUrl) != null) {
							bestImageUrl = imageUrl;
							//logger.info("face Match: "+ imageUrl);
							break;
						}
					}
				}
				catch (Exception e) {
					logger.info(e.toString());
				}
				characterImageFlickr.add(bestImageUrl);
				String bingImageUrl = "images/blank.png";
				try {
					List<String> bingImages = BingUtils.getInstance().getImagesByText(tmpCharacterName);
					if (bingImages != null && bingImages.size() > 0) {
						bingImageUrl = bingImages.get(0);
					}
				}
				catch (Exception e) {
					logger.info(e.toString());
				}
				characterImageBing.add(bingImageUrl);
			}
			
			
			characterSentiment.add(String.format("%.6f",sentimentAvg));
			StringBuffer mb = new StringBuffer();
			if (aExtrovert/cExtrovert>aIntrovert/cIntrovert) {
				mb.append("E");
			}
			else if (aExtrovert/cExtrovert<aIntrovert/cIntrovert) {
				mb.append("I");
			}
			else {
				mb.append("&nbsp;");
			}
			if (aSensing/dSensing>aIntuition/cIntuition) {
				mb.append("S");
			}
			else if (aSensing/dSensing<aIntuition/cIntuition) {
				mb.append("N");
			}
			else {
				mb.append("&nbsp;");
			}
			if (aThinking/dThinking>aFeeling/cFeeling) {
				mb.append("T");
			}
			else if (aThinking/dThinking<aFeeling/cFeeling) {
				mb.append("F");
			}
			else {
				mb.append("&nbsp;");
			}
			characterMyersBriggs.add(mb.toString());
		}
	}
	
	List<String> pullQuotes = new ArrayList<String>();
	//first sentence
	String firstSentence = 
		((JsonNode)request.getAttribute("Stats"))
		.get("sentences")
		.get(0)
		.get("text")
		.asText();
	//last sentence (second to last if ends with *)
	String lastSentence = 
		((JsonNode)request.getAttribute("Stats"))
		.get("sentences")
		.get(((JsonNode)request.getAttribute("Stats")).get("sentences").size()-1)
		.get("text")
		.asText();
	if (lastSentence.trim().equals("*")) {
		lastSentence = 
			((JsonNode)request.getAttribute("Stats"))
			.get("sentences")
			.get(((JsonNode)request.getAttribute("Stats")).get("sentences").size()-2)
			.get("text")
			.asText();
	}
	//sentences with lowest and highest rolling average sentiment
	double firstLowestSentimentScore = 1.0;
	double secondLowestSentimentScore = 1.0;
	int firstLowestSentimentIdx = -1;
	int secondLowestSentimentIdx = -1;
	double firstHighestSentimentScore = 0.0;
	double secondHighestSentimentScore = 0.0;
	int firstHighestSentimentIdx = -1;
	int secondHighestSentimentIdx = -1;
	Iterator<JsonNode> sentenceIter = ((JsonNode)request.getAttribute("Stats")).get("sentences").elements();
	for (int i=0;sentenceIter.hasNext();i++) {
		JsonNode sentence = sentenceIter.next();
		double sentimentScore = sentence.get("io.outofprintmagazine.nlp.pipeline.OOPAnnotations$VaderRollingSentimentAnnotation").asDouble();
		if (sentimentScore < firstLowestSentimentScore) {
			secondLowestSentimentScore = firstLowestSentimentScore;
			secondLowestSentimentIdx = firstLowestSentimentIdx;
			firstLowestSentimentScore = sentimentScore;
			firstLowestSentimentIdx = i;
		}
		else if (sentimentScore < secondLowestSentimentScore) {
			secondLowestSentimentScore = sentimentScore;
			secondLowestSentimentIdx = i;
		}
		if (sentimentScore > firstHighestSentimentScore) {
			secondHighestSentimentScore = firstHighestSentimentScore;
			secondHighestSentimentIdx = firstHighestSentimentIdx;
			firstHighestSentimentScore = sentimentScore;
			firstHighestSentimentIdx = i;
		}
		else if (sentimentScore > secondHighestSentimentScore) {
			secondHighestSentimentScore = sentimentScore;
			secondHighestSentimentIdx = i;
		}
	}
	
	String firstLowestRollingSentiment = ((JsonNode)request.getAttribute("Stats")).get("sentences").get(firstLowestSentimentIdx).get("text").asText();
	String secondLowestRollingSentiment = ((JsonNode)request.getAttribute("Stats")).get("sentences").get(secondLowestSentimentIdx).get("text").asText();
	String firstHighestRollingSentiment = ((JsonNode)request.getAttribute("Stats")).get("sentences").get(firstHighestSentimentIdx).get("text").asText();
	String secondHighestRollingSentiment = ((JsonNode)request.getAttribute("Stats")).get("sentences").get(secondHighestSentimentIdx).get("text").asText();
	
	sentenceIter = ((JsonNode)request.getAttribute("Stats")).get("sentences").elements();
	for (int i=0;sentenceIter.hasNext();i++) {
		JsonNode sentence = sentenceIter.next();
		double sentimentScore = sentence.get("io.outofprintmagazine.nlp.pipeline.OOPAnnotations$VaderSentimentAnnotation").asDouble();
		if (sentimentScore < firstLowestSentimentScore) {
			secondLowestSentimentScore = firstLowestSentimentScore;
			secondLowestSentimentIdx = firstLowestSentimentIdx;
			firstLowestSentimentScore = sentimentScore;
			firstLowestSentimentIdx = i;
		}
		else if (sentimentScore < secondLowestSentimentScore) {
			secondLowestSentimentScore = sentimentScore;
			secondLowestSentimentIdx = i;
		}
		if (sentimentScore > firstHighestSentimentScore) {
			secondHighestSentimentScore = firstHighestSentimentScore;
			secondHighestSentimentIdx = firstHighestSentimentIdx;
			firstHighestSentimentScore = sentimentScore;
			firstHighestSentimentIdx = i;
		}
		else if (sentimentScore > secondHighestSentimentScore) {
			secondHighestSentimentScore = sentimentScore;
			secondHighestSentimentIdx = i;
		}
	}
	
	String firstLowestSentiment = ((JsonNode)request.getAttribute("Stats")).get("sentences").get(firstLowestSentimentIdx).get("text").asText();
	String secondLowestSentiment = ((JsonNode)request.getAttribute("Stats")).get("sentences").get(secondLowestSentimentIdx).get("text").asText();
	String firstHighestSentiment = ((JsonNode)request.getAttribute("Stats")).get("sentences").get(firstHighestSentimentIdx).get("text").asText();
	String secondHighestSentiment = ((JsonNode)request.getAttribute("Stats")).get("sentences").get(secondHighestSentimentIdx).get("text").asText();
	
	//wha
	List<String> whaSentences = new ArrayList<String>();
	Iterator<Entry<String, JsonNode>> whaIter = ((JsonNode)request.getAttribute("Stats")).get("io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPWhaAnnotationList").fields();
	while (whaIter.hasNext()) {
		Iterator<JsonNode> whatIter = whaIter.next().getValue().elements();
		while (whatIter.hasNext()) {
			whaSentences.add(whatIter.next().asText());
		}
	}
	
	//questions
	List<String> questionSentences = new ArrayList<String>();
	Iterator<Entry<String, JsonNode>> qIter = ((JsonNode)request.getAttribute("Stats")).get("io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPQuestionsAnnotationList").fields();
	while (qIter.hasNext()) {
		Entry<String, JsonNode> q = qIter.next();
		if (!q.getKey().equals("SBAR")) {
			Iterator<JsonNode> questIter = q.getValue().elements();
			while (questIter.hasNext()) {
				questionSentences.add(questIter.next().asText());
			}
		}
	}
		
	//comparisons
	List<String> comparisonSentences = new ArrayList<String>();
	Iterator<Entry<String, JsonNode>> cIter = ((JsonNode)request.getAttribute("Stats")).get("io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPSimileAnnotationList").fields();
	while (cIter.hasNext()) {
		Entry<String, JsonNode> c = cIter.next();
		Iterator<JsonNode> compIter = c.getValue().elements();
		while (compIter.hasNext()) {
			comparisonSentences.add(compIter.next().asText());
		}
	}
	
	pullQuotes.add(firstSentence);
	pullQuotes.add(lastSentence);
	pullQuotes.add(firstLowestSentiment);
	pullQuotes.add(firstHighestSentiment);
	pullQuotes.add(secondLowestSentiment);
	pullQuotes.add(secondHighestSentiment);
	pullQuotes.add(firstLowestRollingSentiment);
	pullQuotes.add(firstHighestRollingSentiment);
	pullQuotes.add(secondLowestRollingSentiment);
	pullQuotes.add(secondHighestRollingSentiment);	
	pullQuotes.addAll(whaSentences);
	pullQuotes.addAll(questionSentences);
	pullQuotes.addAll(comparisonSentences);
	
	
// get location names, glosses, and images
	List<String> locationName = new ArrayList<String>();
	List<String> locationGloss = new ArrayList<String>();
	List<String> locationImageFlickr = new ArrayList<String>();
	List<String> locationImageBing = new ArrayList<String>();
	int locationsLength = ((JsonNode)request.getAttribute("Stats"))
	.get("io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPLocationsAnnotationAggregate")
	.get("aggregatedScores")
	.size();
	for (int i=0;i<locationsLength&&i<4;i++) {
		try {
			String locName = ((JsonNode)request.getAttribute("Stats"))
				.get("io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPLocationsAnnotationAggregate")
				.get("aggregatedScores")
				.get(i)
				.get("name")
				.asText();
			locationName.add(locName);
			if (((JsonNode)request.getAttribute("Stats"))
				.get("io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPWikipediaGlossAnnotation")
				.has(locName)) {
				locationGloss.add(((JsonNode)request.getAttribute("Stats"))
						.get("io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPWikipediaGlossAnnotation")
						.get(locName)
						.get(0)
						.asText());
			}
			else {
				locationGloss.add("");
			}
			String bestImageUrl = "images/blank.png";
			String tmpLocName = locName;
			try {
				String[] names = locName.split(" ");
				if (names.length > 1) {
					if (names[0].startsWith("East") || names[0].startsWith("West") || names[0].startsWith("North") || names[0].startsWith("South")) {
						tmpLocName = names[1];
					}
					else {
						tmpLocName = names[0];
					}
				}
				//logger.info("image search: " + tmpLocName);
				List<String> possibleImages = FlickrUtils.getInstance().getImagesByText(tmpLocName);
				if (possibleImages != null && possibleImages.size() > 0) {
					bestImageUrl = possibleImages.get(0);
				}
			}
			catch (Exception e) {
				logger.info(e.toString());
			}
			locationImageFlickr.add(bestImageUrl);
			String bingImageUrl = "images/blank.png";
			try {
				List<String> bingImages = BingUtils.getInstance().getImagesByTag(tmpLocName);
				if (bingImages != null && bingImages.size() > 0) {
					bingImageUrl = bingImages.get(0);
				}
			}
			catch (Exception e) {
				logger.info(e.toString());
			}
			locationImageBing.add(bingImageUrl);
		}
		catch (Exception e) {
			logger.info(e.toString());
		}
	}

%>	
	
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

	</div>
	<div class="container-fluid">
		<div class="row">
		&nbsp;</div>
	</div>	
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-2"></div>
			<div class="col-md-8">
				<p>Estimated reading time: <%=
					String.format(
						"%.0f",
						((JsonNode)request.getAttribute("Stats"))
							.get("io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPDocumentLengthAnnotation")
							.get("wordCount")
							.asDouble()/250
					)
					%> minutes
				</p>
				<p>Estimated listening time: <%=
					String.format(
						"%.0f",
						((JsonNode)request.getAttribute("Stats"))
							.get("io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPSyllablesAnnotation")
							.asDouble()/160
					)
					%> minutes
				</p>
				<p>Reading level: <%=
					String.format(
						"%.6f",
						((JsonNode)request.getAttribute("Stats"))
							.get("io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPFleschKincaidAnnotation")
							.asDouble()
					)
					%>
				</p>
				<p>Vocabulary: <%=
					String.format(
						"%.6f",
						((JsonNode)request.getAttribute("Stats"))
							.get("io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPCommonWordsAnnotationAggregate")
							.get("scoreStats")
							.get("score")
							.get("normalized")
							.asDouble()
					)
					%> common words, <%=
					String.format(
						"%.6f",
						((JsonNode)request.getAttribute("Stats"))
							.get("io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPUncommonWordsAnnotationAggregate")
							.get("scoreStats")
							.get("score")
							.get("normalized")
							.asDouble()
					)
					%> uncommon words
				</p>
				<p></p>
				<p>Locations: </p>
				<div>
				<%
					for (int i=0;i<locationName.size();i++) {
						try {
							if (locationGloss.get(i).length() > 0 && locationGloss.get(i).endsWith(".")) {
							%>
								<p><%=locationGloss.get(i) %></p>
							<%
							}
							else {
							%>
								<p><%=locationName.get(i)%></p>
							<%
							}
							%>
						<img src="<%=locationImageFlickr.get(i) %>"/>
						<img src="<%=locationImageBing.get(i) %>"/>
						<p>&nbsp;</p>
						<% 
						}
						catch (Exception e) {
							logger.info(e.toString());
						}
					}
				%>
				</div>
				<p>Characters: </p>
				<div>
				<%
					for (int i=0;i<characterName.size();i++) {
						try {
						%>
						<p><%=characterName.get(i)%>, personality type <%=characterMyersBriggs.get(i)%>, sentiment <%=characterSentiment.get(i) %></p>
						<img src="<%=characterImageFlickr.get(i) %>"/>
						<img src="<%=characterImageBing.get(i) %>"/>
						<p>&nbsp;</p>
						<% 
						}
						catch (Exception e) {
							logger.info(e.toString());
						}
					}
				%>
				</div>
			</div>
			<div class="col-md-2"></div>			
		</div>
		
		<div class="row">
			<div class="col-md-12">
				<p>Emotional Arc:</p>
				<p class="text-center font-italic" id="sentenceText">
					&nbsp;
				</p>
			</div>
		</div>
		<div class="row">
			<div class="col-lg-12" id="sentenceViz" align="center">
				<svg  width="1600" height="400" id="sentenceAnnotationViz"></svg>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<p>Mise en scene:</p>
			</div>
		</div>						
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
		<div class="row">
			<div class="col-md-2"></div>
			<div class="col-md-8">
				<p>Pull quotes:</p>
				<p>First sentence: <%=firstSentence %> </p>
				<p>Last sentence: <%=lastSentence %> </p>
				<p>Saddest sentence: <%=firstLowestSentiment %> </p>
				<p>Saddest sentence 2: <%=secondLowestSentiment %> </p>
				<p>Happiest sentence: <%=firstHighestSentiment %> </p>
				<p>Happiest sentence 2: <%=secondHighestSentiment %> </p>
				<p>Saddest sentence rolling: <%=firstLowestRollingSentiment %> </p>
				<p>Saddest sentence rolling 2: <%=secondLowestRollingSentiment %> </p>
				<p>Happiest sentence rolling: <%=firstHighestRollingSentiment %> </p>
				<p>Happiest sentence rolling 2: <%=secondHighestRollingSentiment %> </p>				
				<p>&nbsp;</p>
				<p>Wha</p>								
				<%
				for (String quote : whaSentences) {
				%>
					<p><%=quote %></p>
				<%
				}
				%>
				<p>&nbsp;</p>
				<p>?</p>								
				<%
				for (String quote : questionSentences) {
				%>
					<p><%=quote %></p>
				<%
				}
				%>
				<p>&nbsp;</p>
				<p>Summer's day</p>								
				<%
				for (String quote : comparisonSentences) {
				%>
					<p><%=quote %></p>
				<%
				}
				%>							
			</div>
			<div class="col-md-2"></div>
		</div>			
	</div>			
</body>
</html>