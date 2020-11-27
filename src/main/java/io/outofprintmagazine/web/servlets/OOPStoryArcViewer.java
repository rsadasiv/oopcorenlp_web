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


@WebServlet("/OOPStoryArcViewer")
public class OOPStoryArcViewer extends AbstractOOPServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OOPStoryArcViewer() {
        super();
    }
    
    private void setDocumentMyersBriggs(HttpServletRequest request, String corpus, String document) throws IOException {
    	BigDecimal introvert = getDocumentMBI(request, corpus, document, "introvert");
    	BigDecimal intuitive = getDocumentMBI(request, corpus, document, "intuitive");
    	BigDecimal thinking = getDocumentMBI(request, corpus, document, "thinking");
    	BigDecimal judging = getDocumentMBI(request, corpus, document, "judging");
    	BigDecimal extrovert = getDocumentMBI(request, corpus, document, "extrovert");
    	BigDecimal sensing = getDocumentMBI(request, corpus, document, "sensing");
    	BigDecimal feeling = getDocumentMBI(request, corpus, document, "feeling");
    	BigDecimal perceiving = getDocumentMBI(request, corpus, document, "perceiving");

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
    
    private BigDecimal getDocumentMBI(HttpServletRequest request, String corpus, String document, String mbi) throws IOException {
		JsonNode stats = (JsonNode) request.getAttribute("Stats");
		if (stats == null) {
			setStatsAttribute(request, corpus, document);
			stats = (JsonNode) request.getAttribute("Stats");
		}
				
		JsonNode corpusMBIDoc = getStorage().getCorpusAggregatesMBJson(corpus);
		BigDecimal corpusMBI = new BigDecimal(corpusMBIDoc.get(mbi).asText("0"));
		BigDecimal documentMBI = new BigDecimal(stats.get("OOPMyersBriggsAnnotation")==null?"0":stats.get("OOPMyersBriggsAnnotation").get(mbi).asText("0"));
		BigDecimal mbiScore = new BigDecimal(0);

		if (!corpusMBI.equals(new BigDecimal(0))) {
			mbiScore = documentMBI.divide(corpusMBI, 10, RoundingMode.HALF_DOWN);
		}
		return mbiScore;
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String corpus = request.getParameter("Corpus");
		String document = request.getParameter("Document");
        setMetadataAttributes(request, corpus, document);
        setAnnotationDescriptionsAttribute(request, corpus, document);
        setStatsAttribute(request, corpus, document);
        setDocumentMyersBriggs(request, corpus, document);
        request.getSession().getServletContext().getRequestDispatcher("/jsp/OOPStoryArcViewer.jsp").forward(request, response);
	}
}
