package com.bmonterrozo.alertmanager.jobs.senders;

import com.bmonterrozo.alertmanager.entity.Addressee;
import com.bmonterrozo.alertmanager.entity.AddresseeGroup;
import com.bmonterrozo.alertmanager.entity.Alert;
import com.bmonterrozo.alertmanager.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Service
public class SenderService {

    private static final Logger LOG = LoggerFactory.getLogger(SenderService.class);

    @Autowired
    private  NotificationService notificationService;

    @Autowired
    private SMSService smsService;



    public List<Addressee> getAddressees (Alert alert) {
        List<AddresseeGroup> addresseeGroups = notificationService.getAddresseeGroupByNotifi(alert.getNotification().getId());
        List<Addressee> addressees = new ArrayList<>();

        for (AddresseeGroup addresseeGroup: addresseeGroups) {
            addresseeGroup.getAddressees().stream().forEach(addressee -> {
                if (addressee.isActive()) {
                    addressees.add(addressee);
                }
            });
        }

        return addressees;

    }

    public void sendAlert(Alert alert, StringJoiner alertInfo) throws Exception {
        List<Addressee> addressees = getAddressees(alert);

        for (Addressee addressee: addressees) {
            switch (addressee.getNotificationChannel().getName()) {
                case "SMS":
                    smsService.sendSMS(alert.getNotification().getId(), addressee, alertInfo);
                    Thread.sleep(1000);
                    break;
                case "EMAIL":
                    break;
            }
        }
    }

}
