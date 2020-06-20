package io.outofprintmagazine.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import javax.servlet.http.HttpServlet;
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

public abstract class AbstractOOPServlet extends HttpServlet {
    
	private static final long serialVersionUID = 1L;
	
    public AbstractOOPServlet() {
        super();
    }
    
    private ObjectMapper mapper = new ObjectMapper();
    
    protected ObjectMapper getMapper() {
    	return mapper;
    }

	protected String getBaseUrl() {
    	return "http://localhost:8080/oopcorenlp_web/Corpora";
    }
    
    protected String getCorpusDocumentTxtString(String corpus, String document) throws IOException {
    	return getCorpusDocumentString(getBaseUrl()+"/"+corpus+"/TXT_"+document+".txt");
    }
    
    protected String getCorpusDocumentOOPString(String corpus, String document) throws IOException {
    	return getCorpusDocumentString(getBaseUrl()+"/"+corpus+"/OOP_"+document+".json");
    }
    
    protected JsonNode getCorpusDocumentOOPJson(String corpus, String document) throws IOException {
    	return getCorpusDocumentJson(getBaseUrl()+"/"+corpus+"/OOP_"+document+".json");
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
    
    protected String plainTextToHtml(String input) throws IOException {
	    StringBuilder contentBuilder = new StringBuilder();
	    String sCurrentLine;
	    BufferedReader br = null;
	    try {
	    	br = new BufferedReader(
	            	new StringReader(
	            			input
	            	)
	        );
	        while ((sCurrentLine = br.readLine()) != null) {
	        	if (sCurrentLine.equals("")) {
	        		sCurrentLine = "&nbsp;";
	        	}
	        	contentBuilder.append("<p>").append(sCurrentLine).append("</p>").append("\n");
	        }
	    }
	    finally {
	    	if (br != null) {
	    		br.close();
	    	}
	    }
        return contentBuilder.toString();
    }
    
    public void setMetadataAttributes(HttpServletRequest request, String corpus, String document) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode stats = mapper.readTree(
        		new BufferedReader(
        				new StringReader(
        						getCorpusDocumentOOPString(corpus, document)
        				)
        		)
        );
        request.setAttribute("Author", stats.get("metadata").get("AuthorAnnotation").asText());
        request.setAttribute("Date", stats.get("metadata").get("DocDateAnnotation").asText());
        request.setAttribute("Title", stats.get("metadata").get("DocTitleAnnotation").asText());    	
    }

}
