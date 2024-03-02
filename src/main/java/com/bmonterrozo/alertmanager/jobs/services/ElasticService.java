package com.bmonterrozo.alertmanager.jobs.services;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.core.InfoResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.bmonterrozo.alertmanager.entity.DataSource;
import com.bmonterrozo.alertmanager.entity.FrecuencyType;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.List;

@Service
public class ElasticService {
    private static final Logger LOG = LoggerFactory.getLogger(ElasticService.class);

    private final String FIELD_TIME = "@timestamp";

    public ElasticsearchClient getElasticClient(DataSource dataSource) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        LOG.debug("getElasticClient - Name: {} - Host: {}- Port: {} -  Protocol: {} ", dataSource.getName(), dataSource.getHost(), dataSource.getPort(), dataSource.getProtocol());

        ElasticsearchClient elasticsearchClient = null;
        RestClient restClient = null;
        ElasticsearchTransport transport = null;

        if (StringUtils.isEmpty(dataSource.getHost())) {
            LOG.debug("getElasticClient - null endpoint - connection property - " + dataSource.getHost());
            return null;
        }

        if (dataSource.getProtocol().equals("https")) {
            final CredentialsProvider credentialsProvider =
                    new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(dataSource.getUsername(), dataSource.getPassword()));

            SSLContext sslContext = SSLContexts.custom()
                    .loadTrustMaterial(null, (chain, authType) -> true)
                    .build();
            restClient = RestClient.builder(new HttpHost(dataSource.getHost(), dataSource.getPort(), dataSource.getProtocol()))
                    .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                        @Override
                        public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                            return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider).
                                    setDefaultHeaders(
                                            List.of(new BasicHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString()))
                                    )
                                    .addInterceptorLast(
                                            (HttpResponseInterceptor)
                                                    (response, context) ->
                                                            response.addHeader("X-Elastic-Product", "Elasticsearch")
                                    ).setSSLContext(sslContext);
                        }
                    })
                    .build();

            transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

            elasticsearchClient = new ElasticsearchClient(transport);
        } else {
            restClient = RestClient.builder(new HttpHost(dataSource.getHost(), dataSource.getPort()))
                    .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                            .setDefaultHeaders(
                                    List.of(new BasicHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString()))
                            )
                            .addInterceptorLast(
                                    (HttpResponseInterceptor)
                                            (response, context) ->
                                                    response.addHeader("X-Elastic-Product", "Elasticsearch")
                            )
                    )
                    .build();
            transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
            elasticsearchClient = new ElasticsearchClient(transport);
        }

        try {
            InfoResponse response = elasticsearchClient.info();
            response.clusterName();

            LOG.debug("getElasticClient - Nombre del cluster: {}", response.clusterName());
        } catch (IOException e) {
            LOG.error("getElasticClient - Error in ElasticSearch connection:  " + e.getMessage());
            e.printStackTrace();
        }
        return elasticsearchClient;
    }

    public SearchResponse<Void> findGroupByDocuments(DataSource dataSource, String search, Integer frequency, FrecuencyType frecuencyType) throws ElasticsearchException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        LOG.debug("findGroupByDocuments - Datasource: {} - Protocol: {}", dataSource.getHost(), dataSource.getProtocol());

        ElasticsearchClient elasticsearchClient = getElasticClient(dataSource);
        SearchResponse<Void> result = null;
        JSONObject searchDetails = null;

        try {
            searchDetails = convertJSON(search);
            LOG.info("findGroupByDocuments - index: {} - search: {} - group_by: {}", searchDetails.get("index"), searchDetails.get("search"), searchDetails.get("group_by"));

            Reader queryJson = QueryBuilder((String) searchDetails.get("search"), frequency, frecuencyType);
            Reader aggregationJson = AggregationBuilder((String) searchDetails.get("group_by"));

            String index = (String) searchDetails.get("index");
            SearchRequest aggRequest = SearchRequest.of(b -> b
                    .index(index)
                    .withJson(queryJson)
                    .withJson(aggregationJson)
                    .ignoreUnavailable(true)
            );

            result = elasticsearchClient
                    .search(aggRequest, Void.class);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private Reader AggregationBuilder(String groupBy) {
        Reader aggregationJson = new StringReader(
                "{" +
                        "  \"aggregations\": {" +
                        "    \"result\": {" +
                        "      \"terms\": {" +
                        "        \"field\": \""+ groupBy +"\"," +
                        "        \"size\": 10," +
                        "        \"min_doc_count\": 1," +
                        "        \"show_term_doc_count_error\": false," +
                        "        \"order\": [" +
                        "          {" +
                        "            \"_count\": \"desc\"" +
                        "          }," +
                        "          {" +
                        "            \"_key\": \"asc\"" +
                        "          }" +
                        "        ]" +
                        "      }" +
                        "    }" +
                        "  }" +
                        "}");
        return aggregationJson;
    }

    private Reader QueryBuilder(String search,Integer frequency, FrecuencyType frecuencyType) {
        String range = formatBegin(String.valueOf(frequency), frecuencyType);
        Reader queryJson = new StringReader(
                "{" +
                        "  \"query\": {" +
                        "    \"bool\": {" +
                        "      \"must\": [" +
                        "        {" +
                        "          \"query_string\": {" +
                        "            \"query\": \""+ search +"\"," +
                        "            \"tie_breaker\": 0" +
                        "          }" +
                        "        }," +
                        "        {" +
                        "          \"range\": {" +
                        "            \""+FIELD_TIME+"\": {"+
                        "              \"from\": \""+range+"\"," +
                        "              \"to\": null" +
                        "            }" +
                        "          }" +
                        "        }" +
                        "      ]" +
                        "    }" +
                        "  }" +
                        "}");
        return queryJson;
    }

    private String formatBegin(String frequency, FrecuencyType frecuencyType) {
        String frequencyV = null;
        switch (frecuencyType) {
            case SECONDS:
                frequencyV = "s";
                break;
            case MINUTES:
                frequencyV = "m";
                break;
            case HOURS:
                frequencyV = "h";
                break;
            case DAYS:
                frequencyV = "d";
                break;
        }
        if (StringUtils.isEmpty(frequency))
            frequency = "1";

        String BEGIN_TEMPLATE = "now-{0}{1}";
        return MessageFormat.format(BEGIN_TEMPLATE, frequency, frequencyV);
    }
    public JSONObject convertJSON(String jsonString) throws ParseException {
        JSONParser parser = new JSONParser();
        Object jsonObj = parser.parse(jsonString);
        JSONObject jsonObject = (JSONObject) jsonObj;
        return jsonObject;
    }
}
