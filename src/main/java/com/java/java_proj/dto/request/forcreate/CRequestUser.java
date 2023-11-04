package com.java.java_proj.dto.request.forcreate;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class CRequestUser {

    @NotBlank(message = "Name is required.")
    private String name;

    @NotBlank(message = "Email address is required.")
    @Email(message = "Email address is invalid. Please check and input again.")
    private String email;

    @NotBlank(message = "Phone is required.")
    @Pattern(regexp = "^0\\d{9}", message = "Phone is invalid. Please check and input again.")
    private String phone;

    @NotBlank(message = "Date of birth is required.")
    private String dob;

    @NotBlank(message = "User type is required")
    private String role;

    private Boolean gender;

    private Boolean status;
}
