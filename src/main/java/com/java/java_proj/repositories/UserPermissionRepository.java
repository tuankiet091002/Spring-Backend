package com.java.java_proj.repositories;

import com.java.java_proj.dto.response.forlist.LRepsonseUserPermission;
import com.java.java_proj.entities.UserPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserPermissionRepository extends JpaRepository<UserPermission, Integer> {
    List<LRepsonseUserPermission> findAllBy();
    UserPermission findByRole(String role);
}
