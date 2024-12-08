package com.switchmaker.smpay.controller;

import com.switchmaker.smpay.constant.values.GlobalConstantsValues;
import com.switchmaker.smpay.dto.KeycloakUserCreationDto;
import com.switchmaker.smpay.entities.User;
import com.switchmaker.smpay.enumeration.UserAccountType;
import com.switchmaker.smpay.serviceImplement.UserServiceImpl;
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
public class UserController {
  @Autowired
  private UserServiceImpl userService;
  @RolesAllowed({ADMIN,SUPERVISOR,MANAGER})
  @PostMapping(value = "/user/create")
  public Response createUser(@RequestBody KeycloakUserCreationDto userCreationDto){


      Response user=userService.saveUser(userCreationDto);
      if (!(user ==null)){
        return new  Response(GlobalConstantsValues.SUCCESS_CODE,"Compte "+userCreationDto+ " " +"+crée avec succès",user);

      }

      return new  Response(GlobalConstantsValues.FAILURE_CODE,"Une erreur est survenue lors de la création du compte",null);
    }

  @RolesAllowed({ADMIN,SUPERVISOR,MANAGER})
  @GetMapping(value = "/all-users")
  public Response getUser() {
    List<User> users = userService.getAllUser();
    if (!(users == null)) {
      return new Response(GlobalConstantsValues.SUCCESS_CODE, "Liste récupérée avec succès", users);
    } else {
      return new Response(GlobalConstantsValues.SUCCESS_CODE, "Une erreur s'est produite!", null);
    }

  }
  @RolesAllowed({ADMIN,SUPERVISOR,MANAGER})
  @GetMapping(value = "/users")
  public Response getUserByType(@RequestParam UserAccountType compte) {
    List<User> users = userService.getUserByAccountType(compte);
    if (!(users == null)) {
      return new Response(GlobalConstantsValues.SUCCESS_CODE, "Liste récupérée avec succès", users);
    } else {
      return new Response(GlobalConstantsValues.SUCCESS_CODE, "Une erreur s'est produite!", null);
    }

  }
  @RolesAllowed({ADMIN,SUPERVISOR,MANAGER})
  @GetMapping(value = "/users/activate-or-not")
  public Response getUserByStatus(@RequestParam Boolean status) {
    List<User> users = userService.getUserByAccountStatus(status);
    if (!(users == null)) {
      return new Response(GlobalConstantsValues.SUCCESS_CODE, "Liste récupérée avec succès", users);
    } else {
      return new Response(GlobalConstantsValues.SUCCESS_CODE, "Une erreur s'est produite!", null);
    }

  }
  @RolesAllowed({ADMIN,SUPERVISOR,MANAGER})
  @PutMapping(value = "/user/update")
  public Response updateUser(@RequestParam UUID userId,@RequestBody User user) {
    User userSaved = userService.updateUser(userId,user);
    if (!(userSaved == null)) {
      return new Response(GlobalConstantsValues.SUCCESS_CODE, "Utilisateur mise à jour", userSaved);
    } else {
      return new Response(GlobalConstantsValues.SUCCESS_CODE, "Une erreur s'est produite!", null);
    }

  }

  @RolesAllowed({ADMIN,SUPERVISOR,MANAGER})
  @GetMapping("/user/customer")
  public Response getCustomerUser(@RequestParam UUID customerId){
    List<User>  users= userService.getCustomerAccounts(customerId);
    if (users!=null){
      return new Response(SUCCESS_CODE,"Liste des comptes récupéré avec succeès",users);
    }else {
      return new Response(FAILURE_CODE,"une erreur est survenue",null);
    }
  }

  @RolesAllowed({ADMIN,SUPERVISOR,MANAGER})
  @GetMapping("/user/customer-apps-accounts")
  public Response getCustomerAppsAccount(@RequestParam UUID customerId){
    List<User>  users= userService.getCustomerApplicationAccounts(customerId);
    if (users!=null){
      return new Response(SUCCESS_CODE,"Liste des comptes récupéré avec succeès",users);
    }else {
      return new Response(FAILURE_CODE,"une erreur est survenue",null);
    }
  }

  @RolesAllowed({ADMIN,SUPERVISOR,MANAGER})
  @GetMapping("/user/platform")
  public Response getPlatformAppsAccount(@RequestParam UUID platformId){
    List<User>  users= userService.getPlateformApplicationAccounts(platformId);
    if (users!=null){
      return new Response(SUCCESS_CODE,"Liste des comptes récupéré avec succeès",users);
    }else {
      return new Response(FAILURE_CODE,"une erreur est survenue",null);
    }
  }

  @RolesAllowed({ADMIN,SUPERVISOR,MANAGER})
  @GetMapping("/user/no-used-account")
  public Response noUsedAccount(){
    List<User> users= userService.getNeverConnexionAccount();
    if (users!=null){
      return new Response(SUCCESS_CODE,"Liste des comptes récupéré avec succeès",users);
    }else {
      return new Response(FAILURE_CODE,"une erreur est survenue",null);
    }
  }

  @RolesAllowed({ADMIN,SUPERVISOR,MANAGER})
  @GetMapping("/user/enable-disable")
  public Response changeUserAccountSatus(@RequestParam UUID userId){
    User user= userService.enableOrdisableAccount(userId);
    if (user!=null){
      return new Response(SUCCESS_CODE,"compte modifié avec succeès",user);
    }else {
      return new Response(FAILURE_CODE,"une erreur est survenue",null);
    }
  }



 // @RolesAllowed(ADMIN)
  @GetMapping("/user/load-keycloak-admin")
  public Response getAdminFromKeycloak(){
    User user= userService.getKeycloakAdminUser();
    if (user!=null){
      return new Response(SUCCESS_CODE,"compte charger  avec succeès",user);
    }else {
      return new Response(FAILURE_CODE,"une erreur est survenue",null);
    }
  }
  @RolesAllowed({ADMIN,SUPERVISOR,MANAGER})
  @GetMapping("/user/get-api-key")
  public Response getApiKey(@RequestParam UUID developer){
    User user= userService.generateDevelopperApiKey(developer);
    if (user!=null){
      return new Response(SUCCESS_CODE,"clé génerer  avec succeès",user);
    }else {
      return new Response(FAILURE_CODE,"une erreur est survenue",null);
    }
  }

  @RolesAllowed({ADMIN})
  @GetMapping("/user/get-manager")
  public Response getManager(){
    User user= userService.getManager();
    if (user!=null){
      return new Response(SUCCESS_CODE,"gestionnaire récupéré avec succès",user);
    }else {
      return new Response(FAILURE_CODE,"une erreur est survenue",null);
    }
  }
}
