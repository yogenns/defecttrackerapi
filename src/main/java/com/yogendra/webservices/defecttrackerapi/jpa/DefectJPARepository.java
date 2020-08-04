package com.yogendra.webservices.defecttrackerapi.jpa;

import com.yogendra.webservices.defecttrackerapi.data.DefectData;
import com.yogendra.webservices.defecttrackerapi.data.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DefectJPARepository extends JpaRepository<DefectData, Long> {

    List<DefectData> findDefectsByReportedBy(UserData username);

    List<DefectData> findDefectsByAssignedTo(UserData username);
}
