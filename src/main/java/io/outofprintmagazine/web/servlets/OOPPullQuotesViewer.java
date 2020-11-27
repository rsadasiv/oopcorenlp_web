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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;


@WebServlet("/OOPPullQuotesViewer")
public class OOPPullQuotesViewer extends AbstractOOPServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OOPPullQuotesViewer() {
        super();
    }
    
    protected void setPullQuotes(HttpServletRequest request, String corpus, String document) throws IOException {
    	JsonNode oop = getStorage().getCorpusDocumentOOPJson(corpus, document);
     	Map<String, List<String>> pullQuotes = new HashMap<String, List<String>>();
     	//first sentence
     	String firstSentence = 
     		oop
     		.get("sentences")
     		.get(0)
     		.get("text")
     		.asText();
     	//last sentence (second to last if contains *)
     	String lastSentence = 
     		oop
     		.get("sentences")
     		.get(oop.get("sentences").size()-1)
     		.get("text")
     		.asText();
     	if (lastSentence.trim().equals("") || lastSentence.trim().contains("*")) {
     		lastSentence = 
     			oop
     			.get("sentences")
     			.get(oop.get("sentences").size()-2)
     			.get("text")
     			.asText();
     		if (lastSentence.trim().contains("*")) {
     			lastSentence = 
     				oop
     				.get("sentences")
     				.get(oop.get("sentences").size()-3)
     				.get("text")
     				.asText();
     		}
     	}
     	//sentences with lowest and highest sentiment
     	double firstLowestSentimentScore = 1.0;
     	double secondLowestSentimentScore = 1.0;
     	int firstLowestSentimentIdx = -1;
     	int secondLowestSentimentIdx = -1;
     	double firstHighestSentimentScore = 0.0;
     	double secondHighestSentimentScore = 0.0;
     	int firstHighestSentimentIdx = -1;
     	int secondHighestSentimentIdx = -1;
     	Iterator<JsonNode> sentenceIter = oop.get("sentences").elements();
     	for (int i=0;sentenceIter.hasNext();i++) {
     		JsonNode sentence = sentenceIter.next();
     		double sentimentScore = sentence.get("VaderSentimentAnnotation").asDouble();
     		if (sentimentScore < firstLowestSentimentScore) {
     			secondLowestSentimentScore = firstLowestSentimentScore;
     			secondLowestSentimentIdx = firstLowestSentimentIdx;
     			firstLowestSentimentScore = sentimentScore;
     			firstLowestSentimentIdx = i;
     		}
     		else if (sentimentScore < secondLowestSentimentScore) {
     			secondLowestSentimentScore = sentimentScore;
     			secondLowestSentimentIdx = i;
     		}
     		if (sentimentScore > firstHighestSentimentScore) {
     			secondHighestSentimentScore = firstHighestSentimentScore;
     			secondHighestSentimentIdx = firstHighestSentimentIdx;
     			firstHighestSentimentScore = sentimentScore;
     			firstHighestSentimentIdx = i;
     		}
     		else if (sentimentScore > secondHighestSentimentScore) {
     			secondHighestSentimentScore = sentimentScore;
     			secondHighestSentimentIdx = i;
     		}
     	}
    	
     	String firstLowestSentiment = oop.get("sentences").get(firstLowestSentimentIdx).get("text").asText().trim();
     	String secondLowestSentiment = oop.get("sentences").get(secondLowestSentimentIdx).get("text").asText().trim();
     	String firstHighestSentiment = oop.get("sentences").get(firstHighestSentimentIdx).get("text").asText().trim();
     	String secondHighestSentiment = oop.get("sentences").get(secondHighestSentimentIdx).get("text").asText().trim();
    	
  	
     	//wha
     	List<String> whaAnnotators = Arrays.asList(
     		"OOPHowAnnotation",
     		"OOPWhatAnnotation",
     		"OOPWhenAnnotation",
     		"OOPWhereAnnotation",
     		"OOPWhyAnnotation"
     	);
     	List<String> whaSentences = new ArrayList<String>();
     	for (String annotationName : whaAnnotators) {
     		for (JsonNode phraseAnnotation : ((ArrayNode)oop.get(annotationName))) {
     			whaSentences.add(StringUtils.capitalize(phraseAnnotation.get("name").asText().trim()+"?"));
	     	}
	    }
    		
     	//comparisons
     	List<String> comparisonSentences = new ArrayList<String>();
     	
     	List<String> asSentences = new ArrayList<String>();
 		for (JsonNode phraseAnnotation : ((ArrayNode)oop.get("OOPAsAnnotation"))) {
 			asSentences.add(StringUtils.capitalize(phraseAnnotation.get("name").asText().trim()));
     	}
     	asSentences.sort((s1, s2) -> s2.length() - s1.length());
     	comparisonSentences.addAll(asSentences);
     	
     	List<String> likeSentences = new ArrayList<String>();
 		for (JsonNode phraseAnnotation : ((ArrayNode)oop.get("OOPLikeAnnotation"))) {
 			likeSentences.add(StringUtils.capitalize(phraseAnnotation.get("name").asText().trim()));
     	}
     	likeSentences.sort((s1, s2) -> s2.length() - s1.length());
     	comparisonSentences.addAll(likeSentences);
     	
     	List<String> dateSentences = new ArrayList<String>();
 		for (JsonNode phraseAnnotation : ((ArrayNode)oop.get("OOPDatesAnnotation"))) {
 			dateSentences.add(StringUtils.capitalize(phraseAnnotation.get("name").asText().trim()));
     	}
     	dateSentences.sort((s1, s2) -> s2.length() - s1.length());
     	comparisonSentences.addAll(dateSentences);
     	
     	
     	//quotes
     	Pattern extraQuote = Pattern.compile("^\"\\s*\"", Pattern.MULTILINE);
     	List<String> quotes = new ArrayList<String>();
 		for (JsonNode quoteAnnotation : ((ArrayNode)oop.get("quotes"))) {
 			String q = quoteAnnotation.get("text").asText().trim();
 			q = extraQuote.matcher(q).replaceFirst("\"");
 			quotes.add(q);
     	}
    	
     	pullQuotes.put("FirstLast", Arrays.asList(firstSentence, lastSentence));
     	pullQuotes.put("Best", Arrays.asList(firstHighestSentiment, secondHighestSentiment));
     	pullQuotes.put("Worst", Arrays.asList(firstLowestSentiment, secondLowestSentiment));
     	pullQuotes.put("Lede", whaSentences);
     	pullQuotes.put("InMediasRes", comparisonSentences);
    	pullQuotes.put("Dialog", quotes);
     	request.setAttribute("PullQuotes", pullQuotes);	
    }
    
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String corpus = request.getParameter("Corpus");
		String document = request.getParameter("Document");
        setMetadataAttributes(request, corpus, document);
        setStatsAttribute(request, corpus, document);
        setPullQuotes(request, corpus, document);
        request.getSession().getServletContext().getRequestDispatcher("/jsp/OOPPullQuotesViewer.jsp").forward(request, response);
	}
}
