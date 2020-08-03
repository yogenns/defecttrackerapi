package com.yogendra.webservices.defecttrackerapi.jpa;

import org.springframework.stereotype.Repository;

import com.yogendra.webservices.defecttrackerapi.data.ProjectData;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ProjectJPARepository extends JpaRepository<ProjectData, Long>{

	List<ProjectData> findByProjectKey(String projectKey);

}
