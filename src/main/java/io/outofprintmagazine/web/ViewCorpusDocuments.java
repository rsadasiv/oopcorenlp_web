package io.outofprintmagazine.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Servlet implementation class ListCorpusDocuments
 */
@WebServlet("/ViewCorpusDocuments")
public class ViewCorpusDocuments extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewCorpusDocuments() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pCorpus = request.getParameter("Corpus");
		File[] documents = new File(request.getSession().getServletContext().getRealPath("/Corpora/"+pCorpus+"/Annotations/OOP/")).listFiles(File::isFile);
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode json = mapper.createObjectNode();
		ArrayNode corporaNode = json.putArray("Documents");
		for (int i=0;i<documents.length;i++) {
			ObjectNode documentNode = mapper.createObjectNode();
			documentNode.put("DocID", documents[i].getName().substring(0, documents[i].getName().lastIndexOf(".")));
            BufferedReader br = new BufferedReader(new FileReader(documents[i]));
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode stats = objectMapper.readTree(br);
            try {
            	documentNode.put("Author", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$AuthorAnnotation").asText());
            	documentNode.put("Date", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$DocDateAnnotation").asText());
            	documentNode.put("Title", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$DocTitleAnnotation").asText());
            	corporaNode.add(documentNode);
            }
            catch (Exception e) {
            	System.err.println(documents[i] + " NPE");
            }
			br.close();
		}
		request.setAttribute("corpora", json);
		request.getSession().getServletContext().getRequestDispatcher("/CorpusDocumentsViewer.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
