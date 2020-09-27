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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.outofprintmagazine.web.servlets.AbstractOOPCacheableServlet;

@Path("/Corpora/{Corpus}/{Document}/PIPELINE/{Annotation}/{Subannotation}")
public class CorpusDocumentPIPELINEAnnotationSubannotation extends AbstractOOPCacheableServlet  {

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
			@PathParam("Annotation") String annotation,
			@PathParam("Subannotation") String subannotation
			) throws JsonProcessingException, IOException, ServletException
    {
    	if (getStorage() == null) {
    		super.init(servletConfig);
    	}
	
		ObjectNode retval = getMapper().createObjectNode();
		JsonNode pipeline = getStorage().getCorpusDocumentPipelineJson(corpus, document);
		if (pipeline.hasNonNull(annotation)) {
			ArrayNode annotationList = (ArrayNode)pipeline.get(annotation);
			Iterator<JsonNode> annotationListIter = annotationList.iterator();				
			if (annotation.equals("annotations") || annotation.equals("analysis")) {
				while (annotationListIter.hasNext()) {
					JsonNode subannotationNode = annotationListIter.next();
					if (subannotationNode.hasNonNull(subannotation)) {
						retval.set(subannotation, subannotationNode);
						break;
					}
				}
			}
			else if (annotation.equals("coreNlpProperties")) {			
				while (annotationListIter.hasNext()) {
					JsonNode subannotationNode = annotationListIter.next();
					if (subannotationNode.hasNonNull("name") && subannotationNode.get("name").asText().equals(subannotation)) {
						retval.set(subannotation, subannotationNode);
						break;
					}
				}
			}
		}

		return getMapper().writeValueAsString(retval);
	}

}
