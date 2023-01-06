package com.canabrix.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.indices.ElasticsearchIndicesClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.TransportUtils;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CanabrixElasticSearchService {
    private static final Logger logger = LoggerFactory.getLogger(CanabrixElasticSearchService.class);

    private static final String FINGERPRINT;
    private static final String LOGIN;
    private static final String PASSWORD;
    private static final String HOST;
    private static final int PORT;
    static {
        FINGERPRINT = System.getenv("ELASTIC_SEARCH_CERT_FINGERPRINT");
        LOGIN = System.getenv("ELASTIC_SEARCH_LOGIN");
        PASSWORD = System.getenv("ELASTIC_SEARCH_PASSWORD");
        HOST = System.getenv("ELASTIC_SEARCH_ADDRESS");
        PORT = Integer.parseInt(System.getenv("ELASTIC_SEARCH_PORT"));
    }

    private static ElasticsearchClient client;

    public CanabrixElasticSearchService() {

        SSLContext sslContext = TransportUtils
                .sslContextFromCaFingerprint(FINGERPRINT);

        BasicCredentialsProvider credsProv = new BasicCredentialsProvider();
        credsProv.setCredentials(
                AuthScope.ANY, new UsernamePasswordCredentials(LOGIN, PASSWORD)
        );

        RestClient restClient = RestClient
                .builder(new HttpHost(HOST, PORT, "https"))
                .setHttpClientConfigCallback(hc -> hc
                        .setSSLContext(sslContext)
                        .setDefaultCredentialsProvider(credsProv)
                )
                .build();

        // Create the transport and the API client
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        this.client = new ElasticsearchClient(transport);

        logger.info("Elastic Search client instantiated.");

    }

    public List<String> getIndicesInfo() throws IOException {
        List<String> keys = new ArrayList<>();
        ElasticsearchIndicesClient indices = client.indices();
        indices.getSettings().result().keySet().forEach(
                k -> {
                    logger.info("Key: "+ k);
                    keys.add(k);
                }
        );
        return keys;
    }

    public void insertDocument(
            String index,
            String documentId,
            Object documentToInsert
    ) throws IOException {
        IndexResponse response = client.index(
            i -> i
                .index(index)
                .id(documentId)
                .document(documentToInsert)
        );
        logger.info("Indexed " + documentId + " with version: " + response.version() +
                " : Response: " + response.result().toString()
        );
    }

    public static ElasticsearchClient getClient() {
        return client;
    }
}

