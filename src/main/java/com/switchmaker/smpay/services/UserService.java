package com.switchmaker.smpay.services;

import com.switchmaker.smpay.dto.KeycloakUserCreationDto;
import com.switchmaker.smpay.entities.Platform;
import com.switchmaker.smpay.entities.User;
import com.switchmaker.smpay.enumeration.UserAccountType;
import com.switchmaker.smpay.util.Response;

import java.util.List;
import java.util.UUID;

public interface UserService {
  public Response saveUser(KeycloakUserCreationDto userCreationDto);

    void accountHistory(Platform platform);

    public List<User> getAllUser();
  List<User> getUserByAccountType(UserAccountType accountType);
  List<User> getUserByAccountStatus(Boolean status);
  public User updateUser(UUID userId, User user);
  public List<User> getCustomerAccounts(UUID customerId);
  public List<User> getCustomerApplicationAccounts(UUID customerId);
  public List<User> getPlateformApplicationAccounts(UUID platformId);
  public  List<User> getNeverConnexionAccount();
  public User enableOrdisableAccount(UUID userId);
  public User getUserById(UUID userId);

  User getManager();

  public void deletePlatform(UUID userId);

  public User getKeycloakAdminUser();

  public User generateDevelopperApiKey(UUID developerId);
}
