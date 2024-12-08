/**
 * @author: Aichatou
 * @version 1.0
 * @year: 2024
 * @since: 11:38
 * @Date: 15/01/2024
 */
package com.switchmaker.smpay.repository;

import com.switchmaker.smpay.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
}
