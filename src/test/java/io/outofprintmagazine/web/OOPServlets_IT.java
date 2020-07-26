package io.outofprintmagazine.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


public class OOPServlets_IT {
	
	public static String serverUri = "http://localhost:8080";
	public static String webappUri = "/oopcorenlp_web";
	public static String corpus = "Sample";
	public static String document = "de4c996ccd44e38f38cb673d2e5bd3c2";

	
	public static String getUri() {
		return serverUri + webappUri;
	}
	
	public static String getURLString(String path) {
		return getUri() + "/" + path;
	}
	
	public static URL getURL(String path) throws MalformedURLException {
		return new URL(getURLString(path));
	}
	

	public OOPServlets_IT() {
		super();
	}

	@Test
	public void sanityCheck_IT() throws IOException {
		assertTrue(
				"Donald Trump is lying".matches(".*"),
				String.format("His mouth is moving")
		);
	}
	

    @Test
    public void homePage_IT() throws Exception {
        // Test GET
        HttpURLConnection http = (HttpURLConnection)getURL("").openConnection();
        http.connect();
        assertEquals(200, http.getResponseCode(), "tomcat is down");
    }
    
    @Test
    public void listCorpora_IT() throws Exception {
    	ObjectMapper mapper = new ObjectMapper();
		JsonNode json = mapper.readTree(getURL("ListCorpora"));
		assertTrue(json.has("Corpora"), "no corpora node");
		boolean foundMatch = false;
		Iterator<JsonNode> iter = ((ArrayNode)json.get("Corpora")).elements();
		while (iter.hasNext()) {
			if (iter.next().asText().equals(corpus)) {
				foundMatch = true;
				break;
			}
		}
        assertTrue(foundMatch, String.format("no corpus node: %s", corpus));
    }
    
    @Test
    public void listCorpusDocuments_IT() throws Exception {
    	ObjectMapper mapper = new ObjectMapper();
		JsonNode json = mapper.readTree(getURL("ListCorpusDocuments?Corpus="+corpus));
		assertTrue(json.has("Documents"), "no corpora node");
		boolean foundMatch = false;
		Iterator<JsonNode> iter = ((ArrayNode)json.get("Documents")).elements();
		while (iter.hasNext()) {
			if (iter.next().asText().equals(document)) {
				foundMatch = true;
				break;
			}
		}
        assertTrue(foundMatch, String.format("no document node: %s", document));
    }
    
    @Test
    public void listDocumentAnalyses_IT() throws Exception {
    	ObjectMapper mapper = new ObjectMapper();
		ArrayNode json = (ArrayNode) mapper.readTree(getURL("ListDocumentAnalyses?Corpus="+corpus+"&Document="+document));
		assertTrue(json.size() > 0, String.format("no document analyses"));
    }

    @Test
    public void corporaViewer_IT() throws Exception {
    	Document doc = Jsoup.parse(getURLString("CorporaViewer"));
    	Elements corpusLink = doc.select("a[href=CorpusDocumentsViewer?Corpus="+corpus+"]");
		assertTrue(corpusLink.size() > 0, String.format("no corpus link: %s", corpus));
    }
    
}
