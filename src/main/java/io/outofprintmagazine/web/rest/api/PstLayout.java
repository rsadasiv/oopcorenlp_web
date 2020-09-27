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

import io.outofprintmagazine.web.servlets.AbstractOOPCacheableServlet;

@Path("/PstLayout")
public class PstLayout extends AbstractOOPCacheableServlet {
	
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
    @Produces("text/html; charset=utf-8")
	public String doGet(
			@QueryParam("Corpus") String corpus,
			@QueryParam("Document") String document
			) throws ServletException, JsonProcessingException, IOException 
    {
    	if (getStorage() == null) {
    		super.init(servletConfig);
    	}
    	return generatePst(
    			getStorage().getCorpusDocumentOOPJson(corpus, document)
    	).toString();
	}

}