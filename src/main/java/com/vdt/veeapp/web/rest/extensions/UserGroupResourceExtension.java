package com.vdt.veeapp.web.rest.extensions;

import com.vdt.veeapp.service.UserGroupsQueryService;
import com.vdt.veeapp.service.UserGroupsService;
import com.vdt.veeapp.service.dto.UserGroupsCriteria;
import com.vdt.veeapp.service.dto.UserGroupsDTO;
import com.vdt.veeapp.service.extensions.UserGroupQueryServiceExtension;
import com.vdt.veeapp.web.rest.UserGroupsResource;
import io.github.jhipster.web.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Controller
@RequestMapping("/api/extensions")
public class UserGroupResourceExtension {

    private final Logger log = LoggerFactory.getLogger(UserGroupResourceExtension.class);

    @Autowired
    private UserGroupQueryServiceExtension userGroupQueryServiceExtension;



    @GetMapping("/user-groups")
    public ResponseEntity<List<UserGroupsDTO>> getAllGroup(UserGroupsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get UserGroups by criteria: {}", criteria);
        Page<UserGroupsDTO> page = userGroupQueryServiceExtension.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }


}
