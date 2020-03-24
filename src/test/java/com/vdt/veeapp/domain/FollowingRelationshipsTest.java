package com.vdt.veeapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.vdt.veeapp.web.rest.TestUtil;

public class FollowingRelationshipsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FollowingRelationships.class);
        FollowingRelationships followingRelationships1 = new FollowingRelationships();
        followingRelationships1.setId(1L);
        FollowingRelationships followingRelationships2 = new FollowingRelationships();
        followingRelationships2.setId(followingRelationships1.getId());
        assertThat(followingRelationships1).isEqualTo(followingRelationships2);
        followingRelationships2.setId(2L);
        assertThat(followingRelationships1).isNotEqualTo(followingRelationships2);
        followingRelationships1.setId(null);
        assertThat(followingRelationships1).isNotEqualTo(followingRelationships2);
    }
}
