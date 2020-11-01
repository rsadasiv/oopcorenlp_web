package io.outofprintmagazine.web.rest.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.outofprintmagazine.web.servlets.AbstractOOPCacheableServlet;
import io.outofprintmagazine.web.util.JsonSort;

@Path("/CorpusDocumentAnnotationSubannotationsTfidf")
public class CorpusDocumentAnnotationSubannotationsTfidf extends AbstractOOPCacheableServlet {
	
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
	
    	ArrayNode retval = getMapper().createArrayNode();
    	ObjectNode corpusAggregates = (ObjectNode) getStorage().getCorpusAggregatesIdfJson(targetCorpus);
    	ObjectNode documentAggregates = (ObjectNode) getStorage().getCorpusDocumentAggregatesJson(corpus, document);
    	calculateTfidfScores(corpusAggregates, documentAggregates, annotation, retval);

        JsonSort.sort(retval);
        Iterator<JsonNode> iter = retval.iterator();
        for (int i=0;iter.hasNext();i++) {
        	ObjectNode o = (ObjectNode) iter.next();
        	o.put("id", i);
        }
        return getMapper().writeValueAsString(retval);
	}
    
    protected void calculateTfidfScores(ObjectNode corpusIdfScores, ObjectNode documentAggregates, String annotation, ArrayNode retval) {
		Iterator<String> annotationNameIter = documentAggregates.fieldNames();
		while (annotationNameIter.hasNext()) {
			String annotationName = annotationNameIter.next();
			if (documentAggregates.get(annotationName).isObject()) {
				if (annotationName.equals(annotation) && corpusIdfScores.has(annotationName) && corpusIdfScores.get(annotationName).isObject()) {
					ObjectNode documentAnnotationScoreStats = (ObjectNode) documentAggregates.get(annotationName);
					ArrayNode aggregatedScores = (ArrayNode) documentAnnotationScoreStats.get("aggregatedScores");
					Iterator<JsonNode> aggregatedScoreIter = aggregatedScores.iterator();
					while (aggregatedScoreIter.hasNext()) {
						ObjectNode aggregatedScore = (ObjectNode) aggregatedScoreIter.next();
						String subscoreName = aggregatedScore.get("name").asText();
						if (corpusIdfScores.get(annotationName).has(subscoreName)) {
							double tf = aggregatedScore.get("score").get("normalized").asDouble();
							double n = corpusIdfScores.get(annotationName).get(subscoreName).get("corpusSize").asDouble();
							double d = corpusIdfScores.get(annotationName).get(subscoreName).get("documentCount").asDouble();
							BigDecimal tfidfValue = new BigDecimal(0);
							if (d != 0) {
								tfidfValue = new BigDecimal(tf * (java.lang.Math.log((n/d))));
							}
							retval.add(createObjectTidy(0, subscoreName, tfidfValue));
						}
					}
				}
			}
		}
	}
}