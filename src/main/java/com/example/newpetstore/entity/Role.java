package com.example.newpetstore.entity;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author Simon Pirko on 28.06.23
 */
public enum Role implements GrantedAuthority {
  USER, ADMIN;

  @Override
  public String getAuthority() {
    return name();
  }
}
