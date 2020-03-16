package com.vdt.veeapp.service.mapper;


import com.vdt.veeapp.domain.*;
import com.vdt.veeapp.service.dto.HabitDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Habit} and its DTO {@link HabitDTO}.
 */
@Mapper(componentModel = "spring", uses = {ReminderMapper.class})
public interface HabitMapper extends EntityMapper<HabitDTO, Habit> {

    @Mapping(source = "reminder.id", target = "reminderId")
    HabitDTO toDto(Habit habit);

    @Mapping(source = "reminderId", target = "reminder")
    @Mapping(target = "eventTimes", ignore = true)
    @Mapping(target = "removeEventTimes", ignore = true)
    Habit toEntity(HabitDTO habitDTO);

    default Habit fromId(Long id) {
        if (id == null) {
            return null;
        }
        Habit habit = new Habit();
        habit.setId(id);
        return habit;
    }
}
