package com.jpcode.service;

import com.jpcode.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    Long createEntity(User user);

    User getEntity(Long id);

    List<User> getEntityList();

    void updateEntity(Long id, User entity);

    void deleteEntityById(Long id);

}
