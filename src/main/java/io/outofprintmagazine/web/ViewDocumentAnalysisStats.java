package io.outofprintmagazine.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet implementation class GetDocumentStatsViewer
 */
@WebServlet("/ViewDocumentAnalysisStats")
public class ViewDocumentAnalysisStats extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewDocumentAnalysisStats() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pCorpus = request.getParameter("Corpus");
		if (request.getParameter("Analysis").equals("Biber")) {
			request.getSession().getServletContext().getRequestDispatcher("/Corpora/"+pCorpus+"/Biber/MAT_Biber/" + request.getParameter("Document") + "_MAT_features.html").forward(request, response);
		}
		//need to deal with Submissions and Published differently
		else if (request.getParameter("Corpus").equals("Submissions") && request.getParameter("Analysis").equals("Text") && request.getParameter("Viewer") != null && request.getParameter("Viewer").equalsIgnoreCase("medium")) {
            BufferedReader br = new BufferedReader(
            	new InputStreamReader(
            		request.getSession().getServletContext().getResourceAsStream(
            			"/Corpora/"+pCorpus+"/Text/" + request.getParameter("Document") + ".txt"
            		)
            	)
            );
		    StringBuilder contentBuilder = new StringBuilder();
		    String sCurrentLine;
		    boolean inHeader = true;
	        while ((sCurrentLine = br.readLine()) != null) {
	        	if (sCurrentLine.trim().length() == 0) {
	        		inHeader = false;
	        	}
	        	if (inHeader) {
	        		int firstColonPosition = sCurrentLine.indexOf(":");
	        		if (firstColonPosition > -1) {
	        			String fieldName = sCurrentLine.substring(0, firstColonPosition);
	        			String fieldValue = sCurrentLine.substring(firstColonPosition+1);
	        			if (fieldName.equalsIgnoreCase("From")) {
	        				request.setAttribute("Author", fieldValue);
	        			}
	        			else if (fieldName.equalsIgnoreCase("Date")) {
	        				request.setAttribute("Date", fieldValue);
	        			}
	        			else if (fieldName.equalsIgnoreCase("Subject")) {
	        				request.setAttribute("Title", fieldValue);
	        			}
	        			else if (fieldName.equalsIgnoreCase("Title")) {
	        				request.setAttribute("Title", fieldValue);
	        			}
	        		
	        		}
	        		else {
	        			inHeader = false;
	        			contentBuilder.append("<p>").append(sCurrentLine).append("</p>").append("\n");
	        		}
	        	}
	        	else {
		        	if (sCurrentLine.trim().length() == 0) {
		        	//	contentBuilder.append("<p>").append("&nbsp;").append("</p>").append("\n");
		        	}
		        	else {
		        		contentBuilder.append("<p>").append(sCurrentLine).append("</p>").append("\n");
		        	}
	        	}
	        }
	        br.close();
	        request.setAttribute("Text", contentBuilder.toString());
	        request.getSession().getServletContext().getRequestDispatcher("/TextMediumViewer.jsp").forward(request, response);
		}

		else if (request.getParameter("Corpus").equals("Submissions") && request.getParameter("Analysis").equals("Text") && request.getParameter("Viewer") != null && request.getParameter("Viewer").equalsIgnoreCase("oop")) {
            BufferedReader br = new BufferedReader(
            	new InputStreamReader(
            		request.getSession().getServletContext().getResourceAsStream(
            			"/Corpora/"+pCorpus+"/Text/" + request.getParameter("Document") + ".txt"
            		)
            	)
            );
		    StringBuilder contentBuilder = new StringBuilder();
		    String sCurrentLine;
		    boolean inHeader = true;
	        while ((sCurrentLine = br.readLine()) != null) {
	        	if (sCurrentLine.trim().length() == 0) {
	        		inHeader = false;
	        	}
	        	if (inHeader) {
	        		int firstColonPosition = sCurrentLine.indexOf(":");
	        		if (firstColonPosition > -1) {
	        			String fieldName = sCurrentLine.substring(0, firstColonPosition);
	        			String fieldValue = sCurrentLine.substring(firstColonPosition+1);
	        			if (fieldName.equalsIgnoreCase("From")) {
	        				request.setAttribute("Author", fieldValue);
	        			}
	        			else if (fieldName.equalsIgnoreCase("Date")) {
	        				request.setAttribute("Date", fieldValue);
	        			}
	        			else if (fieldName.equalsIgnoreCase("Subject")) {
	        				request.setAttribute("Title", fieldValue);
	        			}
	        			else if (fieldName.equalsIgnoreCase("Title")) {
	        				request.setAttribute("Title", fieldValue);
	        			}
	        		
	        		}
	        		else {
	        			inHeader = false;
	        			contentBuilder.append("<p>").append(sCurrentLine).append("</p>").append("\n");
	        		}
	        	}
	        	else {
		        	if (sCurrentLine.trim().length() == 0) {
		        		//contentBuilder.append("<p>").append("&nbsp;").append("</p>").append("\n");
		        	}
		        	else {
		        		contentBuilder.append("<p>").append(sCurrentLine).append("</p>").append("\n");
		        	}
	        	}
	        }
	        br.close();
	        request.setAttribute("Text", contentBuilder.toString());
	        request.getSession().getServletContext().getRequestDispatcher("/TextOOPViewer.jsp").forward(request, response);
		}
		else if (request.getParameter("Corpus").equals("Published") && request.getParameter("Analysis").equals("Text") && request.getParameter("Viewer") != null && request.getParameter("Viewer").equalsIgnoreCase("oop")) {
            BufferedReader br = new BufferedReader(
                	new InputStreamReader(
                		request.getSession().getServletContext().getResourceAsStream(
                			"/Corpora/"+pCorpus+"/Annotations/OOP/" + request.getParameter("Document") + ".json"
                		)
                	)	
                );
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode stats = objectMapper.readTree(br);
			request.setAttribute("Author", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$AuthorAnnotation").asText());
			request.setAttribute("Date", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$DocDateAnnotation").asText());
			request.setAttribute("Title", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$DocTitleAnnotation").asText());
			request.setAttribute("Stats", stats);
			br.close();
			
            br = new BufferedReader(
            	new InputStreamReader(
            		request.getSession().getServletContext().getResourceAsStream(
            			"/Corpora/"+pCorpus+"/Text/" + request.getParameter("Document") + ".txt"
            		)
            	)
            );
		    StringBuilder contentBuilder = new StringBuilder();
		    String sCurrentLine;
	        while ((sCurrentLine = br.readLine()) != null) {
	        	if (sCurrentLine.trim().length() == 0) {
	        		contentBuilder.append("<p>").append("&nbsp;").append("</p>").append("\n");
	        	}
	        	else {
	        		contentBuilder.append("<p>").append(sCurrentLine).append("</p>").append("\n");
	        	}
	        }
	        br.close();
	        request.setAttribute("Text", contentBuilder.toString());
	        request.getSession().getServletContext().getRequestDispatcher("/TextOOPViewer.jsp").forward(request, response);
		}
		else if (request.getParameter("Corpus").equals("Published") && request.getParameter("Analysis").equals("Text") && request.getParameter("Viewer") != null && request.getParameter("Viewer").equalsIgnoreCase("medium")) {
            BufferedReader br = new BufferedReader(
                	new InputStreamReader(
                		request.getSession().getServletContext().getResourceAsStream(
                			"/Corpora/"+pCorpus+"/Annotations/OOP/" + request.getParameter("Document") + ".json"
                		)
                	)	
                );
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode stats = objectMapper.readTree(br);
			request.setAttribute("Author", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$AuthorAnnotation").asText());
			request.setAttribute("Date", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$DocDateAnnotation").asText());
			request.setAttribute("Title", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$DocTitleAnnotation").asText());
			request.setAttribute("Stats", stats);
			br.close();
			
            br = new BufferedReader(
            	new InputStreamReader(
            		request.getSession().getServletContext().getResourceAsStream(
            			"/Corpora/"+pCorpus+"/Text/" + request.getParameter("Document") + ".txt"
            		)
            	)
            );
		    StringBuilder contentBuilder = new StringBuilder();
		    String sCurrentLine;
	        while ((sCurrentLine = br.readLine()) != null) {
	        	if (sCurrentLine.trim().length() == 0) {
	        		contentBuilder.append("<p>").append("&nbsp;").append("</p>").append("\n");
	        	}
	        	else {
	        		contentBuilder.append("<p>").append(sCurrentLine).append("</p>").append("\n");
	        	}
	        }
	        br.close();
	        request.setAttribute("Text", contentBuilder.toString());
	        request.getSession().getServletContext().getRequestDispatcher("/TextMediumViewer.jsp").forward(request, response);
		}
		else if (request.getParameter("Analysis").equals("OOPCoreNLP")) {
            BufferedReader br = new BufferedReader(
                	new InputStreamReader(
                		request.getSession().getServletContext().getResourceAsStream(
                			"/Corpora/"+pCorpus+"/Annotations/OOP/" + request.getParameter("Document") + ".json"
                		)
                	)	
                );
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode stats = objectMapper.readTree(br);
			request.setAttribute("Author", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$AuthorAnnotation").asText());
			request.setAttribute("Date", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$DocDateAnnotation").asText());
			request.setAttribute("Title", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$DocTitleAnnotation").asText());
			request.setAttribute("Stats", stats);
			br.close();
			request.getSession().getServletContext().getRequestDispatcher("/CoreNLPDocumentViewer.jsp").forward(request, response);
		}
		else if (request.getParameter("Analysis").equals("StanfordCoreNLP")) {
            BufferedReader br = new BufferedReader(
                	new InputStreamReader(
                		request.getSession().getServletContext().getResourceAsStream(
                			"/Corpora/"+pCorpus+"/Annotations/OOP/" + request.getParameter("Document") + ".json"
                		)
                	)	
                );
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode stats = objectMapper.readTree(br);
			request.setAttribute("Author", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$AuthorAnnotation").asText());
			request.setAttribute("Date", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$DocDateAnnotation").asText());
			request.setAttribute("Title", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$DocTitleAnnotation").asText());
			request.setAttribute("Stats", stats);
			request.getSession().getServletContext().getRequestDispatcher("/StanfordCoreNLPDocumentViewer.jsp").forward(request, response);
		}
		else if (request.getParameter("Analysis").equals("OOPFactChecker")) {
            BufferedReader br = new BufferedReader(
                	new InputStreamReader(
                		request.getSession().getServletContext().getResourceAsStream(
                			"/Corpora/"+pCorpus+"/Annotations/OOP/" + request.getParameter("Document") + ".json"
                		)
                	)	
                );
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode stats = objectMapper.readTree(br);
			request.setAttribute("Author", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$AuthorAnnotation").asText());
			request.setAttribute("Date", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$DocDateAnnotation").asText());
			request.setAttribute("Title", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$DocTitleAnnotation").asText());
			request.setAttribute("Stats", stats);
			request.getSession().getServletContext().getRequestDispatcher("/OOPFactCheckerDocumentViewer.jsp").forward(request, response);
		}
		else if (request.getParameter("Analysis").equals("OOPEditor")) {
            BufferedReader br = new BufferedReader(
                	new InputStreamReader(
                		request.getSession().getServletContext().getResourceAsStream(
                			"/Corpora/"+pCorpus+"/Annotations/OOP/" + request.getParameter("Document") + ".json"
                		)
                	)	
                );
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode stats = objectMapper.readTree(br);
			request.setAttribute("Author", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$AuthorAnnotation").asText());
			request.setAttribute("Date", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$DocDateAnnotation").asText());
			request.setAttribute("Title", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$DocTitleAnnotation").asText());
			request.setAttribute("Stats", stats);
			request.getSession().getServletContext().getRequestDispatcher("/OOPEditorDocumentViewer.jsp").forward(request, response);
		}
		else if (request.getParameter("Analysis").equals("Actors")) {
            BufferedReader br = new BufferedReader(
                	new InputStreamReader(
                		request.getSession().getServletContext().getResourceAsStream(
                			"/Corpora/"+pCorpus+"/Annotations/OOP/" + request.getParameter("Document") + ".json"
                		)
                	)	
                );
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode stats = objectMapper.readTree(br);
			request.setAttribute("Author", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$AuthorAnnotation").asText());
			request.setAttribute("Date", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$DocDateAnnotation").asText());
			request.setAttribute("Title", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$DocTitleAnnotation").asText());
			request.setAttribute("Stats", stats);
			request.getSession().getServletContext().getRequestDispatcher("/CoreNLPActorsViewer.jsp").forward(request, response);
		}
		else if (request.getParameter("Analysis").equals("Stream")) {
            BufferedReader br = new BufferedReader(
                	new InputStreamReader(
                		request.getSession().getServletContext().getResourceAsStream(
                			"/Corpora/"+pCorpus+"/Annotations/OOP/" + request.getParameter("Document") + ".json"
                		)
                	)	
                );
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode stats = objectMapper.readTree(br);
			request.setAttribute("Author", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$AuthorAnnotation").asText());
			request.setAttribute("Date", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$DocDateAnnotation").asText());
			request.setAttribute("Title", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$DocTitleAnnotation").asText());
			request.setAttribute("Stats", stats);
			request.getSession().getServletContext().getRequestDispatcher("/CoreNLPStreamViewer.jsp").forward(request, response);
		}
		else if (request.getParameter("Analysis").equals("StreamTokens")) {
            BufferedReader br = new BufferedReader(
                	new InputStreamReader(
                		request.getSession().getServletContext().getResourceAsStream(
                			"/Corpora/"+pCorpus+"/Annotations/OOP/" + request.getParameter("Document") + ".json"
                		)
                	)	
                );
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode stats = objectMapper.readTree(br);
			request.setAttribute("Author", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$AuthorAnnotation").asText());
			request.setAttribute("Date", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$DocDateAnnotation").asText());
			request.setAttribute("Title", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$DocTitleAnnotation").asText());
			request.setAttribute("Stats", stats);
			request.getSession().getServletContext().getRequestDispatcher("/CoreNLPStreamViewerTokens.jsp").forward(request, response);
		}
		else if (request.getParameter("Analysis").equals("Cloud")) {
            BufferedReader br = new BufferedReader(
                	new InputStreamReader(
                		request.getSession().getServletContext().getResourceAsStream(
                			"/Corpora/"+pCorpus+"/Annotations/OOP/" + request.getParameter("Document") + ".json"
                		)
                	)	
                );
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode stats = objectMapper.readTree(br);
			request.setAttribute("Author", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$AuthorAnnotation").asText());
			request.setAttribute("Date", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$DocDateAnnotation").asText());
			request.setAttribute("Title", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$DocTitleAnnotation").asText());
			request.setAttribute("Stats", stats);
			request.getSession().getServletContext().getRequestDispatcher("/CoreNLPCloudViewer.jsp").forward(request, response);
		}
		else if (request.getParameter("Analysis").equals("CloudViz")) {
            BufferedReader br = new BufferedReader(
                	new InputStreamReader(
                		request.getSession().getServletContext().getResourceAsStream(
                			"/Corpora/"+pCorpus+"/Annotations/OOP/" + request.getParameter("Document") + ".json"
                		)
                	)	
                );
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode stats = objectMapper.readTree(br);
			request.setAttribute("Author", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$AuthorAnnotation").asText());
			request.setAttribute("Date", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$DocDateAnnotation").asText());
			request.setAttribute("Title", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$DocTitleAnnotation").asText());
			request.setAttribute("Stats", stats);
			request.getSession().getServletContext().getRequestDispatcher("/CoreNLPCloudViewerEmbed.jsp").forward(request, response);
		}
		else if (request.getParameter("Analysis").equals("Aggregate")) {
            BufferedReader br = new BufferedReader(
                	new InputStreamReader(
                		request.getSession().getServletContext().getResourceAsStream(
                			"/Corpora/"+pCorpus+"/Annotations/OOP/" + request.getParameter("Document") + ".json"
                		)
                	)	
                );
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode stats = objectMapper.readTree(br);
			request.setAttribute("Author", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$AuthorAnnotation").asText());
			request.setAttribute("Date", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$DocDateAnnotation").asText());
			request.setAttribute("Title", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$DocTitleAnnotation").asText());
			request.setAttribute("Stats", stats);
			request.getSession().getServletContext().getRequestDispatcher("/CoreNLPAggregateViewer.jsp").forward(request, response);
		}
		else if (request.getParameter("Analysis").equals("Summary")) {
            BufferedReader br = new BufferedReader(
                	new InputStreamReader(
                		request.getSession().getServletContext().getResourceAsStream(
                			"/Corpora/"+pCorpus+"/Annotations/OOP/" + request.getParameter("Document") + ".json"
                		)
                	)	
                );
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode stats = objectMapper.readTree(br);
			request.setAttribute("Author", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$AuthorAnnotation").asText());
			request.setAttribute("Date", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$DocDateAnnotation").asText());
			request.setAttribute("Title", stats.findValue("edu.stanford.nlp.ling.CoreAnnotations$DocTitleAnnotation").asText());
			request.setAttribute("Stats", stats);
			br.close();
			request.getSession().getServletContext().getRequestDispatcher("/CoreNLPSummaryViewer.jsp").forward(request, response);

			

			

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

}
