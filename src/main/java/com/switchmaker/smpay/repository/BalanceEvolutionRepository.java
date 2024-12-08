package com.switchmaker.smpay.repository;

import com.switchmaker.smpay.entities.BalanceEvolution;
import com.switchmaker.smpay.entities.Platform;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BalanceEvolutionRepository extends JpaRepository<BalanceEvolution, UUID> {
    List<BalanceEvolution> findByPlatform(Platform platform);
}
