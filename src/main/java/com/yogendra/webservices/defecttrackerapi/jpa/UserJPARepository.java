package com.yogendra.webservices.defecttrackerapi.jpa;

import com.yogendra.webservices.defecttrackerapi.data.UserData;
import com.yogendra.webservices.defecttrackerapi.data.UserIdAndName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserJPARepository extends JpaRepository<UserData, Long> {

    List<UserIdAndName> findAllBy();

    UserData findByUsername(String username);
}
