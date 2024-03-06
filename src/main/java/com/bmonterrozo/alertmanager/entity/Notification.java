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
    private String title;
    private boolean active;
    private String message;

    @ManyToMany
    @JoinTable(
            name = "notifications_addressee_groups",
            joinColumns = @JoinColumn(name = "notification_id"),
            inverseJoinColumns = @JoinColumn(name = "addressee_group_id")
    )
    private List<AddresseeGroup> addresseeGroups;

    @Enumerated(EnumType.ORDINAL)
    private NotificationType notificationType;


    public void addNotificationAddresseeGroup(AddresseeGroup notificationGroup) {
        AddresseeGroup cont = this.addresseeGroups.stream().filter(c -> c == notificationGroup).findFirst().orElse(null);
        if (cont == null) this.addresseeGroups.add(notificationGroup);
        notificationGroup.getNotifications().add(this);
    }

    public void removeNotificationAddresseeGroup(int notificationGroupId) {
        AddresseeGroup addresseeGroup = this.addresseeGroups.stream().filter(c -> c.getId() == notificationGroupId).findFirst().orElse(null);
        if (addresseeGroup != null) this.addresseeGroups.remove(addresseeGroup);
        addresseeGroup.getNotifications().remove(this);
    }
}
