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
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;


@WebServlet("/OOPStoryArcViewer")
public class OOPStoryArcViewer extends AbstractOOPServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OOPStoryArcViewer() {
        super();
    }
    
    public void setReadingStats(HttpServletRequest request, String corpus, String document) throws IOException {
    	JsonNode oop = getCorpusDocumentOOPJson(corpus, document);
		request.setAttribute(
				"ReadingTime",
				String.format(
					"%.0f",
					oop.get("OOPWordCountAnnotation").asDouble()/250
				)
		);
		request.setAttribute(
				"ListeningTime",
				String.format(
					"%.0f",
					oop.get("OOPSyllableCountAnnotation").asDouble()/160
				)
		);
		request.setAttribute(
				"ReadingLevel",
				String.format(
					"%.0f",
					oop.get("OOPFleschKincaidAnnotation").asDouble()*100
				)
		);
		request.setAttribute(
				"EmotionalLevel",
				String.format(
					"%.0f",
					oop.get("VaderSentimentAnnotation").asDouble()*100
				)
		);
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String corpus = request.getParameter("Corpus");
		String document = request.getParameter("Document");
        setMetadataAttributes(request, corpus, document);
        setReadingStats(request, corpus, document);
        request.getSession().getServletContext().getRequestDispatcher("/jsp/OOPStoryArcViewer.jsp").forward(request, response);
	}
}
