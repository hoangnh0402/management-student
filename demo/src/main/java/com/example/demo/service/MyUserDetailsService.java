package com.example.demo.service;


import com.example.demo.config.InvalidException;
import com.example.demo.domain.model.Role;
import com.example.demo.domain.model.User;
import com.example.demo.repo.RoleRepo;
import com.example.demo.repo.UserRepo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class MyUserDetailsService implements UserDetailsService {
  private final UserRepo userRepository;
  private final RoleRepo roleRepo;

  public MyUserDetailsService(UserRepo userRepository, RoleRepo roleRepo) {
    this.userRepository = userRepository;
    this.roleRepo = roleRepo;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.getUserByUsername(username);
    if (user == null) {
      throw new InvalidException("Invalid username: " + username);
    }
    Role role = roleRepo.getRoleByRoleId(user.getIdRole());
    Set<GrantedAuthority> grantedAuthorities = Set.of(new SimpleGrantedAuthority(role.getNameRole()));

    return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
        grantedAuthorities);
  }

}
