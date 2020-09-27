package io.outofprintmagazine.web.rest.browse;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import io.outofprintmagazine.web.servlets.AbstractOOPCacheableServlet;

@Path("/Corpora/{Corpus}/{Document}/TXT")
public class CorpusDocumentTXT extends AbstractOOPCacheableServlet {
	
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
    @Produces("text/plain")
	public String doGet(
			@PathParam("Corpus") String corpus,
			@PathParam("Document") String document	
			) throws IOException, ServletException
    {
    	if (getStorage() == null) {
    		super.init(servletConfig);
    	}
	
		return getStorage().getCorpusDocumentTxtString(corpus, document);
	}
}
