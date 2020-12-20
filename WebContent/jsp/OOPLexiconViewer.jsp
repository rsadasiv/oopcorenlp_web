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
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.math.RoundingMode" %>
<%@ page import="com.fasterxml.jackson.databind.JsonNode" %>
<%@ page import="com.fasterxml.jackson.databind.node.ObjectNode" %>
<%@ page import="com.fasterxml.jackson.databind.node.ArrayNode" %>
<%@ page import="io.outofprintmagazine.web.util.JsonSort" %>
<!doctype html>
<html lang="en">
<head>
<jsp:include page="include/icon.jsp" />
<jsp:include page="include/meta.jsp" />
<jsp:include page="include/bootstrap.jsp" />
<jsp:include page="include/vega-lite.jsp" />
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/chosen/1.8.7/chosen.min.css" integrity="sha512-yVvxUQV0QESBt1SyZbNJMAwyKvFTLMyXSyBHDO4BG5t7k/Lw34tyqlSDlKIrIENIzCl+RVUNjmCPG+V/GMesRw==" crossorigin="anonymous" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/chosen/1.8.7/chosen.jquery.min.js" integrity="sha512-rMGGF4wg1R73ehtnxXBt5mbUfN9JUJwbk21KMlnLZDJh7BkPmeovBuddZCENJddHYYMkCh9hPFnPmS9sspki8g==" crossorigin="anonymous"></script>
<script src="js/oopcorenlp.js"></script>
<script src="js/OOPLexiconViewer.js"></script>

<%
ObjectNode stats = (ObjectNode) request.getAttribute("Stats");
ObjectNode annotationDescriptions = (ObjectNode) request.getAttribute("AnnotationDescriptions");
String selectedAnnotation = request.getParameter("Annotation")==null||request.getParameter("Annotation").equals("")?"OOPWordsAnnotation":request.getParameter("Annotation");
String selectedSubannotation = request.getParameter("Subannotation");
int sentenceCount = (int)request.getAttribute("SentenceCount");
ObjectNode lexicon = (ObjectNode) request.getAttribute("Lexicon");
%>

<script>
$(document).ready(function() {
	getProperties()["docId"] = "<%=request.getParameter("Document")%>";
	getProperties()["corpus"] = "<%=request.getParameter("Corpus")%>";
	$('Subannotation').chosen({ width:'200px' });
	let annotation = "<%=request.getParameter("Annotation")==null||request.getParameter("Annotation").equals("")?"VaderSentimentAnnotation":request.getParameter("Annotation")%>";	
	makeSentenceBarChart(annotation, "<%=request.getParameter("Subannotation")%>", "#sentenceAnnotationViz");
	$('#Subannotation').change(
			function() {
		    	window.location.href = location.protocol + '//' + location.host + location.pathname + "?Corpus="+getProperties()["corpus"]+"&Document="+getProperties()["docId"]+"&Subannotation=" + $('#Subannotation option:selected').val();
		    }
	);
});
</script>

<title>OOP Lexicon Viewer</title>	
</head>
<body>
	<jsp:include page="include/nav.jsp" />
	<jsp:include page="include/spacerRow.jsp" />	
	<div class="container">
		<div class="row">
			<div class="col-md-4"></div>
			<div class="col-md-4" id="subannotationPicker">
				<select id="Subannotation" class="form-control" title="Select Lemma">
				<% 
				ObjectNode words = (ObjectNode) stats.get("OOPWordsAnnotation");
				JsonSort.sort(words);
				Iterator<String> wordsIter = words.fieldNames();
				while (wordsIter.hasNext()) {
					String word = wordsIter.next();
					%>
					<option value="<%=word %>" <%=word.equals(selectedSubannotation)?"selected":"" %>><%=word %></option>
					<%
				}
				%>
				</select>
			</div>
			<div class="col-md-4 form-check">
			</div>
		</div>
	</div>
<%
if (lexicon != null) {
%>	
	<jsp:include page="include/spacerRow.jsp" />
	<div class="container-fluid">
		<div class="row">
			<div class="col">
				<div class="card">
					<div class="card-header">
		   				<h5 class="card-title">Info</h5>
		   			</div>
					<div class="card-body">
						<p class="card-text">
							<label for="Importance" class="h5">
								Importance:
							</label> 
							<span id="Importance">
								<%=lexicon.get("importance").asText("") %>
							</span>
						</p>
						<p class="card-text">
							<label for="FirstAppearance" class="h5">
								First Appearance:
							</label> 
							<span id="FirstAppearance">
								<%=lexicon.get("firstAppearance").asText("") %>
							</span>
						</p>
						<p class="card-text">
							<label for="LastAppearance" class="h5">
								Last Appearance:
							</label> 
							<span id="LastAppearance">
								<%=lexicon.get("lastAppearance").asText("") %>
							</span>
						</p>
						<p class="card-text">
							<label for="SyllablesSpan" class="h5">
								Syllables:
							</label> 
							<span id="SyllablesSpan">
								<%=lexicon.get("attributes").get("OOPSyllableCountAnnotation").asText("") %>
							</span>
						</p>						
					</div>
				</div>
			</div>		
			<div class="col">
				<div class="card">
					<div class="card-header">
		    			<h5 class="card-title">Sentiment</h5>
		    		</div>
					<div class="card-body">
						<p class="card-text">
							<label for="coreNlpSentimentAvg" class="h5">
								CoreNlp Sentiment:
							</label>
							<span id="coreNlpSentimentAvg">
								<%=String.format("%.0f", lexicon.get("attributes").get("CoreNlpSentimentAnnotation").asDouble(0)*100) %>								
							</span>
						</p>
						<p class="card-text">
							<label for="vaderSentimentAvg" class="h5">
								Vader Sentiment:
							</label>
							<span id="vaderSentimentAvg">
								<%=String.format("%.0f", lexicon.get("attributes").get("VaderSentimentAnnotation").asDouble(0)*100) %>
							</span>
						</p>
					</div>
				</div>
		<%
			ArrayNode glosses = (ArrayNode) lexicon.get("attributes").get("OOPWordnetGlossAnnotation");
			if (glosses != null) {
		%>
				<p>&nbsp;</p>
				<div class="card">
					<div class="card-header">
		    			<h5 class="card-title">Wordnet Gloss</h5>
		    		</div>
					<div class="card-body">				
				<%
				Iterator<JsonNode> glossesIter = glosses.iterator();
				while (glossesIter.hasNext()) {
					JsonNode n = glossesIter.next();
				%>
						<p class="card-text"><%=n.asText("") %></p>
				<%
					}
				}
				%>
					</div>
				</div>
			</div>
			<div class="col">
				<div class="card">
					<div class="card-header">
		    			<h5 class="card-title">CoreNLP</h5>
		    		</div>
					<div class="card-body">
						<label class="card-text">Surface form</label><ul>
						<%		
						Iterator<String> tokenNameIter = lexicon.get("attributes").get("word").fieldNames();
						while (tokenNameIter.hasNext()) {
							String tokenName = tokenNameIter.next();
						%>
							<li><%=tokenName %>: <%=lexicon.get("attributes").get("word").get(tokenName).asInt() %></li>
						<%
						}
						%>
						</ul>
						<label class="card-text">Part of speech</label><ul>
						<%		
						Iterator<String> posNameIter = lexicon.get("attributes").get("pos").fieldNames();
						while (posNameIter.hasNext()) {
							String posName = posNameIter.next();
						%>
							<li><%=posName %>: <%=lexicon.get("attributes").get("pos").get(posName).asInt() %></li>
						<%
						}
						%>
						</ul>
						<label class="card-text">Named entity recognition</label><ul>
						<%		
						Iterator<String> nerNameIter = lexicon.get("attributes").get("ner").fieldNames();
						while (nerNameIter.hasNext()) {
							String nerName = nerNameIter.next();
						%>
							<li><%=nerName %>: <%=lexicon.get("attributes").get("ner").get(nerName).asInt() %></li>
						<%
						}
						%>
						</ul>										
					</div>
				</div>										
			</div>
		</div>
		<jsp:include page="include/spacerRow.jsp" />
		<div class="row">
		<%
		List<String> skipAnnotations = Arrays.asList("OOPPunctuationMarkAnnotation", "OOPSyllableCountAnnotation", "CoreNlpSentimentAnnotation", "OOPWordCountAnnotation", "OOPWordnetGlossAnnotation", "VaderSentimentAnnotation" );
		Iterator<String> annotationNameIter = lexicon.get("attributes").fieldNames();
		while (annotationNameIter.hasNext()) {
			String annotationName = annotationNameIter.next();
			if (annotationName.endsWith("Annotation") && !skipAnnotations.contains(annotationName)) {
		%>
			<div class="col">
				<div class="card">
					<div class="card-header">
		    			<h5 class="card-title"><%=annotationName %></h5>
		    		</div>
					<div class="card-body">
				<%
				Iterator<String> subannotationNameIter = lexicon.get("attributes").get(annotationName).fieldNames();
				while (subannotationNameIter.hasNext()) {
					String subannotationName = subannotationNameIter.next();	
				%>
						<p class="card-text">
							<label for="<%=annotationName %>_<%=subannotationName %>" class="h5">
								<%=subannotationName %>:
							</label>
							<span id="<%=annotationName %>_<%=subannotationName %>">
								<%=new BigDecimal(lexicon.get("attributes").get(annotationName).get(subannotationName).asDouble()) %>					
							</span>
						</p>			
				<%
				}
				%>
					</div>
				</div>
			</div>
		<%			
			}
		}	
		%>
		</div>
		<jsp:include page="include/spacerRow.jsp" />
		<div class="row">		
		<%
		ArrayNode topicModelList = (ArrayNode) request.getAttribute("TopicModelLemma");
		if (topicModelList != null && topicModelList.size() > 0) {
		%>
			<div class="col">
				<div class="card">
					<div class="card-header">
		    			<h5 class="card-title">Topic Model</h5>
		    		</div>
					<div class="card-body">
			<%
			for (JsonNode nv : topicModelList) {
				String name = nv.get("name").asText();
				BigDecimal val = new BigDecimal(nv.get("value").asText("0"));
			%>
						<p class="card-text">
							<label for="<%=selectedSubannotation %>_<%=name %>" class="h5">
								<%=name %>:
							</label>
							<span id="<%=selectedSubannotation %>_<%=name %>">
								<%=String.format("%.0f", val.doubleValue()*100) %>					
							</span>
						</p>			
			<%
			}
			%>
					</div>
				</div>
			</div>
		<%
		}	
		%>		

		<%
		ArrayNode corpusTopicModelList = (ArrayNode) request.getAttribute("CorpusTopicModelLemma");
		if (corpusTopicModelList != null && corpusTopicModelList.size() > 0) {
		%>
			<div class="col">
				<div class="card">
					<div class="card-header">
		    			<h5 class="card-title">Corpus Topic Model</h5>
		    		</div>
					<div class="card-body">
			<%
			for (JsonNode nv : corpusTopicModelList) {
				String name = nv.get("name").asText();
				BigDecimal val = new BigDecimal(nv.get("value").asText("0"));
			%>
						<p class="card-text">
							<label for="<%=selectedSubannotation %>_<%=name %>" class="h5">
								<%=name %>:
							</label>
							<span id="<%=selectedSubannotation %>_<%=name %>">
								<%=String.format("%.0f", val.doubleValue()*100) %>				
							</span>
						</p>			
			<%
			}
			%>
					</div>
				</div>
			</div>
		<%
		}	
		%>	

		<%
		ObjectNode topicModelPOSList = (ObjectNode) request.getAttribute("TopicModelLemmaPOS");
		if (topicModelPOSList != null && topicModelPOSList.size() > 0) {
			Iterator<String> posIter = topicModelPOSList.fieldNames();
			while (posIter.hasNext()) {
				String pos = posIter.next();
		%>
			<div class="col">
				<div class="card">
					<div class="card-header">
		    			<h5 class="card-title">Topic Model <%=pos %></h5>
		    		</div>
					<div class="card-body">
				<%
	
				for (JsonNode nv : topicModelPOSList.get(pos)) {
					String name = nv.get("name").asText();
					BigDecimal val = new BigDecimal(nv.get("value").asText("0"));
				%>
							<p class="card-text">
								<label for="<%=selectedSubannotation %>_<%=name %>_POS" class="h5">
									<%=name %>:
								</label>
								<span id="<%=selectedSubannotation %>_<%=name %>_POS">
									<%=String.format("%.0f", val.doubleValue()*100) %>					
								</span>
							</p>			
				<%
				}
				%>
					</div>
				</div>
			</div>
		<%
			}
		}	
		%>
		
		<%
		ObjectNode corpusTopicModelPOSList = (ObjectNode) request.getAttribute("CorpusTopicModelLemmaPOS");
		if (corpusTopicModelPOSList != null && corpusTopicModelPOSList.size() > 0) {
			Iterator<String> posIter = corpusTopicModelPOSList.fieldNames();
			while (posIter.hasNext()) {
				String pos = posIter.next();
		%>
			<div class="col">
				<div class="card">
					<div class="card-header">
		    			<h5 class="card-title">Corpus Topic Model <%=pos %></h5>
		    		</div>
					<div class="card-body">
				<%
	
				for (JsonNode nv : corpusTopicModelPOSList.get(pos)) {
					String name = nv.get("name").asText();
					BigDecimal val = new BigDecimal(nv.get("value").asText("0"));
				%>
							<p class="card-text">
								<label for="<%=selectedSubannotation %>_<%=name %>_POS" class="h5">
									<%=name %>:
								</label>
								<span id="<%=selectedSubannotation %>_<%=name %>_POS">
									<%=String.format("%.0f", val.doubleValue()*100) %>					
								</span>
							</p>			
				<%
				}
				%>
					</div>
				</div>
			</div>
		<%
			}
		}	
		%>		

		</div>

		<jsp:include page="include/spacerRow.jsp" />			
		<div class="row">
			<div class="col" id="sentenceAnnotationViz">
			</div>
		</div>
	</div>
<%
}
%>			
	<jsp:include page="include/footer.jsp" />
</body>
</html>
