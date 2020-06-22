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
package io.outofprintmagazine.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.outofprintmagazine.web.util.JsonSort;


@WebServlet("/GetDocumentAnalysisScores")
public class GetDocumentAnalysisScores extends AbstractOOPServlet {
	private static final long serialVersionUID = 1L;

	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetDocumentAnalysisScores() {
        super();
    }
    
    protected JsonNode getDocumentScores(String corpus, String document) throws IOException {
        JsonNode stats = getCorpusDocumentOOPJson(corpus, document);
        ArrayNode retval = getMapper().createArrayNode();
        Iterator<String> statsIterator = stats.fieldNames();
        while (statsIterator.hasNext()) {
        	String fieldName = statsIterator.next();
        	retval.add(getMapper().createObjectNode().set(fieldName, stats.get(fieldName)));
        }
        JsonSort.sort(retval);
        return retval;
    }
    
    protected JsonNode getDocumentAnnotationScores(String corpus, String document, String annotation) throws IOException {
        JsonNode scores = getCorpusDocumentOOPJson(corpus, document);
        ObjectNode retval = getMapper().createObjectNode();
        ArrayNode stats = retval.putArray(annotation);
		if (scores.hasNonNull(annotation)) {
			stats.add(scores.get(annotation));
		}
		else {
			stats.add(getMapper().createObjectNode());
		}
        return retval;
    }
    
    protected JsonNode getSentenceScores(String corpus, String document, int sentenceId) throws IOException {
        JsonNode stats = getCorpusDocumentOOPJson(corpus, document);
        ArrayNode retval = getMapper().createArrayNode();
        JsonNode sentenceStats = stats.get("sentences").get(sentenceId);

        Iterator<String> statsIterator = sentenceStats.fieldNames();
        while (statsIterator.hasNext()) {
        	String fieldName = statsIterator.next();
        	retval.add(getMapper().createObjectNode().set(fieldName, sentenceStats.get(fieldName)));
        }
        JsonSort.sort(retval);
        return retval;
    }
    
    protected JsonNode getSentencesAnnotationScores(String corpus, String document, String annotation) throws IOException {
        JsonNode scores = getCorpusDocumentOOPJson(corpus, document);
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
										sentence.get(annotation).get(valueName).asDouble(0.0)
								)
						);
					}
					stats.add(sz);

    			}
    			else {
    				stats.add(sentence.get(annotation).asDouble(0.0));
    			}
    		}
    		else {
    			stats.add(0);
    		}
        }
        return retval;
    }
    
    protected JsonNode getTokenScores(String corpus, String document, int tokenId) throws IOException {
        JsonNode stats = getCorpusDocumentOOPJson(corpus, document);
        Iterator<JsonNode> sentenceIterator = stats.get("sentences").elements();
        int tokenIdx = 1;
        while (sentenceIterator.hasNext()) {
        	Iterator<JsonNode> tokensIterator = sentenceIterator.next().get("tokens").elements();
        	while (tokensIterator.hasNext()) {
        		JsonNode token = tokensIterator.next();
        		if (tokenIdx == tokenId) {
        			JsonNode retval = token.deepCopy();
                    JsonSort.sort(retval);
            		return retval;
        		}
        		tokenIdx++;
        	}
        }
        return null;
    }
    
    protected JsonNode getTokensAnnotationScores(String corpus, String document, String annotation) throws IOException {
        JsonNode scores = getCorpusDocumentOOPJson(corpus, document);
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
											token.get(annotation).get(valueName).asDouble(0.0)
									)
							);
						}
						stats.add(sz);
	
	    			}
	    			else {
	    				stats.add(token.get(annotation).asDouble(0.0));
	    			}
	    		}
	    		else {
	    			stats.add(0);
	    		}
    		}
        }
        return retval;
    }
    
    protected JsonNode getTokensAnnotationSubannotationScores(String corpus, String document, String annotation, String subannotation) throws IOException {
        JsonNode scores = getCorpusDocumentOOPJson(corpus, document);
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
							if (valueName.equals(subannotation)) {
								sz = sz.add(
										new BigDecimal(
												token.get(annotation).get(valueName).asDouble(0.0)
										)
								);
							}
						}
						stats.add(sz);
	
	    			}
	    			else {
	    				stats.add(token.get(annotation).asDouble(0.0));
	    			}
	    		}
	    		else {
	    			stats.add(0);
	    		}
    		}
        }
        return retval;
    }
    
    protected JsonNode getSyllablesAnnotationScores(String corpus, String document, String annotation) throws IOException {
        JsonNode scores = getCorpusDocumentOOPJson(corpus, document);
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
												token.get(annotation).get(valueName).asDouble(0.0)
										)
								);
							}
							stats.add(sz);
	    				}
	    			}
	    			else {
	    				for (int i=0;i<token.get("OOPSyllablesAnnotation").asInt(1);i++) {
	    					stats.add(token.get(annotation).asDouble(0.0));
	    				}
	    			}
	    		}
	    		else {
    				for (int i=0;i<token.get("OOPSyllablesAnnotation").asInt(1);i++) {
    					stats.add(0);
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
		if (request.getParameter("Scope") != null 
				&& request.getParameter("Scope").equalsIgnoreCase("Document")
				) {
					response.setContentType("application/json");
					getMapper().writeValue(
							response.getWriter(),
							getDocumentScores(
									request.getParameter("Corpus"), 
									request.getParameter("Document")
							)
					);
					response.flushBuffer();

		}
		else if (request.getParameter("Scope") != null 
				&& request.getParameter("Scope").equalsIgnoreCase("Sentence")
				&& request.getParameter("Id") != null
				) {
					response.setContentType("application/json");
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
		else if (request.getParameter("Scope") != null 
				&& request.getParameter("Scope").equalsIgnoreCase("Token")
				&& request.getParameter("Id") != null
				) {
					response.setContentType("application/json");
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
		else if (request.getParameter("Annotation") != null 
				&& request.getParameter("Scope") != null 
				&& request.getParameter("Scope").equalsIgnoreCase("DocumentAnnotation")
				) {
					response.setContentType("application/json");
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
		else if (request.getParameter("Annotation") != null 
				&& request.getParameter("Scope") != null 
				&& request.getParameter("Scope").equalsIgnoreCase("SentencesAnnotation")
				) {
		    		response.setContentType("application/json");
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
		else if (request.getParameter("Annotation") != null
				&& request.getParameter("Subannotation") != null
				&& request.getParameter("Scope") != null 
				&& request.getParameter("Scope").equalsIgnoreCase("TokensAnnotation")
				) {
					response.setContentType("application/json");
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
		else if (request.getParameter("Annotation") != null 
				&& request.getParameter("Scope") != null 
				&& request.getParameter("Scope").equalsIgnoreCase("TokensAnnotation")
				) {
					response.setContentType("application/json");
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
		else if (request.getParameter("Annotation") != null 
				&& request.getParameter("Scope") != null 
				&& request.getParameter("Scope").equalsIgnoreCase("SyllablesAnnotation")
				) {
					response.setContentType("application/json");
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
