package io.outofprintmagazine.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.outofprintmagazine.web.util.JsonSort;

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
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pCorpus = request.getParameter("Corpus");

        BufferedReader br = new BufferedReader(
        	new InputStreamReader(
        		request.getSession().getServletContext().getResourceAsStream(
        			"/Corpora/"+pCorpus+"/Annotations/PIPELINE/" + request.getParameter("Document") + ".json"
        		)
        	)	
        );
        ObjectMapper mapper = new ObjectMapper();
        JsonNode document = mapper.readTree(br);
		response.setContentType("application/json");
		response.getWriter().write(mapper.writeValueAsString(document.get("annotations")));
		response.flushBuffer();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
