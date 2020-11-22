package io.outofprintmagazine.web.rest.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import io.outofprintmagazine.web.util.JsonSort;

@Path("/CorpusDocumentAnnotationsSimilarity")
public class CorpusDocumentAnnotationsSimilarity extends AbstractOOPCacheableServlet {
	
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
			@QueryParam("Document") String document
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
       			calculateSimilarity(corpusAggregates, documentAggregates)
       	);

        return getMapper().writeValueAsString(retval);
	}
    
	protected Double calculateSimilarity(ObjectNode corpusAggregates, ObjectNode documentAggregates) {
		CosineSimilarity similarity = new CosineSimilarity();
		Map<CharSequence,Integer> corpusScores = new HashMap<CharSequence, Integer>();
		Map<CharSequence,Integer> documentScores = new HashMap<CharSequence, Integer>();
		Iterator<String> annotationNameIter = documentAggregates.fieldNames();
		while (annotationNameIter.hasNext()) {
			String annotationName = annotationNameIter.next();
			if (documentAggregates.get(annotationName).isObject()) {
				if (!annotationName.equals("metadata")  && corpusAggregates.has(annotationName) && corpusAggregates.get(annotationName).isObject()) {
					ObjectNode documentAnnotationScoreStats = (ObjectNode) documentAggregates.get(annotationName);
					ObjectNode corpusAnnotationScoreStats = (ObjectNode) corpusAggregates.get(annotationName);
			    	BigDecimal documentAnnotationScore = new BigDecimal(documentAnnotationScoreStats.get("scoreStats").get("score").get("normalized").asText());
			    	BigDecimal corpusAnnotationScore = new BigDecimal(corpusAnnotationScoreStats.get("score").get("normalized").get("median").asText());		    		
			    	corpusScores.put(annotationName, corpusAnnotationScore.multiply(new BigDecimal(10000)).intValue());
			    	documentScores.put(annotationName, documentAnnotationScore.multiply(new BigDecimal(10000)).intValue());
				}
			}
		}
		return similarity.cosineSimilarity(corpusScores, documentScores);
	}

}
