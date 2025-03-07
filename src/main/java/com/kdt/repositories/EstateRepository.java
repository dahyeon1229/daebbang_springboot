package com.kdt.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kdt.domain.entities.Estate;

public interface EstateRepository extends JpaRepository<Estate, Long> {
	@EntityGraph(attributePaths = { "building", "heatingSystem", "room", "structure", "transaction", "images",
			"optionList", "realEstateAgent" })
//	List<Estate> findAllByWriter(String loginId);
	List<Estate> findAllByRealEstateAgentEmail(String email);

	@Query("select m from Estate m where m.soldStatus = false order by estateId desc limit 6")
	List<Estate> findTop6ByOrderByEstateIdDesc();

	@Query("select count(m) from Estate m")
	Long countByEstate();

	@Query("select count(m) from Estate m WHERE DATE(m.writeDate) = CURDATE()")
	Long countTodayByEstate();

	@Query("SELECT m.room, COUNT(m) AS countRoom FROM Estate m GROUP BY m.room")
	List<Object[]> countByRoom();

	@EntityGraph(attributePaths = { "building", "heatingSystem", "room", "structure", "transaction", "images",
			"optionList", "realEstateAgent" })
	List<Estate> findAllBySoldStatusFalse();

	List<Estate> findByRealEstateAgentEmailAndSoldStatusFalseOrderByWriteDateDesc(String email, Pageable pageable);
	
	List<Estate> findAllByRealEstateAgentEmailAndSoldStatusFalseOrderByWriteDateDesc(String email);

}
