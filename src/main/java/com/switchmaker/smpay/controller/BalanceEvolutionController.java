package com.switchmaker.smpay.controller;

import com.switchmaker.smpay.services.BalanceEvolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.switchmaker.smpay.constant.urls.RootUrl.ROOT_API;

@CrossOrigin("*")
@RestController
@RequestMapping(ROOT_API)

public class BalanceEvolutionController {
  @Autowired
  private  BalanceEvolutionService balanceEvolutionService;
}
