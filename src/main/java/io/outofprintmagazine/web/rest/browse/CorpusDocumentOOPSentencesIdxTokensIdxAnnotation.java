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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import io.outofprintmagazine.web.servlets.AbstractOOPCacheableServlet;

@Path("/Corpora/{Corpus}/{Document}/OOP/sentences/{SentenceIdx}/tokens/{TokenIdx}/{Annotation}")
public class CorpusDocumentOOPSentencesIdxTokensIdxAnnotation extends AbstractOOPCacheableServlet  {

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
			@PathParam("Corpus") String corpus,
			@PathParam("Document") String document,
			@PathParam("SentenceIdx") Integer sentenceIdx,
			@PathParam("TokenIdx") Integer tokenIdx,			
			@PathParam("Annotation") String annotation
			) throws IOException, ServletException
    {
    	if (getStorage() == null) {
    		super.init(servletConfig);
    	}
	
		JsonNode retval = getMapper().createObjectNode();
		JsonNode oop = getStorage().getCorpusDocumentOOPJson(corpus, document);
		if (oop.hasNonNull("sentences")) {
			ArrayNode sentencesNode = (ArrayNode) oop.get("sentences");
			if (sentenceIdx.intValue() < 0 || sentenceIdx.intValue() >= sentencesNode.size() ) {
				throw new IndexOutOfBoundsException();
			}
			ArrayNode tokensNode = (ArrayNode) sentencesNode.get(sentenceIdx.intValue()).get("tokens");
			if (tokenIdx.intValue() < 0 || tokenIdx.intValue() >= tokensNode.size() ) {
				throw new IndexOutOfBoundsException();
			}
			retval = tokensNode.get(tokenIdx.intValue()).get(annotation);
		}

		return getMapper().writeValueAsString(retval);
	}

}
