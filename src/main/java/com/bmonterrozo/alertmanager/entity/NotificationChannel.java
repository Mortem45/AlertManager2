package com.bmonterrozo.alertmanager.entity;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name="NOTIFICATION_CHANNEL")
public class NotificationChannel {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private String name;
}
