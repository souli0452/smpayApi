package com.switchmaker.smpay.wave_ci;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepo extends JpaRepository<EventEntity, String> {

}
