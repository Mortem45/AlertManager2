package com.bmonterrozo.alertmanager.repository;

import com.bmonterrozo.alertmanager.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
}
