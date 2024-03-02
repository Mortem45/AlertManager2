package com.bmonterrozo.alertmanager.playground;

import com.bmonterrozo.alertmanager.entity.Alert;
import com.bmonterrozo.alertmanager.jobs.ElasticJob;
import com.bmonterrozo.alertmanager.jobs.services.JobService;
import com.bmonterrozo.alertmanager.jobs.RabbitMQJob;
import com.bmonterrozo.alertmanager.service.AlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PlaygroundService {

    private static final Logger LOG = LoggerFactory.getLogger(PlaygroundService.class);
    private final JobService jobService;


    @Autowired
    private AlertService alertService;

    @Autowired
    public PlaygroundService(JobService jobService) {
        this.jobService = jobService;
    }

    public void runTestJob() {
        List<Alert> alerts = alertService.findAll();
        for (Alert alert: alerts) {
            if (alert.isActive()) {
                LOG.info("PlaygroundService - runTestJob {} - {}", alert.getName(), alert.getSourceGroup().getDataSourceType().getName());
                switch (alert.getSourceGroup().getDataSourceType().getName()) {
                    case "ELK":
                        LOG.info("PlaygroundService - ELK {}", alert.getName());
                        jobService.schedule(ElasticJob.class, alert);
                        break;
                    case "RABBITMQ":
                        LOG.info("PlaygroundService - RABBITMQ {}", alert.getName());
                        jobService.schedule(RabbitMQJob.class, alert);
                        break;
                }
            }
        }
    }

}
