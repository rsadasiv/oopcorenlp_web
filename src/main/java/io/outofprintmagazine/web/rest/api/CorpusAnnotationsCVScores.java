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

@Path("/CorpusAnnotationsCVScores")
public class CorpusAnnotationsCVScores extends AbstractOOPCacheableServlet {
	
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
			@QueryParam("Corpus") String corpus,
			//raw, normalized, count
			@QueryParam("AggregateName") String aggregateName
			) throws JsonProcessingException, IOException, ServletException
    {
    	if (getStorage() == null) {
    		super.init(servletConfig);
    	}
	
    	ArrayNode retval = getMapper().createArrayNode();
    	ObjectNode corpusAggregates = (ObjectNode) getStorage().getCorpusAggregatesJson(corpus);
    	calculateCVScores(corpusAggregates, aggregateName==null?"normalized":aggregateName, retval);

        JsonSort.sort(retval);
        Iterator<JsonNode> iter = retval.iterator();
        for (int i=0;iter.hasNext();i++) {
        	ObjectNode o = (ObjectNode) iter.next();
        	o.put("id", i);
        }
        return getMapper().writeValueAsString(retval);
	}
    
	protected void calculateCVScores(ObjectNode corpusAggregates, String aggregateName, ArrayNode retval) {
		List<String> skipAnnotations = Arrays.asList("OOPSyllableCountAnnotation", "OOPCharCountAnnotation", "OOPWordCountAnnotation", "OOPParagraphCountAnnotation");
		Iterator<String> annotationNameIter = corpusAggregates.fieldNames();
		while (annotationNameIter.hasNext()) {
			String annotationName = annotationNameIter.next();
			if (!skipAnnotations.contains(annotationName) && corpusAggregates.get(annotationName).isObject()) {
				ObjectNode corpusAnnotationScoreStats = (ObjectNode) corpusAggregates.get(annotationName);
			    ObjectNode corpusAnnotationScoreStatsScore = (ObjectNode) corpusAnnotationScoreStats.get("score").get(aggregateName);

			    BigDecimal corpusStddev = new BigDecimal(corpusAnnotationScoreStatsScore.get("stddev").asDouble());
			    BigDecimal corpusMean = new BigDecimal(corpusAnnotationScoreStatsScore.get("mean").asDouble());

	    		if (!corpusMean.equals(new BigDecimal(0)) && !corpusStddev.equals(new BigDecimal(0))) {
				    BigDecimal cvScore = corpusStddev.divide(corpusMean, 10, RoundingMode.HALF_DOWN);
				    ObjectNode r = createObjectTidy(0, annotationName, cvScore);
			    	retval.add(r);
	    		}
			}
		}
	}
}
