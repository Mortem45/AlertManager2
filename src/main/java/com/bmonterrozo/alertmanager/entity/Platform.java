package com.bmonterrozo.alertmanager.entity;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name="PLATFORM")
public class Platform {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    private String name;
}
