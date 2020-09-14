package io.outofprintmagazine.web.servlets;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Servlet implementation class GetDocumentScores
 */
@WebServlet("/GetDocumentScoreDescription")

public class GetDocumentScoreDescription extends AbstractOOPServlet  {
	private static final long serialVersionUID = 1L;
       
    public GetDocumentScoreDescription() {
        super();
    }
    
    protected long getLastModified(HttpServletRequest request) {
        // let the browser cache this forever
        GregorianCalendar cal = new GregorianCalendar();        // current time
        cal.set(Calendar.HOUR, 0);                              // set to midnight
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();                           // convert to ms since 1970
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String corpus = request.getParameter("Corpus");
		String document = request.getParameter("Document");
		String annotation = request.getParameter("Annotation");
		response.setHeader("Cache-Control", "public");
		response.setContentType("application/json; charset=utf-8");
		getMapper().writeValue(
				response.getWriter(),
				getAnnotationDescription(
						corpus, 
						document,
						annotation
				)
		);
		response.flushBuffer();
	}

}
