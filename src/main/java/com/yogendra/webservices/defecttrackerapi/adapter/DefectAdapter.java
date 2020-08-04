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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins="http://localhost:3000")
public class DefectAdapter  {
    @Autowired
    private DefectJPARepository defectJPARepository;
    @Autowired
    private UserJPARepository userJPARepository;
    @Autowired
    private ProjectJPARepository projectJPARepository;

    //Get all defects. This should not be part of production
    @GetMapping(path="/defects")
    public List<DefectData> getAllDefects(){
        System.out.println("getAllDefects");
        return defectJPARepository.findAll();
    }

    @GetMapping(path="/defect/reported/{username}")
    public List<DefectData> getAllReportedDefects(@PathVariable String username){
        System.out.println("getAllReportedDefects");
        UserData userData = userJPARepository.findByUsername(username);
        return defectJPARepository.findDefectsByReportedBy(userData);
    }

    @GetMapping(path="/defect/assigned/{username}")
    public List<DefectData> getAllAssignedDefects(@PathVariable String username){
        System.out.println("getAllAssignedDefects");
        UserData userData = userJPARepository.findByUsername(username);
        return defectJPARepository.findDefectsByAssignedTo(userData);
    }

    @PutMapping(path="/defect")
    public ResponseEntity<Object> addNewDefect(@RequestBody DefectData defectData) {
        StringBuilder errorMessage = new StringBuilder();
        if(errorMessage.length()==0) {
            System.out.println("ReportedBy "+defectData.getReportedBy().getUsername());
            UserData reportedBy = userJPARepository.findByUsername(defectData.getReportedBy().getUsername());
            System.out.println("ReportedBy Id "+reportedBy.getId());
            defectData.setReportedBy(reportedBy);

            System.out.println("ProjectData Id "+defectData.getProjectData().getId());
            Optional<ProjectData> projectData = projectJPARepository.findById(defectData.getProjectData().getId());
            projectData.ifPresent(data -> System.out.println("ProjectData Key " + data.getProjectKey()));
            DefectData createdDefectData = defectJPARepository.save(defectData);
            System.out.println("Add Defect "+createdDefectData);
            Map<String,String> responseMap = new HashMap<>();
            responseMap.put("status","Success");
            responseMap.put("message", "Defect Added Successfully with ID "+createdDefectData.getId());
            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        }else {
            Map<String,String> errorMap = new HashMap<>();
            errorMap.put("status","Failure");
            errorMap.put("error", errorMessage.toString());
            return new ResponseEntity<>(errorMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path="/defect")
    public ResponseEntity<Object> updateNewDefect(@RequestBody DefectData defectData) {
        StringBuilder errorMessage = new StringBuilder();
        if(defectData==null || defectData.getId()==null){
            System.out.println("Defect Key is required. ");
            errorMessage.append("Defect Key is required. ");
            Map<String,String> errorMap = new HashMap<>();
            errorMap.put("status","Failure");
            errorMap.put("error", errorMessage.toString());
            return new ResponseEntity<>(errorMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(errorMessage.length()==0) {
            DefectData createdDefectData = defectJPARepository.save(defectData);
            System.out.println("Add Defect "+createdDefectData);
            Map<String,String> responseMap = new HashMap<>();
            responseMap.put("status","Success");
            responseMap.put("message", "Defect Updated Successfully with ID "+createdDefectData.getId());
            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        }else {
            Map<String,String> errorMap = new HashMap<>();
            errorMap.put("status","Failure");
            errorMap.put("error", errorMessage.toString());
            return new ResponseEntity<>(errorMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
