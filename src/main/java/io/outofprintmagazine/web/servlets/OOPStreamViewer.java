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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;


@WebServlet("/OOPStreamViewer")
public class OOPStreamViewer extends AbstractOOPServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OOPStreamViewer() {
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
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String corpus = request.getParameter("Corpus");
		String document = request.getParameter("Document");
        setMetadataAttributes(request, corpus, document);
        setStatsAttribute(request, corpus, document);
        setSentenceAnnotatorsAttribute(request, corpus, document);
        request.getSession().getServletContext().getRequestDispatcher("/jsp/OOPStreamViewer.jsp").forward(request, response);
	}
}
