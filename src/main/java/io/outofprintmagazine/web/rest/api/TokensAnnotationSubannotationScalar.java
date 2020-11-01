package io.outofprintmagazine.web.rest.api;

import java.io.IOException;
import java.math.BigDecimal;
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

import io.outofprintmagazine.web.servlets.AbstractOOPServlet;

@Path("/TokensAnnotationSubannotationScalar")
public class TokensAnnotationSubannotationScalar extends AbstractOOPServlet {
	
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
			@QueryParam("Document") String document,
			@QueryParam("Annotation") String annotation,
			@QueryParam("Subannotation") String subannotation,
			@QueryParam("Format") String format
			) throws JsonProcessingException, IOException, ServletException
    {
    	if (getStorage() == null) {
    		super.init(servletConfig);
    	}
	
        JsonNode scores = getStorage().getCorpusDocumentOOPJson(corpus, document);
        ArrayNode retval = getMapper().createArrayNode();
        int tokenIdx = 0;
        Iterator<JsonNode> sentenceIterator = scores.get("sentences").elements();
        while (sentenceIterator.hasNext()) {
    		JsonNode sentence = sentenceIterator.next();
    		Iterator<JsonNode> tokensIterator = ((ArrayNode)sentence.get("tokens")).iterator();
        	while (tokensIterator.hasNext()) {
	    		JsonNode token = tokensIterator.next();
	    		if (token.hasNonNull(annotation)) {
	    			if (token.get(annotation).isArray()) {
	    	        	Iterator<JsonNode> subscoreIter = ((ArrayNode)token.get(annotation)).elements();
	    	        	BigDecimal sz = new BigDecimal(0);
	    	        	while (subscoreIter.hasNext()) {
	    	        		JsonNode subscore = subscoreIter.next();
	    	        		if (subscore.isObject()) {
	    	        			if (subscore.has("name") && subscore.has("value")) {
	    	        				if (subscore.get("name").asText().equals(subannotation)) {
		    							sz = sz.add(
		    									new BigDecimal(
		    											subscore.get("value").asText("0.0")
		    									)
		    							);
	    	        				}
	    	        			}
	    	        		}
	    	        	}
	    	        	retval.add(createObjectTidy(tokenIdx, subannotation, sz));
	    			}
	    			else if (token.get(annotation).isObject()) {
						BigDecimal sz = new BigDecimal(0);
						Iterator<String> valueNamesIter = token.get(annotation).fieldNames();
						while (valueNamesIter.hasNext()) {
							String valueName = valueNamesIter.next();
							if (valueName.equals(subannotation)) {
								sz = sz.add(
										new BigDecimal(
												token.get(annotation).get(valueName).asText("0.0")
										)
								);
							}
						}
	    	        	retval.add(createObjectTidy(tokenIdx, subannotation, sz));
	    			}
	    			else {
	    	        	retval.add(createObjectTidy(tokenIdx, subannotation, new BigDecimal(0.0)));
	    			}
	    		}
	    		else {
		        	retval.add(createObjectTidy(tokenIdx, subannotation, new BigDecimal(0.0))); 
	    		}
	    		tokenIdx++;
        	}
        }
        return getMapper().writeValueAsString(reformatTidyArray(retval, format));
	}

}
