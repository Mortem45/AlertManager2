package com.bmonterrozo.alertmanager.jobs.senders;

import com.bmonterrozo.alertmanager.entity.Addressee;
import com.bmonterrozo.alertmanager.entity.Notification;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class EmailService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender emailSender;

    public void sendEmail(Optional<Notification> notification, Addressee addressee, String alertInfo, String platform) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        helper = new MimeMessageHelper(message, true);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy hh:mm a");

        String EMAIL_TEMPLATE = """
                <html><body>
                <b>Alerta:</b> {0}<br>
                <b>Plataforma:</b> {1}<br>
                <b>Tipo De Alerta:</b> {2}<br>
                <b>Mensaje:</b> {3}
                <br>
                <br>
                <b>Detalle De Alerta:</b>\s
                <br>
                {4}
                <br><br>
                <b>Descripcion:</b> 
                <br>              
                {5}
                <br><br>
                {6}<br> 
                </body></html>         
                """;

        String body= MessageFormat.format(
                EMAIL_TEMPLATE,
                notification.get().getTitle(),
                platform,
                notification.get().getNotificationType().name(),
                notification.get().getMessage(),
                alertInfo.replace("\n","<br>"),
                notification.get().getDescription(),
                formatter.format(LocalDateTime.now())
        );

        helper.setFrom("ste45_007@hotmail.com");
        helper.setTo(addressee.getValue());
        helper.setSubject(notification.get().getNotificationType().name()+ " | " + platform + " | " +notification.get().getTitle());
        helper.setText(body, true);

        emailSender.send(message);

        LOG.info("Email sent successfully to: {}", addressee.getValue());
    }
}
