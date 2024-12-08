package com.switchmaker.smpay.repository;

import com.switchmaker.smpay.entities.Platform;
import com.switchmaker.smpay.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PlatformRepository extends JpaRepository<Platform, UUID> {
	Platform findByPlatformCode(String platformCode);
	Platform findByUserAndPlatformName(User user, String name);
//	Platform findByPlatformUsername(String platformUsername);
	@Query(value = "SELECT * FROM platform WHERE costomer_id = ?1 ORDER BY platform_name ASC", nativeQuery = true)
	List<Platform> findCostomerPlatforms(UUID costomer);
	@Query(value = "SELECT * FROM platform ORDER BY platform_name ASC", nativeQuery = true)
	List<Platform> findAllPlatforms();
	//List<Platform> findByCostomer(Customer costomer);
	@Query(value = "SELECT COUNT (*) FROM platform", nativeQuery = true)
	Integer findCountPlatform();
	@Query(value = "SELECT * FROM platform plm, costomer cos WHERE cos.id = plm.costomer_id AND cos.costomer_code =?1 AND plm.platform_code =?2", nativeQuery = true)
	Platform findPlatformByCostomerCodeAndPlatformCode(String costomerCode, String platformCode);

    boolean existsByPlatformNameAndIdNot(String platformName, UUID platformId);

	List<Platform> findByUser(User user);

	long countById(UUID platformId);
}
