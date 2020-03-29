package com.vdt.veeapp.web.rest.extensions;

import com.vdt.veeapp.service.dto.FollowingRelationshipsCriteria;
import com.vdt.veeapp.service.dto.FollowingRelationshipsDTO;
import com.vdt.veeapp.service.extensions.FollowingRelationshipsQueryServiceExtension;
import com.vdt.veeapp.web.rest.FollowingRelationshipsResource;
import io.github.jhipster.web.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/extension/")
public class FollowingRelationshipResourceExtension {

    private final Logger log = LoggerFactory.getLogger(FollowingRelationshipResourceExtension.class);

    private static final String ENTITY_NAME = "followingRelationships";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private FollowingRelationshipsQueryServiceExtension followingRelationshipsQueryServiceExtension;

    @GetMapping("/following-relationships")
    public ResponseEntity<List<FollowingRelationshipsDTO>> getAllFollowingRelationships(Long userId, Pageable pageable) {
        log.debug("REST request to get FollowingRelationships by userId: {}", userId);
        Page<FollowingRelationshipsDTO> page = followingRelationshipsQueryServiceExtension.findYourUserFollowed(userId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
