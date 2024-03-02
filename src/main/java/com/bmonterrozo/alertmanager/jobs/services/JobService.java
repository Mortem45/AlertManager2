package com.bmonterrozo.alertmanager.jobs.services;

import com.bmonterrozo.alertmanager.entity.Alert;
import com.bmonterrozo.alertmanager.jobs.Jobs;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class JobService {
    private static final Logger LOG = LoggerFactory.getLogger(JobService.class);

    private final Scheduler scheduler;

    @Autowired
    public JobService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void schedule(final Class jobClass, final Alert alert) {
        LOG.info("schedule Name: {} - Frequency: {} - FrequencyType: {}", alert.getName(),alert.getFrequency(), alert.getFrequencyType());
        final JobDetail jobDetail = Jobs.buildJobDetail(jobClass, alert.getName(), alert);
        final Trigger trigger = Jobs.buildTrigger(jobClass, alert.getName(), alert);
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            LOG.error("schedule {}", e.getMessage());
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void init() {
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            LOG.error("init {}", e.getMessage());
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void preDestroy() {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            LOG.error("JobService - preDestroy {}  {}", e.getMessage());
            e.printStackTrace();
        }
    }
}