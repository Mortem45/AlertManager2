package com.bmonterrozo.alertmanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import jakarta.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name="ADDRESSEE")
public class Addressee {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String value;


    @ManyToMany(mappedBy = "addressees")
    @JsonIgnore
    private List<AddresseeGroup> addresseeGroups;

    @OneToOne
    @JoinColumn(name = "notification_channel_id")
    private NotificationChannel notificationChannel;
}
