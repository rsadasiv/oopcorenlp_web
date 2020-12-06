package io.outofprintmagazine.web.rest.api;

import java.io.IOException;
import java.math.BigDecimal;
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

@Path("/CorpusDocumentAnnotationSubannotationsAggregates")
public class CorpusDocumentAnnotationSubannotationsAggregates extends AbstractOOPCacheableServlet {
	
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
			@QueryParam("Annotation") String annotation,
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
    	calculateSubScores(corpusAggregates, documentAggregates, annotation, aggregateName==null?"normalized":aggregateName, retval);

        JsonSort.sort(retval);
        Iterator<JsonNode> iter = retval.iterator();
        for (int i=0;iter.hasNext();i++) {
        	ObjectNode o = (ObjectNode) iter.next();
        	o.put("id", i);
        }
        return getMapper().writeValueAsString(retval);
	}
    
    protected void calculateSubScores(ObjectNode corpusAggregates, ObjectNode documentAggregates, String annotation, String aggregateName, ArrayNode retval) {
	    List<String> scoreMeasures = Arrays.asList("raw", "normalized", "count");
	    List<String> statsMeasures = Arrays.asList("rank", "percentage", "percentile");
		String measure = null;
		if (scoreMeasures.contains(aggregateName)) {
			measure = "score";
		}
		if (statsMeasures.contains(aggregateName)) {
			measure = "aggregateScore";
		}
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
					if (measure != null) {
				    	BigDecimal documentScore = new BigDecimal(documentAnnotationSubScoreStatsScore.get(measure).get(aggregateName).asDouble());
			    		ObjectNode r = createObjectTidy(0, subscoreName, documentScore);
			    		retval.add(r);
					}

				}
			}
		}
	}
}
