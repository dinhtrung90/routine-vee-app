package com.vdt.veeapp.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.vdt.veeapp.web.rest.TestUtil;

public class EventTimesDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventTimesDTO.class);
        EventTimesDTO eventTimesDTO1 = new EventTimesDTO();
        eventTimesDTO1.setId(1L);
        EventTimesDTO eventTimesDTO2 = new EventTimesDTO();
        assertThat(eventTimesDTO1).isNotEqualTo(eventTimesDTO2);
        eventTimesDTO2.setId(eventTimesDTO1.getId());
        assertThat(eventTimesDTO1).isEqualTo(eventTimesDTO2);
        eventTimesDTO2.setId(2L);
        assertThat(eventTimesDTO1).isNotEqualTo(eventTimesDTO2);
        eventTimesDTO1.setId(null);
        assertThat(eventTimesDTO1).isNotEqualTo(eventTimesDTO2);
    }
}
