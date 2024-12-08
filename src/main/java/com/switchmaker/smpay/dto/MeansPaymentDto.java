/**
 * @author: Aichatou
 * @version 1.0
 * @year: 2024
 * @since: 18:07
 * @Date: 19/01/2024
 */
package com.switchmaker.smpay.dto;

import com.switchmaker.smpay.entities.Category;
import com.switchmaker.smpay.entities.CountryPresence;
import com.switchmaker.smpay.entities.Currencies;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MeansPaymentDto {
    private String wording;
    private String code;
    @Column(columnDefinition = "TEXT")
    private String logo;
    private float applicableRate;
    private float operatorRate;
    private String colorCode;
    private boolean active = true;
    private Category category;
    private List<Currencies> currencies = new ArrayList<>();
    private List<CountryPresence> countries = new ArrayList<>();
}
