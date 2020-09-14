package io.outofprintmagazine.web.servlets;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GetDocumentScores
 */
@WebServlet("/GetDocumentScores")

public class GetDocumentScores extends AbstractOOPServlet  {
	private static final long serialVersionUID = 1L;
       
    public GetDocumentScores() {
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
		String scores = request.getParameter("Scores");
		response.setHeader("Cache-Control", "public");
		
		if (scores.equals("OOP")) {
			response.setContentType("application/json; charset=utf-8");
			getMapper().writeValue(
					response.getWriter(),
					getStorage().getCorpusDocumentOOPJson(
							corpus, 
							document
					)
			);
		}
		
		else if (scores.equals("AGGREGATES")) {
			response.setContentType("application/json; charset=utf-8");
			getMapper().writeValue(
					response.getWriter(),
					getStorage().getCorpusDocumentAggregatesJson(
							corpus, 
							document
					)
			);
		}
		
		else if (scores.equals("TXT")) {
			response.setContentType("text/plain; charset=utf-8");
			getMapper().writeValue(
					response.getWriter(),
					getStorage().getCorpusDocumentTxtString(
							corpus, 
							document
					)
			);
		}
		
		else if (scores.equals("PIPELINE")) {
			response.setContentType("application/json; charset=utf-8");
			getMapper().writeValue(
					response.getWriter(),
					getStorage().getCorpusDocumentPipelineJson(
							corpus, 
							document
					)
			);
		}
		
		else if (scores.equals("STANFORD")) {
			response.setContentType("application/json; charset=utf-8");
			getMapper().writeValue(
					response.getWriter(),
					getStorage().getCorpusDocumentStanfordJson(
							corpus, 
							document
					)
			);
		}
		
		response.flushBuffer();
	}

}
