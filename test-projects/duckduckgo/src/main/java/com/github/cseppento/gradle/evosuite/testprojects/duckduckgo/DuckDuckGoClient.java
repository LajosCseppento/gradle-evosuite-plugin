package com.github.cseppento.gradle.evosuite.testprojects.duckduckgo;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

/**
 * Simple client for the Duck Duck Go search engine. Hard to test since the {@link org.apache.http.client.HttpClient}
 * cannot be mocked easily.
 */
public class DuckDuckGoClient {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final String baseUrl;
    private final ObjectMapper jsonMapper;

    public DuckDuckGoClient() {
        this.baseUrl = "http://api.duckduckgo.com";
        this.jsonMapper = new ObjectMapper();
        this.jsonMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public DuckDuckGoAnswer search(String term) throws DuckDuckGoClientException {
        HttpClientBuilder builder = HttpClientBuilder.create();
        try (CloseableHttpClient client = builder.build()) {
            // http://api.duckduckgo.com/?q=DuckDuckGo&format=json&pretty=1
            URI uri = new URIBuilder(baseUrl)
                    .addParameter("q", term)
                    .addParameter("format", "json")
                    .addParameter("pretty", "1")
                    .build();
            logger.info("Sending GET to {}", uri);

            HttpGet get = new HttpGet(uri);

            HttpResponse response = client.execute(get);

            logger.info("Response code: {}", response.getStatusLine().getStatusCode());
            if (logger.isDebugEnabled()) {
                for (Header h : response.getAllHeaders()) {
                    logger.debug("Header: {}", h);
                }
            }

            return jsonMapper.readValue(response.getEntity().getContent(), DuckDuckGoAnswer.class);
        } catch (Exception ex) {
            logger.info("Search failure", ex);
            throw new DuckDuckGoClientException("Search for '" + term + "' has failed", ex);
        }
    }
}
