package com.bmonterrozo.alertmanager.repository;

import com.bmonterrozo.alertmanager.entity.Addressee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddresseeRepository extends JpaRepository<Addressee, Integer> {
}
