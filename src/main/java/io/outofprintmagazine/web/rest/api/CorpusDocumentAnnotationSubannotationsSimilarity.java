package io.outofprintmagazine.web.rest.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.apache.commons.text.similarity.CosineSimilarity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.outofprintmagazine.web.servlets.AbstractOOPCacheableServlet;

@Path("/CorpusDocumentAnnotationSubannotationsSimilarity")
public class CorpusDocumentAnnotationSubannotationsSimilarity extends AbstractOOPCacheableServlet {
	
	private static final long serialVersionUID = 1L;
	@Context
	private ServletConfig servletConfig;
	@Context
	private ServletContext servletContext;
	@Context
	private HttpServletRequest httpServletRequest;
	@Context
	private HttpServletResponse httpServletResponse;
	
    @GET
    @Produces("application/json; charset=utf-8")
	public String doGet(
			@QueryParam("TargetCorpus") String targetCorpus,
			@QueryParam("Corpus") String corpus,
			@QueryParam("Document") String document,
			@QueryParam("Annotation") String annotation
			) throws JsonProcessingException, IOException, ServletException
    {
    	if (getStorage() == null) {
    		super.init(servletConfig);
    	}
	
    	ObjectNode retval = getMapper().createObjectNode();
    	ObjectNode corpusAggregates = (ObjectNode) getStorage().getCorpusAggregatesJson(targetCorpus);
    	ObjectNode documentAggregates = (ObjectNode) getStorage().getCorpusDocumentAggregatesJson(corpus, document);
       	retval.put(
       			"value", 
       			calculateSimilarity(corpusAggregates, documentAggregates, annotation)
       	);

        return getMapper().writeValueAsString(retval);
	}
    
	protected Double calculateSimilarity(ObjectNode corpusAggregates, ObjectNode documentAggregates, String annotation) {
		CosineSimilarity similarity = new CosineSimilarity();
		Map<CharSequence,Integer> corpusScores = new HashMap<CharSequence, Integer>();
		Map<CharSequence,Integer> documentScores = new HashMap<CharSequence, Integer>();
		Iterator<String> annotationNameIter = documentAggregates.fieldNames();
		while (annotationNameIter.hasNext()) {
			String annotationName = annotationNameIter.next();
			if (annotationName.equals(annotation) && documentAggregates.get(annotationName).isObject() && corpusAggregates.has(annotationName) && corpusAggregates.get(annotationName).isObject()) {
				ObjectNode documentAnnotationScoreStats = (ObjectNode) documentAggregates.get(annotationName);
				ArrayNode aggregatedScores = (ArrayNode) documentAnnotationScoreStats.get("aggregatedScores");
				Iterator<JsonNode> aggregatedScoreIter = aggregatedScores.iterator();
				while (aggregatedScoreIter.hasNext()) {
					ObjectNode documentAnnotationSubScoreStatsScore = (ObjectNode) aggregatedScoreIter.next();
					String subscoreName = documentAnnotationSubScoreStatsScore.get("name").asText();		
					ObjectNode corpusAnnotationSubScoreStats = (ObjectNode) getSubscoreFromCorpusAggregate(corpusAggregates, annotationName, subscoreName);
					if (corpusAnnotationSubScoreStats != null) {
						BigDecimal documentAnnotationScore = new BigDecimal(documentAnnotationSubScoreStatsScore.get("score").get("normalized").asText());
						BigDecimal corpusAnnotationScore = new BigDecimal(corpusAnnotationSubScoreStats.get("score").get("normalized").get("median").asText());
			    		
				    	corpusScores.put(subscoreName, corpusAnnotationScore.multiply(new BigDecimal(10000)).intValue());
				    	documentScores.put(subscoreName, documentAnnotationScore.multiply(new BigDecimal(10000)).intValue());
					}
				}
			}
		}
		return similarity.cosineSimilarity(corpusScores, documentScores);
	}
				
	protected ObjectNode getSubscoreFromCorpusAggregate(ObjectNode corpusAggregates, String score, String subscore) {
		ObjectNode corpusAnnotationScoreStats = (ObjectNode) corpusAggregates.get(score);
		if (corpusAnnotationScoreStats.has("aggregatedScores")) {
			ArrayNode aggregatedScores = (ArrayNode) corpusAnnotationScoreStats.get("aggregatedScores");
			for (JsonNode aggregatedScore : aggregatedScores) {
				if (aggregatedScore.get("name").asText().equals(subscore)) {
					return (ObjectNode) aggregatedScore;
				}
			}
		}

		return null;
	}


}
