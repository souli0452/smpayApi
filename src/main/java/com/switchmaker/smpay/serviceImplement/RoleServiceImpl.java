package com.switchmaker.smpay.serviceImplement;

import com.switchmaker.smpay.entities.Role;
import com.switchmaker.smpay.entities.User;
import com.switchmaker.smpay.keycloak.KeycloakAdminAuthorization;
import com.switchmaker.smpay.keycloak.KeycloakUtil;
import com.switchmaker.smpay.repository.RoleRepository;
import com.switchmaker.smpay.repository.UserRepository;
import com.switchmaker.smpay.services.RoleService;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.switchmaker.smpay.keycloak.KeycloakConstantValues.WORK_REALM;

@Service
public class RoleServiceImpl implements RoleService {
  @Autowired
  private RoleRepository roleRepository;
  @Autowired
  private UserRepository userRepository;
/*  @Override
  public Role createRole(String libelle) {
    Keycloak keycloak = getKeycloakInstance();

    // Créez une représentation de rôle
    RoleRepresentation roleRepresentation = new RoleRepresentation();
    roleRepresentation.setName(libelle);
    roleRepresentation.setDescription("role " + libelle);

    // Récupérez le client
    ClientsResource clientsResource = keycloak.realm("switch-maker").clients();
    List<ClientRepresentation> clients = clientsResource.findAll();
    ClientRepresentation clientApp=clients.stream().filter(c->c.getClientId().compareToIgnoreCase("smpay")==0).findFirst().get();
    clientsResource.get(clientApp.getId()).roles().create(roleRepresentation);
    RoleResource roleResource=clientsResource.get(clientApp.getId()).roles().get(libelle);

    // Récupérez l'identifiant du rôle
    RoleRepresentation createdRole = roleResource.toRepresentation();
    String roleId = createdRole.getId();

    // Enregistrez le rôle dans votre base de données
    Role role = new Role();
    role.setLibelle(libelle);
    role.setKeycloakRoleCode(roleId); // Utilisez l'identifiant du rôle ici
    Role roleSaved=roleRepository.save(role);
    return roleSaved;
  }*/







  private Keycloak getKeycloakInstance() {
   String token = KeycloakAdminAuthorization.getAdminToken();
    return KeycloakUtil.getKeycloakInstance(token);
  }



 /* @Override
  public Role updateRole(UUID roleId, Role role) {
    return null;
  }*/

  @Override
  public List<Role> getAllRole() {
    return roleRepository.findAll();
  }

 /* @Override
  public void deleteRole(UUID roleId) {

  }*/

  @Override
  public User addRoleToUser(UUID userId) {
    User user=userRepository.findById(userId).get();
    //Role role=roleRepository.findById(user.getAccountType().toString().get();
    Keycloak keycloak = getKeycloakInstance();

    // Récupérez le client
    ClientsResource clientsResource = keycloak.realm(WORK_REALM).clients();
    List<ClientRepresentation> clients = clientsResource.findAll();
    ClientRepresentation clientApp=clients.stream().filter(c->c.getClientId().compareToIgnoreCase("smpay")==0).findFirst().get();

    // Récupérez le rôle
    RoleResource roleResource = clientsResource.get(clientApp.getId()).roles().get(user.getAccountType().toString());
    RoleRepresentation roleToAssign = roleResource.toRepresentation();

    // Récupérez l'utilisateur
    UserResource userResource = keycloak.realm("switch-maker").users().get(String.valueOf(user.getCodeIDKeycloak()));

    // Associez le rôle à l'utilisateur
    userResource.roles().clientLevel(clientApp.getId()).add(Arrays.asList(roleToAssign));
    user.setAccountActivated(true);
    return userRepository.save(user);
  }

  @Override
  public List<Role> pullRoleFromKeycloakToBD() {
    Keycloak keycloak = getKeycloakInstance();

    // Récupérez le client
    ClientsResource clientsResource = keycloak.realm(WORK_REALM).clients();
    List<ClientRepresentation> clients = clientsResource.findAll();
    ClientRepresentation clientApp=clients.stream().filter(c->c.getClientId().compareToIgnoreCase("smpay")==0).findFirst().get();

    // Récupérez tous les rôles du client
    List<RoleRepresentation> allRoles = clientsResource.get(clientApp.getId()).roles().list();

    // Créez une liste pour stocker les rôles personnalisés
    List<RoleRepresentation> customRoles = new ArrayList<>();
    roleRepository.deleteAll();
    // Parcourez tous les rôles et ajoutez les rôles personnalisés à la liste
if (!allRoles.isEmpty()){
  for (RoleRepresentation role : allRoles) {
    if (!isDefaultRole(role)) {
      Role roleToSaved=Role.builder()
        .clientRole(role.getClientRole())
        .composite(role.isComposite())
        .containerId(role.getContainerId())
        .description(role.getDescription())
        .name(role.getName())
        .build();
      roleRepository.save(roleToSaved);
    }
  }
}
return  roleRepository.findAll();
  }

  // Cette méthode vérifie si un rôle est un rôle par défaut
  public boolean isDefaultRole(RoleRepresentation role) {
    if (role.getName().compareToIgnoreCase("uma_protection")==0){
      return  true;
    }
    return false; // Retournez true si le rôle est un rôle par défaut, sinon retournez false
  }
}
