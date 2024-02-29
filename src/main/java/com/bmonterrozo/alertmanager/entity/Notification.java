package com.bmonterrozo.alertmanager.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name="NOTIFICATION")
public class Notification {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private boolean active;
    private String sender;
    private String message;

    @ManyToMany
    @JoinTable(
            name = "notifications_groups",
            joinColumns = @JoinColumn(name = "notification_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private List<Group> groups;

    @Enumerated(EnumType.ORDINAL)
    private NotificationType notificationType;

    @OneToOne
    @JoinColumn(name = "notification_channel_id")
    private NotificationChannel notificationChannel;

    public void addNotificationGroup(Group notificationGroup) {
        Group cont = this.groups.stream().filter(c -> c == notificationGroup).findFirst().orElse(null);
        if (cont == null) this.groups.add(notificationGroup);
        notificationGroup.getNotifications().add(this);
    }

    public void removeNotificationGroup(int notificationGroupId) {
        Group group = this.groups.stream().filter(c -> c.getId() == notificationGroupId).findFirst().orElse(null);
        if (group != null) this.groups.remove(group);
        group.getNotifications().remove(this);
    }
}
