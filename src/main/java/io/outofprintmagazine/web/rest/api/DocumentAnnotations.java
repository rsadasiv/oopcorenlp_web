package io.outofprintmagazine.web.rest.api;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

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

@Path("/DocumentAnnotations")
public class DocumentAnnotations extends AbstractOOPCacheableServlet {
	
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
			@QueryParam("Document") String document
			) throws JsonProcessingException, IOException, ServletException
    {
    	if (getStorage() == null) {
    		super.init(servletConfig);
    	}
	
    	ArrayNode retval = getMapper().createArrayNode();
    	List<String> skipFields = Arrays.asList("metadata", "corefs", "sentences", "quotes");
    	SortedSet<String> fieldNames = new TreeSet<String>();

		JsonNode scores = getStorage().getCorpusDocumentOOPJson(corpus, document);
    	Iterator<String> fieldNameIter = ((ObjectNode)scores).fieldNames();
    	while (fieldNameIter.hasNext()) {
    		String fieldName = fieldNameIter.next();
    		if (!skipFields.contains(fieldName)) {
    			fieldNames.add(fieldName);
    		}
    	}

    	for (String fieldName : fieldNames) {
    		retval.add(fieldName);
    	}

        return getMapper().writeValueAsString(retval);
	}

}
