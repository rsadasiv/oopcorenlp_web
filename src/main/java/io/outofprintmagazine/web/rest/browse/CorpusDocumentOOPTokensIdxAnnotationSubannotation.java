package io.outofprintmagazine.web.rest.browse;

import java.io.IOException;
import java.util.Iterator;

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
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.outofprintmagazine.web.servlets.AbstractOOPCacheableServlet;

@Path("/Corpora/{Corpus}/{Document}/OOP/tokens/{TokenIdx}/{Annotation}/{Subannotation}")
public class CorpusDocumentOOPTokensIdxAnnotationSubannotation extends AbstractOOPCacheableServlet  {

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
			@PathParam("TokenIdx") Integer tokenIdx,
			@PathParam("Annotation") String annotation,
			@PathParam("Subannotation") String subannotation				
			) throws IOException, ServletException
    {
    	if (getStorage() == null) {
    		super.init(servletConfig);
    	}
    	
		if (tokenIdx.intValue() < 0 ) {
			throw new IndexOutOfBoundsException();
		}
	
		ObjectNode retval = getMapper().createObjectNode();
		JsonNode oop = getStorage().getCorpusDocumentOOPJson(corpus, document);
		if (oop.hasNonNull("sentences")) {
			int tokenCount = 0;
			ArrayNode sentencesNode = (ArrayNode) oop.get("sentences");
			Iterator<JsonNode> sentencesIter = sentencesNode.iterator();
			while (sentencesIter.hasNext()) {
				JsonNode sentenceNode = sentencesIter.next();
				ArrayNode tokensNode = (ArrayNode) sentenceNode.get("tokens");
				Iterator<JsonNode> tokensIter = tokensNode.iterator();
				while (tokensIter.hasNext()) {
					JsonNode tokenNode = tokensIter.next();
					if (tokenIdx.intValue() == tokenCount) {
						if (tokenNode.hasNonNull(annotation)) {
							JsonNode annotationNode = tokensNode.get(tokenIdx.intValue()).get(annotation);
							if (annotationNode.isObject()) {
								ObjectNode o = (ObjectNode)annotationNode;
								if (o.hasNonNull(subannotation)) {
									retval.set(subannotation, o.get(subannotation));
									return getMapper().writeValueAsString(retval);
								}
							}
							else if (annotationNode.isArray()) {
								ArrayNode o = (ArrayNode)annotationNode;
								Iterator<JsonNode> annotationListIter = o.iterator();
								while (annotationListIter.hasNext()) {
									JsonNode subannotationNode = annotationListIter.next();
									if (subannotationNode.hasNonNull("name") && subannotationNode.get("name").asText().equals(subannotation)) {
										retval.set(subannotation, subannotationNode);
										return getMapper().writeValueAsString(retval);
									}
								}
							}							
						}
					}
					tokenCount++;
				}
			}
		}
		throw new IndexOutOfBoundsException();
	}

}
