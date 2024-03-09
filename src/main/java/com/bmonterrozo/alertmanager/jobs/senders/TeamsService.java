package com.bmonterrozo.alertmanager.jobs.senders;

import com.bmonterrozo.alertmanager.entity.Addressee;
import com.bmonterrozo.alertmanager.entity.Notification;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class TeamsService {
    private static final Logger LOG = LoggerFactory.getLogger(TeamsService.class);
    public static final MediaType JSON = MediaType.get("application/json");

    public void sendPostTeams(Optional<Notification> notification, Addressee addressee, String alertInfo, String platform) {
        OkHttpClient client = new OkHttpClient();
        String payload = jsonBuilder(notification, alertInfo, platform);
        RequestBody body = RequestBody.create(payload, JSON);
        try {
            Request request = new Request.Builder()
                    .url(addressee.getValue())
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                LOG.info("Response to teamsService: {}", response.body().string());
            } else {
                LOG.error("Error to send post Teams: {}", response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String jsonBuilder(Optional<Notification> notification, String alertInfo, String platform) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy hh:mm a");
        String TEMPLATE = "{" +
                "  \"type\": \"MessageCard\"," +
                "  \"context\": \"http://schema.org/extensions\"," +
                "  \"themeColor\": \"0076D7\"," +
                "  \"summary\": \""+ notification.get().getTitle() +"\"," +
                "  \"sections\": [" +
                "    {" +
                "      \"activityTitle\": \""+ notification.get().getNotificationType().name()+ " | " + platform + " | " +notification.get().getTitle() +"\"," +
                "      \"facts\": [" +
                "        {" +
                "          \"name\": \"<b>Plataforma:</b>\"," +
                "          \"value\": \""+ platform +"\"," +
                "        }," +
                "        {" +
                "          \"name\": \"<b>Tipo De Alerta:</b>\"," +
                "          \"value\": \""+ notification.get().getNotificationType().name() +"\"," +
                "        }," +
                "        {" +
                "          \"name\": \"<b>Mensaje:</b>\"," +
                "          \"value\": \""+ notification.get().getMessage() +"\"," +
                "        }," +
                "        {" +
                "          \"name\": \"<b>Detalle De Alerta:</b>\"," +
                "          \"value\": \""+ alertInfo.replace("\n","<br>") +"\"," +
                "        }," +
                "        {" +
                "          \"name\": \"<b>Descripcion:</b>\"," +
                "          \"value\": \""+ notification.get().getDescription() +"\"," +
                "        }," +
                "        {" +
                "          \"name\": \"<b>Fecha:</b>\"," +
                "          \"value\": \""+ formatter.format(LocalDateTime.now()) +"\"," +
                "        }" +
                "      ]," +
                "      \"markdown\": true" +
                "    }" +
                "  ]" +
                "}"
                ;

        return TEMPLATE;
    }
}
