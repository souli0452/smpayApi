package com.switchmaker.smpay.mappers;

import com.switchmaker.smpay.dto.CustomerDto;
import com.switchmaker.smpay.entities.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
 Customer customerDtoToCustomer(CustomerDto customerDto);
  CustomerDto customerToCustomerDto(Customer customer);
}



