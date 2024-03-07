package com.bmonterrozo.alertmanager.jobs;

import com.bmonterrozo.alertmanager.entity.Alert;
import com.bmonterrozo.alertmanager.jobs.services.RabbitMQService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.StringJoiner;

@Component
public class RabbitMQJob implements Job {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitMQJob.class);

    @Autowired
    private  RabbitMQService rabbitMQService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        Alert alert = (Alert) jobDataMap.get(RabbitMQJob.class.getSimpleName());
        try {
            Map<String, Integer> queues = rabbitMQService.getAllQueues(alert.getSourceGroup().getRandomDataSource());
            if (queues != null) {
                validateRabbitAlert(queues, alert);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    private void validateRabbitAlert(Map<String, Integer> queues, Alert alert) throws ParseException {

        if (!alert.getSearch().equals(null)) {
            LOG.debug("RabbitMQJob - validateRabbitAlert - search: {}", alert.getSearch());
            JSONObject searchDetails = convertJSON(alert.getSearch());
            JSONArray monitoredQueues = (JSONArray) searchDetails.get("queues");
            Boolean isAlert = false;
            StringJoiner alarmInfo = new StringJoiner("\n");


            LOG.debug("validateRabbitAlert - getAllQueues - monitoredQueues: {}", monitoredQueues);

            for (Map.Entry<String, Integer> entry : queues.entrySet()) {
                String name = entry.getKey();
                Integer value = entry.getValue();

                if (value > alert.getThreshold()) {
                    LOG.debug("validateRabbitAlert - getAllQueues - Queue: {} - Messages: {}", name, value);
                    isAlert = true;
                    alarmInfo.add(name + ": " + value);
                }

                for (Object element : monitoredQueues) {
                    JSONObject object = (JSONObject) element;
                    String nameq = (String) object.get("name");
                    long thredshold = (long) object.get("thredshold");
                    if (name.equals(nameq) && value >= thredshold) {
                        LOG.debug("validateRabbitAlert - monitoredQueues - Queue: {} - Messages: {}", nameq, value);
                        if(!alarmInfo.toString().contains(nameq)) {
                            isAlert = true;
                            alarmInfo.add(name + ": " + value);
                        }
                    }
                }

            }

            if (isAlert) {
                LOG.debug("ALARMAAA!!! - getAllQueues - ALARM: {}", alarmInfo);
//                TODO: Save History alarm
//                TODO: Send MSG
            }

        } else {
            LOG.debug("RabbitMQJob - validateRabbitAlert - search: {}", alert.getSearch());
        }
    }

    public JSONObject convertJSON(String jsonString) throws ParseException {
        JSONParser parser = new JSONParser();
        Object jsonObj = parser.parse(jsonString);
        JSONObject jsonObject = (JSONObject) jsonObj;
        return jsonObject;
    }
}
