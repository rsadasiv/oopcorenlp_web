package io.outofprintmagazine.web.rest.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;

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

@Path("/CorpusDocumentAnnotationsZScores")
public class CorpusDocumentAnnotationsZScores extends AbstractOOPCacheableServlet {
	
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
			//raw, normalized, count
			@QueryParam("AggregateName") String aggregateName
			) throws JsonProcessingException, IOException, ServletException
    {
    	if (getStorage() == null) {
    		super.init(servletConfig);
    	}
	
    	ArrayNode retval = getMapper().createArrayNode();
    	ObjectNode corpusAggregates = (ObjectNode) getStorage().getCorpusAggregatesJson(targetCorpus);
    	ObjectNode documentAggregates = (ObjectNode) getStorage().getCorpusDocumentAggregatesJson(corpus, document);
    	calculateZScores(corpusAggregates, documentAggregates, aggregateName==null?"normalized":aggregateName, retval);

        JsonSort.sort(retval);
        Iterator<JsonNode> iter = retval.iterator();
        for (int i=0;iter.hasNext();i++) {
        	ObjectNode o = (ObjectNode) iter.next();
        	o.put("id", i);
        }
        return getMapper().writeValueAsString(retval);
	}
    
	protected void calculateZScores(ObjectNode corpusAggregates, ObjectNode documentAggregates, String aggregateName, ArrayNode retval) {
		Iterator<String> annotationNameIter = documentAggregates.fieldNames();
		while (annotationNameIter.hasNext()) {
			String annotationName = annotationNameIter.next();
			if (documentAggregates.get(annotationName).isObject()) {
				if (!annotationName.equals("metadata")  && corpusAggregates.has(annotationName) && corpusAggregates.get(annotationName).isObject()) {
					ObjectNode documentAnnotationScoreStats = (ObjectNode) documentAggregates.get(annotationName);
					ObjectNode corpusAnnotationScoreStats = (ObjectNode) corpusAggregates.get(annotationName);
			    	ObjectNode documentAnnotationScoreStatsScore = (ObjectNode) documentAnnotationScoreStats.get("scoreStats").get("score");
			    	ObjectNode corpusAnnotationScoreStatsScore = (ObjectNode) corpusAnnotationScoreStats.get("score").get(aggregateName);		    		
	
		    		BigDecimal corpusStddev = corpusAnnotationScoreStatsScore.get("stddev").decimalValue();
		    		BigDecimal zScore = new BigDecimal(0);
		    		if (! corpusStddev.equals(new BigDecimal(0))) {		    				
			    		BigDecimal corpusMean = new BigDecimal(corpusAnnotationScoreStatsScore.get("mean").asDouble());
			    		BigDecimal documentScore = new BigDecimal(documentAnnotationScoreStatsScore.get(aggregateName).asDouble());
			    		zScore = (documentScore.subtract(corpusMean)).divide(corpusStddev, 10, RoundingMode.HALF_DOWN);
		    		}
		    		ObjectNode r = createObjectTidy(0, annotationName, zScore);
			    	r.put("documentScore", new BigDecimal(documentAnnotationScoreStatsScore.get(aggregateName).asDouble()));    		
			    	r.put("corpusStddev", new BigDecimal(corpusAnnotationScoreStatsScore.get("stddev").asDouble()));
			    	r.put("corpusMin", new BigDecimal(corpusAnnotationScoreStatsScore.get("min").asDouble()));
			    	r.put("corpusMean", new BigDecimal(corpusAnnotationScoreStatsScore.get("mean").asDouble()));
			    	r.put("corpusMedian", new BigDecimal(corpusAnnotationScoreStatsScore.get("median").asDouble()));
			    	r.put("corpusMax", new BigDecimal(corpusAnnotationScoreStatsScore.get("max").asDouble()));
		    		retval.add(r);
				}
			}
		}
	}

}
