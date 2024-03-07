package com.bmonterrozo.alertmanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import jakarta.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name="ADDRESSEE_GROUP")
public class AddresseeGroup {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private boolean active;

    @ManyToMany(cascade ={ CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "addressee_group_adressee",
            joinColumns = @JoinColumn(name = "addressee_group_id"),
            inverseJoinColumns = @JoinColumn(name = "addressee_id")
    )
    private List<Addressee> addressees;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "addresseeGroups")
    @JsonIgnore
    private List<Notification> notifications;

    public void addAddressee(Addressee addressee) {
        Addressee cont = this.addressees.stream().filter(c -> c == addressee).findFirst().orElse(null);
        if (cont == null) this.addressees.add(addressee);
        addressee.getAddresseeGroups().add(this);
    }

    public void removeAddressee(int addresseeId) {
        Addressee addressee = this.addressees.stream().filter(c -> c.getId() == addresseeId).findFirst().orElse(null);
        if (addressee != null) this.addressees.remove(addressee);
        addressee.getAddresseeGroups().remove(this);
    }
}
