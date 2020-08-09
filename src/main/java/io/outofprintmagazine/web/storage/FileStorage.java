package io.outofprintmagazine.web.storage;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class FileStorage implements IStorage {

	public FileStorage() {
		// TODO Auto-generated constructor stub
	}

	private ObjectMapper mapper = new ObjectMapper();
    
    protected ObjectMapper getMapper() {
    	return mapper;
    }

	protected String getBaseUrl() {
		return "http://localhost:8080/oopcorenlp_web/Corpora";
	}
	
	
	protected String getRealPath() {
		return "C:\\Users\\rsada\\git\\oopcorenlp_web\\WebContent\\Corpora";
	}
	
	private String getRealPath(HttpServletRequest request) {
		return request.getSession().getServletContext().getRealPath("/Corpora");
	}
	
	@Override
	public JsonNode listCorpora() throws IOException {
		File[] directories = new File(getRealPath()).listFiles(File::isDirectory);
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode json = mapper.createObjectNode();
		ArrayNode corporaNode = json.putArray("Corpora");
		for (int i=0;i<directories.length;i++) {
			corporaNode.add(directories[i].getName());
		}
		return json;
	}
	
	public JsonNode listCorpusDocuments(String corpus) throws IOException {
		File[] documents = new File(getRealPath() + "/"+corpus+"/").listFiles(File::isFile);
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode json = mapper.createObjectNode();
		ArrayNode corporaNode = json.putArray("Documents");
		for (int i=0;i<documents.length;i++) {
			if (documents[i].getName().substring(0, documents[i].getName().lastIndexOf(".")).startsWith("OOP_")) {
				corporaNode.add(documents[i].getName().substring(4, documents[i].getName().lastIndexOf(".")));
			}
		}
		return json;
	}

	@Override
    public String getCorpusDocumentTxtString(String corpus, String document) throws IOException {
    	return getCorpusDocumentString(getBaseUrl()+"/"+corpus+"/TXT_"+document+".txt");
    }
    
	@Override
    public String getCorpusDocumentOOPString(String corpus, String document) throws IOException {
    	return getCorpusDocumentString(getBaseUrl()+"/"+corpus+"/OOP_"+document+".json");
    }
    
	@Override
    public JsonNode getCorpusDocumentOOPJson(String corpus, String document) throws IOException {
    	return getCorpusDocumentJson(getBaseUrl()+"/"+corpus+"/OOP_"+document+".json");
    }
    
	@Override
    public String getCorpusDocumentAggregatesString(String corpus, String document) throws IOException {
    	return getCorpusDocumentString(getBaseUrl()+"/"+corpus+"/AGGREGATES_"+document+".json");
    }
    
	@Override
    public JsonNode getCorpusDocumentAggregatesJson(String corpus, String document) throws IOException {
    	return getCorpusDocumentJson(getBaseUrl()+"/"+corpus+"/AGGREGATES_"+document+".json");
    }
    
    protected String getCorpusDocumentString(String url) throws IOException {
    	String responseBody = null;
        CloseableHttpClient httpclient = HttpClients.custom()
                .setServiceUnavailableRetryStrategy(
                		new ServiceUnavailableRetryStrategy() {
                			@Override
                			public boolean retryRequest(
                					final HttpResponse response, final int executionCount, final HttpContext context) {
                					int statusCode = response.getStatusLine().getStatusCode();
                					return (statusCode == 503 || statusCode == 500) && executionCount < 5;
                			}

                			@Override
                			public long getRetryInterval() {
                				return 5;
                			}
                		})
                .setRedirectStrategy(new LaxRedirectStrategy())
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();
        try {

            HttpGet http = new HttpGet(url);
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                @Override
                public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } 
                    else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }
            };
            responseBody = httpclient.execute(http, responseHandler);

        } finally {
            httpclient.close();
        }
        return responseBody;
    }
    
    protected JsonNode getCorpusDocumentJson(String url) throws IOException {
    	JsonNode responseBody = null;
        CloseableHttpClient httpclient = HttpClients.custom()
                .setServiceUnavailableRetryStrategy(
                		new ServiceUnavailableRetryStrategy() {
                			@Override
                			public boolean retryRequest(
                					final HttpResponse response, final int executionCount, final HttpContext context) {
                					int statusCode = response.getStatusLine().getStatusCode();
                					return (statusCode == 503 || statusCode == 500) && executionCount < 5;
                			}

                			@Override
                			public long getRetryInterval() {
                				return 5;
                			}
                		})
                .setRedirectStrategy(new LaxRedirectStrategy())
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();
        try {

            HttpGet http = new HttpGet(url);
            ResponseHandler<JsonNode> responseHandler = new ResponseHandler<JsonNode>() {

                @Override
                public JsonNode handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? getMapper().readTree(entity.getContent()) : null;
                    } 
                    else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }
            };
            responseBody = httpclient.execute(http, responseHandler);

        } finally {
            httpclient.close();
        }
        return responseBody;
    }

}
