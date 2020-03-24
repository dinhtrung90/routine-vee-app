package com.vdt.veeapp.repository.extensions;

import com.vdt.veeapp.domain.UserGroups;
import com.vdt.veeapp.repository.UserProfileRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
public interface UserProfileRepositoryExtension extends UserProfileRepository {


}
