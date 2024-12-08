package com.switchmaker.smpay.services;

import com.switchmaker.smpay.entities.Role;
import com.switchmaker.smpay.entities.User;

import java.util.List;
import java.util.UUID;

public interface RoleService {
 // public Role createRole(String role);
 // public Role updateRole(UUID roleId, Role role);
  public List<Role> getAllRole();
  //public void  deleteRole(UUID roleId);

  public User addRoleToUser(UUID userId);

  public List<Role> pullRoleFromKeycloakToBD();
}
