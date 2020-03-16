package com.vdt.veeapp.service.mapper;


import com.vdt.veeapp.domain.*;
import com.vdt.veeapp.service.dto.EventTimesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventTimes} and its DTO {@link EventTimesDTO}.
 */
@Mapper(componentModel = "spring", uses = {HabitMapper.class})
public interface EventTimesMapper extends EntityMapper<EventTimesDTO, EventTimes> {

    @Mapping(source = "habit.id", target = "habitId")
    EventTimesDTO toDto(EventTimes eventTimes);

    @Mapping(source = "habitId", target = "habit")
    EventTimes toEntity(EventTimesDTO eventTimesDTO);

    default EventTimes fromId(Long id) {
        if (id == null) {
            return null;
        }
        EventTimes eventTimes = new EventTimes();
        eventTimes.setId(id);
        return eventTimes;
    }
}
