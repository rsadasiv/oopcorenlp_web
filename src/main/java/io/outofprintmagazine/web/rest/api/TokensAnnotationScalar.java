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

@Path("/TokensAnnotationScalar")
public class TokensAnnotationScalar extends AbstractOOPServlet {
	
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
			@QueryParam("Format") String format
			) throws JsonProcessingException, IOException, ServletException
    {
    	if (getStorage() == null) {
    		super.init(servletConfig);
    	}
	
    	JsonNode scores = getStorage().getCorpusDocumentOOPJson(corpus, document);
        ArrayNode retval = getMapper().createArrayNode();
        Iterator<JsonNode> sentenceIterator = scores.get("sentences").elements();
        int tokenIdx = 0;
        while (sentenceIterator.hasNext()) {
    		JsonNode sentence = sentenceIterator.next();
    		Iterator<JsonNode> tokensIterator = ((ArrayNode)sentence.get("tokens")).iterator();
    		while (tokensIterator.hasNext()) {
    			JsonNode token = tokensIterator.next();		
	    		if (token.hasNonNull(annotation)) {
	    			if (sentence.get(annotation).isArray()) {
	    	        	retval.add(createObjectTidy(tokenIdx, annotation, new BigDecimal(token.get(annotation).size()))); 				
	    			}
	    			else if (token.get(annotation).isObject()) {
						BigDecimal sz = new BigDecimal(0);
						Iterator<String> valueNamesIter = token.get(annotation).fieldNames();
						while (valueNamesIter.hasNext()) {
							String valueName = valueNamesIter.next();
							sz = sz.add(
									new BigDecimal(
											token.get(annotation).get(valueName).asText("0.0")
									)
							);
						}
	    	        	retval.add(createObjectTidy(tokenIdx, annotation, sz)); 
	    			}
	    			else {
	    	        	retval.add(createObjectTidy(tokenIdx, annotation, new BigDecimal(token.get(annotation).asText("0.0"))));
	    			}
	    		}
	    		else {
		        	retval.add(createObjectTidy(tokenIdx, annotation, new BigDecimal(("0.0"))));
	    		}
	    		tokenIdx++;
    		}
        }
        return getMapper().writeValueAsString(reformatTidyArray(retval, format));
	}

}
