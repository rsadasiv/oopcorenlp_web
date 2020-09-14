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

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.outofprintmagazine.web.util.JsonSort;


@WebServlet("/GetDocumentAnalysisScores")
public class GetDocumentAnalysisScores extends AbstractOOPServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LogManager.getLogger(GetDocumentAnalysisScores.class);
	
	@SuppressWarnings("unused")
	private Logger getLogger() {
		return logger;
	}
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetDocumentAnalysisScores() {
        super();
    }
    
    protected JsonNode getDocumentScores(String corpus, String document) throws IOException {
        return getStorage().getCorpusDocumentOOPJson(corpus, document);
    }
    
    protected JsonNode getDocumentAnnotationScores(String corpus, String document, String annotation) throws IOException {
        return getStorage().getCorpusDocumentOOPJson(corpus, document).get(annotation);
    }
    
    //Should these be sorted?
    protected ArrayNode getDocumentAnnotationScoresD3Cloud(String corpus, String document, String annotation) throws IOException {
        JsonNode scores = getDocumentAnnotationScores(corpus, document, annotation);
        ArrayNode retval = getMapper().createArrayNode();
        if (scores.isArray()) {
        	JsonSort.sort((ArrayNode)scores);
        	Iterator<JsonNode> subscoreIter = ((ArrayNode)scores).elements();
        	while (subscoreIter.hasNext()) {
        		JsonNode subscore = subscoreIter.next();
        		if (subscore.isObject()) {
        			if (subscore.has("name") && subscore.has("value")) {
    					ObjectNode val = getMapper().createObjectNode();
    					val.put("text", subscore.get("name").asText());
    					val.put("size", new BigDecimal(subscore.get("value").asText()));
    					retval.add(val);        				
        			}
        		}
        	}
        }
        else if (scores.isObject()) {
        	ArrayNode sortedAttributes = getMapper().createArrayNode();
        	Iterator<String> fieldNameIter = ((ObjectNode)scores).fieldNames();
        	while (fieldNameIter.hasNext()) {
        		String subscoreName = fieldNameIter.next();
				ObjectNode val = getMapper().createObjectNode();
				val.put("name", subscoreName);
				val.put("value", new BigDecimal(scores.get(subscoreName).asText()));
				sortedAttributes.add(val);
        	}
        	JsonSort.sort(sortedAttributes);
        	Iterator<JsonNode> subscoreIter = sortedAttributes.elements();
        	while (subscoreIter.hasNext()) {
        		JsonNode subscore = subscoreIter.next();
        		if (subscore.isObject()) {
        			if (subscore.has("name") && subscore.has("value")) {
    					ObjectNode val = getMapper().createObjectNode();
    					val.put("text", subscore.get("name").asText());
    					val.put("size", new BigDecimal(subscore.get("value").asText()));
    					retval.add(val);        				
        			}
        		}
        	}
        }
        else {
			ObjectNode val = getMapper().createObjectNode();
			val.put("text", annotation);
			val.put("size", new BigDecimal(scores.get(annotation).asText()));
			retval.add(val);       	
        }
        return retval;
    }
    
    protected JsonNode getSentenceScores(String corpus, String document, int sentenceId) throws IOException {
        JsonNode stats = getStorage().getCorpusDocumentOOPJson(corpus, document);
        ArrayNode retval = getMapper().createArrayNode();
        JsonNode sentenceStats = stats.get("sentences").get(sentenceId);

        Iterator<String> statsIterator = sentenceStats.fieldNames();
        while (statsIterator.hasNext()) {
        	String fieldName = statsIterator.next();
        	retval.add(getMapper().createObjectNode().set(fieldName, sentenceStats.get(fieldName)));
        }
        //JsonSort.sort(retval);
        return retval;
    }
    
    protected JsonNode getSentencesAnnotationScores(String corpus, String document, String annotation) throws IOException {
        JsonNode scores = getStorage().getCorpusDocumentOOPJson(corpus, document);
        ObjectNode retval = getMapper().createObjectNode();
        ArrayNode stats = retval.putArray(annotation);
        Iterator<JsonNode> sentenceIterator = scores.get("sentences").elements();
        while (sentenceIterator.hasNext()) {
    		JsonNode sentence = sentenceIterator.next();
    		if (sentence.hasNonNull(annotation)) {
    			if (sentence.get(annotation).isArray()) {
   					stats.add(sentence.get(annotation).size());
    			}
    			else if (sentence.get(annotation).isObject()) {
					BigDecimal sz = new BigDecimal(0);
					Iterator<String> valueNamesIter = sentence.get(annotation).fieldNames();
					while (valueNamesIter.hasNext()) {
						String valueName = valueNamesIter.next();
						sz = sz.add(
								new BigDecimal(
										sentence.get(annotation).get(valueName).asText("0.0")
								)
						);
					}
					stats.add(sz);

    			}
    			else {
    				stats.add(new BigDecimal(sentence.get(annotation).asText("0.0")));
    			}
    		}
    		else {
    			stats.add(new BigDecimal(0.0));
    		}
        }
        return retval;
    }
    
    protected ArrayNode getSentencesAnnotationScoresD3(String corpus, String document, String annotation) throws IOException {
        JsonNode scores = getStorage().getCorpusDocumentOOPJson(corpus, document);
        //ObjectNode retval = getMapper().createObjectNode();
        ArrayNode retval = getMapper().createArrayNode();
        Iterator<JsonNode> sentenceIterator = scores.get("sentences").elements();
        
        for (int i=0;sentenceIterator.hasNext();i++) {
    		JsonNode sentence = sentenceIterator.next();
    		if (sentence.hasNonNull(annotation)) {
    			if (sentence.get(annotation).isArray()) {
					ObjectNode val = getMapper().createObjectNode();
					val.put("id", Integer.toString(i));
					val.put(annotation, sentence.get(annotation).size());
					retval.add(val);    				
    			}
    			else if (sentence.get(annotation).isObject()) {
					BigDecimal sz = new BigDecimal(0);
					Iterator<String> valueNamesIter = sentence.get(annotation).fieldNames();
					while (valueNamesIter.hasNext()) {
						String valueName = valueNamesIter.next();
						sz = sz.add(
								new BigDecimal(
										sentence.get(annotation).get(valueName).asText("0.0")
								)
						);
					}
					ObjectNode val = getMapper().createObjectNode();
					val.put("id", Integer.toString(i));
					val.put(annotation, sz);
					retval.add(val);

    			}
    			else {
					ObjectNode val = getMapper().createObjectNode();
					val.put("id", Integer.toString(i));
					val.put(annotation, new BigDecimal(sentence.get(annotation).asText("0.0")));
					retval.add(val);
    			}
    		}
    		else {
				ObjectNode val = getMapper().createObjectNode();
				val.put("id", Integer.toString(i));
				val.put(annotation, new BigDecimal(0.0));
				retval.add(val);
    		}
        }
        return retval;
    }
    
    protected ArrayNode getSentencesAnnotationScoresRollingD3(String corpus, String document, String annotation, int window) throws IOException {
        ArrayNode rawScores = getSentencesAnnotationScoresD3(corpus, document, annotation);
        ArrayNode retval = getMapper().createArrayNode();
        Iterator<JsonNode> rawScoreIterator = rawScores.elements();
        Deque<BigDecimal> windowBuffer = new ArrayDeque<BigDecimal>();
        for (int i=0;rawScoreIterator.hasNext();i++) {
    		JsonNode rawScore = rawScoreIterator.next();
    		if (i > window) {
    			windowBuffer.removeLast();
    		}
    		windowBuffer.addFirst(new BigDecimal(rawScore.get(annotation).asText()));
    		BigDecimal windowTotal = new BigDecimal(0);
    		Iterator<BigDecimal> windowIter = windowBuffer.iterator();
    		while (windowIter.hasNext()) {
    			windowTotal = windowTotal.add(windowIter.next());
    		}
			ObjectNode val = getMapper().createObjectNode();
			val.put("id", rawScore.get("id").asText());
			val.put(annotation, windowTotal.divide(new BigDecimal(windowBuffer.size()), 10, RoundingMode.HALF_DOWN));
			retval.add(val);
        }
        return retval;
    }
    
    protected JsonNode getSentencesAnnotationSubannotationScores(String corpus, String document, String annotation, String subannotation) throws IOException {
        JsonNode scores = getStorage().getCorpusDocumentOOPJson(corpus, document);
        ObjectNode retval = getMapper().createObjectNode();
        ArrayNode stats = retval.putArray(annotation);
        Iterator<JsonNode> sentenceIterator = scores.get("sentences").elements();
        while (sentenceIterator.hasNext()) {
    		JsonNode sentence = sentenceIterator.next();
    		if (sentence.hasNonNull(annotation)) {
    			if (sentence.get(annotation).isArray()) {
   					stats.add(sentence.get(annotation).size());
    			}
    			else if (sentence.get(annotation).isObject()) {
					BigDecimal sz = new BigDecimal(0);
					Iterator<String> valueNamesIter = sentence.get(annotation).fieldNames();
					while (valueNamesIter.hasNext()) {
						String valueName = valueNamesIter.next();
						if (valueName.equals(subannotation)) {
							sz = sz.add(
									new BigDecimal(
											sentence.get(annotation).get(valueName).asText("0.0")
									)
							);
						}
					}
					
					stats.add(sz);

    			}
    			else {
    				stats.add(new BigDecimal(sentence.get(annotation).asText("0.0")));
    			}
    		}
    		else {
    			stats.add(new BigDecimal(0.0));
    		}
        }
        return retval;
    }
    
    protected ArrayNode getSentencesAnnotationSubannotationScoresD3(String corpus, String document, String annotation, String subannotation) throws IOException {
        JsonNode scores = getStorage().getCorpusDocumentOOPJson(corpus, document);
        ArrayNode retval = getMapper().createArrayNode();
        Iterator<JsonNode> sentenceIterator = scores.get("sentences").elements();
        for (int i=0;sentenceIterator.hasNext();i++) {
    		JsonNode sentence = sentenceIterator.next();
    		if (sentence.hasNonNull(annotation)) {
    			if (sentence.get(annotation).isArray()) {
					ObjectNode val = getMapper().createObjectNode();
					val.put("id", Integer.toString(i));
					val.put(annotation, sentence.get(annotation).size());
					retval.add(val);  
    			}
    			else if (sentence.get(annotation).isObject()) {
					BigDecimal sz = new BigDecimal(0);
					Iterator<String> valueNamesIter = sentence.get(annotation).fieldNames();
					while (valueNamesIter.hasNext()) {
						String valueName = valueNamesIter.next();
						if (valueName.equals(subannotation)) {
							sz = sz.add(
									new BigDecimal(
											sentence.get(annotation).get(valueName).asText("0.0")
									)
							);
						}
					}
					ObjectNode val = getMapper().createObjectNode();
					val.put("id", Integer.toString(i));
					val.put(annotation, sz);
					retval.add(val);


    			}
    			else {
					ObjectNode val = getMapper().createObjectNode();
					val.put("id", Integer.toString(i));
					val.put(annotation, new BigDecimal(sentence.get(annotation).asText("0.0")));
					retval.add(val);
    			}
    		}
    		else {
				ObjectNode val = getMapper().createObjectNode();
				val.put("id", Integer.toString(i));
				val.put(annotation, new BigDecimal(0.0));
				retval.add(val);
    		}
        }
        return retval;
    }
    
    protected ArrayNode getSentencesAnnotationSubannotationScoresRollingD3(String corpus, String document, String annotation, String subannotation, int window) throws IOException {
        ArrayNode rawScores = getSentencesAnnotationSubannotationScoresD3(corpus, document, annotation, subannotation);
        ArrayNode retval = getMapper().createArrayNode();
        Iterator<JsonNode> rawScoreIterator = rawScores.elements();
        Deque<BigDecimal> windowBuffer = new ArrayDeque<BigDecimal>();
        for (int i=0;rawScoreIterator.hasNext();i++) {
    		JsonNode rawScore = rawScoreIterator.next();
    		if (i > window) {
    			windowBuffer.removeLast();
    		}
    		windowBuffer.addFirst(new BigDecimal(rawScore.get(annotation).asText()));
    		BigDecimal windowTotal = new BigDecimal(0);
    		Iterator<BigDecimal> windowIter = windowBuffer.iterator();
    		while (windowIter.hasNext()) {
    			windowTotal = windowTotal.add(windowIter.next());
    		}
			ObjectNode val = getMapper().createObjectNode();
			val.put("id", rawScore.get("id").asText());
			val.put(annotation, windowTotal.divide(new BigDecimal(windowBuffer.size()), 10, RoundingMode.HALF_DOWN));
			retval.add(val);
        }
        return retval;
    }
    
    protected JsonNode getTokenScores(String corpus, String document, int tokenId) throws IOException {
        JsonNode stats = getStorage().getCorpusDocumentOOPJson(corpus, document);
        Iterator<JsonNode> sentenceIterator = stats.get("sentences").elements();
        int tokenIdx = 1;
        while (sentenceIterator.hasNext()) {
        	Iterator<JsonNode> tokensIterator = sentenceIterator.next().get("tokens").elements();
        	while (tokensIterator.hasNext()) {
        		JsonNode token = tokensIterator.next();
        		if (tokenIdx == tokenId) {
        			JsonNode retval = token.deepCopy();
                    //JsonSort.sort(retval);
            		return retval;
        		}
        		tokenIdx++;
        	}
        }
        return null;
    }
    
    protected JsonNode getTokensAnnotationScores(String corpus, String document, String annotation) throws IOException {
        JsonNode scores = getStorage().getCorpusDocumentOOPJson(corpus, document);
        ObjectNode retval = getMapper().createObjectNode();
        ArrayNode stats = retval.putArray(annotation);
        Iterator<JsonNode> sentenceIterator = scores.get("sentences").elements();
        while (sentenceIterator.hasNext()) {
    		Iterator<JsonNode> tokensIterator = sentenceIterator.next().get("tokens").elements();
    		while (tokensIterator.hasNext()) {
    			JsonNode token = tokensIterator.next();

	    		if (token.hasNonNull(annotation)) {
	    			if (token.get(annotation).isArray()) {
	   					stats.add(token.get(annotation).size());
	    			}
	    			else if (token.get(annotation).isObject()) {
						BigDecimal sz = new BigDecimal(0);
						Iterator<String> valueNamesIter = token.get(annotation).fieldNames();
						while (valueNamesIter.hasNext()) {
							String valueName = valueNamesIter.next();
							sz = sz.add(
									new BigDecimal(
											token.get(annotation).get(valueName).asText("0.0")
									)
							);
						}
						stats.add(sz);
	
	    			}
	    			else {
	    				stats.add(new BigDecimal(token.get(annotation).asText("0.0")));
	    			}
	    		}
	    		else {
	    			stats.add(new BigDecimal(0.0));
	    		}
    		}
        }
        return retval;
    }
    
    protected JsonNode getTokensAnnotationSubannotationScores(String corpus, String document, String annotation, String subannotation) throws IOException {
        JsonNode scores = getStorage().getCorpusDocumentOOPJson(corpus, document);
        ObjectNode retval = getMapper().createObjectNode();
        ArrayNode stats = retval.putArray(annotation);
        Iterator<JsonNode> sentenceIterator = scores.get("sentences").elements();
        while (sentenceIterator.hasNext()) {
    		Iterator<JsonNode> tokensIterator = sentenceIterator.next().get("tokens").elements();
    		while (tokensIterator.hasNext()) {
    			JsonNode token = tokensIterator.next();

	    		if (token.hasNonNull(annotation)) {
	    			if (token.get(annotation).isArray()) {
	    				BigDecimal sz = new BigDecimal(0);
	    				Iterator<JsonNode> valuesIter = token.get(annotation).elements();
	    				while (valuesIter.hasNext()) {
	    					JsonNode val = valuesIter.next();
	    					if (val.get("name").asText().equals(subannotation)) {
	    						sz = sz.add(new BigDecimal(val.get("value").asText("0.0")));
	    					}
	    				}
	    				stats.add(sz);
	    			}
	    			else if (token.get(annotation).isObject()) {
						BigDecimal sz = new BigDecimal(0);
						Iterator<String> valueNamesIter = token.get(annotation).fieldNames();
						while (valueNamesIter.hasNext()) {
							String valueName = valueNamesIter.next();
							if (valueName.equals(subannotation)) {
								sz = sz.add(
										new BigDecimal(
												token.get(annotation).get(valueName).asText("0.0")
										)
								);
							}
						}
						stats.add(sz);
	
	    			}
	    			else {
	    				stats.add(new BigDecimal(token.get(annotation).asText("0.0")));
	    			}
	    		}
	    		else {
	    			stats.add(new BigDecimal(0.0));
	    		}
    		}
        }
        return retval;
    }
    
    protected JsonNode getSyllablesAnnotationScores(String corpus, String document, String annotation) throws IOException {
        JsonNode scores = getStorage().getCorpusDocumentOOPJson(corpus, document);
        ObjectNode retval = getMapper().createObjectNode();
        ArrayNode stats = retval.putArray(annotation);
        Iterator<JsonNode> sentenceIterator = scores.get("sentences").elements();
        while (sentenceIterator.hasNext()) {
    		Iterator<JsonNode> tokensIterator = sentenceIterator.next().get("tokens").elements();
    		while (tokensIterator.hasNext()) {
    			JsonNode token = tokensIterator.next();
	    		if (token.hasNonNull(annotation)) {
	    			if (token.get(annotation).isArray()) {
	    				for (int i=0;i<token.get("OOPSyllablesAnnotation").asInt(1);i++) {
	    					stats.add(token.get(annotation).size());
	    				}
	    			}
	    			else if (token.get(annotation).isObject()) {
	    				for (int i=0;i<token.get("OOPSyllablesAnnotation").asInt(1);i++) {
							BigDecimal sz = new BigDecimal(0);
							Iterator<String> valueNamesIter = token.get(annotation).fieldNames();
							while (valueNamesIter.hasNext()) {
								String valueName = valueNamesIter.next();
								sz = sz.add(
										new BigDecimal(
												token.get(annotation).get(valueName).asText("0.0")
										)
								);
							}
							stats.add(sz);
	    				}
	    			}
	    			else {
	    				for (int i=0;i<token.get("OOPSyllablesAnnotation").asInt(1);i++) {
	    					stats.add(new BigDecimal(token.get(annotation).asText("0.0")));
	    				}
	    			}
	    		}
	    		else {
    				for (int i=0;i<token.get("OOPSyllablesAnnotation").asInt(1);i++) {
    					stats.add(new BigDecimal(0.0));
    				}
	    		}
    		}
        }
        return retval;
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//all annotations at document level
		if (request.getParameter("Scope") != null 
				&& request.getParameter("Scope").equalsIgnoreCase("Document")
				) {
					response.setContentType("application/json; charset=utf-8");
					getMapper().writeValue(
							response.getWriter(),
							getDocumentScores(
									request.getParameter("Corpus"), 
									request.getParameter("Document")
							)
					);
					response.flushBuffer();

		}
		//all annotations at sentence[id] level
		else if (request.getParameter("Scope") != null 
				&& request.getParameter("Scope").equalsIgnoreCase("Sentence")
				&& request.getParameter("Id") != null
				) {
					response.setContentType("application/json; charset=utf-8");
					getMapper().writeValue(
							response.getWriter(),
							getSentenceScores(
									request.getParameter("Corpus"), 
									request.getParameter("Document"),
									Integer.parseInt(request.getParameter("Id"))
							)
					);
					response.flushBuffer();
		}
		//all annotations at token[id] level
		else if (request.getParameter("Scope") != null 
				&& request.getParameter("Scope").equalsIgnoreCase("Token")
				&& request.getParameter("Id") != null
				) {
					response.setContentType("application/json; charset=utf-8");
					getMapper().writeValue(
							response.getWriter(),
							getTokenScores(
									request.getParameter("Corpus"), 
									request.getParameter("Document"),
									Integer.parseInt(request.getParameter("Id"))
							)
					);
					response.flushBuffer();
		}
		//specific annotation at document level
		else if (request.getParameter("Annotation") != null 
				&& request.getParameter("Scope") != null 
				&& request.getParameter("Scope").equalsIgnoreCase("DocumentAnnotation")
				&& request.getParameter("Format") != null				
				&& request.getParameter("Format").equalsIgnoreCase("D3Cloud")				
				) {
					response.setContentType("application/json; charset=utf-8");
					getMapper().writeValue(
							response.getWriter(),
							getDocumentAnnotationScoresD3Cloud(
									request.getParameter("Corpus"), 
									request.getParameter("Document"),
									request.getParameter("Annotation")
							)
					);
					response.flushBuffer();
		}
		
		else if (request.getParameter("Annotation") != null 
				&& request.getParameter("Scope") != null 
				&& request.getParameter("Scope").equalsIgnoreCase("DocumentAnnotation")
				) {
					response.setContentType("application/json; charset=utf-8");
					getMapper().writeValue(
							response.getWriter(),
							getDocumentAnnotationScores(
									request.getParameter("Corpus"), 
									request.getParameter("Document"),
									request.getParameter("Annotation")
							)
					);
					response.flushBuffer();
		}

		//specific subannotation at sentences level
		else if (request.getParameter("Annotation") != null
				&& request.getParameter("Subannotation") != null
				&& request.getParameter("Scope") != null 
				&& request.getParameter("Scope").equalsIgnoreCase("SentencesAnnotation")
				&& request.getParameter("Format") != null				
				&& request.getParameter("Format").equalsIgnoreCase("D3")
				&& request.getParameter("Rolling") != null	
				) {
					response.setContentType("application/json; charset=utf-8");
					getMapper().writeValue(
							response.getWriter(),
							getSentencesAnnotationSubannotationScoresRollingD3(
									request.getParameter("Corpus"), 
									request.getParameter("Document"),
									request.getParameter("Annotation"),
									request.getParameter("Subannotation"),
									Integer.parseInt(request.getParameter("Rolling"))
							)
					);
					response.flushBuffer();
		}		
		else if (request.getParameter("Annotation") != null
				&& request.getParameter("Subannotation") != null
				&& request.getParameter("Scope") != null 
				&& request.getParameter("Scope").equalsIgnoreCase("SentencesAnnotation")
				&& request.getParameter("Format") != null				
				&& request.getParameter("Format").equalsIgnoreCase("D3")				
				) {
					response.setContentType("application/json; charset=utf-8");
					getMapper().writeValue(
							response.getWriter(),
							getSentencesAnnotationSubannotationScoresD3(
									request.getParameter("Corpus"), 
									request.getParameter("Document"),
									request.getParameter("Annotation"),
									request.getParameter("Subannotation")
							)
					);
					response.flushBuffer();
		}
		else if (request.getParameter("Annotation") != null
				&& request.getParameter("Subannotation") != null
				&& request.getParameter("Scope") != null 
				&& request.getParameter("Scope").equalsIgnoreCase("SentencesAnnotation")
				) {
					response.setContentType("application/json; charset=utf-8");
					getMapper().writeValue(
							response.getWriter(),
							getSentencesAnnotationSubannotationScores(
									request.getParameter("Corpus"), 
									request.getParameter("Document"),
									request.getParameter("Annotation"),
									request.getParameter("Subannotation")
							)
					);
					response.flushBuffer();
		}		
		//specific annotation at sentences level
		else if (request.getParameter("Annotation") != null 
				&& request.getParameter("Scope") != null
				&& request.getParameter("Scope").equalsIgnoreCase("SentencesAnnotation")
				&& request.getParameter("Format") != null				
				&& request.getParameter("Format").equalsIgnoreCase("D3")
				&& request.getParameter("Rolling") != null				
				) {
		    		response.setContentType("application/json; charset=utf-8");
					getMapper().writeValue(
							response.getWriter(),
							getSentencesAnnotationScoresRollingD3(
									request.getParameter("Corpus"), 
									request.getParameter("Document"),
									request.getParameter("Annotation"),
									Integer.parseInt(request.getParameter("Rolling"))
							)
		    		);
		    		response.flushBuffer();
		}
		else if (request.getParameter("Annotation") != null 
				&& request.getParameter("Scope") != null
				&& request.getParameter("Scope").equalsIgnoreCase("SentencesAnnotation")
				&& request.getParameter("Format") != null				
				&& request.getParameter("Format").equalsIgnoreCase("D3")
				) {
		    		response.setContentType("application/json; charset=utf-8");
					getMapper().writeValue(
							response.getWriter(),
							getSentencesAnnotationScoresD3(
									request.getParameter("Corpus"), 
									request.getParameter("Document"),
									request.getParameter("Annotation")
							)
		    		);
		    		response.flushBuffer();
		}
		else if (request.getParameter("Annotation") != null 
				&& request.getParameter("Scope") != null 
				&& request.getParameter("Scope").equalsIgnoreCase("SentencesAnnotation")
				) {
		    		response.setContentType("application/json; charset=utf-8");
					getMapper().writeValue(
							response.getWriter(),
							getSentencesAnnotationScores(
									request.getParameter("Corpus"), 
									request.getParameter("Document"),
									request.getParameter("Annotation")
							)
		    		);
		    		response.flushBuffer();
		}
		//specific subannotation at tokens level
		else if (request.getParameter("Annotation") != null
				&& request.getParameter("Subannotation") != null
				&& request.getParameter("Scope") != null 
				&& request.getParameter("Scope").equalsIgnoreCase("TokensAnnotation")
				) {
					response.setContentType("application/json; charset=utf-8");
					getMapper().writeValue(
							response.getWriter(),
							getTokensAnnotationSubannotationScores(
									request.getParameter("Corpus"), 
									request.getParameter("Document"),
									request.getParameter("Annotation"),
									request.getParameter("Subannotation")
							)
					);
					response.flushBuffer();
		}
		//specific annotation at tokens level
		else if (request.getParameter("Annotation") != null 
				&& request.getParameter("Scope") != null 
				&& request.getParameter("Scope").equalsIgnoreCase("TokensAnnotation")
				) {
					response.setContentType("application/json; charset=utf-8");
					getMapper().writeValue(
							response.getWriter(),
							getTokensAnnotationScores(
									request.getParameter("Corpus"), 
									request.getParameter("Document"),
									request.getParameter("Annotation")
							)
					);
					response.flushBuffer();
		}
		//specific annotation at syllables level
		else if (request.getParameter("Annotation") != null 
				&& request.getParameter("Scope") != null 
				&& request.getParameter("Scope").equalsIgnoreCase("SyllablesAnnotation")
				) {
					response.setContentType("application/json; charset=utf-8");
					getMapper().writeValue(
							response.getWriter(),
							getSyllablesAnnotationScores(
									request.getParameter("Corpus"), 
									request.getParameter("Document"),
									request.getParameter("Annotation")
							)
					);
					response.flushBuffer();
		}
		else {
			response.sendError(404, "Scope: " + request.getParameter("Scope") + " not found");
		}
	}

}
