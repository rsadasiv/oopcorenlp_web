package io.outofprintmagazine.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/TextOutOfPrintViewer")
public class TextOutOfPrintViewer extends AbstractOOPServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TextOutOfPrintViewer() {
        super();
    }
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String corpus = request.getParameter("Corpus");
		String document = request.getParameter("Document");
        setMetadataAttributes(request, corpus, document);
		request.setAttribute(
				"Text", 
				plainTextToHtml(getCorpusDocumentTxtString(corpus, document))
		);

        request.getSession().getServletContext().getRequestDispatcher("/TextOutOfPrintViewer.jsp").forward(request, response);
	}
}
