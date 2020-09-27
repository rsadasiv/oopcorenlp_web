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

import io.outofprintmagazine.web.servlets.AbstractOOPCacheableServlet;
import io.outofprintmagazine.web.util.JsonSort;

@Path("/DocumentAnnotation")
public class DocumentAnnotation extends AbstractOOPCacheableServlet {
	
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
			@QueryParam("Annotation") String annotation,			
			@QueryParam("Format") String format
			) throws JsonProcessingException, IOException, ServletException
    {
    	if (getStorage() == null) {
    		super.init(servletConfig);
    	}
	
        ArrayNode retval = getMapper().createArrayNode();
        JsonNode scores = getStorage().getCorpusDocumentOOPJson(corpus,	document);
        if (scores.hasNonNull(annotation)) {
        	JsonNode annotationNode = scores.get(annotation);
			if (annotationNode.isObject()) {
				ObjectNode o = (ObjectNode)annotationNode;
				Iterator<String> subannotationIter =  o.fieldNames();
				while (subannotationIter.hasNext()) {
					String subannotation = subannotationIter.next();
					retval.add(createObjectD3(0, subannotation, new BigDecimal(o.get(subannotation).asText("0.0"))));
				}
			}
			else if (annotationNode.isArray()) {
				ArrayNode o = (ArrayNode)annotationNode;
				Iterator<JsonNode> annotationListIter = o.iterator();
				while (annotationListIter.hasNext()) {
					JsonNode subannotationNode = annotationListIter.next();
					if (subannotationNode.hasNonNull("name") && subannotationNode.hasNonNull("value")) {
						retval.add(createObjectD3(0, subannotationNode.get("name").asText(), new BigDecimal(subannotationNode.get("value").asText("0.0"))));
					}
				}
			}
        }
        JsonSort.sort(retval);
        Iterator<JsonNode> iter = retval.iterator();
        for (int i=0;iter.hasNext();i++) {
        	ObjectNode o = (ObjectNode) iter.next();
        	o.put("id", Integer.toString(i));
        }
        return getMapper().writeValueAsString(reformatD3Array(retval, format));
	}

}
