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
		JsonNode stats = (JsonNode) request.getAttribute("Stats");
		if (stats == null) {
			setStatsAttribute(request, corpus, document);
			stats = (JsonNode) request.getAttribute("Stats");
		}
		
		boolean firstActor = true;
		ArrayNode actors = (ArrayNode) stats.get("OOPActorsAnnotation");
		JsonSort.sortActors(actors);
		for (JsonNode actorNode : actors) {
	    	if (actor == null && firstActor) {
	    		request.setAttribute("SelectedActor", actorNode);
	    		firstActor = false;
	    	}
	    	else if (actorNode.get("canonicalName").asText().equals(actor)) {
				request.setAttribute("SelectedActor", actorNode);
				return;
			}
		}
    }
    
    private void setActorMyersBriggs(HttpServletRequest request, String corpus, String document) throws IOException {
    	BigDecimal introvert = getActorMBI(request, corpus, document, "introvert");
    	BigDecimal intuitive = getActorMBI(request, corpus, document, "intuitive");
    	BigDecimal thinking = getActorMBI(request, corpus, document, "thinking");
    	BigDecimal judging = getActorMBI(request, corpus, document, "judging");
    	BigDecimal extrovert = getActorMBI(request, corpus, document, "extrovert");
    	BigDecimal sensing = getActorMBI(request, corpus, document, "sensing");
    	BigDecimal feeling = getActorMBI(request, corpus, document, "feeling");
    	BigDecimal perceiving = getActorMBI(request, corpus, document, "perceiving");

    	StringBuffer mb = new StringBuffer();
    	mb.append(extractPairLabel(introvert, "I", extrovert, "E"));
    	mb.append(extractPairLabel(sensing, "S", intuitive, "N"));
    	mb.append(extractPairLabel(feeling, "F", thinking, "T"));	
    	mb.append(extractPairLabel(judging, "J", perceiving, "P"));
    	request.setAttribute("myersBriggs", mb.toString());
    	
    	request.setAttribute(
    			"introvert",
    			String.format("%.0f", extractPairNormalized(introvert, extrovert).multiply(new BigDecimal(100)).floatValue())
    	);
    	request.setAttribute(
    			"extrovert",
    			String.format("%.0f", extractPairNormalized(extrovert, introvert).multiply(new BigDecimal(100)).floatValue())
    	);
    	
    	request.setAttribute(
    			"intuitive",
    			String.format("%.0f", extractPairNormalized(intuitive, sensing).multiply(new BigDecimal(100)).floatValue())
    	);
    	request.setAttribute(
    			"sensing",
    			String.format("%.0f", extractPairNormalized(sensing, intuitive).multiply(new BigDecimal(100)).floatValue())
    	);
    	
    	request.setAttribute(
    			"thinking",
    			String.format("%.0f", extractPairNormalized(thinking, feeling).multiply(new BigDecimal(100)).floatValue())
    	);
    	request.setAttribute(
    			"feeling",
    			String.format("%.0f", extractPairNormalized(feeling, thinking).multiply(new BigDecimal(100)).floatValue())
    	);
    	
    	request.setAttribute(
    			"judging",
    			String.format("%.0f", extractPairNormalized(judging, perceiving).multiply(new BigDecimal(100)).floatValue())
    	);
    	request.setAttribute(
    			"perceiving",
    			String.format("%.0f", extractPairNormalized(perceiving, judging).multiply(new BigDecimal(100)).floatValue())
    	);

    	
    }
    
    private String extractPairLabel(BigDecimal left, String leftLabel, BigDecimal right, String rightLabel) {
    	if (left.compareTo(right) < 0) {
    		return rightLabel;
    	}
    	else {
    		return leftLabel;
    	}
    }
    
    private BigDecimal extractPairNormalized(BigDecimal left, BigDecimal right) {
    	BigDecimal retval = new BigDecimal(0);
    	if (right.equals(new BigDecimal(0))) {
    		retval = new BigDecimal(1);
    	}
    	else if (left.add(right).compareTo(new BigDecimal(0)) != 0) {
    		try {
    			retval = left.divide(left.add(right), 10, RoundingMode.HALF_DOWN);
    		}
    		catch (Exception e) {
    			System.err.println("left: " + left.toString());
    			System.err.println("right: " + right.toString());
    			e.printStackTrace();
    		}
    	}
    	return retval;
    }
    
    private BigDecimal getActorMBI(HttpServletRequest request, String corpus, String document, String mbi) throws IOException {
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
		
		JsonNode corpusMBIDoc = getStorage().getCorpusAggregatesMBJson(corpus);
    	//BigDecimal documentMBI = new BigDecimal(((JsonNode) request.getAttribute("Stats")).get("OOPMyersBriggsAnnotation").get(mbi).asText("0"));
		BigDecimal corpusMBI = new BigDecimal(corpusMBIDoc.get(mbi).asText("0"));
		BigDecimal actorMBI = new BigDecimal(selectedActor.get("oopmyersBriggs")==null?"0":selectedActor.get("oopmyersBriggs").get(mbi).asText("0"));
		BigDecimal mbiScore = new BigDecimal(0);
		//if (!documentMBI.equals(new BigDecimal(0))) {
		//	mbiScore = String.format("%.0f", actorMBI.divide(documentMBI, 10, RoundingMode.HALF_DOWN).multiply(new BigDecimal(100)).floatValue());
		//}
		if (!corpusMBI.equals(new BigDecimal(0))) {
			mbiScore = actorMBI.divide(corpusMBI, 10, RoundingMode.HALF_DOWN);
		}		
		return mbiScore;
    }
    
    private void setActorAttributes(HttpServletRequest request, String corpus, String document) throws IOException {
		JsonNode selectedActor = (JsonNode) request.getAttribute("SelectedActor");
		if (selectedActor == null) {
			setSelectedActor(request, corpus, document);
			selectedActor = (JsonNode) request.getAttribute("SelectedActor");
		}
		request.setAttribute(
				"OOPNounsAnnotation",
				getActorAttribute(selectedActor, "OOPNounsAnnotation")
		);
		request.setAttribute(
				"OOPVerbsAnnotation",
				getActorAttribute(selectedActor, "OOPVerbsAnnotation")
		);
		request.setAttribute(
				"OOPAdjectivesAnnotation",
				getActorAttribute(selectedActor, "OOPAdjectivesAnnotation")
		);
		request.setAttribute(
				"OOPAdverbsAnnotation",
				getActorAttribute(selectedActor, "OOPAdverbsAnnotation")
		);
    }
    
    private ArrayNode getActorAttribute(JsonNode actorNode, String attributeName) {
    	ArrayNode retval = getMapper().createArrayNode();
		if (actorNode.get("attributes").hasNonNull(attributeName)) {
			Iterator<String> attributeIter = ((ObjectNode)actorNode.get("attributes").get(attributeName)).fieldNames();
			while (attributeIter.hasNext()) {
				String subannotation = attributeIter.next();
	        	retval.add(createObjectTidy(0, subannotation, new BigDecimal(actorNode.get("attributes").get(attributeName).get(subannotation).asText())));
	        }
		}
        JsonSort.sort(retval);
        return retval;
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
        setActorAttributes(request, corpus, document);
        request.getSession().getServletContext().getRequestDispatcher("/jsp/OOPActorsViewer.jsp").forward(request, response);
	}
}
