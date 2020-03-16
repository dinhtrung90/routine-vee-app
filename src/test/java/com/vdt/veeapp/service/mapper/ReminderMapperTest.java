package com.vdt.veeapp.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ReminderMapperTest {

    private ReminderMapper reminderMapper;

    @BeforeEach
    public void setUp() {
        reminderMapper = new ReminderMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(reminderMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(reminderMapper.fromId(null)).isNull();
    }
}
