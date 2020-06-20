package io.outofprintmagazine.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet implementation class ListAnalyses
 */
@WebServlet("/ListDocumentAnalyses")
public class ListDocumentAnalyses extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListDocumentAnalyses() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pCorpus = request.getParameter("Corpus");

        BufferedReader br = new BufferedReader(
        	new InputStreamReader(
        		request.getSession().getServletContext().getResourceAsStream(
        			"/Corpora/"+pCorpus+"/PIPELINE_" + request.getParameter("Document") + ".json"
        		)
        	)	
        );
        ObjectMapper mapper = new ObjectMapper();
        JsonNode document = mapper.readTree(br);
		response.setContentType("application/json");
		response.getWriter().write(mapper.writeValueAsString(document.get("annotations")));
		response.flushBuffer();
	}

}
