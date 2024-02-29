package com.bmonterrozo.alertmanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import jakarta.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name="CONTACT")
public class Contact {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private boolean active;
    private String email;
    @Column(name = "ip_address")
    private String ipAddress;
    @Column(name = "phone_number")
    private Long phoneNumber;
    @Column(name = "user_name")
    private String userName;

    @ManyToMany(mappedBy = "contacts")
    @JsonIgnore
    private List<Group> groups;
}
