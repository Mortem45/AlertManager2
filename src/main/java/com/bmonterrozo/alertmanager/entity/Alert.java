package com.bmonterrozo.alertmanager.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table (name="ALERT")
public class Alert {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String search;
    private boolean active;
    private Integer threshold;
    private Integer frequency;
    @Enumerated(EnumType.ORDINAL)
    private FrecuencyType frequencyType;

    @ManyToOne
    @JoinColumn(name = "source_group_id", referencedColumnName = "id")
    private SourceGroup sourceGroup;

    @OneToOne
    @JoinColumn(name = "notification_id")
    private Notification notification;

    @ManyToOne
    @JoinColumn(name = "platform_id", referencedColumnName = "id")
    private Platform platform;
}
