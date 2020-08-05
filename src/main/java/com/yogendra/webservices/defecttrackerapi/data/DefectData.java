package com.yogendra.webservices.defecttrackerapi.data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "defect")
public class DefectData {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private ProjectData projectData;
    private String title;
    private String severity;
    private String priority;

    @ManyToOne
    private UserData reportedBy;
    @ManyToOne
    private UserData assignedTo;
    private String description;
    private String status;
    private Date dateCreated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProjectData getProjectData() {
        return projectData;
    }

    public void setProjectData(ProjectData projectData) {
        this.projectData = projectData;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public UserData getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(UserData reportedBy) {
        this.reportedBy = reportedBy;
    }

    public UserData getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(UserData assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
