package com.switchmaker.smpay.serviceImplement;

import com.switchmaker.smpay.entities.CostomerRate;
import com.switchmaker.smpay.entities.Customer;
import com.switchmaker.smpay.entities.MeansPayment;
import com.switchmaker.smpay.repository.CostomerRateRepository;
import com.switchmaker.smpay.repository.CostomerRepository;
import com.switchmaker.smpay.repository.MeansPaymentRepository;
import com.switchmaker.smpay.services.CostomerRateService;
import com.switchmaker.smpay.services.CostomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CostomerRateServiceImp implements CostomerRateService {

  @Autowired
  CostomerService costomerService;
  @Autowired
  CostomerRepository costomerRepo;
  @Autowired
  MeansPaymentRepository meansPaymentRepository;
  @Autowired
  CostomerRateRepository costomerRateRepo;
  @Override
  public CostomerRate saveCostomerRate(UUID costomerId, UUID meansPaymentId, float rate) {
    // TODO Auto-generated method stub
    CostomerRate costomerRate = new CostomerRate();
    Optional<Customer> customer =   costomerRepo.findById(costomerId);
    Optional<MeansPayment> meansPayment = meansPaymentRepository.findById(meansPaymentId);
    if(customer.isPresent() && meansPayment.isPresent()){
      costomerRate.setCostomer(customer.get());
      costomerRate.setMeansPayment(meansPayment.get());
      costomerRate.setRate(rate);
      return costomerRateRepo.save(costomerRate);
    }

    return null;
  }
  @Override
  public CostomerRate getCostomerRateByCostomerAndMeansPayment(UUID costomerId, UUID meansPaymentId) {
    // TODO Auto-generated method stub
    return costomerRateRepo.getRateCostomerAndPaymentService(costomerId, meansPaymentId);
  }
  @Override
  public List<CostomerRate> getCostomerRatesById(UUID costomerId) {
    // TODO Auto-generated method stub
    return costomerRateRepo.findByCostomer(costomerService.getCostomerById(costomerId));
  }

  @Override
  public List<CostomerRate> getAllCustomersRates() {
    return costomerRateRepo.findAll();
  }

  @Override
  public CostomerRate updateCostumerRate(UUID id, CostomerRate costomerRate) {
    costomerRateRepo.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
      "Id"+ id +" invalid"
    ));
    MeansPayment meansPayment =  meansPaymentRepository.findById(costomerRate.getMeansPayment().getId()).get();
    Customer customer = costomerRepo.findById(costomerRate.getCostomer().getId()).get();
    costomerRate.setId(id);
    costomerRate.setCostomer(customer);
    costomerRate.setMeansPayment(meansPayment);
    return costomerRateRepo.save(costomerRate);
  }

  @Override
  public void deleCostumerRate(UUID id) {
    costomerRateRepo.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
      "Id"+ id +" invalid"));
    costomerRateRepo.deleteById(id);

  }

}
