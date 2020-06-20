package io.outofprintmagazine.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pCorpus = request.getParameter("Corpus");
		File[] documents = new File(request.getSession().getServletContext().getRealPath("/Corpora/"+pCorpus+"/")).listFiles(File::isFile);
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode json = mapper.createObjectNode();
		ArrayNode corporaNode = json.putArray("Documents");
		for (int i=0;i<documents.length;i++) {
			if (documents[i].getName().substring(0, documents[i].getName().lastIndexOf(".")).startsWith("OOP_")) {
				ObjectNode documentNode = mapper.createObjectNode();
				documentNode.put("DocID", documents[i].getName().substring(4, documents[i].getName().lastIndexOf(".")));
	            BufferedReader br = new BufferedReader(new FileReader(documents[i]));
	            ObjectMapper objectMapper = new ObjectMapper();
	            JsonNode stats = objectMapper.readTree(br);
            	documentNode.put("Author", stats.findValue("AuthorAnnotation").asText());
            	documentNode.put("Date", stats.findValue("DocDateAnnotation").asText());
            	documentNode.put("Title", stats.findValue("DocTitleAnnotation").asText());
            	corporaNode.add(documentNode);

				br.close();
			}
		}
		request.setAttribute("corpora", json);
		request.getSession().getServletContext().getRequestDispatcher("/CorpusDocumentsViewer.jsp").forward(request, response);
	}
}
