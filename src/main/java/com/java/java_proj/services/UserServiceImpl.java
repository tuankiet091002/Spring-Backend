package com.java.java_proj.services;

import com.java.java_proj.dto.request.forcreate.CRequestUser;
import com.java.java_proj.dto.request.forupdate.URequestUser;
import com.java.java_proj.dto.request.security.RequestLogin;
import com.java.java_proj.dto.response.fordetail.DResponseUser;
import com.java.java_proj.entities.User;
import com.java.java_proj.entities.UserPermission;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.repositories.UserPermissionRepository;
import com.java.java_proj.repositories.UserRepository;
import com.java.java_proj.services.templates.UserService;
import com.java.java_proj.util.CustomUserDetail;
import com.java.java_proj.util.DateFormatter;
import com.java.java_proj.util.EmailSender;
import com.java.java_proj.util.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserPermissionRepository userPermissionRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private DateFormatter dateFormatter;

    private User getOwner() {
        try {
            return ((CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    @Transactional
    public Page<DResponseUser> getAllUser(Integer id, String name, String email,
                                          String orderBy, Integer page, Integer size, String orderDirection) {

        // if orderBy = role, need to access field of child class (Permission.role)
        Pageable paging = orderDirection.equals("ASC")
                ? PageRequest.of(page - 1, size, Sort.by(
                Objects.equals(orderBy, "role") ? "role.role" : orderBy).ascending())
                : PageRequest.of(page - 1, size, Sort.by(
                Objects.equals(orderBy, "role") ? "role.role" : orderBy).descending());

        return userRepository.findAllBy(id, name, email, paging);
    }

    @Override
    public DResponseUser createUser(CRequestUser requestUser) {

        User owner = getOwner();

        // check if email exist
        DResponseUser oldUser = userRepository.findByEmail(requestUser.getEmail());
        if (oldUser != null) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Email address is existed. Please check and input another email address.");
        }

        // check if phone exist
        if (userRepository.countByPhone(requestUser.getPhone()) > 0) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "User with that phone number is existed. Please pick another number.");
        }

        // create new User
        User user = new User();
        user.setName(requestUser.getName());
        user.setEmail(requestUser.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(requestUser.getPassword()));
        user.setPhone(requestUser.getPhone());
        user.setGender(requestUser.getGender());
        user.setDob(dateFormatter.formatDate((requestUser.getDob())));

        // find role
        UserPermission role = userPermissionRepository.findByRole(requestUser.getRole());
        if (role == null) {
            throw new HttpException(HttpStatus.NOT_FOUND, "Role not found.");
        }
        user.setRole(role);
        user.setIsActive(true);
        user.setCreatedDate(LocalDate.now());
        user.setCreatedBy(owner);


        userRepository.save(user);
        return userRepository.findByEmail(user.getEmail());
    }


    @Override
    @Transactional
    public DResponseUser updateUser(URequestUser requestUser) {

        User owner = getOwner();

        // get User from db
        User user = userRepository.findById(requestUser.getId())
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "User not found"));

        //get UserPermission by role from db
        UserPermission role = userPermissionRepository.findByRole(requestUser.getRole());
        if (role == null) {
            throw new HttpException(HttpStatus.NOT_FOUND, "Role not found.");
        }

        // check if phone exist
        if (userRepository.countByPhone(requestUser.getPhone()) > 0) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "User with that phone number is existed. Please pick another number.");
        }

        // Set value to user after update to db
        user.setRole(role);
        user.setName(requestUser.getName());
        user.setPassword(bCryptPasswordEncoder.encode(requestUser.getPassword()));
        user.setPhone(requestUser.getPhone());
        user.setDob(dateFormatter.formatDate(requestUser.getDob()));
        user.setGender(requestUser.getGender());
        user.setIsActive(requestUser.getIsActive());
        user.setModifiedDate(LocalDate.now());
        user.setModifiedBy(owner);

        // Save to db
        userRepository.save(user);
        return userRepository.findByEmail(user.getEmail());
    }

    @Override
    @Transactional
    public DResponseUser updateUserRole(Integer id, String newRole) {

        // check user
        User user = userRepository.findById(id)
                .orElseThrow(() -> new HttpException(HttpStatus.BAD_REQUEST, "User not found."));

        // check role
        UserPermission role = userPermissionRepository.findByRole(newRole);
        if (role == null) {
            throw new HttpException(HttpStatus.NOT_FOUND, "Role not found.");
        }

        userRepository.updateUserRole(id, role);

        return userRepository.findByEmail(user.getEmail());
    }

    @Override
    public User verifyUser(RequestLogin requestLogin) {
        User user = userRepository.findUserByEmail(requestLogin.getEmail());
        if (user == null) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "User not found.");
        }

        if (!bCryptPasswordEncoder.matches(requestLogin.getPassword(), user.getPassword())) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Wrong password.");
        }

        return user;
    }

}
