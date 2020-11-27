package io.outofprintmagazine.web.rest.api;

import java.io.IOException;

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
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.outofprintmagazine.web.servlets.AbstractOOPCacheableServlet;

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
       	retval.put(
       			"value", 
       			calculateSimilarity(targetCorpus, corpus, document)
       	);

        return getMapper().writeValueAsString(retval);
	}
}
