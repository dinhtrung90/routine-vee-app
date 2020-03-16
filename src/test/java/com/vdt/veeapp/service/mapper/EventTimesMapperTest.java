package com.vdt.veeapp.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class EventTimesMapperTest {

    private EventTimesMapper eventTimesMapper;

    @BeforeEach
    public void setUp() {
        eventTimesMapper = new EventTimesMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(eventTimesMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(eventTimesMapper.fromId(null)).isNull();
    }
}
