/*******************************************************************************
 * Copyright (C) 2020 Ram Sadasiv
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package io.outofprintmagazine.web.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.outofprintmagazine.web.storage.FileStorage;
import io.outofprintmagazine.web.storage.IStorage;


public abstract class AbstractOOPServlet extends HttpServlet {
    
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = LogManager.getLogger(AbstractOOPServlet.class);
	
	@SuppressWarnings("unused")
	private Logger getLogger() {
		return logger;
	}
	
    public AbstractOOPServlet() {
        super();
    }
    
    @Override
    public void init(ServletConfig config) throws ServletException {
    	super.init(config);
    	Properties properties = new Properties();
    	properties.put(
    			"fileCorpus_Path", 
    			config.getServletContext().getRealPath("/Corpora")
    	);
    	try {
			storage = FileStorage.getInstance(properties);
		} 
    	catch (IOException e) {
    		getLogger().error(e);
			throw new ServletException(e);
		}
    }
    
    private IStorage storage = null;
    
    protected IStorage getStorage() {
    	return storage;
    }
    
    private ObjectMapper mapper = new ObjectMapper();
    
    protected ObjectMapper getMapper() {
    	return mapper;
    }

	
    protected String plainTextToHtml(String input) throws IOException {
	    StringBuilder contentBuilder = new StringBuilder();
	    String sCurrentLine;
	    BufferedReader br = null;
	    try {
	    	br = new BufferedReader(
	            	new StringReader(
	            			input
	            	)
	        );
	        while ((sCurrentLine = br.readLine()) != null) {
	        	if (sCurrentLine.equals("")) {
	        		sCurrentLine = "&nbsp;";
	        	}
	        	contentBuilder.append("<p>").append(sCurrentLine).append("</p>").append("\n");
	        }
	    }
	    finally {
	    	if (br != null) {
	    		br.close();
	    	}
	    }
        return contentBuilder.toString();
    }
    
    public void setMetadataAttributes(HttpServletRequest request, String corpus, String document) throws IOException {
        JsonNode stats = getStorage().getCorpusDocumentOOPMetadata(corpus, document);
        request.setAttribute("DocumentMetadata", stats);
        request.setAttribute("Author", stats.get("AuthorAnnotation").asText());
        request.setAttribute("Date", stats.get("DocDateAnnotation").asText());
        request.setAttribute("Title", stats.get("DocTitleAnnotation").asText());    	
    }
    
    public void setStatsAttribute(HttpServletRequest request, String corpus, String document) throws IOException {
		request.setAttribute("Stats", getStorage().getCorpusDocumentOOPJson(corpus, document));
    }
    
    protected ObjectNode getAnnotationDescription(String corpus, String document, String annotation) throws IOException {
		ObjectNode retval = getMapper().createObjectNode();
		JsonNode pipeline = getStorage().getCorpusDocumentPipelineJson(corpus, document);
		ArrayNode annotations = (ArrayNode) pipeline.get("annotations");
		Iterator<JsonNode> annotationsIter = annotations.elements();
		while (annotationsIter.hasNext()) {
			JsonNode annotationNode = annotationsIter.next();
			Iterator<String> keyIter = annotationNode.fieldNames();
			while (keyIter.hasNext()) {
				String keyName = keyIter.next();
				if (annotation == null || annotation.equals(keyName)) {
					retval.put(keyName, annotationNode.get(keyName).asText());
					break;
				}
				
			}
		}
		return retval;
    }
    
    public void setAnnotationDescriptionsAttribute(HttpServletRequest request, String corpus, String document) throws IOException {
    	request.setAttribute("AnnotationDescriptions", getAnnotationDescription(corpus, document, null));
    }

}
