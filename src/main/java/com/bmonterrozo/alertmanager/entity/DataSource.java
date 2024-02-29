package com.bmonterrozo.alertmanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import jakarta.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name="DATA_SOURCE")
public class DataSource {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String url;
    private String host;
    private String protocol;
    private Integer port;
    private String username;
    private String password;

    @OneToOne
    @JoinColumn(name = "data_source_type_id")
    private DataSourceType dataSourceType;


    @ManyToMany(mappedBy = "dataSources")
    @JsonIgnore
    private List<SourceGroup> sourceGroups;

//    @ManyToOne
//    @JoinColumn(name = "data_source_group_id", referencedColumnName = "id")
//    @JsonIgnore
//    private SourceGroup sourceGroup;
}
