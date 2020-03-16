package com.vdt.veeapp.repository;

import com.vdt.veeapp.domain.Habit;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Habit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HabitRepository extends JpaRepository<Habit, Long>, JpaSpecificationExecutor<Habit> {
}
