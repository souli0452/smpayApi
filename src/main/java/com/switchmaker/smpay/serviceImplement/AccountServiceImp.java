/*
package com.switchmaker.smpay.serviceImplement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.switchmaker.smpay.entities.Account;
import com.switchmaker.smpay.entities.Customer;
import com.switchmaker.smpay.entities.Platform;
import com.switchmaker.smpay.repository.AccountRepository;
import com.switchmaker.smpay.repository.CostomerRepository;
import com.switchmaker.smpay.repository.PlatformRepository;
import com.switchmaker.smpay.services.AccountService;

import javax.security.auth.login.AccountNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountServiceImp implements AccountService {

	@Autowired
	AccountRepository accountRepo;
@Autowired
	CostomerRepository customerRepository;
	@Autowired
	PlatformRepository platformRepository;



	@Override
	public void updateAccountStatus(UUID accountId) {
		System.out.println("ID ACCOUNT");
		System.out.println(accountId);
		Optional<Account> account = accountRepo.findById(accountId)  ;         //orElseThrow(() -> new RuntimeException(accountId.toString())); //orElseThrow(() -> new AccountNotFoundException(accountId));
		System.out.println("ID ACCOUNT");
		System.out.println(accountId);
		System.out.println("ID ACCOUNT");
		if (account.get().isAccountActivate()) {
			account.get().setAccountActivate(false);
		} else {
			account.get().setAccountActivate(true);
		}
		account.get().setAccountModificationDate(LocalDateTime.now());
		accountRepo.save(account.get());
	}

	@Override
	public List<Account> listAccountsByPlatformId(UUID platformId) {
		return accountRepo.findByPlatformId(platformId);
	}

	@Override
	public List<Account> listAccountsByPlatformAndStatus(UUID platformId, boolean status) {
		return accountRepo.findByPlatformIdAndAccountActivate(platformId, status);
	}

	@Override
	public List<Account> listAccountsByPlatformAndClient(UUID platformId, UUID customerId) {
		return accountRepo.findByPlatformIdAndCustomerId(platformId, customerId);
	}

	
	public List<Account> listAccountsByPlatformAndClientB( UUID customerId) {
		Customer customer = customerRepository.findById(customerId).get();
		List<Platform> platforms=platformRepository.findByCostomer(customer);
		List<Account> accounts=new ArrayList<>();
		platforms.stream().forEach(p->{
			accounts.addAll(accountRepo.findByPlatform(p));
		});
		return accounts;
	}


}
*/
