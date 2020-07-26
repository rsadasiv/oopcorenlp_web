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

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;


@WebServlet("/OOPActorsViewer")
public class OOPActorsViewer extends AbstractOOPServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OOPActorsViewer() {
        super();
    }
    
    private void setSelectedActor(HttpServletRequest request, String corpus, String document) throws IOException {
    	String actor = request.getParameter("Actor");
    	if (actor == null) {
    		request.setAttribute("SelectedActor", getMapper().createObjectNode());
    		return;
    	}
    	
		JsonNode stats = (JsonNode) request.getAttribute("Stats");
		if (stats == null) {
			setStatsAttribute(request, corpus, document);
			stats = (JsonNode) request.getAttribute("Stats");
		}
		
		ArrayNode actors = (ArrayNode) stats.get("OOPActorsAnnotation");
		for (JsonNode actorNode : actors) {
			if (actorNode.get("canonicalName").asText().equals(actor)) {
				request.setAttribute("SelectedActor", actorNode);
				return;
			}
		}
    }
    
    private void setActorMyersBriggs(HttpServletRequest request, String corpus, String document) throws IOException {
    	setActorMBI(request, corpus, document, "introvert");
    	setActorMBI(request, corpus, document, "intuitive");
    	setActorMBI(request, corpus, document, "thinking");
    	setActorMBI(request, corpus, document, "judging");
    	setActorMBI(request, corpus, document, "extrovert");
    	setActorMBI(request, corpus, document, "sensing");
    	setActorMBI(request, corpus, document, "feeling");
    	setActorMBI(request, corpus, document, "perceiving");
    	
    }
    
    private void setActorMBI(HttpServletRequest request, String corpus, String document, String mbi) throws IOException {
		JsonNode stats = (JsonNode) request.getAttribute("Stats");
		if (stats == null) {
			setStatsAttribute(request, corpus, document);
			stats = (JsonNode) request.getAttribute("Stats");
		}
		
		JsonNode selectedActor = (JsonNode) request.getAttribute("SelectedActor");
		if (selectedActor == null) {
			setSelectedActor(request, corpus, document);
			selectedActor = (JsonNode) request.getAttribute("SelectedActor");
		}
		
    	BigDecimal documentMBI = new BigDecimal(((JsonNode) request.getAttribute("Stats")).get("OOPMyersBriggsAnnotation").get(mbi).asText("0"));
		BigDecimal actorMBI = new BigDecimal(selectedActor.get("oopmyersBriggs")==null?"0":selectedActor.get("oopmyersBriggs").get(mbi).asText("0"));
		String mbiScore = "0";
		if (!documentMBI.equals(new BigDecimal(0))) {
			mbiScore = String.format("%.0f", actorMBI.divide(documentMBI, 10, RoundingMode.HALF_DOWN).multiply(new BigDecimal(100)).floatValue());
		}
		request.setAttribute(mbi, mbiScore);
    }
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String corpus = request.getParameter("Corpus");
		String document = request.getParameter("Document");
        setMetadataAttributes(request, corpus, document);
        setStatsAttribute(request, corpus, document);
        setSelectedActor(request, corpus, document);
        setActorMyersBriggs(request, corpus, document);
        request.getSession().getServletContext().getRequestDispatcher("/jsp/OOPActorsViewer.jsp").forward(request, response);
	}
}
