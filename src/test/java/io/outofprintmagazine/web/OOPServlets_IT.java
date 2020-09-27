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
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;


public class OOPServlets_IT {
	
	public static String serverUri = "http://localhost:8888";
	public static String webappUri = "/oopcorenlp_web";
	public static String corpus = "Sample";
	public static String document = "de4c996ccd44e38f38cb673d2e5bd3c2";

	
	public static String getUri() {
		return serverUri + webappUri;
	}
	
	public static String getURLString(String path) {
		return getUri() + "/" + path;
	}
	
	public static String getURLString(String path, String pCorpus, String pDocument) {
		return getUri() + "/" + path + "?Corpus="+pCorpus+"&Document="+pDocument;
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
    public void corporaViewer_IT() throws Exception {
    	Document doc = Jsoup.connect(getURLString("CorporaViewer")).get();
    	Elements corpusLink = doc.select("a[href=CorpusDocumentsViewer?Corpus="+corpus+"]");
		assertTrue(corpusLink.size() > 0, String.format("no corpus link: %s", corpus));
    }
    
    @Test
    public void corpusDocumentsViewer_IT() throws Exception {
    	Document doc = Jsoup.connect(getURLString("CorpusDocumentsViewer?Corpus="+corpus)).get();
    	Elements corpusLink = doc.select("a[href=TextViewer?Corpus="+corpus+"&Document="+document+"]");
		assertTrue(corpusLink.size() > 0, String.format("no corpus document link: %s %s", corpus, document));
    }
    
    @Test
    public void OOPStoryArcViewer_IT() throws Exception {
    	Document doc = Jsoup.connect(getURLString("OOPStoryArcViewer", corpus, document)).get();
    	assertTrue(doc.select("#corpusDocumentTitle").size() == 1, String.format("no document title: %s %s", corpus, document));
    	assertTrue(doc.select("#corpusDocumentAuthor").size() == 1, String.format("no document author: %s %s", corpus, document));
    	assertTrue(doc.select("#corpusDocumentDate").size() == 1, String.format("no document date: %s %s", corpus, document));
    }
    
    @Test
    public void OOPPullQuotesViewer_IT() throws Exception {
    	Document doc = Jsoup.connect(getURLString("OOPPullQuotesViewer", corpus, document)).get();
    	assertTrue(doc.select("#corpusDocumentTitle").size() == 1, String.format("no document title: %s %s", corpus, document));
    	assertTrue(doc.select("#corpusDocumentAuthor").size() == 1, String.format("no document author: %s %s", corpus, document));
    	assertTrue(doc.select("#corpusDocumentDate").size() == 1, String.format("no document date: %s %s", corpus, document));
    }
    
    @Test
    public void OOPEditorViewer_IT() throws Exception {
    	Document doc = Jsoup.connect(getURLString("OOPEditorViewer", corpus, document)).get();
    	assertTrue(doc.select("#corpusDocumentTitle").size() == 1, String.format("no document title: %s %s", corpus, document));
    	assertTrue(doc.select("#corpusDocumentAuthor").size() == 1, String.format("no document author: %s %s", corpus, document));
    	assertTrue(doc.select("#corpusDocumentDate").size() == 1, String.format("no document date: %s %s", corpus, document));
    }
    
    @Test
    public void OOPActorsViewer_IT() throws Exception {
    	Document doc = Jsoup.connect(getURLString("OOPActorsViewer", corpus, document)).get();
    	assertTrue(doc.select("#corpusDocumentTitle").size() == 1, String.format("no document title: %s %s", corpus, document));
    	assertTrue(doc.select("#corpusDocumentAuthor").size() == 1, String.format("no document author: %s %s", corpus, document));
    	assertTrue(doc.select("#corpusDocumentDate").size() == 1, String.format("no document date: %s %s", corpus, document));
    }    

    @Test
    public void OOPCloudViewer_IT() throws Exception {
    	Document doc = Jsoup.connect(getURLString("OOPCloudViewer", corpus, document)).get();
    	assertTrue(doc.select("#corpusDocumentTitle").size() == 1, String.format("no document title: %s %s", corpus, document));
    	assertTrue(doc.select("#corpusDocumentAuthor").size() == 1, String.format("no document author: %s %s", corpus, document));
    	assertTrue(doc.select("#corpusDocumentDate").size() == 1, String.format("no document date: %s %s", corpus, document));
    }
    
    @Test
    public void OOPStreamViewer_IT() throws Exception {
    	Document doc = Jsoup.connect(getURLString("OOPStreamViewer", corpus, document)).get();
    	assertTrue(doc.select("#corpusDocumentTitle").size() == 1, String.format("no document title: %s %s", corpus, document));
    	assertTrue(doc.select("#corpusDocumentAuthor").size() == 1, String.format("no document author: %s %s", corpus, document));
    	assertTrue(doc.select("#corpusDocumentDate").size() == 1, String.format("no document date: %s %s", corpus, document));
    }
    
    @Test
    public void OOPStreamViewerTokens_IT() throws Exception {
    	Document doc = Jsoup.connect(getURLString("OOPStreamViewerTokens", corpus, document)).get();
    	assertTrue(doc.select("#corpusDocumentTitle").size() == 1, String.format("no document title: %s %s", corpus, document));
    	assertTrue(doc.select("#corpusDocumentAuthor").size() == 1, String.format("no document author: %s %s", corpus, document));
    	assertTrue(doc.select("#corpusDocumentDate").size() == 1, String.format("no document date: %s %s", corpus, document));
    }
    
    @Test
    public void OOPDocumentViewer_IT() throws Exception {
    	Document doc = Jsoup.connect(getURLString("OOPDocumentViewer", corpus, document)).get();
    	assertTrue(doc.select("#corpusDocumentTitle").size() == 1, String.format("no document title: %s %s", corpus, document));
    	assertTrue(doc.select("#corpusDocumentAuthor").size() == 1, String.format("no document author: %s %s", corpus, document));
    	assertTrue(doc.select("#corpusDocumentDate").size() == 1, String.format("no document date: %s %s", corpus, document));
    }
    
    @Test
    public void StanfordBratViewer_IT() throws Exception {
    	Document doc = Jsoup.connect(getURLString("StanfordBratViewer", corpus, document)).get();
    	assertTrue(doc.select("#corpusDocumentTitle").size() == 1, String.format("no document title: %s %s", corpus, document));
    	assertTrue(doc.select("#corpusDocumentAuthor").size() == 1, String.format("no document author: %s %s", corpus, document));
    	assertTrue(doc.select("#corpusDocumentDate").size() == 1, String.format("no document date: %s %s", corpus, document));
    }    
}
