package io.outofprintmagazine.web.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;

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
		File[] directories = new File(
				properties.getProperty("fileCorpus_Path")).listFiles(File::isDirectory);
		for (int i=0;i<directories.length;i++) {
			String corpus = directories[i].getName();
			Map<String, ObjectNode> documentMetadata = new HashMap<String, ObjectNode>();
			corpusDocumentMetadata.put(corpus, documentMetadata);
			initCorpusDocumentMetadata(corpus, documentMetadata);
		}
	}
	
	private void initCorpusDocumentMetadata(String corpus, Map<String, ObjectNode> documentMetadata) throws IOException  {
		File[] documents = new File(
				getCorpusPath(corpus)
				+ System.getProperty("file.separator", "/")	
				+ "Analyze"
		).listFiles(File::isFile);
		for (int i=0;i<documents.length;i++) {
			if (documents[i].getName().substring(0, documents[i].getName().lastIndexOf(".")).startsWith("OOP_")) {
				String document = documents[i].getName().substring(4, documents[i].getName().lastIndexOf("."));
				documentMetadata.put(
						document,
						(ObjectNode) getCorpusDocumentJson(
								corpus,
								"Analyze" + System.getProperty("file.separator", "/") + "OOP_" + document + ".json"
						).get("metadata").deepCopy()
				);
			}
		}	
	}

	protected InputStream getCorpusDocumentStream(String corpus, String scratchFileName) throws IOException  {
        File f = new File(
        		getCorpusFilePath(
        				corpus, 
        				scratchFileName
        		)
        );
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
    public String getCorpusBatchString(String corpus) throws IOException {
		return getCorpusDocumentString(corpus, corpus+"Batch.json");		
	}
    
	@Override
    public JsonNode getCorpusBatchJson(String corpus) throws IOException {
		return getCorpusDocumentJson(corpus, corpus+"Batch.json");		
	}
	
	@Override
    public String getCorpusAggregatesString(String corpus) throws IOException {
		return getCorpusDocumentString(
				corpus, 
				"CorpusAggregate" + System.getProperty("file.separator", "/") + "CORPUS_AGGREGATES.json"
		);
	}
    
	@Override
    public JsonNode getCorpusAggregatesJson(String corpus) throws IOException {
		return getCorpusDocumentJson(
				corpus,
				"CorpusAggregate" + System.getProperty("file.separator", "/") + "CORPUS_AGGREGATES.json"
		);
	}
	
    public String getCorpusAggregatesIdfString(String corpus) throws IOException {
		return getCorpusDocumentString(
				corpus, 
				"CorpusAggregate" + System.getProperty("file.separator", "/") + "CORPUS_AGGREGATES_IDF.json"
		);
	}
    
    public JsonNode getCorpusAggregatesIdfJson(String corpus) throws IOException {
		return getCorpusDocumentJson(
				corpus, 
				"CorpusAggregate" + System.getProperty("file.separator", "/") + "CORPUS_AGGREGATES_IDF.json"
		);		
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
    	return getCorpusDocumentString(
    			corpus,	
				"Analyze" + System.getProperty("file.separator", "/") + "TXT_"+document+".txt"
    	);
    }
    
	@Override
    public String getCorpusDocumentOOPString(String corpus, String document) throws IOException {
    	return getCorpusDocumentString(
    			corpus,
				"Analyze" + System.getProperty("file.separator", "/") + "OOP_"+document+".json"
    	);
    }
    
	@Override
    public JsonNode getCorpusDocumentOOPJson(String corpus, String document) throws IOException {
    	return getCorpusDocumentJson(
    			corpus,
				"Analyze" + System.getProperty("file.separator", "/") + "OOP_"+document+".json"
    	);
    }
    
	@Override
    public String getCorpusDocumentAggregatesString(String corpus, String document) throws IOException {
    	return getCorpusDocumentString(
    			corpus, 
				"Analyze" + System.getProperty("file.separator", "/") + "AGGREGATES_"+document+".json");
    }
    
	@Override
    public JsonNode getCorpusDocumentAggregatesJson(String corpus, String document) throws IOException {
    	return getCorpusDocumentJson(
    			corpus, 
				"Analyze" + System.getProperty("file.separator", "/") + "AGGREGATES_"+document+".json"
		);
    }
    

	@Override
	public String getCorpusDocumentPipelineString(String corpus, String document) throws IOException {
    	return getCorpusDocumentString(
    			corpus,
				"Analyze" + System.getProperty("file.separator", "/") + "PIPELINE_"+document+".json"
    	);
	}

	@Override
	public JsonNode getCorpusDocumentPipelineJson(String corpus, String document) throws IOException {
    	return getCorpusDocumentJson(
				corpus,
				"Analyze" + System.getProperty("file.separator", "/") + "PIPELINE_"+document+".json"
		);
	}

	@Override
	public String getCorpusDocumentStanfordString(String corpus, String document) throws IOException {
    	return getCorpusDocumentString(
				corpus,
				"Analyze" + System.getProperty("file.separator", "/") + "STANFORD_"+document+".json"
		);
	}

	@Override
	public JsonNode getCorpusDocumentStanfordJson(String corpus, String document) throws IOException {
    	return getCorpusDocumentJson(
				corpus,
				"Analyze" + System.getProperty("file.separator", "/") + "STANFORD_"+document+".json"
		);
	}
	
	@Override
    public ObjectNode getCorpusDocumentOOPMetadata(String corpus, String document) throws IOException {
		return corpusDocumentMetadata.get(corpus).get(document);
	}
	
	@Override
    public String getCorpusDocumentTfidfString(String corpus, String document) throws IOException {
    	return getCorpusDocumentString(
    			corpus, 
				"CoreNLPTfidf" + System.getProperty("file.separator", "/") + "TfidfScores_"+document+".json"
		);
	}
    
	@Override
    public JsonNode getCorpusDocumentTfidfJson(String corpus, String document) throws IOException {
    	return getCorpusDocumentJson(
    			corpus, 
				"CoreNLPTfidf" + System.getProperty("file.separator", "/") + "TfidfScores_"+document+".json"
		);
	}

	@Override
    public String getCorpusDocumentZString(String corpus, String document) throws IOException {
    	return getCorpusDocumentString(
    			corpus, 
				"CoreNLPZ" + System.getProperty("file.separator", "/") + "ZScores_"+document+".json"
		);
	}
	
	@Override
    public JsonNode getCorpusDocumentZJson(String corpus, String document) throws IOException {
    	return getCorpusDocumentJson(
    			corpus, 
				"CoreNLPZ" + System.getProperty("file.separator", "/") + "ZScores_"+document+".json");
	}
	
	@Override
    public String getCorpusAggregatesMBString(String corpus) throws IOException {
    	return getCorpusDocumentString(
    			corpus,
				"CorpusAggregate" + System.getProperty("file.separator", "/") + "CORPUS_AGGREGATES_MB.json"
    	);
    }
    
	@Override
    public JsonNode getCorpusAggregatesMBJson(String corpus) throws IOException {
    	return getCorpusDocumentJson(
    			corpus,
				"CorpusAggregate" + System.getProperty("file.separator", "/") + "CORPUS_AGGREGATES_MB.json"
    	);
    }
	
	@Override
    public JsonNode getCorpusDocumentTopicModelLemma(String corpus, String document, String lemma) throws IOException {
		ArrayNode retval = getMapper().createArrayNode();
		try {
			Word2Vec vec = WordVectorSerializer.readWord2VecModel(
					getCorpusFilePath(corpus, "Word2Vec/Lemmas_"+document+".word2vec")
			);
			
			for (String similarWord : vec.wordsNearest(lemma, 25)) {
				double cosSim = vec.similarity(lemma, similarWord);
				ObjectNode tidy = getMapper().createObjectNode();
				tidy.put("name", similarWord);
				tidy.put("value", cosSim);
				retval.add(tidy);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return retval;
    }
    
    
	@Override
    public JsonNode getCorpusDocumentTopicModelLemmaPOS(String corpus, String document, String lemma) throws IOException {
		ObjectNode retval = getMapper().createObjectNode();
		try {
			Word2Vec vec = WordVectorSerializer.readWord2VecModel(
					getCorpusFilePath(corpus, "Word2Vec/Lemmas_POS_"+document+".word2vec")
			);
			for (VocabWord vocabWord : vec.getVocab().vocabWords()) {
				if (vocabWord.getWord().startsWith(lemma)) {
					ArrayNode similarity = retval.putArray(vocabWord.getWord());
					for (String similarWord : vec.wordsNearest(vocabWord.getWord(), 25)) {
						double cosSim = vec.similarity(vocabWord.getWord(), similarWord);
						ObjectNode tidy = getMapper().createObjectNode();
						tidy.put("name", similarWord);
						tidy.put("value", cosSim);
						similarity.add(tidy);
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return retval;
    }
	
	private void setAttributeAvg(ObjectNode retval, List<BigDecimal> scores, String annotationName) {
		if (scores.size() > 0) {
			BigDecimal total = new BigDecimal(0);
			BigDecimal count = new BigDecimal(scores.size());
			for (BigDecimal score : scores) {
				total = total.add(score);
			}
			((ObjectNode)retval.get("attributes")).put(
					annotationName, 
					total.divide(count, 10, RoundingMode.HALF_DOWN)
			);
		}
	}
	
	private void setAttributeListBigDecimal(ObjectNode retval, List<BigDecimal> scores, String annotationName) {
		if (scores.size() > 0) {
			ArrayNode list = ((ObjectNode)retval.get("lists")).putArray(annotationName);
			for (BigDecimal score : scores) {
				list.add(score);
			}
		}
	}
	
	private void setAttributeListString(ObjectNode retval, List<String> scores, String annotationName) {
		if (scores.size() > 0) {
			ArrayNode list = ((ObjectNode)retval.get("lists")).putArray(annotationName);
			for (String score : scores) {
				list.add(score);
			}
		}
	}
	
	private void setAttributeSubAnnotation(ObjectNode retval, ObjectNode tokenNode, String subAnnotationName) {
		ObjectNode annotationNode = getMapper().createObjectNode();
		if (tokenNode.has(subAnnotationName)) {
			if (retval.get("attributes").has(subAnnotationName)) {
				annotationNode = (ObjectNode) retval.get("attributes").get(subAnnotationName);
			}
			else {
				annotationNode = ((ObjectNode)retval.get("attributes")).putObject(subAnnotationName);
			}
			BigDecimal val = new BigDecimal(1);
			if (annotationNode.has(tokenNode.get(subAnnotationName).asText())) {
				val = new BigDecimal(annotationNode.get(tokenNode.get(subAnnotationName).asText()).asText()).add(val);
			}
			annotationNode.put(tokenNode.get(subAnnotationName).asText(), val);
		}
	}
	
	private void setAttributeAnnotation(ObjectNode retval, ObjectNode tokenNode, String annotation) {
		ObjectNode annotationNode = getMapper().createObjectNode();
		if (tokenNode.has(annotation)) {
			if (tokenNode.get(annotation).isObject()) {
				Iterator<String> fieldNameIter = ((ObjectNode)tokenNode.get(annotation)).fieldNames();
				while (fieldNameIter.hasNext()) {
					String subAnnotationName = fieldNameIter.next();
					if (tokenNode.get(annotation).has(subAnnotationName)) {
						BigDecimal val = new BigDecimal(tokenNode.get(annotation).get(subAnnotationName).asText());
						if (retval.get("attributes").has(annotation)) {
							annotationNode = (ObjectNode) retval.get("attributes").get(annotation);
						}
						else {
							annotationNode = ((ObjectNode)retval.get("attributes")).putObject(annotation);
						}
						if (annotationNode.has(subAnnotationName)) {
							val = new BigDecimal(annotationNode.get(subAnnotationName).asText()).add(val);
						}
						annotationNode.put(subAnnotationName, val);
					}
				}
			}
			else {
				System.out.println(annotation);
			}
		}
	}
	
	private void setAttributeList(ObjectNode retval, ObjectNode tokenNode, String annotation) {
		ArrayNode annotationNode = getMapper().createArrayNode();

		if (tokenNode.has(annotation)) {
			if (retval.get("attributes").has(annotation)) {
				annotationNode = (ArrayNode) retval.get("attributes").get(annotation);
			}
			else {
				annotationNode = ((ObjectNode)retval.get("attributes")).putArray(annotation);
			}
			boolean annotationInList = false;
			Iterator<JsonNode> attributeListIter = annotationNode.iterator();
			while (attributeListIter.hasNext()) {
				if (attributeListIter.next().asText().equals(tokenNode.get(annotation).asText())) {
					annotationInList = true;
					break;
				}
			}
			if (!annotationInList) {
				annotationNode.add(tokenNode.get(annotation).asText());
			}
		}
	}
	
	@Override
    public JsonNode getCorpusDocumentLexiconLemma(String corpus, String document, String lemma) throws IOException {
		ObjectNode retval = null;
		List<BigDecimal> oopSyllableCountAnnotations = new ArrayList<BigDecimal>();
		List<BigDecimal> vaderSentimentAnnotations = new ArrayList<BigDecimal>();
		List<BigDecimal> coreNlpSentimentAnnotations = new ArrayList<BigDecimal>();
		List<BigDecimal> oopWordCountAnnotations = new ArrayList<BigDecimal>();
		List<BigDecimal> oopFleschKinkaidAnnotations = new ArrayList<BigDecimal>();
		List<BigDecimal> paragraphIndexAnnotations = new ArrayList<BigDecimal>();
		List<BigDecimal> sentenceIndexAnnotations = new ArrayList<BigDecimal>();
		List<String> sentenceTextAnnotations = new ArrayList<String>();	
		JsonNode scores = getCorpusDocumentOOPJson(corpus, document);
		Iterator<JsonNode> sentenceIterator = scores.get("sentences").elements();
		for (int sentenceIdx=0;sentenceIterator.hasNext();sentenceIdx++) {
			JsonNode sentenceNode = sentenceIterator.next();
			Iterator<JsonNode> tokenIterator = sentenceNode.get("tokens").elements();
			boolean lemmaInSentence = false;
			while (tokenIterator.hasNext()) {
				ObjectNode tokenNode = (ObjectNode) tokenIterator.next();
				ObjectNode tokensAnnotation = (ObjectNode) tokenNode.get("TokensAnnotation");
				if (tokensAnnotation.get("lemma").asText().equals(lemma)) {
					lemmaInSentence = true;
					if (retval == null) {
						retval = getMapper().createObjectNode();
						retval.put("canonicalName", lemma);
						retval.put("firstAppearance", sentenceIdx);
						retval.put("lastAppearance", sentenceIdx);
						retval.put("importance", new BigDecimal(0));
						retval.putObject("attributes");
						retval.putObject("lists");
					}
					retval.put("lastAppearance", sentenceIdx);
					retval.put("importance", retval.get("importance").asInt()+1);
					//attributes
					setAttributeSubAnnotation(retval, tokensAnnotation, "word");
					setAttributeSubAnnotation(retval, tokensAnnotation, "pos");
					setAttributeSubAnnotation(retval, tokensAnnotation, "ner");
					
					setAttributeAnnotation(retval, tokenNode, "CoreNlpGenderAnnotation");
					setAttributeAnnotation(retval, tokenNode, "OOPAmericanizeAnnotation");
					setAttributeAnnotation(retval, tokenNode, "OOPAngliciseAnnotation");
					setAttributeAnnotation(retval, tokenNode, "OOPBiberAnnotation");
					setAttributeAnnotation(retval, tokenNode, "OOPBiberDimensionsAnnotation");
					setAttributeAnnotation(retval, tokenNode, "OOPColorsAnnotation");
					setAttributeAnnotation(retval, tokenNode, "OOPCommonWordsAnnotation");
					setAttributeAnnotation(retval, tokenNode, "OOPFlavorsAnnotation");
					setAttributeAnnotation(retval, tokenNode, "OOPFunctionWordsAnnotation");
					setAttributeAnnotation(retval, tokenNode, "OOPGenderAnnotation");
					setAttributeAnnotation(retval, tokenNode, "OOPNonAffirmativeAnnotation");
					setAttributeAnnotation(retval, tokenNode, "OOPNounGroupsAnnotation");
					setAttributeAnnotation(retval, tokenNode, "OOPNounNypernymsAnnotation");
					setAttributeAnnotation(retval, tokenNode, "OOPPointlessAdjectivesAnnotation");
					setAttributeAnnotation(retval, tokenNode, "OOPPointlessAdverbsAnnotation");
					setAttributeAnnotation(retval, tokenNode, "OOPSVOAnnotation");
					if (tokenNode.has("OOPSyllableCountAnnotation")) {
						oopSyllableCountAnnotations.add(new BigDecimal(tokenNode.get("OOPSyllableCountAnnotation").asText()));
					}
					setAttributeAnnotation(retval, tokenNode, "OOPTemporalNGramsAnnotation");
					setAttributeAnnotation(retval, tokenNode, "OOPUncommonWordsAnnotation");
					setAttributeAnnotation(retval, tokenNode, "OOPVerbGroupsAnnotation");
					setAttributeAnnotation(retval, tokenNode, "OOPVerbHypernymsAnnotation");
					setAttributeAnnotation(retval, tokenNode, "OOPVerbnetGroupsAnnotation");
					setAttributeAnnotation(retval, tokenNode, "OOPWordlessWordsAnnotation");
					
					setAttributeList(retval, tokenNode, "OOPWikipediaGlossAnnotation");
					setAttributeList(retval, tokenNode, "OOPWordnetGlossAnnotation");
				}	
			}
			if (lemmaInSentence) {
				if (sentenceNode.has("VaderSentimentAnnotation")) {
					vaderSentimentAnnotations.add(new BigDecimal(sentenceNode.get("VaderSentimentAnnotation").asText()));
				}
				if (sentenceNode.has("CoreNlpSentimentAnnotation")) {
					coreNlpSentimentAnnotations.add(new BigDecimal(sentenceNode.get("CoreNlpSentimentAnnotation").asText()));
				}
				if (sentenceNode.has("OOPWordCountAnnotation")) {
					oopWordCountAnnotations.add(new BigDecimal(sentenceNode.get("OOPWordCountAnnotation").asText()));
				}
				if (sentenceNode.has("OOPFleschKinkaidAnnotation")) {
					oopFleschKinkaidAnnotations.add(new BigDecimal(sentenceNode.get("OOPFleschKinkaidAnnotation").asText()));
				}
				if (sentenceNode.has("text")) {
					sentenceTextAnnotations.add(sentenceNode.get("text").asText());
				}
				if (sentenceNode.has("SentenceIndexAnnotation")) {
					sentenceIndexAnnotations.add(new BigDecimal(sentenceNode.get("SentenceIndexAnnotation").asText()));
				}
				if (sentenceNode.has("ParagraphIndexAnnotation")) {
					paragraphIndexAnnotations.add(new BigDecimal(sentenceNode.get("ParagraphIndexAnnotation").asText()));
				}				
				if (sentenceNode.has("OOPPunctuationMarkAnnotation")) {
					setAttributeAnnotation(retval, (ObjectNode)sentenceNode, "OOPPunctuationMarkAnnotation");
				}
			}
		}
		setAttributeAvg(retval, oopSyllableCountAnnotations, "OOPSyllableCountAnnotation");
		setAttributeAvg(retval, vaderSentimentAnnotations, "VaderSentimentAnnotation");
		setAttributeAvg(retval, coreNlpSentimentAnnotations, "CoreNlpSentimentAnnotation");
		setAttributeAvg(retval, oopWordCountAnnotations, "OOPWordCountAnnotation");
		setAttributeAvg(retval, oopFleschKinkaidAnnotations, "OOPFleschKinkaidAnnotation");
		setAttributeListBigDecimal(retval, paragraphIndexAnnotations, "ParagraphIndexAnnotation");
		setAttributeListBigDecimal(retval, sentenceIndexAnnotations, "SentenceIndexAnnotation");
		setAttributeListString(retval, sentenceTextAnnotations, "SentenceTextAnnotation");		
		
		return retval;
	}
}
