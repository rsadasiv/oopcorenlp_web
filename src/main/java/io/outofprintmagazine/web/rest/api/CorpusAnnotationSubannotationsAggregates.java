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

@Path("/CorpusAnnotationSubannotationsAggregates")
public class CorpusAnnotationSubannotationsAggregates extends AbstractOOPCacheableServlet {
	
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
    	calculateSubScores(corpusAggregates, annotation, aggregateName==null?"normalized":aggregateName, retval);

        JsonSort.sort(retval);
        Iterator<JsonNode> iter = retval.iterator();
        for (int i=0;iter.hasNext();i++) {
        	ObjectNode o = (ObjectNode) iter.next();
        	o.put("id", i);
        }
        return getMapper().writeValueAsString(retval);
	}
    
    protected void calculateSubScores(ObjectNode corpusAggregates, String annotation, String aggregateName, ArrayNode retval) {
	    List<String> scoreMeasures = Arrays.asList("raw", "normalized", "count");
	    List<String> statsMeasures = Arrays.asList("rank", "percentage", "percentile");
		String measure = null;
		if (scoreMeasures.contains(aggregateName)) {
			measure = "score";
		}
		if (statsMeasures.contains(aggregateName)) {
			measure = "aggregateScore";
		}
		ObjectNode corpusAnnotationScoreStats = (ObjectNode) corpusAggregates.get(annotation);
		if (corpusAnnotationScoreStats.has("aggregatedScores")) {
			ArrayNode aggregatedScores = (ArrayNode) corpusAnnotationScoreStats.get("aggregatedScores");
			for (JsonNode aggregatedScore : aggregatedScores) {
				ObjectNode r = createObjectTidy(
						0, 
						aggregatedScore.get("name").asText(), 
						new BigDecimal(aggregatedScore.get(measure).get(aggregateName).get("mean").asDouble())
				);
		    	r.put("corpusStddev", new BigDecimal(aggregatedScore.get(measure).get(aggregateName).get("stddev").asDouble()));
		    	r.put("corpusMin", new BigDecimal(aggregatedScore.get(measure).get(aggregateName).get("min").asDouble()));
		    	r.put("corpusMean", new BigDecimal(aggregatedScore.get(measure).get(aggregateName).get("mean").asDouble()));
		    	r.put("corpusMedian", new BigDecimal(aggregatedScore.get(measure).get(aggregateName).get("median").asDouble()));
		    	r.put("corpusMax", new BigDecimal(aggregatedScore.get(measure).get(aggregateName).get("max").asDouble()));
		    	retval.add(r);
			}
		}
    }
}
