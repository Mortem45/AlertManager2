package com.bmonterrozo.alertmanager.entity;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name="DATA_SOURCE_TYPE")
public class DataSourceType {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    private String name;
}
