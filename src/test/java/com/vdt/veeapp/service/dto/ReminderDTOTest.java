package com.vdt.veeapp.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.vdt.veeapp.web.rest.TestUtil;

public class ReminderDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReminderDTO.class);
        ReminderDTO reminderDTO1 = new ReminderDTO();
        reminderDTO1.setId(1L);
        ReminderDTO reminderDTO2 = new ReminderDTO();
        assertThat(reminderDTO1).isNotEqualTo(reminderDTO2);
        reminderDTO2.setId(reminderDTO1.getId());
        assertThat(reminderDTO1).isEqualTo(reminderDTO2);
        reminderDTO2.setId(2L);
        assertThat(reminderDTO1).isNotEqualTo(reminderDTO2);
        reminderDTO1.setId(null);
        assertThat(reminderDTO1).isNotEqualTo(reminderDTO2);
    }
}
