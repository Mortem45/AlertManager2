package com.bmonterrozo.alertmanager.jobs;

import com.bmonterrozo.alertmanager.entity.Alert;
import com.bmonterrozo.alertmanager.jobs.senders.SenderService;
import com.bmonterrozo.alertmanager.jobs.services.SqlService;
import io.micrometer.common.util.StringUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.StringJoiner;

@Component
public class SqlJob implements Job {

    private static final Logger LOG = LoggerFactory.getLogger(SqlJob.class);

    @Autowired
    private SqlService sqlService;
    @Autowired
    private SenderService senderService;

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
            Map<String, Long> result = sqlService.checkSQLAlert(alert);
            if (result != null) {
                try {
                    validateSqlAlert(result, alert);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void validateSqlAlert(Map<String, Long> result, Alert alert) throws Exception {
        long countTotal = 0L;
        StringJoiner info = new StringJoiner("\n");

        for (Map.Entry<String, Long> entry : result.entrySet()) {
            String name = entry.getKey();
            Long value = entry.getValue();
            countTotal += value;
            info.add(name + ": " + value);
            LOG.debug("validateSqlAlert() - name: {}, count: {}", name, value);
        }
        if (countTotal >= alert.getThreshold()) {
//            TODO: Save History alarm
            senderService.sendAlert(alert, info);
            LOG.debug("ALARMA!!!!! - countTotal: {}, groupBy: {}", countTotal, info);
        }
    }
}
