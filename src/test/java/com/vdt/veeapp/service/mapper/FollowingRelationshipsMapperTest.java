package com.vdt.veeapp.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class FollowingRelationshipsMapperTest {

    private FollowingRelationshipsMapper followingRelationshipsMapper;

    @BeforeEach
    public void setUp() {
        followingRelationshipsMapper = new FollowingRelationshipsMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(followingRelationshipsMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(followingRelationshipsMapper.fromId(null)).isNull();
    }
}
