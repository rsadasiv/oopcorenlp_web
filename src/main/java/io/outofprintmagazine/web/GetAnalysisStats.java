package io.outofprintmagazine.web;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

/**
 * Servlet implementation class GetCorpusStats
 */
@WebServlet("/GetAnalysisStats")
public class GetAnalysisStats extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetAnalysisStats() {
        super();
        // TODO Auto-generated constructor stub
    }
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("Analysis").equals("Biber")) {
			getBiberStats(request, response);
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
	
	protected void getBiberStats(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CsvParserSettings settings = new CsvParserSettings();
		settings.setHeaderExtractionEnabled(true);
		CsvParser parser = new CsvParser(settings);
		List<Record> statistics = parser.parseAllRecords(new InputStreamReader(request.getSession().getServletContext().getResourceAsStream("/Corpora/Submissions/BiberTextTypes.csv")));
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode json = mapper.createObjectNode();
		ArrayNode stats = json.putArray("Stats");
		for (Record record : statistics) {
			ObjectNode score = stats.addObject();
			score.put("Score", record.getBigDecimal("Score").toPlainString());
			score.put("Dimension", "Dimension"+record.getString("Dimension"));
			score.put("TextType", record.getString("TextType"));
		}
		response.setContentType("application/json");
		response.getWriter().write(mapper.writeValueAsString(json));
		response.flushBuffer();		
	}

}
