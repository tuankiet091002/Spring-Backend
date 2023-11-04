package com.java.java_proj.services.templates;

import com.java.java_proj.dto.request.forupdate.URequestUserPermission;
import com.java.java_proj.dto.response.forlist.LRepsonseUserPermission;

import java.util.List;

public interface UserPermissionService {

    public List<LRepsonseUserPermission> getAll();

    public List<LRepsonseUserPermission> updateAll(List<URequestUserPermission> userPermissionList);
}
