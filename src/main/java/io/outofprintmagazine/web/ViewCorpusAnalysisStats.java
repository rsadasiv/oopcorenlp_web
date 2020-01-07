package io.outofprintmagazine.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet implementation class GetDocumentStatsViewer
 */
@WebServlet("/ViewCorpusAnalysisStats")
public class ViewCorpusAnalysisStats extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewCorpusAnalysisStats() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pCorpus = request.getParameter("Corpus");
		//need to deal with Submissions and Published differently
		//WHY?????

		if (request.getParameter("Analysis").equals("Cloud")) {
            BufferedReader br = new BufferedReader(
                	new InputStreamReader(
                		request.getSession().getServletContext().getResourceAsStream(
                			"/Corpora/"+pCorpus+"/Annotations/OOP/" + request.getParameter("Document") + ".json"
                		)
                	)	
                );
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode stats = objectMapper.readTree(br);
			request.setAttribute("Author", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$AuthorAnnotation").asText());
			request.setAttribute("Date", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$DocDateAnnotation").asText());
			request.setAttribute("Title", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$DocTitleAnnotation").asText());
			request.setAttribute("Stats", stats);
			request.getSession().getServletContext().getRequestDispatcher("/CorpusCloudViewer.jsp").forward(request, response);
		}
		else if (request.getParameter("Analysis").equals("Stream")) {
            BufferedReader br = new BufferedReader(
                	new InputStreamReader(
                		request.getSession().getServletContext().getResourceAsStream(
                			"/Corpora/"+pCorpus+"/Annotations/OOP/" + request.getParameter("Document") + ".json"
                		)
                	)	
                );
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode stats = objectMapper.readTree(br);
			request.setAttribute("Author", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$AuthorAnnotation").asText());
			request.setAttribute("Date", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$DocDateAnnotation").asText());
			request.setAttribute("Title", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$DocTitleAnnotation").asText());
			request.setAttribute("Stats", stats);
			request.getSession().getServletContext().getRequestDispatcher("/CorpusStreamViewer.jsp").forward(request, response);
		}

		else {
			response.sendError(404, "Analysis: " + request.getParameter("Analysis") + " not found");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
