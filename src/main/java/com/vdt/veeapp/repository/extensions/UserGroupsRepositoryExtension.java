package com.vdt.veeapp.repository.extensions;

import com.vdt.veeapp.domain.UserGroups;
import com.vdt.veeapp.repository.UserGroupsRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
public interface UserGroupsRepositoryExtension extends UserGroupsRepository {


}
