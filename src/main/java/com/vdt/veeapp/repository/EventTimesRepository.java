package com.vdt.veeapp.repository;

import com.vdt.veeapp.domain.EventTimes;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the EventTimes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventTimesRepository extends JpaRepository<EventTimes, Long>, JpaSpecificationExecutor<EventTimes> {
}
