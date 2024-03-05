package com.bmonterrozo.alertmanager.repository;

import com.bmonterrozo.alertmanager.entity.AddresseeGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddresseeGroupRepository extends JpaRepository<AddresseeGroup, Integer> {
}
