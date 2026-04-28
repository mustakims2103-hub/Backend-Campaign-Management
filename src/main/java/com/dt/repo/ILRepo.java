package com.dt.repo;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dt.entity.AddCampaignForm;

@Repository
public interface ILRepo extends JpaRepository<AddCampaignForm, Integer>, JpaSpecificationExecutor<AddCampaignForm> {

	@EntityGraph(attributePaths = {"assignees"})
	Page<AddCampaignForm> findAll(Pageable pageable);
	
	
	@Query("SELECT c.status, COUNT(c) FROM AddCampaignForm c GROUP BY c.status")
    List<Object[]> countCampaignsByStatus();
	
    @Query("SELECT DISTINCT c.type FROM AddCampaignForm c WHERE c.type IS NOT NULL")
    List<String> findDistinctTypes();

    @Query("SELECT DISTINCT c.status FROM AddCampaignForm c WHERE c.status IS NOT NULL")
    List<String> findDistinctStatuses();
}
