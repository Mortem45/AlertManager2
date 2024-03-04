package com.bmonterrozo.alertmanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import jakarta.persistence.*;
import java.util.List;
import java.util.Random;

@Data
@Entity
@Table(name="SOURCE_GROUP")
public class SourceGroup {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @ManyToMany(cascade ={ CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "source_group_data_source",
            joinColumns = @JoinColumn(name = "source_group_id"),
            inverseJoinColumns = @JoinColumn(name = "data_source_id")
    )
    private List<DataSource> dataSources;

    @OneToOne
    @JoinColumn(name = "data_source_type_id")
    private DataSourceType dataSourceType;


    public void addDataSource(DataSource dataSource) {
        DataSource cont = this.dataSources.stream().filter(c -> c == dataSource).findFirst().orElse(null);
        if (cont == null) this.dataSources.add(dataSource);
        dataSource.getSourceGroups().add(this);
    }

    public void removeDataSource(int dataSourceId) {
        DataSource dataSource = this.dataSources.stream().filter(c -> c.getId() == dataSourceId).findFirst().orElse(null);
        if (dataSource != null) this.dataSources.remove(dataSource);
        dataSource.getSourceGroups().remove(this);
    }
//    @OneToMany(mappedBy = "sourceGroup")
//    private List<DataSource> dataSources;

    @JsonIgnore
    public DataSource getRandomDataSource() {
        Random random = new Random();
        int indiceAleatorio = random.nextInt(this.dataSources.size());
        return this.dataSources.stream()
                .filter(obj -> !obj.isActive())
                .toList()
                .get(indiceAleatorio);
    }
}

