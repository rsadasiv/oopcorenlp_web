package io.outofprintmagazine.web.storage;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;

public interface IStorage {
	
	public JsonNode listCorpora() throws IOException;
	
	public JsonNode listCorpusDocuments(String corpus) throws IOException;
	
    public String getCorpusDocumentTxtString(String corpus, String document) throws IOException;

    public String getCorpusDocumentOOPString(String corpus, String document) throws IOException;
    
    public JsonNode getCorpusDocumentOOPJson(String corpus, String document) throws IOException;
    
    public String getCorpusDocumentAggregatesString(String corpus, String document) throws IOException;
    
    public JsonNode getCorpusDocumentAggregatesJson(String corpus, String document) throws IOException;
}
