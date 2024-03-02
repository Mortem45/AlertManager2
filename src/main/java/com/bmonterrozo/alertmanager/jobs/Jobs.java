package com.bmonterrozo.alertmanager.jobs;

import com.bmonterrozo.alertmanager.entity.Alert;
import org.quartz.*;

import java.util.Date;

public class Jobs {

    private Jobs() {}
    public static JobDetail buildJobDetail(final Class jobClass, String name, final Alert job) {
        final JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(jobClass.getSimpleName(), job);

        return JobBuilder
                .newJob(jobClass)
                .withIdentity(name, job.getPlatform().getName())
                .setJobData(jobDataMap)
                .build();
    }

    public static Trigger buildTrigger(final Class jobClass, String name, final Alert job) {
        SimpleScheduleBuilder builder = null;
        switch (job.getFrequencyType()) {
            case SECONDS:
                builder = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(job.getFrequency());
                break;
            case MINUTES:
                builder = SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(job.getFrequency());
                break;
            case HOURS:
                builder = SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(job.getFrequency());
                break;
            case DAYS:
                builder = SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(job.getFrequency() * 24);
                break;
        }
//        if (job.isRunForever()) {
//            builder = builder.repeatForever();
//        } else {
//            builder = builder.withRepeatCount(job.getTotalFireCount() - 1);
//        }
        builder = builder.repeatForever();
        return TriggerBuilder
                .newTrigger()
                .withIdentity(name, job.getPlatform().getName())
                .withSchedule(builder)
                .startAt(new Date(System.currentTimeMillis()))
                .build();
    }
}
