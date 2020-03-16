package com.vdt.veeapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.vdt.veeapp.web.rest.TestUtil;

public class EventTimesTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventTimes.class);
        EventTimes eventTimes1 = new EventTimes();
        eventTimes1.setId(1L);
        EventTimes eventTimes2 = new EventTimes();
        eventTimes2.setId(eventTimes1.getId());
        assertThat(eventTimes1).isEqualTo(eventTimes2);
        eventTimes2.setId(2L);
        assertThat(eventTimes1).isNotEqualTo(eventTimes2);
        eventTimes1.setId(null);
        assertThat(eventTimes1).isNotEqualTo(eventTimes2);
    }
}
