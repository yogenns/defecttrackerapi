package com.yogendra.webservices.defecttrackerapi.adapter;

import com.yogendra.webservices.defecttrackerapi.data.DefectData;
import com.yogendra.webservices.defecttrackerapi.data.ProjectData;
import com.yogendra.webservices.defecttrackerapi.data.UserData;
import com.yogendra.webservices.defecttrackerapi.jpa.DefectJPARepository;
import com.yogendra.webservices.defecttrackerapi.jpa.ProjectJPARepository;
import com.yogendra.webservices.defecttrackerapi.jpa.UserJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class DefectAdapter {
    @Autowired
    private DefectJPARepository defectJPARepository;
    @Autowired
    private UserJPARepository userJPARepository;
    @Autowired
    private ProjectJPARepository projectJPARepository;

    //Get all defects. This should not be part of production
    @GetMapping(path = "/defects")
    public List<DefectData> getAllDefects() {
        System.out.println("getAllDefects");
        return defectJPARepository.findAll();
    }

    @GetMapping(path = "/defect/reported/{username}")
    public List<DefectData> getAllReportedDefects(@PathVariable String username) {
        System.out.println("getAllReportedDefects");
        UserData userData = userJPARepository.findByUsername(username);
        return defectJPARepository.findDefectsByReportedBy(userData);
    }

    @GetMapping(path = "/defect/assigned/{username}")
    public List<DefectData> getAllAssignedDefects(@PathVariable String username) {
        System.out.println("getAllAssignedDefects");
        UserData userData = userJPARepository.findByUsername(username);
        return defectJPARepository.findDefectsByAssignedTo(userData);
    }

    @PutMapping(path = "/defect")
    public ResponseEntity<Object> addNewDefect(@RequestBody DefectData defectData) {
        StringBuilder errorMessage = new StringBuilder();
        if (defectData == null || defectData.getProjectData() == null || defectData.getProjectData().getId() == null) {
            System.out.println("Project Id is required. ");
            errorMessage.append("Project Id is required. ");
        } else {
            System.out.println("ProjectData Id " + defectData.getProjectData().getId());
            Optional<ProjectData> projectData = projectJPARepository.findById(defectData.getProjectData().getId());
            projectData.ifPresent(data -> {
                System.out.println("ProjectData Key " + data.getProjectKey());
                defectData.setProjectData(data);
            });
        }
        if (defectData == null || defectData.getReportedBy() == null || defectData.getReportedBy().getUsername() == null) {
            System.out.println("Reported Username is required. ");
            errorMessage.append("Reported Username is required. ");
        } else {
            System.out.println("ReportedBy " + defectData.getReportedBy().getUsername());
            UserData reportedBy = userJPARepository.findByUsername(defectData.getReportedBy().getUsername());
            if (reportedBy == null) {
                System.out.println("Reported Username NOT found. ");
                errorMessage.append("Reported Username NOT found. ");
            } else {
                System.out.println("ReportedBy Id " + reportedBy.getId());
                defectData.setReportedBy(reportedBy);
            }
        }
        if (defectData == null || defectData.getAssignedTo() == null || defectData.getAssignedTo().getUsername() == null) {
            System.out.println("Assign Username is required. ");
            errorMessage.append("Assign Username is required. ");
        } else {
            System.out.println("AssignedTo " + defectData.getAssignedTo().getUsername());
            UserData assignedTo = userJPARepository.findByUsername(defectData.getAssignedTo().getUsername());
            if (assignedTo == null) {
                System.out.println("AssignedTo Username NOT found. ");
                errorMessage.append("AssignedTo Username NOT found. ");
            } else {
                System.out.println("AssignedTo Id " + assignedTo.getId());
                defectData.setAssignedTo(assignedTo);
            }
        }
        if (defectData == null || defectData.getTitle() == null || defectData.getTitle().isEmpty()) {
            System.out.println("Title is required. ");
            errorMessage.append("Title is required. ");
        }
        if (errorMessage.length() == 0 && defectData != null) {
            defectData.setDateCreated(new Date());
            DefectData createdDefectData = defectJPARepository.save(defectData);
            System.out.println("Add Defect " + createdDefectData);
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("status", "Success");
            responseMap.put("message", "Defect Added Successfully with ID " + createdDefectData.getId());
            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        } else {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("status", "Failure");
            errorMap.put("error", errorMessage.toString());
            return new ResponseEntity<>(errorMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "/update/defect")
    public ResponseEntity<Object> updateNewDefect(@RequestBody DefectData defectData) {
        StringBuilder errorMessage = new StringBuilder();
        if (defectData == null || defectData.getId() == null) {
            System.out.println("Defect Key is required. ");
            errorMessage.append("Defect Key is required. ");
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("status", "Failure");
            errorMap.put("error", errorMessage.toString());
            return new ResponseEntity<>(errorMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        DefectData dbDefectData = null;
        Optional<DefectData> existingDefectData = defectJPARepository.findById(defectData.getId());
        if (existingDefectData.isPresent()) {
            dbDefectData = existingDefectData.get();
        } else {
            System.out.println("Defect Id NOT Found.");
            errorMessage.append("Defect Id NOT Found.");
        }
        if (errorMessage.length() == 0 && dbDefectData != null) {
            if (defectData.getProjectData() != null && defectData.getProjectData().getId() != null) {
                System.out.println("ProjectData Id " + defectData.getProjectData().getId());
                Optional<ProjectData> projectData = projectJPARepository.findById(defectData.getProjectData().getId());
                if (projectData.isPresent()) {
                    dbDefectData.setProjectData(projectData.get());
                }
            }
            if (defectData.getReportedBy() != null && defectData.getReportedBy().getUsername() != null) {
                System.out.println("ReportedBy " + defectData.getReportedBy().getUsername());
                UserData reportedBy = userJPARepository.findByUsername(defectData.getReportedBy().getUsername());
                if (reportedBy == null) {
                    System.out.println("Reported Username NOT found. ");
                    errorMessage.append("Reported Username NOT found. ");
                } else {
                    System.out.println("ReportedBy Id " + reportedBy.getId());
                    dbDefectData.setReportedBy(reportedBy);
                }
            }
            if (defectData.getAssignedTo() != null || defectData.getAssignedTo().getUsername() != null) {
                System.out.println("AssignedTo " + defectData.getAssignedTo().getUsername());
                UserData assignedTo = userJPARepository.findByUsername(defectData.getAssignedTo().getUsername());
                if (assignedTo == null) {
                    System.out.println("AssignedTo Username NOT found. ");
                    errorMessage.append("AssignedTo Username NOT found. ");
                } else {
                    System.out.println("AssignedTo Id " + assignedTo.getId());
                    dbDefectData.setAssignedTo(assignedTo);
                }
            }
            if (defectData.getTitle() != null && !defectData.getTitle().isEmpty()) {
                dbDefectData.setTitle(defectData.getTitle());
            }
            if (defectData.getSeverity() != null && !defectData.getSeverity().isEmpty()) {
                dbDefectData.setSeverity(defectData.getSeverity());
            }
            if (defectData.getPriority() != null && !defectData.getPriority().isEmpty()) {
                dbDefectData.setPriority(defectData.getPriority());
            }
            if (defectData.getDescription() != null && !defectData.getDescription().isEmpty()) {
                dbDefectData.setDescription(defectData.getDescription());
            }
            if (defectData.getStatus() != null && !defectData.getStatus().isEmpty()) {
                dbDefectData.setStatus(defectData.getStatus());
            }

            DefectData updatedDefectData = defectJPARepository.save(dbDefectData);
            System.out.println("Updated Defect " + updatedDefectData);
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("status", "Success");
            responseMap.put("message", "Defect Updated Successfully with ID " + updatedDefectData.getId());
            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        } else {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("status", "Failure");
            errorMap.put("error", errorMessage.toString());
            return new ResponseEntity<>(errorMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
