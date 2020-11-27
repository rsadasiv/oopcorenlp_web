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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.outofprintmagazine.web.util.JsonSort;


@WebServlet("/OOPLexiconViewer")
public class OOPLexiconViewer extends AbstractOOPServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OOPLexiconViewer() {
        super();
    }

    private void setSentenceAnnotatorsAttribute(HttpServletRequest request, String corpus, String document) throws IOException {
    	JsonNode stats = (JsonNode) request.getAttribute("Stats");
    	if (stats == null) {
    		setStatsAttribute(request, corpus, document);
    		stats = (JsonNode) request.getAttribute("Stats");
    	}
    	Iterator<JsonNode> sentenceIter = stats.get("sentences").elements();
    	List<String> sentenceAnnotators = new ArrayList<String>();
    	while (sentenceIter.hasNext()) {
    		Iterator<String> statsIter = sentenceIter.next().fieldNames();
	    	while (statsIter.hasNext()) {
	    		String annotationName = statsIter.next();
	    		if (!annotationName.startsWith("text") && !annotationName.startsWith("tokens")) {
	    			if (!sentenceAnnotators.contains(annotationName)) {
	    				sentenceAnnotators.add(annotationName);
	    			}
	    		}
	    	}
    	}
    	Collections.sort(sentenceAnnotators);
    	request.setAttribute("Annotators", sentenceAnnotators);
    }
    
    private void setSentenceCountAttribute(HttpServletRequest request, String corpus, String document) throws IOException {
    	JsonNode stats = (JsonNode) request.getAttribute("Stats");
    	if (stats == null) {
    		setStatsAttribute(request, corpus, document);
    		stats = (JsonNode) request.getAttribute("Stats");
    	}
    	request.setAttribute("SentenceCount", stats.get("OOPSentenceCountAnnotation").asInt());
    }
    
    
    //Distribution of OOPWordsAnnotation
    //http://localhost:8080/oopcorenlp_web/OOPCloudViewer?Corpus=Chekhov&Document=2d9b6161c6c8e1001ea23ec45b33b347&Annotation=OOPWordsAnnotation
    //http://localhost:8080/oopcorenlp_web/rest/api/DocumentAnnotation?Corpus=Chekhov&Document=2d9b6161c6c8e1001ea23ec45b33b347&Annotation=OOPWordsAnnotation
    //DocumentAggregates aggregatedScore
    //http://localhost:8080/oopcorenlp_web/rest/api/CorpusDocumentAnnotationSubannotationsAggregates?Corpus=Chekhov&Document=2d9b6161c6c8e1001ea23ec45b33b347&Annotation=OOPWordsAnnotation&AggregateName=raw
    //CorpusAggregates aggregatedScore
    //http://localhost:8080/oopcorenlp_web/rest/api/CorpusAnnotationSubannotationsAggregates?Corpus=Chekhov&Annotation=OOPWordsAnnotation&AggregateName=raw
    //OOPCorpusDocumentAnnotationZScoresViewer
    //http://localhost:8080/oopcorenlp_web/OOPCorpusDocumentAnnotationZScoresViewer?Corpus=Chekhov&Document=2d9b6161c6c8e1001ea23ec45b33b347&TargetCorpus=Chekhov&Annotation=OOPWordsAnnotation&AggregateName=raw    
    //http://localhost:8080/oopcorenlp_web/rest/api/CorpusDocumentAnnotationSubannotationsZScores?Corpus=Chekhov&Document=2d9b6161c6c8e1001ea23ec45b33b347&TargetCorpus=Chekhov&Annotation=OOPWordsAnnotation&AggregateName=raw
    
    //getCorpusDocumentLexiconLemma
    //http://localhost:8080/oopcorenlp_web/rest/api/LexiconLemma?Corpus=Chekhov&Document=2d9b6161c6c8e1001ea23ec45b33b347&Lemma=garden
    //display sentence texts
    //http://localhost:8080/oopcorenlp_web/OOPStreamSubannotationViewer?Corpus=Chekhov&Document=2d9b6161c6c8e1001ea23ec45b33b347&Annotation=OOPWordsAnnotation&Subannotation=garden
    //http://localhost:8080/rest/api/SentencesAnnotationSubannotationScalar?Corpus=Chekhov&Document=2d9b6161c6c8e1001ea23ec45b33b347&Annotation=OOPWordsAnnotation&Subannotation=garden&Format=D3
    //getCorpusDocumentTopicModelLemma
    //http://localhost:8080/oopcorenlp_web/rest/browse/Corpora/Chekhov/2d9b6161c6c8e1001ea23ec45b33b347/WORD2VEC/Lemma/garden
    //getCorpusDocumentTopicModelLemmaPOS
    //http://localhost:8080/oopcorenlp_web/rest/browse/Corpora/Chekhov/2d9b6161c6c8e1001ea23ec45b33b347/WORD2VEC/LemmaPOS/garden

    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String corpus = request.getParameter("Corpus");
		String document = request.getParameter("Document");
        setMetadataAttributes(request, corpus, document);
        setStatsAttribute(request, corpus, document);
        setSentenceAnnotatorsAttribute(request, corpus, document);
        setAnnotationDescriptionsAttribute(request, corpus, document);
        setSentenceCountAttribute(request, corpus, document);
        //if (((ObjectNode)request.getAttribute("Stats")).get("OOPWordsAnnotation").has(request.getParameter("Subannotation"))) {
        	//request.setAttribute("SubannotationIsWord", new Boolean(true));
        	request.setAttribute("Lexicon", getStorage().getCorpusDocumentLexiconLemma(corpus, document, request.getParameter("Subannotation")));
        	request.setAttribute("TopicModelLemma", getStorage().getCorpusDocumentTopicModelLemma(corpus, document, request.getParameter("Subannotation")));
        	request.setAttribute("TopicModelLemmaPOS", getStorage().getCorpusDocumentTopicModelLemmaPOS(corpus, document, request.getParameter("Subannotation")));        	
        //}
        //else {
        //	request.setAttribute("SubannotationIsWord", new Boolean(false));
        //}
        request.getSession().getServletContext().getRequestDispatcher("/jsp/OOPLexiconViewer.jsp").forward(request, response);
	}
}
