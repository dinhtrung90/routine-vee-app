package com.vdt.veeapp.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.vdt.veeapp.web.rest.TestUtil;

public class HabitDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HabitDTO.class);
        HabitDTO habitDTO1 = new HabitDTO();
        habitDTO1.setId(1L);
        HabitDTO habitDTO2 = new HabitDTO();
        assertThat(habitDTO1).isNotEqualTo(habitDTO2);
        habitDTO2.setId(habitDTO1.getId());
        assertThat(habitDTO1).isEqualTo(habitDTO2);
        habitDTO2.setId(2L);
        assertThat(habitDTO1).isNotEqualTo(habitDTO2);
        habitDTO1.setId(null);
        assertThat(habitDTO1).isNotEqualTo(habitDTO2);
    }
}
