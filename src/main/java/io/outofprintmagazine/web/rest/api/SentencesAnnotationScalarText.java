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

import io.outofprintmagazine.web.servlets.AbstractOOPCacheableServlet;

@Path("/SentencesAnnotationScalarText")
public class SentencesAnnotationScalarText extends AbstractOOPCacheableServlet {
	
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
        
        for (int i=0;sentenceIterator.hasNext();i++) {
    		JsonNode sentence = sentenceIterator.next();
    		if (sentence.hasNonNull(annotation)) {
    			if (sentence.get(annotation).isArray()) {
    	        	retval.add(
    	        			createObjectTidy(i, annotation, new BigDecimal(sentence.get(annotation).size()))
    	        			.set("text", sentence.get("text"))
    	        			); 				
    			}
    			else if (sentence.get(annotation).isObject()) {
					BigDecimal sz = new BigDecimal(0);
					Iterator<String> valueNamesIter = sentence.get(annotation).fieldNames();
					while (valueNamesIter.hasNext()) {
						String valueName = valueNamesIter.next();
						sz = sz.add(
								new BigDecimal(
										sentence.get(annotation).get(valueName).asText("0.0")
								)
						);
					}
    	        	retval.add(createObjectTidy(i, annotation, sz).set("text", sentence.get("text"))); 
    			}
    			else {
    	        	retval.add(createObjectTidy(i, annotation, new BigDecimal(sentence.get(annotation).asText("0.0"))).set("text", sentence.get("text")));
    			}
    		}
    		else {
	        	retval.add(createObjectTidy(i, annotation, new BigDecimal(("0.0"))).set("text", sentence.get("text")));
    		}
        }
        return getMapper().writeValueAsString(retval);
	}

}
