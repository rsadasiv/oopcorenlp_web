package io.outofprintmagazine.web.rest.api;

import java.io.FileNotFoundException;
import java.io.IOException;
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

@Path("/TokenIdxSentence")
public class TokenIdxSentence extends AbstractOOPServlet {
	
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
			@QueryParam("TokenIdx") Integer tokenIdx
			) throws ServletException, JsonProcessingException, IOException 
    {
    	if (getStorage() == null) {
    		super.init(servletConfig);
    	}
    	
    	JsonNode stats = getStorage().getCorpusDocumentOOPJson(corpus, document);
		int currentTokenId = 0;
		ArrayNode sentences = (ArrayNode) stats.get("sentences");
		Iterator<JsonNode> sentencesIter = sentences.iterator();
		while (sentencesIter.hasNext()) {
			JsonNode sentence = sentencesIter.next();
			ArrayNode tokens = (ArrayNode) sentence.get("tokens");
			if (tokenIdx.intValue() < (currentTokenId+tokens.size())) {
		        return getMapper().writeValueAsString(sentence);
			}
			currentTokenId=currentTokenId+tokens.size();
		}
		throw new FileNotFoundException();
    			
	}

}