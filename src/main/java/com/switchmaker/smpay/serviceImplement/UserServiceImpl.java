package com.switchmaker.smpay.serviceImplement;

import com.switchmaker.smpay.constant.values.GlobalConstantsValues;
import com.switchmaker.smpay.dto.KeycloakUserCreationDto;
import com.switchmaker.smpay.entities.Customer;
import com.switchmaker.smpay.entities.Platform;
import com.switchmaker.smpay.entities.User;
import com.switchmaker.smpay.enumeration.UserAccountType;
import com.switchmaker.smpay.keycloak.KeycloakAdminAuthorization;
import com.switchmaker.smpay.keycloak.KeycloakUtil;
import com.switchmaker.smpay.repository.CostomerRepository;
import com.switchmaker.smpay.repository.PlatformRepository;
import com.switchmaker.smpay.repository.UserRepository;
import com.switchmaker.smpay.services.UserService;
import com.switchmaker.smpay.util.CodeGenerator;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.switchmaker.smpay.constant.values.GlobalConstantsValues.ACCOUNT_DEFAULT_PASSWORD;
import static com.switchmaker.smpay.constant.values.GlobalConstantsValues.DEVELOPER;
import static com.switchmaker.smpay.enumeration.UserAccountType.*;
import static com.switchmaker.smpay.keycloak.KeycloakConstantValues.WORK_REALM;
import static com.switchmaker.smpay.keycloak.KeycloakUrls.KEYCLOAK_CLIENT_REALM;

@Service
public class UserServiceImpl implements UserService {
  @Autowired
  UserRepository userRepository;
  @Autowired
  CostomerRepository costomerRepository;
  @Autowired
  private PlatformRepository platformRepository;
  @Override
  public com.switchmaker.smpay.util.Response saveUser(KeycloakUserCreationDto userCreationDto) {
    Keycloak keycloak = getKeycloakInstance();
      User user=new User();
      Customer customer=new Customer();
      String code= CodeGenerator.generate();
      String username = "";
      String password = "";
      switch (userCreationDto.getAccountType()) {
        case developer:
          if (!(userCreationDto.getCustomerId()==null)){
            customer=costomerRepository.findById(UUID.fromString(userCreationDto.getCustomerId())).get();
          }
          // Code pour le cas DEVELOPPER
          username =customer.getDenomination()+code;
          password=  (UUID.randomUUID().toString());
          break;
        case supervisor:
          // Code pour le cas SUPERVISOR
          if (!(userCreationDto.getCustomerId()==null)){
            customer=costomerRepository.findById(UUID.fromString(userCreationDto.getCustomerId())).get();
          }
          username=customer.getDenomination()+"@"+code;
          password=ACCOUNT_DEFAULT_PASSWORD;
          break;
        case application:
          if (!(userCreationDto.getCustomerId()==null)){
            user=userRepository.findById(UUID.fromString(userCreationDto.getCustomerId())).get();
          }
          // Code pour le cas APPLICATION
          username= userCreationDto.getFirstName()+"@"+code;
          password=ACCOUNT_DEFAULT_PASSWORD;
          break;
        case manager:
          // Code pour le cas APPLICATION
          username= userCreationDto.getFirstName()+"@"+code;
          password=ACCOUNT_DEFAULT_PASSWORD;
          break;
        default:
          // Code par défaut si le type ne correspond à aucun des cas précédents
          break;
      }
      UserRepresentation keycloakUser = createKeycloakUser(username,password,userCreationDto.getAccountType().toString());
      Response response = keycloak.realm(KEYCLOAK_CLIENT_REALM).users().create(keycloakUser);
      if (response.getStatus() == 201) {
        User userNew=new User();
        String userId = getUserIdFromLocation(response.getLocation().getPath());
        userNew.setCodeIDKeycloak(UUID.fromString(userId));
        userNew.setAccountType(userCreationDto.getAccountType());
        userNew.setCreationDate(LocalDateTime.now());
        userNew.setAccountActivated(false);
        userNew.setUsername(username);
          if (userCreationDto.getAccountType().compareTo(developer)==0 ){
            userNew.setDeveloperAccountSecret(password);
            userNew.setEmail(customer.getEmail());
            userNew.setCustomer(customer);
            userNew.setFirstConnexion(false);
          }
          else if (userCreationDto.getAccountType().compareTo(supervisor)==0 ) {
            userNew.setEmail(customer.getEmail());
            userNew.setFirstConnexion(true);
            userNew.setCustomer(customer);
          }
          else if (userCreationDto.getAccountType().compareTo(manager)==0 ) {
            userNew.setFirstName(userCreationDto.getFirstName());
            userNew.setEmail(userCreationDto.getEmail());
            userNew.setLastName(userCreationDto.getLastName());
            userNew.setFirstConnexion(true);
          }
          else if (userCreationDto.getAccountType().compareTo(application)==0 ) {
            userNew.setFirstName(userCreationDto.getFirstName());
            userNew.setEmail(userCreationDto.getEmail());
            userNew.setLastName(userCreationDto.getLastName());
            userNew.setFirstConnexion(true);
            Platform platform=platformRepository.findById(userCreationDto.getPlateformeId()).get();
            userNew.setCustomer(platform.getUser().getCustomer());
            userNew.getPlatforms().add(platform);
      }
        User userSaved=userRepository.save(userNew);
        return new com.switchmaker.smpay.util.Response(GlobalConstantsValues.SUCCESS_CODE,"Compte utilisateur crée avec succès !",userSaved);
      }else {
        return new com.switchmaker.smpay.util.Response(GlobalConstantsValues.SUCCESS_CODE,"Une erreur est survenue lors de la création du compte!",null);

      }
  }

  @Override
  public void accountHistory(Platform platform) {

  }

  private Keycloak getKeycloakInstance() {
    String token = KeycloakAdminAuthorization.getAdminToken();
    return KeycloakUtil.getKeycloakInstance(token);
  }

  private UserRepresentation createKeycloakUser(String Userusername, String Userpassword,String userType) {
    UserRepresentation keycloakUser = new UserRepresentation();
    keycloakUser.setUsername(Userusername);
   // keycloakUser.setEmail(CodeGenerator.generate()+user.getEmail());
    keycloakUser.setEnabled(true);
    CredentialRepresentation password = new CredentialRepresentation();
    if (userType.compareToIgnoreCase(DEVELOPER)==0){
      password.setTemporary(false);
    }else {
      password.setTemporary(false);
    }
   // password.setTemporary(userType.compareToIgnoreCase(DEVELOPER) != 0);
    password.setValue(Userpassword);
    password.setType(CredentialRepresentation.PASSWORD);

    keycloakUser.setCredentials(Arrays.asList(password));

    return keycloakUser;
  }

/*  private void addRoleToUser(Keycloak keycloak, String userId, String clientId, String roleName) {
    RealmResource realmResource = keycloak.realm(KEYCLOAK_CLIENT_REALM);

    RoleResource roleResource = realmResource.clients().get(clientId).roles().get(roleName);
    if (roleResource == null || !roleResource.toRepresentation().getName().equals(roleName)) {
      // Le rôle n'existe pas, vous pouvez le créer si nécessaire
      RoleRepresentation newRole = new RoleRepresentation();
      newRole.setName(roleName);
      realmResource.clients().get(clientId).roles().create(newRole);
    }

    keycloak.realm(KEYCLOAK_CLIENT_REALM).users().get(userId).roles().clientLevel(clientId).add(Arrays.asList(roleResource.toRepresentation()));
  }*/



  private String getUserIdFromLocation(String locationPath) {
    return locationPath.replaceAll(".*/([^/]+)$", "$1");
  }


  @Override
  public List<User> getAllUser() {
    return userRepository.findAll();
  }

  @Override
  public List<User> getUserByAccountType(UserAccountType accountType) {
    return userRepository.findAll().stream().filter(c->c.getAccountType().toString().compareToIgnoreCase(accountType.toString())==0).collect(Collectors.toList());
  }

  @Override
  public List<User> getUserByAccountStatus(Boolean status) {
    return userRepository.findAll().stream().filter(c->c.getAccountActivated().compareTo(status)==0).collect(Collectors.toList());

  }

  @Override
  public User updateUser(UUID userId,User user) {
    User userToUpdate=userRepository.findById(userId).get();
    if (!user.getFirstName().isEmpty()){
      userToUpdate.setFirstName(user.getFirstName());
    }  if (!user.getLastName().isEmpty()) {
      userToUpdate.setLastName(user.getLastName());
    }  if (!user.getEmail().isEmpty()) {
      userToUpdate.setEmail(user.getEmail());
    }
    userToUpdate.setId(userId);
    return userRepository.save(userToUpdate);
  }

  @Override
  public List<User> getCustomerAccounts(UUID customerId) {
    return userRepository.findByCustomer(costomerRepository.findById(customerId).get());
  }

  @Override
  public List<User> getCustomerApplicationAccounts(UUID customerId) {
    List<Platform> platforms=platformRepository.findByUser(userRepository.findById(customerId).get());
    List<User> users=new ArrayList<>();
    platforms.forEach(p->{
      userRepository.findAll().forEach(u->{
      if (u.getPlatforms().contains(p)){
        users.add(u);
      }
      });
    });

    return users;
  }

  @Override
  public List<User> getPlateformApplicationAccounts(UUID platformId) {
    Platform platform=platformRepository.findById(platformId).get();
    List<User> users=new ArrayList<>();
    userRepository.findAll().forEach(u->{
      if (u.getPlatforms().contains(platform)){
        users.add(u);
      }
    });
    return users;
  }

  @Override
  public List<User> getNeverConnexionAccount() {
    return userRepository.findAll().stream().filter(User::getFirstConnexion).collect(Collectors.toList());
  }

  @Override
  public User enableOrdisableAccount(UUID useId) {
    User user=userRepository.findById(useId).get();
    if (user.getAccountActivated()){
      user.setAccountActivated(false);
    }else {
      user.setAccountActivated(true);
    }
  return   userRepository.save(user);
  }

  @Override
  public User getUserById(UUID userId) {
    return userRepository.findById(userId).get();
  }

  @Override
  public User getManager(){
    return  userRepository.findAll().stream().filter(u->u.getAccountType().compareTo(manager)==0).findFirst().get();
  }

  @Override
  public void deletePlatform(UUID userId) {
    Optional<User> user=userRepository.findById(userId);
    user.ifPresent(value -> userRepository.delete(value));
    AuthzClient authzClient = AuthzClient.create();

//    // Utilisez la méthode obtainAccessToken pour obtenir un token d'accès
//    AccessTokenResponse response = authzClient.obtainAccessToken("username", "password");

  }

  @Override
  public User getKeycloakAdminUser() {
    Keycloak keycloak = getKeycloakInstance();

    // Récupérez les utilisateurs
    UsersResource usersResource = keycloak.realm(WORK_REALM).users();
    List<UserRepresentation> users = usersResource.search("administrateur");
 //userRepository.delete(userRepository.findAll().stream().filter(u->u.getAccountType().compareTo(administrator)==0).collect(Collectors.toList()).get(0));
    // Vérifiez si l'administrateur existe
    if (!users.isEmpty()) {
      for (UserRepresentation user : users) {
        if (user.getUsername().equals("administrateur")) {
        //  userRepository.delete(userRepository.findAll().stream().filter(u->u.getAccountType().compareTo(administrator)==0).collect(Collectors.toList()).get(0));
          User userSaved=User.builder()
            .firstName(user.getFirstName())
            .codeIDKeycloak(UUID.fromString(user.getId()))
            .accountType(administrator)
            .accountActivated(true)
            .username(user.getUsername())
            .lastName(user.getLastName())
            .email(user.getEmail())
            .build();
          userRepository.save(userSaved);
          return userSaved;
        }
      }
    }

    return null;
  }
  private UserRepresentation getKeycloakUser(String username) {
    // Obtenez une instance de Keycloak
    Keycloak keycloak = getKeycloakInstance();

    // Obtenez le realm
    RealmResource realmResource = keycloak.realm(WORK_REALM);

    // Obtenez les utilisateurs
    UsersResource usersResource = realmResource.users();

    // Obtenez l'utilisateur par son nom d'utilisateur
    List<UserRepresentation> users = usersResource.search(username);

    // Vérifiez si l'utilisateur existe
    if (users == null || users.isEmpty()) {
      throw new NotFoundException("Utilisateur non trouvé");
    }

    // Retournez le premier utilisateur trouvé
    return users.get(0);
  }

  private void updateKeycloakUser(UserRepresentation user) {
    // Obtenez une instance de Keycloak
    Keycloak keycloak = getKeycloakInstance();

    // Obtenez le realm
    RealmResource realmResource = keycloak.realm(WORK_REALM);

    // Obtenez les utilisateurs
    UsersResource usersResource = realmResource.users();

    // Mettez à jour l'utilisateur
    usersResource.get(user.getId()).update(user);
  }


  private boolean changeKeycloakUserPassword(String username, String newPassword) {
    try {
      // Obtenez l'utilisateur existant
      UserRepresentation keycloakUser = getKeycloakUser(username);

      // Créez une nouvelle représentation d'informations d'identification pour le nouveau mot de passe
      CredentialRepresentation password = new CredentialRepresentation();
      password.setTemporary(false);
      password.setValue(newPassword);
      password.setType(CredentialRepresentation.PASSWORD);

      // Définissez les nouvelles informations d'identification pour l'utilisateur
      keycloakUser.setCredentials(Arrays.asList(password));

      // Mettez à jour l'utilisateur dans Keycloak
      updateKeycloakUser(keycloakUser);

      // Si aucune exception n'a été levée, l'opération a réussi
      return true;
    } catch (Exception e) {
      // Si une exception a été levée, l'opération a échoué
      return false;
    }
  }



  @Override
  public User generateDevelopperApiKey(UUID developerId) {
    User user=userRepository.findById(developerId).get();
String pass=UUID.randomUUID().toString();
    boolean success = changeKeycloakUserPassword(user.getUsername(), UUID.randomUUID().toString());
User userSaved=new User();
    if (success) {
      // La modification du mot de passe a réussi
      user.setDeveloperAccountSecret(pass);
      user.setId(developerId);
    userSaved = userRepository.save(user);
    }

    return userSaved;
  }

}
