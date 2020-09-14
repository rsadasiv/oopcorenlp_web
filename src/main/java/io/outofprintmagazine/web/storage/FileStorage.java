package io.outofprintmagazine.web.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


public class FileStorage implements IStorage {
	
	private static final Logger logger = LogManager.getLogger(FileStorage.class);
	
	@SuppressWarnings("unused")
	private Logger getLogger() {
		return logger;
	}
	
	private Properties properties = new Properties();
	private ObjectMapper mapper = new ObjectMapper();
    
    protected ObjectMapper getMapper() {
    	return mapper;
    }
	
	private static Map<Properties, FileStorage> instances = new HashMap<Properties, FileStorage>();
	private Map<String, Map<String, JsonNode>> corpusDocumentMetadata = new HashMap<String, Map<String, JsonNode>>();

	private FileStorage() {
		super();
	}
	
	private FileStorage(Properties p) throws IOException {
		this();
		this.properties = p;
		this.initCorpora();
		
	}
	
    public static FileStorage getInstance(Properties p) throws IOException { 
        if (instances.get(p) == null) {
        	FileStorage instance = new FileStorage(p);
            instances.put(p, instance);
        }
        return instances.get(p); 
    }
    
	protected String getCorpusPath(String corpus) throws IOException {
		String path = (
				properties.getProperty("fileCorpus_Path")
				+ System.getProperty("file.separator", "/")	
				+ corpus
		);
		File dir = new File(path);
		if (!dir.exists()) dir.mkdirs();
		return path;
		
	}

	protected String getCorpusFilePath(String corpus, String fileName) throws IOException {
		String path = (
				getCorpusPath(corpus) 
				+ System.getProperty("file.separator", "/") 
				+ fileName
		);
		File file = new File(path);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		return path;
	}
	
	private void initCorpora() throws IOException {
		File[] directories = new File(properties.getProperty("fileCorpus_Path")).listFiles(File::isDirectory);
		for (int i=0;i<directories.length;i++) {
			String corpus = directories[i].getName();
			Map<String, JsonNode> documentMetadata = new HashMap<String, JsonNode>();
			corpusDocumentMetadata.put(corpus, documentMetadata);
			initCorpusDocumentMetadata(corpus, documentMetadata);
		}
	}
	
	private void initCorpusDocumentMetadata(String corpus, Map<String, JsonNode> documentMetadata) throws IOException  {
		File[] documents = new File(getCorpusPath(corpus)).listFiles(File::isFile);
		for (int i=0;i<documents.length;i++) {
			if (documents[i].getName().substring(0, documents[i].getName().lastIndexOf(".")).startsWith("OOP_")) {
				String document = documents[i].getName().substring(4, documents[i].getName().lastIndexOf("."));
				documentMetadata.put(
						document,
						getCorpusDocumentJson(
								corpus,
								"OOP_" + document + ".json"
						).get("metadata").deepCopy()
				);
			}
		}	
	}

	protected InputStream getCorpusDocumentStream(String corpus, String scratchFileName) throws IOException  {
        File f = new File(getCorpusFilePath(corpus, scratchFileName));
        FileInputStream fin = new FileInputStream(f);
        return fin;
	}
	

	protected String getCorpusDocumentString(String corpus, String scratchFileName) throws IOException {	
	    return IOUtils.toString(
	    		getCorpusDocumentStream(corpus, scratchFileName),
	    		StandardCharsets.UTF_8.name()
	    );
	}
	
	protected JsonNode getCorpusDocumentJson(String corpus, String scratchFileName) throws IOException {
		return getMapper().readTree(
				getCorpusDocumentStream(
						corpus,
						scratchFileName
				)
		);
	}
	
	@Override
	public JsonNode listCorpora() throws IOException {
		List<String> sortedCorpora = new ArrayList<String>();
		sortedCorpora.addAll(corpusDocumentMetadata.keySet());
		Collections.sort(sortedCorpora);
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode json = mapper.createObjectNode();
		ArrayNode corporaNode = json.putArray("Corpora");
		for (String corpus : sortedCorpora) {
			corporaNode.add(corpus);
		}
		return json;
	}
	
	@Override
	public JsonNode listCorpusDocuments(String corpus) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode json = mapper.createObjectNode();
		ArrayNode corporaNode = json.putArray("Documents");
		Map<String, JsonNode> documentMetadata = corpusDocumentMetadata.get(corpus);
		List<String> sortedDocuments = new ArrayList<String>();
		sortedDocuments.addAll(documentMetadata.keySet());
		Collections.sort(sortedDocuments);
		for (String document : sortedDocuments) {
			corporaNode.add(document);
		}		
		return json;
	}
	
	@Override
    public JsonNode getCorpusDocumentOOPMetadata(String corpus, String document) throws IOException {
		return corpusDocumentMetadata.get(corpus).get(document);
	}
	
	

	@Override
    public String getCorpusDocumentTxtString(String corpus, String document) throws IOException {
    	return getCorpusDocumentString(corpus, "/TXT_"+document+".txt");
    }
    
	@Override
    public String getCorpusDocumentOOPString(String corpus, String document) throws IOException {
    	return getCorpusDocumentString(corpus, "/OOP_"+document+".json");
    }
    
	@Override
    public JsonNode getCorpusDocumentOOPJson(String corpus, String document) throws IOException {
    	return getCorpusDocumentJson(corpus, "/OOP_"+document+".json");
    }
    
	@Override
    public String getCorpusDocumentAggregatesString(String corpus, String document) throws IOException {
    	return getCorpusDocumentString(corpus, "/AGGREGATES_"+document+".json");
    }
    
	@Override
    public JsonNode getCorpusDocumentAggregatesJson(String corpus, String document) throws IOException {
    	return getCorpusDocumentJson(corpus, "/AGGREGATES_"+document+".json");
    }
    

	@Override
	public String getCorpusDocumentPipelineString(String corpus, String document) throws IOException {
    	return getCorpusDocumentString(corpus, "/PIPELINE_"+document+".json");
	}

	@Override
	public JsonNode getCorpusDocumentPipelineJson(String corpus, String document) throws IOException {
    	return getCorpusDocumentJson(corpus, "/PIPELINE_"+document+".json");
	}

	@Override
	public String getCorpusDocumentStanfordString(String corpus, String document) throws IOException {
    	return getCorpusDocumentString(corpus, "/STANFORD_"+document+".json");
	}

	@Override
	public JsonNode getCorpusDocumentStanfordJson(String corpus, String document) throws IOException {
    	return getCorpusDocumentJson(corpus, "/STANFORD_"+document+".json");
	}

}
