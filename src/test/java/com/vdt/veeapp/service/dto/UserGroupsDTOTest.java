package com.vdt.veeapp.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.vdt.veeapp.web.rest.TestUtil;

public class UserGroupsDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserGroupsDTO.class);
        UserGroupsDTO userGroupsDTO1 = new UserGroupsDTO();
        userGroupsDTO1.setId(1L);
        UserGroupsDTO userGroupsDTO2 = new UserGroupsDTO();
        assertThat(userGroupsDTO1).isNotEqualTo(userGroupsDTO2);
        userGroupsDTO2.setId(userGroupsDTO1.getId());
        assertThat(userGroupsDTO1).isEqualTo(userGroupsDTO2);
        userGroupsDTO2.setId(2L);
        assertThat(userGroupsDTO1).isNotEqualTo(userGroupsDTO2);
        userGroupsDTO1.setId(null);
        assertThat(userGroupsDTO1).isNotEqualTo(userGroupsDTO2);
    }
}
