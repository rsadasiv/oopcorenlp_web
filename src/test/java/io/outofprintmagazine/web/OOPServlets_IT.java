package io.outofprintmagazine.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class OOPServlets_IT {
	
	public static String serverUri = "http://localhost:8888";
	public static String webappUri = "/oopcorenlp_web";

	public static String corpus = null; //"Sample";
	public static String document = null; //"b1c0b1bf9462122f5aa52514511e1f8c";

	
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
	
	public static String getCorpus() throws MalformedURLException, IOException {
		if (corpus == null) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode corpora = mapper.readTree(new URL(getUri()+"/rest/browse/Corpora"));
			corpus = corpora.get(0).asText("Sample");
		}
		return corpus;
	}
	
	public static String getDocument() throws MalformedURLException, IOException {
		if (document == null) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode corpusDocuments = mapper.readTree(new URL(getUri()+"/rest/browse/Corpora/"+getCorpus()));
			document = corpusDocuments.get(0).asText("b1c0b1bf9462122f5aa52514511e1f8c");
		}
		return document;
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
    	Elements corpusLink = doc.select("a[href=CorpusDocumentsViewer?Corpus="+getCorpus()+"]");
		assertTrue(corpusLink.size() > 0, String.format("no corpus link: %s", getCorpus()));
    }
    
    @Test
    public void corpusDocumentsViewer_IT() throws Exception {
    	Document doc = Jsoup.connect(getURLString("CorpusDocumentsViewer?Corpus="+getCorpus())).get();
    	Elements corpusLink = doc.select("a[href=TextViewer?Corpus="+getCorpus()+"&Document="+getDocument()+"]");
		assertTrue(corpusLink.size() > 0, String.format("no corpus document link: %s %s", getCorpus(), getDocument()));
    }
    
    @Test
    public void OOPStoryArcViewer_IT() throws Exception {
    	Document doc = Jsoup.connect(getURLString("OOPStoryArcViewer", getCorpus(), getDocument())).get();
    	assertTrue(doc.select("#corpusDocumentTitle").size() == 1, String.format("no document title: %s %s", getCorpus(), getDocument()));
    	assertTrue(doc.select("#corpusDocumentAuthor").size() == 1, String.format("no document author: %s %s", getCorpus(), getDocument()));
    	assertTrue(doc.select("#corpusDocumentDate").size() == 1, String.format("no document date: %s %s", getCorpus(), getDocument()));
    }
    
    @Test
    public void OOPPullQuotesViewer_IT() throws Exception {
    	Document doc = Jsoup.connect(getURLString("OOPPullQuotesViewer", getCorpus(), getDocument())).get();
    	assertTrue(doc.select("#corpusDocumentTitle").size() == 1, String.format("no document title: %s %s", getCorpus(), getDocument()));
    	assertTrue(doc.select("#corpusDocumentAuthor").size() == 1, String.format("no document author: %s %s", getCorpus(), getDocument()));
    	assertTrue(doc.select("#corpusDocumentDate").size() == 1, String.format("no document date: %s %s", getCorpus(), getDocument()));
    }
    
    @Test
    public void OOPEditorViewer_IT() throws Exception {
    	Document doc = Jsoup.connect(getURLString("OOPEditorViewer", getCorpus(), getDocument())).get();
    	assertTrue(doc.select("#corpusDocumentTitle").size() == 1, String.format("no document title: %s %s", getCorpus(), getDocument()));
    	assertTrue(doc.select("#corpusDocumentAuthor").size() == 1, String.format("no document author: %s %s", getCorpus(), getDocument()));
    	assertTrue(doc.select("#corpusDocumentDate").size() == 1, String.format("no document date: %s %s", getCorpus(), getDocument()));
    }
    
    @Test
    public void OOPActorsViewer_IT() throws Exception {
    	Document doc = Jsoup.connect(getURLString("OOPActorsViewer", getCorpus(), getDocument())).get();
    	assertTrue(doc.select("#corpusDocumentTitle").size() == 1, String.format("no document title: %s %s", getCorpus(), getDocument()));
    	assertTrue(doc.select("#corpusDocumentAuthor").size() == 1, String.format("no document author: %s %s", getCorpus(), getDocument()));
    	assertTrue(doc.select("#corpusDocumentDate").size() == 1, String.format("no document date: %s %s", getCorpus(), getDocument()));
    }    

    @Test
    public void OOPCloudViewer_IT() throws Exception {
    	Document doc = Jsoup.connect(getURLString("OOPCloudViewer", getCorpus(), getDocument())).get();
    	assertTrue(doc.select("#corpusDocumentTitle").size() == 1, String.format("no document title: %s %s", getCorpus(), getDocument()));
    	assertTrue(doc.select("#corpusDocumentAuthor").size() == 1, String.format("no document author: %s %s", getCorpus(), getDocument()));
    	assertTrue(doc.select("#corpusDocumentDate").size() == 1, String.format("no document date: %s %s", getCorpus(), getDocument()));
    }
    
    @Test
    public void OOPStreamViewer_IT() throws Exception {
    	Document doc = Jsoup.connect(getURLString("OOPStreamViewer", getCorpus(), getDocument())).get();
    	assertTrue(doc.select("#corpusDocumentTitle").size() == 1, String.format("no document title: %s %s", getCorpus(), getDocument()));
    	assertTrue(doc.select("#corpusDocumentAuthor").size() == 1, String.format("no document author: %s %s", getCorpus(), getDocument()));
    	assertTrue(doc.select("#corpusDocumentDate").size() == 1, String.format("no document date: %s %s", getCorpus(), getDocument()));
    }
    
    @Test
    public void OOPStreamViewerTokens_IT() throws Exception {
    	Document doc = Jsoup.connect(getURLString("OOPStreamViewerTokens", getCorpus(), getDocument())).get();
    	assertTrue(doc.select("#corpusDocumentTitle").size() == 1, String.format("no document title: %s %s", getCorpus(), getDocument()));
    	assertTrue(doc.select("#corpusDocumentAuthor").size() == 1, String.format("no document author: %s %s", getCorpus(), getDocument()));
    	assertTrue(doc.select("#corpusDocumentDate").size() == 1, String.format("no document date: %s %s", getCorpus(), getDocument()));
    }
    
    @Test
    public void OOPDocumentViewer_IT() throws Exception {
    	Document doc = Jsoup.connect(getURLString("OOPDocumentViewer", getCorpus(), getDocument())).get();
    	assertTrue(doc.select("#corpusDocumentTitle").size() == 1, String.format("no document title: %s %s", getCorpus(), getDocument()));
    	assertTrue(doc.select("#corpusDocumentAuthor").size() == 1, String.format("no document author: %s %s", getCorpus(), getDocument()));
    	assertTrue(doc.select("#corpusDocumentDate").size() == 1, String.format("no document date: %s %s", getCorpus(), getDocument()));
    }
    
    @Test
    public void StanfordBratViewer_IT() throws Exception {
    	Document doc = Jsoup.connect(getURLString("StanfordBratViewer", getCorpus(), getDocument())).get();
    	assertTrue(doc.select("#corpusDocumentTitle").size() == 1, String.format("no document title: %s %s", getCorpus(), getDocument()));
    	assertTrue(doc.select("#corpusDocumentAuthor").size() == 1, String.format("no document author: %s %s", getCorpus(), getDocument()));
    	assertTrue(doc.select("#corpusDocumentDate").size() == 1, String.format("no document date: %s %s", getCorpus(), getDocument()));
    }    
}
