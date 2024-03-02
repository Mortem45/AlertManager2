package com.bmonterrozo.alertmanager.jobs;

import com.bmonterrozo.alertmanager.entity.Alert;
import com.bmonterrozo.alertmanager.jobs.services.SqlService;
import io.micrometer.common.util.StringUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class SqlJob implements Job {

    private static final Logger LOG = LoggerFactory.getLogger(SqlJob.class);

    private final SqlService sqlService;

    public SqlJob(SqlService sqlService) {
        this.sqlService = sqlService;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        Alert alert = (Alert) jobDataMap.get(SqlJob.class.getSimpleName());
        LOG.info("execute - Alert - Id: {} - Name:'{}' - isActive:{}", alert.getId(), alert.getName(), alert.isActive());
        if (!StringUtils.isEmpty(alert.getSearch())) {
            LOG.debug("execute - Alert - Search {}", alert.getSearch());
            Map<String, Long> result = sqlService.checkOracleAlert(alert);
            if (result != null) {
                validateSqlAlert(result, alert);
            }
        }
    }

    public void validateSqlAlert(Map<String, Long> result, Alert alert) {
        long countTotal = 0L;
        String groupBy = "";

        for (Map.Entry<String, Long> entry : result.entrySet()) {
            String name = entry.getKey();
            Long value = entry.getValue();
            countTotal += value;
            groupBy = groupBy + "\n" + name + ": " + value;
            LOG.debug("checkOracleAlert() - name: {}, count: {}", name, value);
        }
        if (countTotal >= alert.getThreshold()) {
//            TODO: Save History alarm
//            TODO: Send MSG
            LOG.debug("ALARMA!!!!! - countTotal: {}, groupBy: {}", countTotal, groupBy);
        }
    }
}
