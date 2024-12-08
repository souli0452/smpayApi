package com.switchmaker.smpay.serviceImplement;

import com.switchmaker.smpay.controller.FilesController;
import com.switchmaker.smpay.dto.CostomerApps;
import com.switchmaker.smpay.dto.CustomerDto;
import com.switchmaker.smpay.entities.Customer;
import com.switchmaker.smpay.enumeration.ClientType;
import com.switchmaker.smpay.enumeration.UserAccountType;
import com.switchmaker.smpay.mappers.CustomerMapper;
import com.switchmaker.smpay.repository.CostomerRepository;
import com.switchmaker.smpay.repository.PlatformRepository;
import com.switchmaker.smpay.services.CostomerService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.switchmaker.smpay.constant.RootPath.imageRootPath;


@Service
@Transactional
public class CostomerServiceImp implements CostomerService {

	@Autowired
	CostomerRepository costomerRepo;

  @Autowired
  CustomerMapper customerMapper;
  @Autowired
  PlatformRepository platformRepository;
  @PostConstruct
  public void init() {
    try {
      Files.createDirectories(imageRootPath);
    } catch (IOException e) {
      throw new RuntimeException("Could not initialize folder for upload!");
    }
  }

	@Override
  public Customer saveCostomer(CustomerDto customerDto) throws IOException {
    try {
      Customer customer = customerMapper.customerDtoToCustomer(customerDto);
      if (customerDto.getClientType().toString().compareToIgnoreCase(ClientType.LEGAL_PERSON.toString()) == 0) {
        customer.setClientType(ClientType.LEGAL_PERSON);
        //customer.setDenomination(customerDto.getCompanyName().trim().toLowerCase());
        convertBase64(customerDto, customer);
      } else {
        customer.setClientType(ClientType.INDIVIDUAL_PERSON);
        //customer.setDenomination(customerDto.getName().trim().toLowerCase());
        customer.setCompanyLogo(null);
        customer.setCompanyAddress(null);
        customer.setCompanyContact(null);
        customer.setCompanyDomiciledCountry(null);
        customer.setIfu(null);
        customer.setRccm(null);
        customer.setSocialReason(null);

      }


      byte[] imageByteCnib = Base64.decodeBase64(customerDto.getIdentityDocumentbase());
      new FileOutputStream(String.valueOf(imageRootPath.resolve(customerDto.getCnibOrPassport()+ ".jpg"))).write(imageByteCnib);
      String fileUrlCnib = MvcUriComponentsBuilder.fromMethodName(FilesController.class, "getFile", customerDto.getCnibOrPassport()+ ".jpg").build().toString();
      customer.setIdentityDocument(fileUrlCnib);
      customer.setCreationDate(LocalDateTime.now());
      return costomerRepo.save(customer);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return null; // Retourne null si une exception est levée
  }



	@Override
	public boolean existsByCostomerCode(String costomerCode) {
		// TODO Auto-generated method stub
		//return costomerRepo.existsByCostomerCode(costomerCode);
    return false;
	}

	@Override
	public boolean existsByUsername(String username) {
		// TODO Auto-generated method stub
		//return costomerRepo.existsByUsername(username);
    return  false;
	}

	@Override
	public boolean existsByUserCode(String userCode) {
		// TODO Auto-generated method stub
		//return costomerRepo.existsByUserCode(userCode);
    return  false;
	}

	@Override
	public boolean existsByEmail(String email) {
		// TODO Auto-generated method stub
		return costomerRepo.existsByEmail(email);
	}

	@Override
	public Customer getCostomerById(UUID id) {
		// TODO Auto-generated method stub
		return costomerRepo.findById(id).get();
	}

	@Override
	public Customer updateCostomer(UUID id, CustomerDto costomerdto) throws IOException {
    Customer customer=costomerRepo.findById(id).get();
    if (!costomerdto.getName().isEmpty()){
      customer.setName(costomerdto.getName());
    } if (!costomerdto.getFirstName().isEmpty()){
      customer.setFirstName(costomerdto.getFirstName());
    }
    if (!costomerdto.getAddress().isEmpty()) {
      customer.setAddress(costomerdto.getAddress());
    } if (!costomerdto.getEmail().isEmpty()) {
      customer.setEmail(costomerdto.getEmail());
    }  if (!(costomerdto.getCustomerAccountNumberForPayment() ==null)) {
      customer.setCustomerAccountNumberForPayment(costomerdto.getCustomerAccountNumberForPayment());
    } if (!(costomerdto.getEstablishmentDate() == null)) {
      customer.setEstablishmentDate(costomerdto.getEstablishmentDate());
    }  if (!(costomerdto.getExpirationDate() == null)) {
      customer.setEstablishmentDate(costomerdto.getExpirationDate());
    }  if (!(costomerdto.getEstablishmentPlace() == null)) {
      customer.setEstablishmentPlace(costomerdto.getEstablishmentPlace());
    }
    if (!costomerdto.getPhone().isEmpty()) {
      customer.setPhone(costomerdto.getPhone());
    }  if (!costomerdto.getCnibOrPassport().isEmpty()) {
      customer.setCnibOrPassport(costomerdto.getCnibOrPassport());
    }  if (!costomerdto.getDenomination().isEmpty()) {
      customer.setDenomination(costomerdto.getDenomination());
    }  if (!costomerdto.getTown().isEmpty()) {
      customer.setTown(costomerdto.getTown());
    }  if (!costomerdto.getCountryOfResidence().isEmpty()) {
      customer.setCountryOfResidence(costomerdto.getCountryOfResidence());
    }  if (!(costomerdto.getIdentityDocumentbase() ==null) ) {
      UUID imageName=UUID.randomUUID();
      byte[] imageByteCnib = Base64.decodeBase64(costomerdto.getIdentityDocumentbase());
      new FileOutputStream(String.valueOf(imageRootPath.resolve(imageName+ ".jpg"))).write(imageByteCnib);
      String fileUrlCnib = MvcUriComponentsBuilder.fromMethodName(FilesController.class, "getFile", imageName+ ".jpg").build().toString();
      customer.setIdentityDocument(fileUrlCnib);
    } if (!costomerdto.getDomiciliaryAccountStructure().isEmpty()){
      customer.setDomiciliaryAccountStructure(costomerdto.getDomiciliaryAccountStructure());
    }

    if (customer.getClientType().toString().compareToIgnoreCase(ClientType.LEGAL_PERSON.toString())==0){
      //List<Map<String, String>> errors = costomerdto.validateFields();
      if (!costomerdto.getName().isEmpty()){
        customer.setName(costomerdto.getName());
      }  if (!costomerdto.getCompanyName().isEmpty()) {
        customer.setCompanyName(costomerdto.getCompanyName());
      }   if (!costomerdto.getCompanyAddress().isEmpty()) {
        customer.setCompanyAddress(costomerdto.getCompanyAddress());
      }  if (!costomerdto.getIfu().isEmpty()) {
        customer.setIfu(costomerdto.getIfu());
      }  if (!costomerdto.getRccm().isEmpty()) {
        customer.setRccm(costomerdto.getRccm());
      }  if (!costomerdto.getSocialReason().isEmpty()) {
        customer.setSocialReason(costomerdto.getSocialReason());
      }  if (!costomerdto.getCompanyDomiciledCountry().isEmpty()) {
        customer.setCompanyDomiciledCountry(costomerdto.getCompanyDomiciledCountry());
      }  if (!(costomerdto.getCompanyLogobase() ==null)) {
        UUID imageName=UUID.randomUUID();
        byte[] imageByteCnib = Base64.decodeBase64(costomerdto.getIdentityDocumentbase());
        new FileOutputStream(String.valueOf(imageRootPath.resolve(imageName+ ".jpg"))).write(imageByteCnib);
        String fileUrlCnib = MvcUriComponentsBuilder.fromMethodName(FilesController.class, "getFile", imageName+ ".jpg").build().toString();
        customer.setIdentityDocument(fileUrlCnib);
      }
    }
    customer.setId(id);
      return costomerRepo.save(customer);
	}

  private void convertBase64(CustomerDto costomerDto, Customer customer) throws IOException {
    byte[] imageByte = Base64.decodeBase64(costomerDto.getCompanyLogobase());
    String imageName= UUID.randomUUID().toString();
    new FileOutputStream(String.valueOf(imageRootPath.resolve(imageName + ".jpg"))).write(imageByte);
    String fileUrl = MvcUriComponentsBuilder.fromMethodName(FilesController.class, "getFile", imageName + ".jpg").build().toString();
    customer.setCompanyLogo(fileUrl);
  }

  @Override
	public List<Customer> getAllCostomers() {
		// TODO Auto-generated method stub
		return costomerRepo.findAll(Sort.by(Direction.ASC, "denomination"));
	}

  @Override
  public List<Customer> getAllCostomersByCustomertype(ClientType type) {
    // TODO Auto-generated method stub
    return costomerRepo.findAll().stream().filter(c->c.getClientType().compareTo(type)==0).collect(Collectors.toList());
  }

  @Override
  public Customer disableOrEnableCustomerDeveloperOrSupervisorAccount(UUID customerId, UserAccountType type) {
    Customer customer=costomerRepo.findById(customerId).get();
/*    if (type.toString().compareToIgnoreCase(DEVELOPER)==0){
      if (customer.getDeveloperAccountActivated()){
        customer.setDeveloperAccountActivated(false) ;
      }else {
        customer.setDeveloperAccountActivated(true);
      }
    } else if (type.toString().compareToIgnoreCase(String.valueOf(SUPERVISOR))==0) {
      if (customer.getSupervisorAccountActivated()){
        customer.setSupervisorAccountActivated(false);
      }else {
        customer.setSupervisorAccountActivated(true);
      }
    }*/
    return costomerRepo.save(customer);
  }

  @Override
	public List<CostomerApps> getAllCostomersWithCostomerPlatforms() {
		// TODO Auto-generated method stub
		List <Customer> costomers = getAllCostomers();
		List<CostomerApps> costomerApps = new ArrayList<CostomerApps>();
		for(Customer costomer : costomers) {
			CostomerApps costomerApp = new CostomerApps();

			costomerApp.setAccountType(costomer.getClientType().toString());
			costomerApp.setAddress(costomer.getAddress());
			//costomerApp.setCostomerCode(costomer.getCostomerCode());
			costomerApp.setCountry(costomer.getCountryOfResidence());
			costomerApp.setCreationDate(costomer.getCreationDate());
			costomerApp.setDenomination(costomer.getDenomination());
			costomerApp.setEmail(costomer.getEmail());
			costomerApp.setId(costomer.getId());
			costomerApp.setPaymentAccount(costomer.getCustomerAccountNumberForPayment());
			costomerApp.setPhone(costomer.getPhone());
    //  costomerApp.setPlatforms(platformRepository.findByCostomer(costomer));
			costomerApp.setTown(costomer.getTown());
			costomerApps.add(costomerApp);
			}

      return costomerApps;


	}

  public CostomerApps getCostomerWithCostomerPlatforms(UUID customerId) {
    // TODO Auto-generated method stub
    Customer costomer = getCostomerById(customerId);
      CostomerApps costomerApp = new CostomerApps();
      costomerApp.setAccountType(costomer.getClientType().toString());
      costomerApp.setAddress(costomer.getAddress());
      //costomerApp.setCostomerCode(costomer.getCostomerCode());
      costomerApp.setCountry(costomer.getCompanyDomiciledCountry());
      costomerApp.setCreationDate(costomer.getCreationDate());
      costomerApp.setDenomination(costomer.getDenomination());
      costomerApp.setEmail(costomer.getEmail());
      costomerApp.setId(costomer.getId());
      costomerApp.setPaymentAccount(costomer.getCustomerAccountNumberForPayment());
      costomerApp.setPhone(costomer.getPhone());
      //clientApp.setPlatforms(platformRepository.findByClient(client));
      costomerApp.setTown(costomer.getTown());
    return costomerApp;
  }

	@Override
	public void deleteCostomerById(UUID id) {
		// TODO Auto-generated method stub
		costomerRepo.deleteById(id);

	}

	@Override
	public int countCostomer() {
		// TODO Auto-generated method stub
		return costomerRepo.findCountCostomer();
	}

}
