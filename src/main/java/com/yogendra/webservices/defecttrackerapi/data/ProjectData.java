package com.yogendra.webservices.defecttrackerapi.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="project")
public class ProjectData {
	@Id
	@GeneratedValue
	private Long id;
	private String projectKey;
	private String projectName;
	private String projectRelease;


	public ProjectData() {
		//Default Constructor
	}
	public ProjectData(Long id, String projectKey, String projectName, String projectRelease) {
		super();
		this.id = id;
		this.projectKey = projectKey;
		this.projectName = projectName;
		this.projectRelease = projectRelease;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProjectKey() {
		return projectKey;
	}
	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProjectRelease() {
		return projectRelease;
	}
	public void setProjectRelease(String projectRelease) {
		this.projectRelease = projectRelease;
	}
	@Override
	public String toString() {
		return "ProjectData [id=" + id + ", projectKey=" + projectKey + ", projectName=" + projectName
				+ ", projectRelease=" + projectRelease + "]";
	}

}
