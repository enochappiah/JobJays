package com.example.jobjays.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
public class JobPost implements Post {


  private String title ;
  private String description;
  private String location;
  private Double minSalary;
  private Double maxSalary;

  @Id
  @GeneratedValue
  private Long jobID;
  private LocalDateTime postedDate;
  private LocalDateTime closedDate;

  @ManyToMany
  private List<Applicant> applicants;

  @ManyToOne
  public Employer employer;

public JobPost() {}

  public JobPost(
      String title,
      String description,
      String location,
      Double minSalary,
      Double maxSalary,
      LocalDateTime closedDate,
      Employer employer
      ) {

    //this.jobID = UUID.randomUUID().toString();
    this.title = title;
    this.description = description;
    this.location = location;
    this.minSalary = minSalary;
    this.maxSalary = maxSalary;
    this.postedDate = LocalDateTime.now();
    this.closedDate = closedDate;
    this.employer = employer;
    this.applicants = new ArrayList<>();

  }

  public String getTitle() {
    return title;
  }

  public String setTitle(String title) {
    this.title = title;
    return title;
  }

  public String getDescription() {
    return description;
  }

  public String setDescription(String description) {
    this.description = description;
    return description;
  }

  public String getLocation() {
    return location;
  }

  public String setLocation(String location) {
    this.location = location;
    return location;
  }

  public Double getMinSalary() {
    return minSalary;
  }
  public Double getMaxSalary() {
    return maxSalary;
  }

  public Double setMinSalary(Double minSalary) {
    this.minSalary = minSalary;
    return minSalary;
  }
  public Double setMaxSalary(Double maxSalary) {
    this.maxSalary = maxSalary;
    return maxSalary;
  }

  //Should not change
  public Long getID() {
    return jobID;
  }

  public LocalDateTime getPostedDate() {
    return postedDate;
  }

  public LocalDateTime setPostedDate(LocalDateTime postedDate) {
    this.postedDate = postedDate;
    return postedDate;
  }

  public LocalDateTime getClosedDate() {
    return closedDate;
  }

  public LocalDateTime setClosedDate(LocalDateTime closedDate) {
    this.closedDate = closedDate;
    return closedDate;
  }

  //Should not change
  public Employer getEmployer() {
    return employer;
  }

  public void addApplicant(Applicant applicant) {
    applicants.add(applicant);
  }

  public void removeApplicant(Applicant applicant) {
    applicants.remove(applicant);
  }

  public List<Applicant> getApplicants() {
    return applicants;
  }

  /*
    * Publish the job post, done by the Employer
   */
  public void publish() {
    System.out.println("Job post published");
  }

  /*
    * Close the job post, done by the Employer that published the job post
   */
  public void close() {
    System.out.println("Job post closed");
  }

}
