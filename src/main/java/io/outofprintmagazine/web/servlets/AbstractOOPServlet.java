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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.outofprintmagazine.web.storage.FileStorage;
import io.outofprintmagazine.web.storage.IStorage;


public abstract class AbstractOOPServlet extends HttpServlet {
    
	private static final long serialVersionUID = 1L;
	
    public AbstractOOPServlet() {
        super();
    }
    
    private IStorage storage = new FileStorage();
    
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
        JsonNode stats = getStorage().getCorpusDocumentOOPJson(corpus, document);
        request.setAttribute("Author", stats.get("metadata").get("AuthorAnnotation").asText());
        request.setAttribute("Date", stats.get("metadata").get("DocDateAnnotation").asText());
        request.setAttribute("Title", stats.get("metadata").get("DocTitleAnnotation").asText());    	
    }
    
    public void setStatsAttribute(HttpServletRequest request, String corpus, String document) throws IOException {
		request.setAttribute("Stats", getStorage().getCorpusDocumentOOPJson(corpus, document));
    }

}
