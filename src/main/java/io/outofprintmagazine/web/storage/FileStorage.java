package io.outofprintmagazine.web.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;

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
	private Map<String, Map<String, ObjectNode>> corpusDocumentMetadata = new HashMap<String, Map<String, ObjectNode>>();

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
			Map<String, ObjectNode> documentMetadata = new HashMap<String, ObjectNode>();
			corpusDocumentMetadata.put(corpus, documentMetadata);
			initCorpusDocumentMetadata(corpus, documentMetadata);
		}
	}
	
	private void initCorpusDocumentMetadata(String corpus, Map<String, ObjectNode> documentMetadata) throws IOException  {
		File[] documents = new File(getCorpusPath(corpus)).listFiles(File::isFile);
		for (int i=0;i<documents.length;i++) {
			if (documents[i].getName().substring(0, documents[i].getName().lastIndexOf(".")).startsWith("OOP_")) {
				String document = documents[i].getName().substring(4, documents[i].getName().lastIndexOf("."));
				documentMetadata.put(
						document,
						(ObjectNode) getCorpusDocumentJson(
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
	public ArrayNode listCorpora() throws IOException {
		ArrayNode retval = getMapper().createArrayNode();
		SortedSet<String> sortedCorpora = new TreeSet<String>();
		sortedCorpora.addAll(corpusDocumentMetadata.keySet());

		for (String corpus : sortedCorpora) {
			retval.add(corpus);
		}
		return retval;
	}
	
	@Override
    public String getCorpusAggregatesString(String corpus) throws IOException {
		return getCorpusDocumentString(corpus, "CORPUS_AGGREGATES.json");
	}
    
	@Override
    public JsonNode getCorpusAggregatesJson(String corpus) throws IOException {
		return getCorpusDocumentJson(corpus, "CORPUS_AGGREGATES.json");		
	}
	
    public String getCorpusAggregatesIdfString(String corpus) throws IOException {
		return getCorpusDocumentString(corpus, "CORPUS_AGGREGATES_IDF.json");
	}
    
    public JsonNode getCorpusAggregatesIdfJson(String corpus) throws IOException {
		return getCorpusDocumentJson(corpus, "CORPUS_AGGREGATES_IDF.json");		
	}
	
	@Override
	public ArrayNode listCorpusDocuments(String corpus) throws IOException {
		ArrayNode retval = getMapper().createArrayNode();
		Map<String, ObjectNode> documentMetadata = corpusDocumentMetadata.get(corpus);
		SortedSet<String> sortedDocuments = new TreeSet<String>();
		sortedDocuments.addAll(documentMetadata.keySet());

		for (String document : sortedDocuments) {
			retval.add(document);
		}		
		return retval;
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
	
	@Override
    public ObjectNode getCorpusDocumentOOPMetadata(String corpus, String document) throws IOException {
		return corpusDocumentMetadata.get(corpus).get(document);
	}
	
	@Override
    public String getCorpusDocumentTfidfString(String corpus, String document) throws IOException {
    	return getCorpusDocumentString(corpus, "/CoreNLPTfidf/TfidfScores_"+document+".json");
	}
    
	@Override
    public JsonNode getCorpusDocumentTfidfJson(String corpus, String document) throws IOException {
    	return getCorpusDocumentJson(corpus, "/CoreNLPTfidf/TfidfScores_"+document+".json");
	}

	@Override
    public String getCorpusDocumentZString(String corpus, String document) throws IOException {
    	return getCorpusDocumentString(corpus, "CoreNLPZ/ZScores_"+document+".json");
	}
	
	@Override
    public JsonNode getCorpusDocumentZJson(String corpus, String document) throws IOException {
    	return getCorpusDocumentJson(corpus, "CoreNLPZ/ZScores_"+document+".json");
	}
	
	@Override
    public String getCorpusAggregatesMBString(String corpus) throws IOException {
    	return getCorpusDocumentString(corpus, "/CORPUS_AGGREGATES_MB.json");
    }
    
	@Override
    public JsonNode getCorpusAggregatesMBJson(String corpus) throws IOException {
    	return getCorpusDocumentJson(corpus, "/CORPUS_AGGREGATES_MB.json");
    }
}
