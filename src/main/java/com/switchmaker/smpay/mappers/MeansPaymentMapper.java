/**
 * @author: Aichatou
 * @version 1.0
 * @year: 2024
 * @since: 18:18
 * @Date: 19/01/2024
 */
package com.switchmaker.smpay.mappers;

import com.switchmaker.smpay.dto.MeansPaymentDto;
import com.switchmaker.smpay.entities.MeansPayment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MeansPaymentMapper {
    MeansPayment meansPatmentDtoToMeansPayment(MeansPaymentDto meansPayment);

}
