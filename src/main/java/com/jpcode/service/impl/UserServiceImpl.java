package com.jpcode.service.impl;

import com.jpcode.entity.User;
import com.jpcode.repository.RoleRepository;
import com.jpcode.repository.UserRepository;
import com.jpcode.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private Boolean exist(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public Long createEntity(User user) {
        if (exist(user.getEmail())) {
            return null;
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(roleRepository.findById(1L).get());

        return userRepository.save(user).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public User getEntity(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getEntityList() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void updateEntity(Long id, User entity) {
        userRepository.findById(id)
                .map(userJpa -> {
                    Optional.ofNullable(entity.getFirstName()).ifPresent(userJpa::setFirstName);
                    Optional.ofNullable(entity.getLastName()).ifPresent(userJpa::setLastName);

                    if (entity.getPassword() != null) {
                        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
                    }
                    Optional.ofNullable(entity.getPassword()).ifPresent(userJpa::setPassword);

                    return userRepository.save(userJpa);
                }).orElse(null);
    }

    @Override
    @Transactional
    public void deleteEntityById(Long id) {
        userRepository.findById(id).ifPresent(userRepository::delete);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("username %s not found".formatted(email)));
    }

}
