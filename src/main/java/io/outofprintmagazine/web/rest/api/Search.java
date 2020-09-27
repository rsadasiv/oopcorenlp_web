package io.outofprintmagazine.web.rest.api;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import io.burt.jmespath.Expression;
import io.burt.jmespath.JmesPath;
import io.burt.jmespath.jackson.JacksonRuntime;
import io.outofprintmagazine.web.servlets.AbstractOOPCacheableServlet;

@Path("/Search")
public class Search extends AbstractOOPCacheableServlet {
	
	private static final long serialVersionUID = 1L;
	@Context
	private ServletConfig servletConfig;
	@Context
	private ServletContext servletContext;
	@Context
	private HttpServletRequest httpServletRequest;
	@Context
	private HttpServletResponse httpServletResponse;
	
	private JmesPath<JsonNode> jmespath = new JacksonRuntime();
	
	
    @POST
    @Produces("application/json; charset=utf-8")
	public String doPost(
			@FormParam("Corpus") String corpus,
			@FormParam("Document") String document,
			@FormParam("q") String q
			) throws ServletException, JsonProcessingException, IOException 
    {
    	if (getStorage() == null) {
    		super.init(servletConfig);
    	}
    	Expression<JsonNode> expression = jmespath.compile(q);
        return getMapper().writeValueAsString(
        		expression.search(
        				getStorage().getCorpusDocumentOOPJson(
        						corpus, 
        						document
        				)
        		)
        );
	}

}

