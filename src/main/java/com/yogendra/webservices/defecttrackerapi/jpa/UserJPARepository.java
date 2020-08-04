package com.yogendra.webservices.defecttrackerapi.jpa;

import com.yogendra.webservices.defecttrackerapi.data.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJPARepository extends JpaRepository<UserData, Long> {
    UserData findByUsername(String username);
}
