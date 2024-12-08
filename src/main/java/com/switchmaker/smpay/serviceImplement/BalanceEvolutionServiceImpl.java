package com.switchmaker.smpay.serviceImplement;

import com.switchmaker.smpay.entities.BalanceEvolution;
import com.switchmaker.smpay.entities.Platform;
import com.switchmaker.smpay.repository.BalanceEvolutionRepository;
import com.switchmaker.smpay.services.BalanceEvolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BalanceEvolutionServiceImpl implements BalanceEvolutionService {
  @Autowired
  private  BalanceEvolutionRepository balanceEvolutionRepository;

  @Override
  public List<BalanceEvolution> getPlatformeBalanceEvolutionSolde(Platform platform) {
    List<BalanceEvolution> balanceEvolutionList;
    balanceEvolutionList=balanceEvolutionRepository.findByPlatform(platform);
    return balanceEvolutionList;
  }
}
