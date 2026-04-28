package com.dt.repo;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dt.entity.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
   
//	User findByEmail(String email);
	
	Optional<Users> findByUsername(String username);
	

	
}