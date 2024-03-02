package com.bmonterrozo.alertmanager.jobs.services;

import com.bmonterrozo.alertmanager.entity.DataSource;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class RabbitMQService {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitMQService.class);

    public Map<String, Integer> getAllQueues(DataSource dataSource) throws IOException, ParseException {
        HttpClient client = HttpClients.createDefault();
        JSONParser parser = new JSONParser();
        String baseUrl = dataSource.getProtocol()  + "://" + dataSource.getHost() + ":" +dataSource.getPort() +"/api";
        String username = dataSource.getUsername();
        String password = dataSource.getPassword();

        LOG.debug("RabbitMQService - getAllQueues - by: {}", baseUrl);
        Map<String, Integer> queues = new HashMap<>();

        HttpGet get = new HttpGet(baseUrl + "/queues");
        get.addHeader("Authorization", "Basic " + encode(username + ":" + password));
        HttpResponse response = client.execute(get);
        String content = EntityUtils.toString(response.getEntity());
        JSONArray array = (JSONArray) parser.parse(content);

        for (Object element : array) {
            JSONObject object = (JSONObject) element;
            String name = (String) object.get("name");
            long messages = (long) object.get("messages");
            queues.put(name, (int) messages);
        }

        return queues;
    }

    private String encode(String value) {
        return java.util.Base64.getEncoder().encodeToString(value.getBytes());
    }

}
