package io.outofprintmagazine.web.rest.browse;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.outofprintmagazine.web.servlets.AbstractOOPCacheableServlet;

@Path("/Corpora/{Corpus}/{Document}/{Scores}/{Annotation}")
public class CorpusDocumentScoresAnnotation extends AbstractOOPCacheableServlet {
	
	private static final long serialVersionUID = 1L;
	@Context
	private ServletConfig servletConfig;
	@Context
	private ServletContext servletContext;
	@Context
	private HttpServletRequest httpServletRequest;
	@Context
	private HttpServletResponse httpServletResponse;

    @GET
    @Produces("application/json; charset=utf-8")
	public String doGet(
			@PathParam("Corpus") String corpus,
			@PathParam("Document") String document,
			@PathParam("Scores") String scores,
			@PathParam("Annotation") String annotation
			) throws JsonProcessingException, IOException, ServletException
    {
    	if (getStorage() == null) {
    		super.init(servletConfig);
    	}
	
		if (scores.equals("OOP")) {
			return getMapper().writeValueAsString(
					getStorage().getCorpusDocumentOOPJson(
							corpus, 
							document
					).get(annotation)
			);
		}
		
		else if (scores.equals("AGGREGATES")) {
			return getMapper().writeValueAsString(
					getStorage().getCorpusDocumentAggregatesJson(
							corpus, 
							document
					).get(annotation)
			);
		}
				
		else if (scores.equals("PIPELINE")) {
			return getMapper().writeValueAsString(
					getStorage().getCorpusDocumentPipelineJson(
							corpus, 
							document
					).get(annotation)
			);
		}
		
		else if (scores.equals("STANFORD")) {
			return getMapper().writeValueAsString(
					getStorage().getCorpusDocumentStanfordJson(
							corpus, 
							document
					).get(annotation)
			);
		}
		else {
			throw new FileNotFoundException(scores);
		}
	}

}
