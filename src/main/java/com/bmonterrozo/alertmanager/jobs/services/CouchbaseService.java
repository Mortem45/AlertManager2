package com.bmonterrozo.alertmanager.jobs.services;

import com.bmonterrozo.alertmanager.entity.Alert;
import com.couchbase.client.core.error.CouchbaseException;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class CouchbaseService {

    private static final Logger LOG = LoggerFactory.getLogger(CouchbaseService.class);

    public void checkCouchbaseAlert(Alert alert) {
        System.setProperty("com.couchbase.env.timeout.kvTimeout", "20s");
        System.setProperty("com.couchbase.env.timeout.queryTimeout", "15s");

        String connectionString = alert.getSourceGroup().getRandomDataSource().getUrl();
        String username = alert.getSourceGroup().getRandomDataSource().getUsername();
        String password = alert.getSourceGroup().getRandomDataSource().getPassword();

        Cluster cluster = Cluster.connect(connectionString, username, password);

        String query = alert.getSearch();

        QueryOptions options = QueryOptions.queryOptions().timeout(Duration.ofSeconds(30));

        try {


            QueryResult result = cluster.query(query, options);

            for (JsonObject row : result.rowsAsObject()) {
                LOG.info("checkCouchbaseAlert - Row:'{}'", row);
                LOG.info("checkCouchbaseAlert - Name:'{}' - Total:{}", row.getString("nombre"), row.getInt("TOTAL"));
            }
        } catch (CouchbaseException ex) {
            ex.printStackTrace();
        } finally {
            LOG.debug("Closed Connection");
            cluster.disconnect();
        }
    }
}
