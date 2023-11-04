package com.java.java_proj.dto.request.forupdate;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class URequestUserPermission {

    @NotNull
    private Integer id;

    @NotNull
    public String syllabus;

    @NotNull
    public String userManagement;
    @NotNull
    public String documentManagement;
}
