package io.outofprintmagazine.web.storage;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public interface IStorage {
		
	public ArrayNode listCorpora() throws IOException;

	public ArrayNode listCorpusDocuments(String corpus) throws IOException;
	
    public String getCorpusAggregatesString(String corpus) throws IOException;
    
    public JsonNode getCorpusAggregatesJson(String corpus) throws IOException;
    
    public String getCorpusAggregatesIdfString(String corpus) throws IOException;
    
    public JsonNode getCorpusAggregatesIdfJson(String corpus) throws IOException;
	
    public String getCorpusDocumentTxtString(String corpus, String document) throws IOException;

    public String getCorpusDocumentOOPString(String corpus, String document) throws IOException;
    
    public JsonNode getCorpusDocumentOOPJson(String corpus, String document) throws IOException;
    
    public String getCorpusDocumentAggregatesString(String corpus, String document) throws IOException;
    
    public JsonNode getCorpusDocumentAggregatesJson(String corpus, String document) throws IOException;
    
    public String getCorpusDocumentPipelineString(String corpus, String document) throws IOException;
    
    public JsonNode getCorpusDocumentPipelineJson(String corpus, String document) throws IOException;
    
    public String getCorpusDocumentStanfordString(String corpus, String document) throws IOException;
    
    public JsonNode getCorpusDocumentStanfordJson(String corpus, String document) throws IOException;
    
    public ObjectNode getCorpusDocumentOOPMetadata(String corpus, String document) throws IOException;
    
    public String getCorpusDocumentTfidfString(String corpus, String document) throws IOException;
    
    public JsonNode getCorpusDocumentTfidfJson(String corpus, String document) throws IOException;
    
    public String getCorpusDocumentZString(String corpus, String document) throws IOException;
    
    public JsonNode getCorpusDocumentZJson(String corpus, String document) throws IOException;
    
    public String getCorpusAggregatesMBString(String corpus) throws IOException;
    
    public JsonNode getCorpusAggregatesMBJson(String corpus) throws IOException;
    
    public JsonNode getCorpusDocumentTopicModelLemma(String corpus, String document, String lemma) throws IOException;
    
    public JsonNode getCorpusDocumentTopicModelLemmaPOS(String corpus, String document, String lemma) throws IOException;
    
    public JsonNode getCorpusDocumentLexiconLemma(String corpus, String document, String lemma) throws IOException;
  
}
