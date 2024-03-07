package com.bmonterrozo.alertmanager.jobs;

import com.bmonterrozo.alertmanager.entity.Alert;
import com.bmonterrozo.alertmanager.jobs.services.CouchbaseService;
import io.micrometer.common.util.StringUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CouchbaseJob implements Job {

    private static final Logger LOG = LoggerFactory.getLogger(CouchbaseJob.class);

    @Autowired
    private CouchbaseService couchbaseService;


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        Alert alert = (Alert) jobDataMap.get(CouchbaseJob.class.getSimpleName());
        LOG.info("execute - Alert - Id: {} - Name:'{}' - isActive:{}", alert.getId(), alert.getName(), alert.isActive());

        if (!StringUtils.isEmpty(alert.getSearch())) {
        couchbaseService.checkCouchbaseAlert(alert);
//        TODO: Add alarm validation
        }
    }
}
