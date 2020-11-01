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
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Element;

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
    
    protected Element generatePst(JsonNode stats) {
    	Element div = new Element("div");
		int paragraphIdx = -1;
		int tokenIdx = 0;
		Element currentParagraphNode = null;
		ArrayNode sentences = (ArrayNode) stats.get("sentences");
		Iterator<JsonNode> sentencesIter = sentences.iterator();
		while (sentencesIter.hasNext()) {
			JsonNode sentence = sentencesIter.next();
			if (sentence.get("ParagraphIndexAnnotation").asInt(-1) > paragraphIdx) {
				paragraphIdx = sentence.get("ParagraphIndexAnnotation").asInt(-1);
				currentParagraphNode = new Element("p");
				currentParagraphNode.attr("id", "paragraph_"+paragraphIdx);
				currentParagraphNode.appendTo(div);
			}
			Element sentenceNode = new Element("span");
			sentenceNode.attr("id", "sentence_"+sentence.get("SentenceIndexAnnotation").asText());
			sentenceNode.attr("class", "sentence");
			sentenceNode.appendTo(currentParagraphNode);
			ArrayNode tokens = (ArrayNode) sentence.get("tokens");
			Iterator<JsonNode> tokensIter = tokens.iterator();
			while (tokensIter.hasNext()) {
				JsonNode token = tokensIter.next();
				sentenceNode.append(token.get("TokensAnnotation").get("before").asText());
				Element tokenNode = new Element("span");
				tokenNode.attr("id", "token_"+tokenIdx);
				tokenNode.attr("class", "token");
				tokenNode.text(token.get("TokensAnnotation").get("originalText").asText());
				tokenNode.appendTo(sentenceNode);
				
				tokenIdx++;
			}
		}
		
		return div;
    }
    
    protected void setPstAttribute(HttpServletRequest request, String corpus, String document) throws IOException {

        request.setAttribute(
        	"Pst",
        	generatePst(
        			getStorage().getCorpusDocumentOOPJson(corpus, document)
        	)
        );
    }
    
    protected void setMetadataAttributes(HttpServletRequest request, String corpus, String document) throws IOException {
        JsonNode stats = getStorage().getCorpusDocumentOOPMetadata(corpus, document);
        request.setAttribute("DocumentMetadata", stats);
        request.setAttribute("Author", stats.get("AuthorAnnotation").asText());
        request.setAttribute("Date", stats.get("DocDateAnnotation").asText());
        request.setAttribute("Title", stats.get("DocTitleAnnotation").asText());    	
    }
    
    protected void setStatsAttribute(HttpServletRequest request, String corpus, String document) throws IOException {
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
    
    protected void setAnnotationDescriptionsAttribute(HttpServletRequest request, String corpus, String document) throws IOException {
    	request.setAttribute("AnnotationDescriptions", getAnnotationDescription(corpus, document, null));
    }
    
    protected void setContentTypeJson(HttpServletResponse response) {
    	response.setContentType("application/json; charset=utf-8");
    }
    
	protected ObjectNode createObjectTidy(int id, String name, BigDecimal score) {
		ObjectNode val = getMapper().createObjectNode();
		val.put("id", id);
		val.put("name", name);
		val.put("value", score);
		return val;
	}
	
	protected ObjectNode createObject(int id, String name, BigDecimal score) {
		ObjectNode val = getMapper().createObjectNode();
		val.put("id", id);
		val.put(name, score);
		return val;
	}
	
	protected ObjectNode createObjectCloud(int id, String name, BigDecimal score) {
		ObjectNode val = getMapper().createObjectNode();
		val.put("id", id);
		val.put("text", name);
		val.put("size", score);
		return val;
	}
	
	protected ArrayNode reformatTidyArray(ArrayNode input, String format) {
        ArrayNode retval = getMapper().createArrayNode();
        Iterator<JsonNode> iter = input.iterator();
        for (int i=0;iter.hasNext();i++) {
        	ObjectNode o = (ObjectNode) iter.next();
        	if ("D3".equals(format)) {
        		retval.add(createObjectTidy(o.get("id").asInt(), o.get("name").asText(), o.get("value").decimalValue()));
        	}
        	else if ("Cloud".equals(format)) {
        		retval.add(createObjectCloud(o.get("id").asInt(), o.get("name").asText(), o.get("value").decimalValue()));
        	}
        	else if ("Object".equals(format)){
        		retval.add(createObject(o.get("id").asInt(), o.get("name").asText(), o.get("value").decimalValue()));
        	}
        	else {
        		retval.add(createObjectTidy(o.get("id").asInt(), o.get("name").asText(), o.get("value").decimalValue()));
        	}        	
        }
        return retval;
	}

}
