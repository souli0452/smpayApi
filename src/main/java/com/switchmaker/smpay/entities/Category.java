/**
 * @author: Aichatou
 * @version 1.0
 * @year: 2024
 * @since: 11:31
 * @Date: 15/01/2024
 */
package com.switchmaker.smpay.entities;

import com.switchmaker.smpay.abstractClass.Identifier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "categories")
public class Category extends Identifier {
    @Column(nullable = false, unique = true)
    private String wording;

}
