package io.outofprintmagazine.web.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Servlet implementation class GetDocument
 */
@WebServlet("/GetDocument")
public class GetDocument extends AbstractOOPServlet  {
	private static final long serialVersionUID = 1L;
       
    public GetDocument() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String corpus = request.getParameter("Corpus");
		String document = request.getParameter("Document");
		response.setContentType("application/json");
		getMapper().writeValue(
				response.getWriter(),
				getStorage().getCorpusDocumentOOPJson(
						corpus, 
						document
				)
		);
		response.flushBuffer();
	}

}
