package com.switchmaker.smpay.controller;

import com.switchmaker.smpay.constant.values.GlobalConstantsValues;
import com.switchmaker.smpay.entities.Role;
import com.switchmaker.smpay.entities.User;
import com.switchmaker.smpay.serviceImplement.RoleServiceImpl;
import com.switchmaker.smpay.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.UUID;

import static com.switchmaker.smpay.constant.urls.RootUrl.ROOT_API;
import static com.switchmaker.smpay.constant.values.GlobalConstantsValues.*;

@CrossOrigin("*")
@RestController
@RequestMapping(ROOT_API)
public class RoleController {
  @Autowired
  private RoleServiceImpl roleService;
/*  @RolesAllowed(ADMIN)
  @PostMapping(path = "/role/create")
  public Response createRole(@RequestParam String libelle){
Role role=roleService.createRole(libelle);
if (!role.getKeycloakRoleCode().isEmpty()){
  return new Response(GlobalConstantsValues.SUCCESS_CODE,"role crée avec succès",role);
}else {
  return new Response(GlobalConstantsValues.FAILURE_CODE,"Une erreur est survenue",null);
}
  }*/
  @RolesAllowed({ADMIN,MANAGER})
  @GetMapping(path = "/roles")
  public Response getAllRole(){
   //List<Role> roles=roleService.getAllRole();
    List<Role> roles=roleService.getAllRole();
      return new Response(GlobalConstantsValues.SUCCESS_CODE,"roles recupérés avec succès",roles);
  }


  @RolesAllowed({ADMIN,SUPERVISOR})
  @GetMapping(path = "/role-to-user")
  public Response assignRoleToUser(@RequestParam UUID user){
  User userSaveed= roleService.addRoleToUser(user);
    return new Response(GlobalConstantsValues.SUCCESS_CODE,"role associé avec succès",userSaveed);
  }

  @RolesAllowed({ADMIN,MANAGER})
  @GetMapping(path = "/pull-keycloak-role")
  public Response pullRolesOnKeycloak(){
    roleService.pullRoleFromKeycloakToBD();
    return new Response(GlobalConstantsValues.SUCCESS_CODE,"récupération éffectué avec syccès",null);
  }
}
