package com.java.java_proj;

import com.java.java_proj.dto.response.forlist.LRepsonseUserPermission;
import com.java.java_proj.entities.Email;
import com.java.java_proj.entities.UserPermission;
import com.java.java_proj.entities.enums.PermissionAccessType;
import com.java.java_proj.repositories.EmailRepository;
import com.java.java_proj.repositories.UserPermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class JavaProjApplication implements CommandLineRunner {

    @Autowired
    private UserPermissionRepository userPermissionRepository;

    @Autowired
    private EmailRepository emailRepository;

    public static void main(String[] args) throws IOException {
        SpringApplication.run(JavaProjApplication.class, args);

    }

    @Override
    public void run(String... args) {
        this.autoUserPermission();
        this.autoEmailContent();
    }

    private void autoEmailContent() {
        List<Email> email = emailRepository.findAll();

        // check if email's exist
        if (email.isEmpty()) {
            Email emailAdd = Email.builder().subject("[FAMS] - Tài khoản được tạo thành công")
                    .body("<p>Hi, %s, </p>" +
                            "<p>Tài khoản đăng nhập vào hệ thống FAMS của bạn đã được tạo thành công. </p>" +
                            "<p>Vui lòng truy cập hệ thống theo thông tin sau: </p>" +
                            "<ul>" +
                            "<li>Username: %s</li>" +
                            "<li>Password: %s</li></ul>" +
                            "<p><b>Lưu ý</b>: Vui lòng thay đổi mật khẩu sau khi đăng nhập.</p>")
                    .build();

            emailRepository.save(emailAdd);
            System.out.println("Email content added!");
        }


    }

    private void autoUserPermission() {
        List<LRepsonseUserPermission> userPermissionList = userPermissionRepository.findAllBy();

        // check if user permission's exist
        if (userPermissionList.isEmpty()) {
            UserPermission superAdmin = new UserPermission();
            superAdmin.setRole("super_admin");
            superAdmin.setUserManagement(PermissionAccessType.FULL_ACCESS);
            superAdmin.setSyllabus(PermissionAccessType.FULL_ACCESS);
            superAdmin.setTrainingProgram(PermissionAccessType.FULL_ACCESS);
            superAdmin.setClassManagement(PermissionAccessType.FULL_ACCESS);
            superAdmin.setLearningMaterial(PermissionAccessType.FULL_ACCESS);
            // class admin
            UserPermission classAdmin = new UserPermission();
            classAdmin.setRole("class_admin");
            classAdmin.setUserManagement(PermissionAccessType.VIEW);
            classAdmin.setSyllabus(PermissionAccessType.FULL_ACCESS);
            classAdmin.setTrainingProgram(PermissionAccessType.FULL_ACCESS);
            classAdmin.setClassManagement(PermissionAccessType.FULL_ACCESS);
            classAdmin.setLearningMaterial(PermissionAccessType.FULL_ACCESS);
            // trainer
            UserPermission trainer = new UserPermission();
            trainer.setRole("trainer");
            trainer.setUserManagement(PermissionAccessType.VIEW);
            trainer.setSyllabus(PermissionAccessType.MODIFY);
            trainer.setTrainingProgram(PermissionAccessType.MODIFY);
            trainer.setClassManagement(PermissionAccessType.VIEW);
            trainer.setLearningMaterial(PermissionAccessType.VIEW);
            // adding to db
            userPermissionRepository.save(superAdmin);
            userPermissionRepository.save(classAdmin);
            userPermissionRepository.save(trainer);

            System.out.println("Adding missing user permissions!");
        }
    }
}
