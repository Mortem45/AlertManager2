package com.bmonterrozo.alertmanager.jobs.senders;

import com.bmonterrozo.alertmanager.entity.Addressee;
import com.bmonterrozo.alertmanager.entity.Notification;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.StringJoiner;

@Service
public class SMSService {
    private static final Logger LOG = LoggerFactory.getLogger(SMSService.class);

    public void sendSMS(Optional<Notification> notification, Addressee addressee, StringJoiner alertInfo) throws Exception {
        OkHttpClient client = new OkHttpClient();
        String message = notification.get().getMessage().replace(" ", "%20").replace("\n", "%0A") + ":%0A" +  alertInfo.toString().replace(" ", "%20").replace("\n", "%0A");
        String URL_TEMPLATE = "http://172.22.120.17:13100/sendsms?username=it&password=itcomcel&from={0}&to={1}&text={2}";
        String url= MessageFormat.format(
                URL_TEMPLATE,
                notification.get().getTitle(),
                addressee.getValue(),
                message
        );

        try{
            LOG.debug("Send SMS Alert to : {}", url);
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                LOG.info("Response to smsService: {}", response.body().string());
            } else {
                LOG.error("Error to send SMS: {}", response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
