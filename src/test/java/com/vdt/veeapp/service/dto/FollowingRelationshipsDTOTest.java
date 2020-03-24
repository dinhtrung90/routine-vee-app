package com.vdt.veeapp.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.vdt.veeapp.web.rest.TestUtil;

public class FollowingRelationshipsDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FollowingRelationshipsDTO.class);
        FollowingRelationshipsDTO followingRelationshipsDTO1 = new FollowingRelationshipsDTO();
        followingRelationshipsDTO1.setId(1L);
        FollowingRelationshipsDTO followingRelationshipsDTO2 = new FollowingRelationshipsDTO();
        assertThat(followingRelationshipsDTO1).isNotEqualTo(followingRelationshipsDTO2);
        followingRelationshipsDTO2.setId(followingRelationshipsDTO1.getId());
        assertThat(followingRelationshipsDTO1).isEqualTo(followingRelationshipsDTO2);
        followingRelationshipsDTO2.setId(2L);
        assertThat(followingRelationshipsDTO1).isNotEqualTo(followingRelationshipsDTO2);
        followingRelationshipsDTO1.setId(null);
        assertThat(followingRelationshipsDTO1).isNotEqualTo(followingRelationshipsDTO2);
    }
}
