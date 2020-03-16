package com.vdt.veeapp.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class HabitMapperTest {

    private HabitMapper habitMapper;

    @BeforeEach
    public void setUp() {
        habitMapper = new HabitMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(habitMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(habitMapper.fromId(null)).isNull();
    }
}
