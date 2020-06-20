package io.outofprintmagazine.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/OOPCloudViewer")
public class OOPCloudViewer extends AbstractOOPServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OOPCloudViewer() {
        super();
    }
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String corpus = request.getParameter("Corpus");
		String document = request.getParameter("Document");
        setMetadataAttributes(request, corpus, document);
        request.getSession().getServletContext().getRequestDispatcher("/OOPCloudViewer.jsp").forward(request, response);
	}
}
