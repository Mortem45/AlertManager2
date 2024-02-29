package com.bmonterrozo.alertmanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import jakarta.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name="AM_GROUP")
public class Group {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String email;
    @Column(name = "teams_hook")
    private String teamsHook;
    @Column(name = "slack_hook")
    private String slackHook;
    private boolean active;

    @ManyToMany(cascade ={ CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "group_contacts",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "contact_id")
    )
    private List<Contact> contacts;

    @ManyToMany(mappedBy = "groups")
    @JsonIgnore
    private List<Notification> notifications;

    public void addContact(Contact contact) {
        Contact cont = this.contacts.stream().filter(c -> c == contact).findFirst().orElse(null);
        if (cont == null) this.contacts.add(contact);
        contact.getGroups().add(this);
    }

    public void removeContact(int contactId) {
        Contact contact = this.contacts.stream().filter(c -> c.getId() == contactId).findFirst().orElse(null);
        if (contact != null) this.contacts.remove(contact);
        contact.getGroups().remove(this);
    }
}
