package io.outofprintmagazine.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

import io.outofprintmagazine.web.util.JsonSort;


/**
 * Servlet implementation class GetDocumentAnalysisStats
 */
@WebServlet("/GetDocumentAnalysisStats")
public class GetDocumentAnalysisStats extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetDocumentAnalysisStats() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pCorpus = request.getParameter("Corpus");
		if (request.getParameter("Analysis").equals("Biber")) {
			getBiberStats(request, response);
		}

		if (request.getParameter("Analysis").equals("StanfordCoreNLP")) {
			request.getSession().getServletContext().getRequestDispatcher("/Corpora/"+pCorpus+"/Annotations/STANFORD/" + request.getParameter("Document") + ".json").forward(request, response);
		}
		
		else if (request.getParameter("Analysis").equals("OOPCoreNLP") 
				&& request.getParameter("Annotation") == null 
				&& request.getParameter("Scope") != null 
				&& request.getParameter("Scope").equalsIgnoreCase("Document")
		) {
            BufferedReader br = new BufferedReader(
                	new InputStreamReader(
                		request.getSession().getServletContext().getResourceAsStream(
                			"/Corpora/"+pCorpus+"/Annotations/OOP/" + request.getParameter("Document") + ".json"
                		)
                	)	
                );
            ObjectMapper mapper = new ObjectMapper();
            JsonNode stats = mapper.readTree(br);
            ArrayNode retval = mapper.createArrayNode();
            Iterator<String> statsIterator = stats.fieldNames();
            while (statsIterator.hasNext()) {
            	String fieldName = statsIterator.next();
            	retval.add(mapper.createObjectNode().set(fieldName, stats.get(fieldName)));
            }
            JsonSort.sort(retval);
    		response.setContentType("application/json");
    		response.getWriter().write(mapper.writeValueAsString(retval));
    		response.flushBuffer();

		}
		else if (request.getParameter("Analysis").equals("OOPCoreNLP") 
				&& request.getParameter("Annotation") == null 
				&& request.getParameter("Scope") != null 
				&& request.getParameter("Scope").equalsIgnoreCase("Sentence")
				&& request.getParameter("Id") != null
		) {
            BufferedReader br = new BufferedReader(
                	new InputStreamReader(
                		request.getSession().getServletContext().getResourceAsStream(
                			"/Corpora/"+pCorpus+"/Annotations/OOP/" + request.getParameter("Document") + ".json"
                		)
                	)	
                );
            ObjectMapper mapper = new ObjectMapper();
            JsonNode stats = mapper.readTree(br);
            JsonNode sentenceStats = stats.get("sentences").get(Integer.parseInt(request.getParameter("Id")));
            ArrayNode retval = mapper.createArrayNode();
            Iterator<String> statsIterator = sentenceStats.fieldNames();
            while (statsIterator.hasNext()) {
            	String fieldName = statsIterator.next();
            	retval.add(mapper.createObjectNode().set(fieldName, sentenceStats.get(fieldName)));
            }
            JsonSort.sort(retval);
    		response.setContentType("application/json");
    		response.getWriter().write(mapper.writeValueAsString(retval));
    		response.flushBuffer();
		}
		else if (request.getParameter("Analysis").equals("OOPCoreNLP") 
				&& request.getParameter("Annotation") == null 
				&& request.getParameter("Scope") != null 
				&& request.getParameter("Scope").equalsIgnoreCase("Token")
				&& request.getParameter("Id") != null
		) {
            BufferedReader br = new BufferedReader(
                	new InputStreamReader(
                		request.getSession().getServletContext().getResourceAsStream(
                			"/Corpora/"+pCorpus+"/Annotations/OOP/" + request.getParameter("Document") + ".json"
                		)
                	)	
                );
            ObjectMapper mapper = new ObjectMapper();
            JsonNode stats = mapper.readTree(br);
            Iterator<JsonNode> sentenceIterator = stats.get("sentences").elements();
            int tokenIdx = 1;
            while (sentenceIterator.hasNext()) {
            	Iterator<JsonNode> tokensIterator = sentenceIterator.next().get("tokens").elements();
            	while (tokensIterator.hasNext()) {
            		JsonNode token = tokensIterator.next();
            		if (tokenIdx == Integer.parseInt(request.getParameter("Id"))) {
                        JsonSort.sort(token);
                		response.setContentType("application/json");
                		response.getWriter().write(mapper.writeValueAsString(token));
                		response.flushBuffer();
                		return;
            		}
            		tokenIdx++;
            	}
            }
		}
		else if (
				request.getParameter("Analysis").equals("OOPCoreNLP") 
				&& request.getParameter("Annotation") != null 
				&& request.getParameter("Scope") != null 
				&& request.getParameter("Scope").equalsIgnoreCase("Document")
				) {
            BufferedReader br = new BufferedReader(
                	new InputStreamReader(
                		request.getSession().getServletContext().getResourceAsStream(
                			"/Corpora/"+pCorpus+"/Annotations/OOP/" + request.getParameter("Document") + ".json"
                		)
                	)	
                );
            ObjectMapper mapper = new ObjectMapper();
            JsonNode document = mapper.readTree(br);
            ObjectNode retval = mapper.createObjectNode();
            ArrayNode stats = retval.putArray(request.getParameter("Annotation"));
    		if (document.hasNonNull(request.getParameter("Annotation"))) {
    			stats.add(document.get(request.getParameter("Annotation")));
    		}
    		else {
    			stats.add(mapper.createObjectNode());
    		}
    		response.setContentType("application/json");
    		response.getWriter().write(mapper.writeValueAsString(retval));
    		response.flushBuffer();
    		return;
		}
		else if (
				request.getParameter("Analysis").equals("OOPCoreNLP") 
				&& request.getParameter("Annotation") != null 
				&& request.getParameter("Scope") != null 
				&& request.getParameter("Scope").equalsIgnoreCase("Sentences")
				) {
            BufferedReader br = new BufferedReader(
                	new InputStreamReader(
                		request.getSession().getServletContext().getResourceAsStream(
                			"/Corpora/"+pCorpus+"/Annotations/OOP/" + request.getParameter("Document") + ".json"
                		)
                	)	
                );
            ObjectMapper mapper = new ObjectMapper();
            JsonNode document = mapper.readTree(br);
            ObjectNode retval = mapper.createObjectNode();
            ArrayNode stats = retval.putArray(request.getParameter("Annotation"));
            Iterator<JsonNode> sentenceIterator = document.get("sentences").elements();
            while (sentenceIterator.hasNext()) {
        		JsonNode sentence = sentenceIterator.next();
        		if (sentence.hasNonNull(request.getParameter("Annotation"))) {
        			if (sentence.get(request.getParameter("Annotation")).isArray()) {
        				if (request.getParameter("Aggregate") != null && request.getParameter("Aggregate").equals("Normalized")) {
        					BigDecimal sz = new BigDecimal(sentence.get(request.getParameter("Annotation")).size());
        					BigDecimal len = new BigDecimal(sentence.get("tokens").size());
        					stats.add(sz.divide(len, 4, RoundingMode.HALF_EVEN));
        				}
        				else {
        					stats.add(sentence.get(request.getParameter("Annotation")).size());
        				}
        			}
        			else if (sentence.get(request.getParameter("Annotation")).isObject()) {
        				if (request.getParameter("Aggregate") != null && request.getParameter("Aggregate").equals("Normalized")) {
        					BigDecimal sz = new BigDecimal(0);
        					Iterator<JsonNode> valuesIter = sentence.get(request.getParameter("Annotation")).elements();
        					while (valuesIter.hasNext()) {
        						JsonNode val = valuesIter.next();
        						sz = sz.add(val.decimalValue());
        					}
        					sz = new BigDecimal(sentence.get(request.getParameter("Annotation")).size());
        					BigDecimal len = new BigDecimal(sentence.get("tokens").size());
        					stats.add(sz.divide(len, 4, RoundingMode.HALF_EVEN));
        				}
        				else {
        					BigDecimal sz = new BigDecimal(0);
        					Iterator<String> valueNamesIter = sentence.get(request.getParameter("Annotation")).fieldNames();
        					while (valueNamesIter.hasNext()) {
        						String valueName = valueNamesIter.next();
        						sz = sz.add(new BigDecimal(sentence.get(request.getParameter("Annotation")).get(valueName).asInt(0)));
        					}
        					//stats.add(sentence.get(request.getParameter("Annotation")).size());
        					stats.add(sz);
        				}
        			}
        			else {
        				if (request.getParameter("Aggregate") != null && request.getParameter("Aggregate").equals("Normalized")) {
        					BigDecimal sz = new BigDecimal(sentence.get(request.getParameter("Annotation")).asText());
        					BigDecimal len = new BigDecimal(sentence.get("tokens").size());
        					stats.add(sz.divide(len, 4, RoundingMode.HALF_EVEN));
        				}
        				else {
        					stats.add(sentence.get(request.getParameter("Annotation")));
        				}
        			}
        		}
        		else {
        			stats.add(0);
        		}

            }
    		response.setContentType("application/json");
    		response.getWriter().write(mapper.writeValueAsString(retval));
    		response.flushBuffer();
    		return;
		}
		else if (
				request.getParameter("Analysis").equals("OOPCoreNLP") 
				&& request.getParameter("Annotation") != null 
				&& request.getParameter("Scope") != null 
				&& request.getParameter("Scope").equalsIgnoreCase("Tokens")
				) {
            BufferedReader br = new BufferedReader(
                	new InputStreamReader(
                		request.getSession().getServletContext().getResourceAsStream(
                			"/Corpora/"+pCorpus+"/Annotations/OOP/" + request.getParameter("Document") + ".json"
                		)
                	)	
                );
            ObjectMapper mapper = new ObjectMapper();
            JsonNode document = mapper.readTree(br);
            ObjectNode retval = mapper.createObjectNode();
            ArrayNode stats = retval.putArray(request.getParameter("Annotation"));
            Iterator<JsonNode> sentenceIterator = document.get("sentences").elements();
            while (sentenceIterator.hasNext()) {
            	Iterator<JsonNode> tokensIterator = sentenceIterator.next().get("tokens").elements();
            	while (tokensIterator.hasNext()) {
            		JsonNode token = tokensIterator.next();
            		if (token.hasNonNull(request.getParameter("Annotation"))) {
            			stats.add(token.get(request.getParameter("Annotation")));
            		}
            		else {
            			stats.add(mapper.createObjectNode());
            		}
            	}
            }
    		response.setContentType("application/json");
    		response.getWriter().write(mapper.writeValueAsString(retval));
    		response.flushBuffer();
    		return;
		}
		else if (
				request.getParameter("Analysis").equals("OOPCoreNLP") 
				&& request.getParameter("Annotation") != null 
				&& request.getParameter("Scope") != null 
				&& request.getParameter("Scope").equalsIgnoreCase("Syllables")
				) {
            BufferedReader br = new BufferedReader(
                	new InputStreamReader(
                		request.getSession().getServletContext().getResourceAsStream(
                			"/Corpora/"+pCorpus+"/Annotations/OOP/" + request.getParameter("Document") + ".json"
                		)
                	)	
                );
            ObjectMapper mapper = new ObjectMapper();
            JsonNode document = mapper.readTree(br);
            ObjectNode retval = mapper.createObjectNode();
            ArrayNode stats = retval.putArray(request.getParameter("Annotation"));
            Iterator<JsonNode> sentenceIterator = document.get("sentences").elements();
            while (sentenceIterator.hasNext()) {
            	Iterator<JsonNode> tokensIterator = sentenceIterator.next().get("tokens").elements();
            	while (tokensIterator.hasNext()) {
            		JsonNode token = tokensIterator.next();
            		for (int i=0;i<token.get("io.outofprintmagazine.nlp.pipeline.OOPAnnotations$OOPSyllablesAnnotation").asInt();i++) {
            			if (token.hasNonNull(request.getParameter("Annotation"))) {
            				stats.add(token.get(request.getParameter("Annotation")));
            			}
            			else {
            				stats.add(mapper.createObjectNode());
            			}
            		}
            	}
            }
    		response.setContentType("application/json");
    		response.getWriter().write(mapper.writeValueAsString(retval));
    		response.flushBuffer();
    		return;
		}
		else if (request.getParameter("Analysis").equals("OOPCoreNLP")) {
			request.getSession().getServletContext().getRequestDispatcher("/Corpora/"+pCorpus+"/Annotations/OOP/" + request.getParameter("Document") + ".json").forward(request, response);
		}
		else if (request.getParameter("Analysis").equals("OOPCoreNLP") 
				&& request.getParameter("Annotation") == null 
				&& request.getParameter("Scope") != null 
				&& request.getParameter("Scope").equalsIgnoreCase("Document")
		) {
            BufferedReader br = new BufferedReader(
                	new InputStreamReader(
                		request.getSession().getServletContext().getResourceAsStream(
                			"/Corpora/"+pCorpus+"/Annotations/OOP/" + request.getParameter("Document") + ".json"
                		)
                	)	
                );
            ObjectMapper mapper = new ObjectMapper();
            JsonNode stats = mapper.readTree(br);
            ArrayNode retval = mapper.createArrayNode();
            Iterator<String> statsIterator = stats.fieldNames();
            while (statsIterator.hasNext()) {
            	String fieldName = statsIterator.next();
            	retval.add(mapper.createObjectNode().set(fieldName, stats.get(fieldName)));
            }
            JsonSort.sort(retval);
    		response.setContentType("application/json");
    		response.getWriter().write(mapper.writeValueAsString(retval));
    		response.flushBuffer();

		}
		else if (request.getParameter("Analysis").equals("OOPCoreNLP") 
				&& request.getParameter("Annotation") == null 
				&& request.getParameter("Scope") != null 
				&& request.getParameter("Scope").equalsIgnoreCase("Sentence")
				&& request.getParameter("Id") != null
		) {
            BufferedReader br = new BufferedReader(
                	new InputStreamReader(
                		request.getSession().getServletContext().getResourceAsStream(
                			"/Corpora/"+pCorpus+"/Annotations/OOP/" + request.getParameter("Document") + ".json"
                		)
                	)	
                );
            ObjectMapper mapper = new ObjectMapper();
            JsonNode stats = mapper.readTree(br);
            JsonNode sentenceStats = stats.get("sentences").get(Integer.parseInt(request.getParameter("Id")));
            ArrayNode retval = mapper.createArrayNode();
            Iterator<String> statsIterator = sentenceStats.fieldNames();
            while (statsIterator.hasNext()) {
            	String fieldName = statsIterator.next();
            	retval.add(mapper.createObjectNode().set(fieldName, sentenceStats.get(fieldName)));
            }
            JsonSort.sort(retval);
    		response.setContentType("application/json");
    		response.getWriter().write(mapper.writeValueAsString(retval));
    		response.flushBuffer();
		}
		else if (request.getParameter("Analysis").equals("OOPCoreNLP") 
				&& request.getParameter("Annotation") == null 
				&& request.getParameter("Scope") != null 
				&& request.getParameter("Scope").equalsIgnoreCase("Token")
				&& request.getParameter("Id") != null
		) {
            BufferedReader br = new BufferedReader(
                	new InputStreamReader(
                		request.getSession().getServletContext().getResourceAsStream(
                			"/Corpora/"+pCorpus+"/Annotations/OOP/" + request.getParameter("Document") + ".json"
                		)
                	)	
                );
            ObjectMapper mapper = new ObjectMapper();
            JsonNode stats = mapper.readTree(br);
            Iterator<JsonNode> sentenceIterator = stats.get("sentences").elements();
            int tokenIdx = 1;
            while (sentenceIterator.hasNext()) {
            	Iterator<JsonNode> tokensIterator = sentenceIterator.next().get("tokens").elements();
            	while (tokensIterator.hasNext()) {
            		JsonNode token = tokensIterator.next();
            		if (tokenIdx == Integer.parseInt(request.getParameter("Id"))) {
                        JsonSort.sort(token);
                		response.setContentType("application/json");
                		response.getWriter().write(mapper.writeValueAsString(token));
                		response.flushBuffer();
                		return;
            		}
            		tokenIdx++;
            	}
            }
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
		String pCorpus = request.getParameter("Corpus");
		TsvParserSettings settings = new TsvParserSettings();
		settings.setHeaderExtractionEnabled(true);
		TsvParser parser = new TsvParser(settings);
		List<Record> dimensions = parser.parseAllRecords(new InputStreamReader(request.getSession().getServletContext().getResourceAsStream("/Corpora/"+pCorpus+"/Biber/MAT_Biber/Statistics/Dimensions_Biber.dat")));
		ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
		ObjectNode documentNode = mapper.createObjectNode();
		ObjectNode dimensionNode = documentNode.putObject("Dimensions");
		ObjectNode statisticsNode = documentNode.putObject("Statistics");
		String docId = request.getParameter("Document");
		for (Record record : dimensions) {
			if (record.getString("Filename").equals(docId)) {
				dimensionNode.put("Dimension1", record.getBigDecimal("Dimension1"));
				dimensionNode.put("Dimension2", record.getBigDecimal("Dimension2"));
				dimensionNode.put("Dimension3", record.getBigDecimal("Dimension3"));
				dimensionNode.put("Dimension4", record.getBigDecimal("Dimension4"));
				dimensionNode.put("Dimension5", record.getBigDecimal("Dimension5"));
				dimensionNode.put("Dimension6", record.getBigDecimal("Dimension6"));
				dimensionNode.put("ClosestTextType", record.getString("Closest Text Type"));
				break;
			}
		}
		List<Record> statistics = parser.parseAllRecords(new InputStreamReader(request.getSession().getServletContext().getResourceAsStream("/Corpora/"+pCorpus+"/Biber/MAT_Biber/Statistics/Statistics_Biber.dat")));

		for (Record record : statistics) {
			if (record.getString("Filename").equals(docId)) {
				for (String columnName : record.getMetaData().headers()) {
					String displayColumnName = "wtf";
					if (columnName != null) {
						displayColumnName = columnName;
						if (displayColumnName.contains("[")) {
							displayColumnName = displayColumnName.replaceAll("\\[", "");
						}
						if (displayColumnName.contains("]")) {
							displayColumnName = displayColumnName.replaceAll("\\]", "");
						}
						ObjectNode statisticNode = statisticsNode.putObject(displayColumnName);
						statisticNode.put("RawScore", record.getString(columnName));
					}
				}
				break;
			}
		}
		
		List<Record> zStatistics = parser.parseAllRecords(new InputStreamReader(request.getSession().getServletContext().getResourceAsStream("/Corpora/"+pCorpus+"/Biber/MAT_Biber/Statistics/Zscores_Biber.dat")));

		for (Record record : zStatistics) {
			if (record.getString("Filename").equals(docId)) {
				for (String columnName : record.getMetaData().headers()) {
					String displayColumnName = "wtf";
					if (columnName != null) {
						displayColumnName = columnName;
						if (displayColumnName.contains("[")) {
							displayColumnName = displayColumnName.replaceAll("\\[", "");
						}
						if (displayColumnName.contains("]")) {
							displayColumnName = displayColumnName.replaceAll("\\]", "");
						}
						ObjectNode statisticNode = (ObjectNode) statisticsNode.get(displayColumnName);
						if (statisticNode == null) {
							statisticNode = statisticsNode.putObject(displayColumnName);
						}
						statisticNode.put("ZScore", record.getString(columnName));
					}
				}
				break;
			}
		}
		
		
		response.setContentType("application/json");
		response.getWriter().write(mapper.writeValueAsString(documentNode));
		response.flushBuffer();
	}

}
