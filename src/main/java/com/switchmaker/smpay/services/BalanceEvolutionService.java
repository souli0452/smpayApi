package com.switchmaker.smpay.services;

import com.switchmaker.smpay.entities.BalanceEvolution;
import com.switchmaker.smpay.entities.Platform;

import java.util.List;

public interface BalanceEvolutionService {
List<BalanceEvolution> getPlatformeBalanceEvolutionSolde(Platform platform);
}
