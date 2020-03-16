package com.vdt.veeapp.service.mapper;


import com.vdt.veeapp.domain.*;
import com.vdt.veeapp.service.dto.ReminderDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Reminder} and its DTO {@link ReminderDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ReminderMapper extends EntityMapper<ReminderDTO, Reminder> {



    default Reminder fromId(Long id) {
        if (id == null) {
            return null;
        }
        Reminder reminder = new Reminder();
        reminder.setId(id);
        return reminder;
    }
}
