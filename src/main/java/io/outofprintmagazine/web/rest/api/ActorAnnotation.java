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
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.outofprintmagazine.web.servlets.AbstractOOPServlet;
import io.outofprintmagazine.web.util.JsonSort;

@Path("/ActorAnnotation")
public class ActorAnnotation extends AbstractOOPServlet {
	
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
			@QueryParam("Actor") String actor,
			@QueryParam("Annotation") String annotation,			
			@QueryParam("Format") String format
			) throws JsonProcessingException, IOException, ServletException
    {
    	if (getStorage() == null) {
    		super.init(servletConfig);
    	}
	
    	JsonNode scores = getStorage().getCorpusDocumentOOPJson(corpus, document);
        ArrayNode retval = getMapper().createArrayNode();
        Iterator<JsonNode> iter = scores.get("OOPActorsAnnotation").iterator();
        while (iter.hasNext()) {
        	JsonNode actorNode = iter.next();
        	if (actor.equals(actorNode.get("canonicalName").asText())) {
        		if (actorNode.get("attributes").hasNonNull(annotation)) {
        			Iterator<String> attributeIter = ((ObjectNode)actorNode.get("attributes").get(annotation)).fieldNames();
        			while (attributeIter.hasNext()) {
        				String subannotation = attributeIter.next();
			        	retval.add(createObjectTidy(0, subannotation, new BigDecimal(actorNode.get("attributes").get(annotation).get(subannotation).asText())));
			        }
        		}
        	}
        }
        JsonSort.sort(retval);
        iter = retval.iterator();
        for (int i=0;iter.hasNext();i++) {
        	ObjectNode actorNode = (ObjectNode) iter.next();
        	actorNode.put("id", Integer.toString(i));
        }
        return getMapper().writeValueAsString(reformatTidyArray(retval, format));
	}

}
