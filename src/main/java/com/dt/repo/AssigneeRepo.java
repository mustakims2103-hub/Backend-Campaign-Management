package com.dt.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dt.entity.Assignee;

@Repository
public interface AssigneeRepo extends JpaRepository<Assignee, Long> {

}
