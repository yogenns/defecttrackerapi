package com.yogendra.webservices.defecttrackerapi.adapter;

import com.yogendra.webservices.defecttrackerapi.data.ProjectData;
import com.yogendra.webservices.defecttrackerapi.jpa.ProjectJPARepository;
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
public class ProjectAdapter {
    @Autowired
    private ProjectJPARepository projectJPARepository;

    @GetMapping(path="/projects")
    public List<ProjectData> getAllProjects(){
        System.out.println("getAllProjects");
        return projectJPARepository.findAll();
    }

    @GetMapping(path="/isadmin/{username}")
    public boolean isAdminUser(@PathVariable String username){
        System.out.println("Username entered ["+username+"]");
        return username.equals("admin");
    }

    @PutMapping(path="/project")
    public ResponseEntity<Object> addNewProject(@RequestBody ProjectData projectData) {
        System.out.println("addNewProject "+projectData);
        StringBuilder errorMessage = new StringBuilder();
        if(projectData.getProjectName()==null) {
            System.out.println("Project Name is required. ");
            errorMessage.append("Project Name is required. ");
        }
        if(projectData.getProjectKey()==null) {
            System.out.println("Project Key is required. ");
            errorMessage.append("Project Key is required. ");
        }
        if(projectData.getId()!=null) {
            System.out.println("Project Id Found. ");
            Optional<ProjectData> existingProjectList = projectJPARepository.findById(projectData.getId());
            if(existingProjectList!=null &&  existingProjectList.isPresent()) {
                System.out.println("Project Id already present. ");
                errorMessage.append("Project Id already present. ");
            }
        }
        if(projectData.getProjectKey()!=null) {
            System.out.println("Project Key found");
            List<ProjectData> existingProjectList = projectJPARepository.findByProjectKey(projectData.getProjectKey());
            if(existingProjectList!=null && !existingProjectList.isEmpty() && existingProjectList.get(0)!=null) {
                System.out.println("Project Key already present. ");
                errorMessage.append("Project Key already present. ");
            }
        }
        System.out.println("ErrMessage "+errorMessage);
        if(errorMessage.length()==0) {
            ProjectData createdProjectData = projectJPARepository.save(projectData);
            System.out.println("Add Project "+createdProjectData);
            Map<String,String> responseMap = new HashMap<String, String>();
            responseMap.put("status","Success");
            responseMap.put("message", "Project Added Successfully with ID "+createdProjectData.getId());
            return new ResponseEntity<Object>(responseMap, HttpStatus.OK);
        }else {
            Map<String,String> errorMap = new HashMap<String, String>();
            errorMap.put("status","Failure");
            errorMap.put("error", errorMessage.toString());
            return new ResponseEntity<Object>(errorMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
