package com.bmonterrozo.alertmanager.repository;

import com.bmonterrozo.alertmanager.entity.SourceGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SourceGroupRepository extends JpaRepository<SourceGroup, Integer> {
}
