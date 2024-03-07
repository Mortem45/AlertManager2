package com.bmonterrozo.alertmanager.jobs;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsAggregate;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import com.bmonterrozo.alertmanager.entity.Alert;
import com.bmonterrozo.alertmanager.jobs.services.ElasticService;
import com.bmonterrozo.alertmanager.jobs.senders.SenderService;
import io.micrometer.common.util.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.StringJoiner;


@DisallowConcurrentExecution
@Component
public class ElasticJob implements Job {
    private static final Logger LOG = LoggerFactory.getLogger(ElasticJob.class);
    @Autowired

    private ElasticService elasticService;
    @Autowired
    private SenderService senderService;
    SearchResponse<Void> response;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        Alert alert = (Alert) jobDataMap.get(ElasticJob.class.getSimpleName());

        if (alert.isActive()) {
            LOG.info("execute - Alert - Id: {} - Name:'{}' - isActive:{}", alert.getId(), alert.getName(), alert.isActive());
            if (!StringUtils.isEmpty(alert.getSearch())) {
                LOG.debug("execute - Alert - Search {}", alert.getSearch());
                try {
                    response = elasticService.findGroupByDocuments(alert.getSourceGroup().getRandomDataSource(), alert.getSearch(), alert.getFrequency(), alert.getFrequencyType());
                    if (response != null) {
                        validateELK(response, alert);
                    }
                } catch (RuntimeException | NoSuchAlgorithmException | KeyStoreException |
                         KeyManagementException e) {
                    throw new RuntimeException(e);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } else LOG.info("Alert - Id: {} - Name:'{}' - isActive:{}", alert.getId(), alert.getName(), alert.isActive());

    }

    protected void validateELK(SearchResponse<Void> response, Alert alert ) throws Exception {
        TotalHits total = response.hits().total();
        if (total.value() > alert.getThreshold()) {
            LOG.debug("validateELK - AlertId: {} - AlertName: {} - Total Hits: {} ", alert.getId(), alert.getName(),total.value());
            Aggregate agre = response.aggregations().get("result");
            StringJoiner info = new StringJoiner("\n");
            StringTermsAggregate sterms= agre.sterms();

            info.add("Total Hits: " + total.value());

            sterms.buckets().array().stream().forEach(bucket -> {
                info.add(bucket.key().stringValue()+ ": " + bucket.docCount() );
            });

            senderService.sendAlert(alert, info);
//          TODO send message
        }
    }
}

