package com.switchmaker.smpay.controller;

import com.switchmaker.smpay.dto.EmailMessage;
import com.switchmaker.smpay.dto.Identifiers;
import com.switchmaker.smpay.dto.ResponseMessage;
import com.switchmaker.smpay.entities.Platform;
import com.switchmaker.smpay.entities.User;
import com.switchmaker.smpay.entities.ValidateCode;
import com.switchmaker.smpay.keycloak.*;
import com.switchmaker.smpay.repository.CodeRepository;
import com.switchmaker.smpay.repository.CostomerRepository;
import com.switchmaker.smpay.repository.PlatformRepository;
import com.switchmaker.smpay.repository.UserRepository;
import com.switchmaker.smpay.util.Response;
import com.switchmaker.smpay.utilsClass.UtilsClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.security.RolesAllowed;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.*;

import static com.switchmaker.smpay.constant.urls.AuthorizationUrls.*;
import static com.switchmaker.smpay.constant.urls.RootUrl.ROOT_API;
import static com.switchmaker.smpay.constant.values.GlobalConstantsValues.*;
import static com.switchmaker.smpay.keycloak.KeycloakConstantValues.*;
import static com.switchmaker.smpay.keycloak.KeycloakUrls.*;

@CrossOrigin("*")
@RestController
public class AuthorizationController {

	@Autowired
	CostomerRepository costomerRepository;
	@Autowired
	CodeRepository codeRepository;
	@Autowired
	PlatformRepository platformRepository;
	@Autowired
	UserRepository userRepository;




	/*-------------------------------------------------------/
	/*       Créer un compte applicatif sur keycloack       /
	/*----------------------------------------------------*/

	@RolesAllowed({ADMIN, USER})
	@PostMapping(ROOT_API + "/create/platform/account")
	public ResponseEntity<?> createPlatformAccount(
			@RequestBody Platform platform) {
		try {
			String token = KeycloakAdminAuthorization.getAdminToken();
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setBearerAuth(token);
			KeycloakUser body = new KeycloakUser();
			KeycloakPasswordReset password = new KeycloakPasswordReset();
			password.setType(GRANT_TYPE);
			password.setValue(DEFAULT_PASSWORD);
			List<KeycloakPasswordReset> credential = new ArrayList<KeycloakPasswordReset>();
			credential.add(password);
			List<String> roles = new ArrayList<String>();
			roles.add(MB_USER);
			//  body.setEmail(platform.getEmail());
			//  body.setUsername(platform.getPlatformUsername());
			body.setCredentials(credential);
			body.setRealmRoles(roles);
			HttpEntity<KeycloakUser> entity = new HttpEntity<>(body, headers);
			ResponseEntity<String> response = restTemplate.exchange(KEYCLOAK_USERS_URL, HttpMethod.POST, entity, String.class);
			if (response.getStatusCodeValue() == 201) {
				//  platform.setAccountCreationDate(LocalDateTime.now());
				platformRepository.save(platform);
				return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(SUCCESS_CODE, "Succès"), HttpStatus.OK);
			}
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode().value() == 409) {
				return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE, "Username ou email existe déjà"), HttpStatus.OK);
			}
			return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE, "error"), HttpStatus.OK);
		}

		return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE, "echec"), HttpStatus.OK);
	}


  /*----------------------------------------------------------------/
	/*            Valider un compte applicatif sur keycloak          /
	/*-------------------------------------------------------------*/
  /*@RolesAllowed({ADMIN, USER})
  @PostMapping(ROOT_API + "/validate/platform/account")
  public ResponseEntity<?> validatePlatformAccount(@RequestBody Platform platform) {
    try {
      String token = KeycloakAdminAuthorization.getAdminToken();
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.setBearerAuth(token);
      HttpEntity<?> httpEntity = new HttpEntity<>(headers);
      ResponseEntity<List<KeycloakUserList>> usersResponse = restTemplate.exchange(KEYCLOAK_USERS_URL, HttpMethod.GET, httpEntity, new ParameterizedTypeReference<List<KeycloakUserList>>() {
      });
      if (usersResponse.getStatusCodeValue() == 200) {
        List<KeycloakUserList> userList = usersResponse.getBody();
        for (KeycloakUserList user : userList) {
          if (platform.getPlatformUsername().equals(user.getUsername())) {
            KeycloakRole role = new KeycloakRole();
            List<KeycloakRole> roles = new ArrayList<KeycloakRole>();
            roles.add(role);
            HttpEntity<List<KeycloakRole>> entity = new HttpEntity<>(roles, headers);
            ResponseEntity<String> response = restTemplate.exchange(KEYCLOAK_USERS_URL + "/" + user.getId() + "/role-mappings/realm", HttpMethod.POST, entity, String.class);
            if (response.getStatusCodeValue() == 204) {
              platform.setAccountId(user.getId().toString());
              platformRepository.save(platform);
              return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(SUCCESS_CODE, "Succès"), HttpStatus.OK);
            }
          }
        }
      }
      return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE, "echec"), HttpStatus.OK);
    } catch (HttpClientErrorException e) {
      return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE, "erreur"), HttpStatus.OK);
    }
  }*/



	/*----------------------------------------------------------------/
	/*           Modifier un compte applicatif sur keycloak          /
	/*-------------------------------------------------------------*/

	@RolesAllowed({ADMIN, USER})
	@PostMapping(ROOT_API + "/update/platform/account")
	public ResponseEntity<?> updatePlatformAccount(@RequestBody Platform platform) {
		try {
			String token = KeycloakAdminAuthorization.getAdminToken();
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setBearerAuth(token);
			HttpEntity<?> httpEntity = new HttpEntity<>(headers);
			ResponseEntity<KeycloakUserList> userResponse = restTemplate.exchange(KEYCLOAK_USERS_URL + "/", /* + platform.getAccountId(), */ HttpMethod.GET, httpEntity, new ParameterizedTypeReference<KeycloakUserList>() {
			});
			if (userResponse.getStatusCodeValue() == 200) {
				KeycloakUserList user = userResponse.getBody();
				//  user.setEmail(platform.getEmail());
				HttpEntity<KeycloakUserList> entity = new HttpEntity<>(user, headers);
				ResponseEntity<String> response = restTemplate.exchange(KEYCLOAK_USERS_URL + "/" + user.getId(), HttpMethod.PUT, entity, String.class);
				if (response.getStatusCodeValue() == 204) {
					//  platform.setAccountId(user.getId().toString());
					platformRepository.save(platform);
					return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(SUCCESS_CODE, "Succès"), HttpStatus.OK);
				}
			}
			return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE, "echec"), HttpStatus.OK);
		} catch (HttpClientErrorException e) {
			return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE, "erreur"), HttpStatus.OK);
		}
	}



	/*----------------------------------------------------------------/
	/*          Supprimer un compte applicatif sur keycloak          /
	/*-------------------------------------------------------------*/

/*
  @RolesAllowed({ADMIN, USER})
  @PostMapping(ROOT_API + "/delete/platform/account")
  public ResponseEntity<?> deletePlatformAccount(@RequestBody Platform platform) {
    try {
      String token = KeycloakAdminAuthorization.getAdminToken();
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<?> httpEntity = new HttpEntity<>(headers);

      if (platform.getAccountId() == null) {
        ResponseEntity<List<KeycloakUserList>> usersResponse = restTemplate.exchange(KEYCLOAK_USERS_URL, HttpMethod.GET, httpEntity, new ParameterizedTypeReference<List<KeycloakUserList>>() {
        });
        if (usersResponse.getStatusCodeValue() == 200) {
          List<KeycloakUserList> userList = usersResponse.getBody();
          for (KeycloakUserList user : userList) {
            if (platform.getPlatformUsername().equals(user.getUsername())) {
              platform.setAccountId(user.getId().toString());
              break;
            }
          }
        }
      }

      ResponseEntity<Void> response = restTemplate.exchange(KEYCLOAK_USERS_URL + "/" + platform.getAccountId(), HttpMethod.DELETE, httpEntity, Void.class);
      if (response.getStatusCodeValue() == 204) {
        platform.setAccountId(null);
        platform.setAccountCreationDate(null);
        platform.setEmail(null);
        platform.setPlatformUsername(null);
        platformRepository.save(platform);
        return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(SUCCESS_CODE, "Succès"), HttpStatus.OK);
      }
      return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE, "echec"), HttpStatus.OK);
    } catch (HttpClientErrorException e) {
      return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE, "erreur"), HttpStatus.OK);
    }
  }
*/


	@PostMapping(ADMIN_ACCESS_TOKEN)
	public Response userAdminAccessToken(@RequestBody Identifiers userIdentifiers) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
			map.add("client_id", CLIENT_ID);
			map.add("grant_type", GRANT_TYPE);
			map.add("client_secret", CLIENT_SECRET);
			map.add("username", userIdentifiers.getUsername());
			map.add("password", userIdentifiers.getPassword());
			// if (userRepository.findByUsername(userIdentifiers.getUsername()).get().getAccountActivated().compareTo(Boolean.TRUE) == 0) {
			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
			ResponseEntity<String> response = restTemplate.exchange(KEYCLOAK_CLIENT_AUTHORIZATION, HttpMethod.POST, entity, String.class);
			if (response.getStatusCodeValue() == 200) {
				User user= User.builder().firstName(userIdentifiers.getUsername()).build();
				return new Response(SUCCESS_CODE, "accès autorisé", TokenResponse.accessToken(response.getBody(), user));
				//} else {
				//  return new Response(FAILURE_CODE, "access denied", null);
			}
			// }

		} catch (Exception e)
		{
			return new Response(FAILURE_CODE, "access denied", null);
		}

		return new Response(FAILURE_CODE,"access denied",null);

	}




	/*------------------------------------------------/
    /*            Accès Token DEEVELOPER            /
    /*---------------------------------------------*/
	@PostMapping(DEVELOPER_ACCESS_TOKEN)
	public Response ClientAccessToken(@RequestBody Identifiers developerIdentifiers) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
			map.add("client_id", CLIENT_ID);
			map.add("grant_type", GRANT_TYPE);
			map.add("client_secret", CLIENT_SECRET);
			map.add("username", developerIdentifiers.getClient());
			map.add("password", developerIdentifiers.getSecret());
			//if (userRepository.findByUsername(developerIdentifiers.getUsername()).get().getAccountActivated().compareTo(true)==0){
			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
			ResponseEntity<String> response = restTemplate.exchange(KEYCLOAK_CLIENT_AUTHORIZATION, HttpMethod.POST, entity, String.class);
			if (response.getStatusCodeValue() == 200) {

				return new Response(SUCCESS_CODE, "accès autorisé", TokenResponse.accessToken(response.getBody(), null));
			}
		} catch (Exception e) {
			return new Response(FAILURE_CODE, "access denied", null);
		}

		return new Response(FAILURE_CODE, "access denied", null);
	}


	/*------------------------------------------/
	/*            User Access token            /
	/*---------------------------------------*/


	@PostMapping(USER_ACCESS_TOKEN)
	public Response userAccessToken(@RequestBody Identifiers userIdentifiers) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
			map.add("client_id", CLIENT_ID);
			map.add("grant_type", GRANT_TYPE);
			map.add("client_secret", CLIENT_SECRET);
			map.add("username", userIdentifiers.getUsername());
			map.add("password", userIdentifiers.getPassword());
		if (userRepository.findByUsername(userIdentifiers.getUsername()).get().getAccountActivated().compareTo(Boolean.TRUE) == 0) {
				HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
				ResponseEntity<String> response = restTemplate.exchange(KEYCLOAK_CLIENT_AUTHORIZATION, HttpMethod.POST, entity, String.class);
				if (response.getStatusCodeValue() == 200) {
          User user = userRepository.findByUsername(userIdentifiers.getUsername()).get();
          return new Response(SUCCESS_CODE, "accès autorisé", TokenResponse.accessToken(response.getBody(), user));
        }
				} else {
					return new Response(FAILURE_CODE, "access denied", null);
				}


		} catch (Exception e)
		{
			return new Response(FAILURE_CODE, "access denied", e.getMessage());
		}

		return new Response(FAILURE_CODE,"access denied",null);

	}



	/*----------------------------------/
	/*          Reset password         /
	/*-------------------------------*/
//	@RolesAllowed({ADMIN,USER})
	@PostMapping(USER_RESET_PASSWORD)
	public ResponseEntity<?> resetPassword(@PathVariable("id") String id, @RequestBody KeycloakPasswordReset body){
		try {
			String token = KeycloakAdminAuthorization.getAdminToken();
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setBearerAuth(token);
			HttpEntity<KeycloakPasswordReset> entity = new HttpEntity<>(body,headers);
			User user=userRepository.findById(UUID.fromString(id)).get();
			ResponseEntity<String> response = restTemplate.exchange(KEYCLOAK_RESET_PASSWORD + user.getCodeIDKeycloak() + "/reset-password", HttpMethod.PUT, entity, String.class);
			if (response.getStatusCodeValue() == 204) {
				return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(SUCCESS_CODE,"success"),HttpStatus.OK);
			}
		}catch (Exception e) {
			return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"error"),HttpStatus.OK);
		}

		return new ResponseEntity<ResponseMessage>(UtilsClass.responseMessage(FAILURE_CODE,"echec"),HttpStatus.OK);
	}



	/*--------------------------------------------------/
	/*          Change password first connexion        /
	/*----------------------------------------------*/
	//@RolesAllowed(USER)
	@PostMapping(USER_CHANGE_PASSWORD)
	public ResponseEntity<?> changePassword(
			@PathVariable("id") String id,
			/*	@PathVariable("clientId") UUID clientId,*/
			@RequestBody KeycloakPasswordReset body){
		try {
			String token = KeycloakAdminAuthorization.getAdminToken();
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setBearerAuth(token);
			HttpEntity<KeycloakPasswordReset> entity = new HttpEntity<>(body,headers);
			User user=userRepository.findById(UUID.fromString(id)).get();
			ResponseEntity<String> response = restTemplate.exchange(KEYCLOAK_RESET_PASSWORD + user.getCodeIDKeycloak() + "/reset-password", HttpMethod.PUT, entity, String.class);
			if (response.getStatusCodeValue() == 204) {
				Optional<User> userOptional = userRepository.findById(UUID.fromString(id));
				if(userOptional.isPresent()) {
					User currentClient = userOptional.get();
					currentClient.setFirstConnexion(true);

					User userSaved = userRepository.save(currentClient);
					return ResponseEntity.status(HttpStatus.OK).body(userSaved);
				}
				return ResponseEntity.status(HttpStatus.OK).body("\"ok\"");
			}
		}catch (Exception e) {
			return ResponseEntity
					.badRequest()
					.body("Une erreur est survenue !");

		}

		return ResponseEntity
				.badRequest()
				.body("Echec !");

	}



	/*--------------------------------------------------/
	/*             Mot de passe oublié                 /
	/*----------------------------------------------*/


	@GetMapping(FORGOT_PASSWORD)
	public ResponseEntity<?> forgotPassword(@PathVariable("email") String email){
		//	Customer costomer = costomerRepository.findByEmail(email);
		User user=userRepository.findByEmail(email);
		if(user!=null) {
			ValidateCode validateCode = codeRepository.findByUser(user);
			if(validateCode != null) {
				codeRepository.delete(validateCode);
			}
			ValidateCode code = new ValidateCode();
			int EXPIRATION = 30 * 1;//temps de validation du jeton : 30 mn
			code.setUser(user);
			code.setExpiryDate(calculateExpiryDate(EXPIRATION));
			code.setToken(codeGenerate());
			ValidateCode codeSaved = codeRepository.save(code);
			if(codeSaved != null) {
				EmailMessage emailMessage = new EmailMessage();
				emailMessage.setBody("Votre code de vérification : " + "<b>" + codeSaved.getToken() + "</b>");
				emailMessage.setSubject("SMpay");
				emailMessage.setTo_address(user.getEmail());
				try {
					sendmail(emailMessage);
				} catch (AddressException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return ResponseEntity
					.ok("\"ok\"");
		}
		return ResponseEntity
				.ok("\"ko\"");
	}




	/*----------------------------------------/
	/*            Envoie du code             /
	/*-------------------------------------*/


	@GetMapping(SEND_CODE)
	public ResponseEntity<?> sendCode(@PathVariable("code") String code){
		ValidateCode validateCode = codeRepository.findByToken(code);
		if(validateCode!=null) {
			if(validateCode.getUser() != null) {
				User user = validateCode.getUser();
				Calendar cal = Calendar.getInstance();
				if((validateCode.getExpiryDate().getTime() - cal.getTime().getTime()) >= 0) {
					codeRepository.delete(validateCode);
					return ResponseEntity.status(HttpStatus.OK).body(user);
				}
			}
		}
		return ResponseEntity.ok("\"ko\"");
	}




	/*-----------------------------------------------/
	/*          Autorisation de la demande          /
	/*--------------------------------------------*/

	@RolesAllowed({USER, ADMIN, "application", SUPERVISOR})
	@PostMapping("/smpay/api/v1/demande/auth")
	public ResponseEntity<?> authDemande(
			@RequestBody Identifiers userIdentifiers){
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
			map.add("client_id",CLIENT_ID);
			map.add("grant_type",GRANT_TYPE);
			map.add("client_secret",CLIENT_SECRET);
			map.add("username", userIdentifiers.getUsername());
			map.add("password", userIdentifiers.getPassword());
			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map,headers);
			ResponseEntity<String> response = restTemplate.exchange(KEYCLOAK_CLIENT_AUTHORIZATION, HttpMethod.POST, entity, String.class);
			if (response.getStatusCodeValue() == 200) {
				User user = userRepository.findByEmail(userIdentifiers.getEmail());
				if(user!=null) {
					ValidateCode validateCode = codeRepository.findByUser(user);
					if(validateCode != null) {
						codeRepository.delete(validateCode);
					}
					ValidateCode code = new ValidateCode();
					int EXPIRATION = 30 * 1;//temps de validation du jeton : 30 mn
					code.setUser(user);
					code.setExpiryDate(calculateExpiryDate(EXPIRATION));
					code.setToken(codeGenerate());
					ValidateCode codeSaved = codeRepository.save(code);
					if(codeSaved != null) {
						EmailMessage emailMessage = new EmailMessage();
						emailMessage.setBody("Votre code de validation : " + "<b>" + codeSaved.getToken() + "</b>");
						emailMessage.setSubject("SMpay");
						emailMessage.setTo_address(user.getEmail());
						try {
							sendmail(emailMessage);
						} catch (AddressException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (MessagingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					return ResponseEntity
							.ok("\"ok\"");
				}
			}
		} catch (Exception e) {
			return ResponseEntity.ok("\"Une erreur est survenue !\"");
		}
		return null;

	}





	  /*==============================================
	 /         Fonction de generation de token       /
	 *=============================================*/

	private String codeGenerate() {
		UUID uuid = UUID.randomUUID();
		String randomUUIDString = uuid.toString();
		String[] strings = randomUUIDString.toUpperCase().split("-");
		String code = strings[4].substring(0, 5);//5 chiffres
		return code;

	}


	  /*====================================================
	 /       Calcul de la date d'expiration du token      /
	 *==================================================*/


	private Date calculateExpiryDate(int expiryTimeInMinutes) {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(new Date().getTime());
		cal.add(Calendar.MINUTE, expiryTimeInMinutes);
		return new Date(cal.getTime().getTime());
	}



	 /*=================================================
	 /                	Gestion mail                  /
	 *==============================================*/


	private void sendmail(EmailMessage emailmessage) throws  AddressException, MessagingException, IOException {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.protocol", "smtp");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("smpay.noreply@gmail.com", "asubgbnbqvtngvxz");
			}
		});
		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress("smpay.noreply@gmail.com", false));

		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailmessage.getTo_address()));
		msg.setSubject(emailmessage.getSubject());
		msg.setContent(emailmessage.getBody(), "text/html");
		msg.setSentDate(new Date());

		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(emailmessage.getBody(), "text/html; charset=utf-8");

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);

		Transport.send(msg);
	}



}
