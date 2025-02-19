package com.example.jobjays.service;

import com.example.jobjays.dto.employer.CreateEmployerDto;
import com.example.jobjays.dto.employer.UpdateEmployerDto;
import com.example.jobjays.exception.ResourceNotFoundException;
import com.example.jobjays.model.*;
import com.example.jobjays.repository.ApplicantRepository;
import com.example.jobjays.repository.EmployerRepository;
import com.example.jobjays.repository.JobPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class EmployerService {
  private final EmployerRepository employerRepository;
  private final JobPostRepository jobPostRepository;
  private final ApplicantRepository applicantRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public EmployerService(EmployerRepository employerRepository, ApplicantRepository applicantRepository, JobPostRepository jobPostRepository) {
    this.employerRepository = employerRepository;
    this.jobPostRepository = jobPostRepository;
    this.applicantRepository = applicantRepository;
  }


  public Employer findByVerificationToken(String token){
    return employerRepository.findByToken(token);

  }
  public Employer addEmployer(CreateEmployerDto employer) {

    Employer newEmployer = new Employer(
      employer.getUsername(),
      passwordEncoder.encode(employer.getPassword()),
        employer.getEmail(),
      employer.getEmployerName(),
      employer.getEmployerInfo(),
            employer.getEnabled(),
            employer.getVerificationToken()
    );

    assert newEmployer.getProfile() != null;

    return employerRepository.save(newEmployer);
  }

  public Employer updateEmployer(UpdateEmployerDto newEmployer, Long id) {

    Employer employerToUpdate = employerRepository.findById(id).orElse(null);

    if (employerToUpdate == null) {
      throw new ResourceNotFoundException("Employer not found with id: " + id);
    }

    if (newEmployer.getEmployerInfo() != null && !newEmployer.getEmployerInfo().isEmpty()) {
      employerToUpdate.getProfile().setBio(newEmployer.getEmployerInfo());
    }

    if (newEmployer.getEmployerName() != null && !newEmployer.getEmployerName().isEmpty()) {
      employerToUpdate.getProfile().setName(newEmployer.getEmployerName());
    }

    if (newEmployer.getIndustry() != null && !newEmployer.getIndustry().isEmpty()) {
      employerToUpdate.getProfile().setIndustry(newEmployer.getIndustry());
    }

    if (newEmployer.getCurrentPassword() != null && !newEmployer.getCurrentPassword().isEmpty() &&
        newEmployer.getNewPassword() != null && !newEmployer.getNewPassword().isEmpty()) {
      if (passwordEncoder.matches(newEmployer.getCurrentPassword(), employerToUpdate.getPassword())) {
        employerToUpdate.setPassword(passwordEncoder.encode(newEmployer.getNewPassword()));
      } else {
        throw new ResourceNotFoundException("Current password is incorrect");
      }
    }




    return employerRepository.save(employerToUpdate);
  }


  public void deleteEmployer(Long id) {
    EmployerProfile profile = Objects.requireNonNull(employerRepository.findById(id).orElse(null)).getProfile();
    if (profile != null) {
      List<JobPost> jobPosts = jobPostRepository.findJobPostsByEmployer_Profile_NameIgnoreCase(profile.getName());
      if (jobPosts != null) {
        for (JobPost jobPost : jobPosts) {
          for (Applicant applicant : jobPost.getApplicants()) {
            applicant.getProfile().getAppliedJobs().remove(jobPost);
            applicant.getProfile().getSavedJobs().remove(jobPost);
            applicantRepository.save(applicant);
          }
        }
      }
    }
    employerRepository.deleteById(id);

    assert profile == null;
  }


  public Employer findEmployerById(Long id) {
    return employerRepository.findById(id).orElse(null);
  }

  public List<Employer> findAllEmployers() {
    return employerRepository.findAll();
  }
  public List<EmployerProfile> findAllEmployerProfiles() {
    return employerRepository.findAll().stream()
        .map(Employer::getProfile)  // Assuming getProfile() returns EmployerProfile
        .collect(Collectors.toList());
  }

  public EmployerProfile findEmployerProfileById(Long id) {
    Employer employer = employerRepository.findById(id).orElse(null);
    assert employer != null;
    return employer.getProfile();
  }

  public Employer findEmployerByUsername(String username) {
    return employerRepository.findByUsernameIs(username);
  }


  public EmployerProfile findEmployerProfileByUsername(String username) {
    Employer employer = employerRepository.findByUsernameIs(username);
    return employer.getProfile();
  }

  public List<Employer> findEmployersByName(String name) {
    return employerRepository.findAllByEmployerNameContainingIgnoreCase(name);
  }

  public List<EmployerProfile> findEmployerProfilesByName(String name) {
    List<Employer> employers = employerRepository.findAllByEmployerNameContainingIgnoreCase(name);
    return employers.stream()
        .map(Employer::getProfile)  // Assuming getProfile() returns EmployerProfile
        .collect(Collectors.toList());
  }

  //WILL NOT USE
  public List<Employer> findEmployersByEmail(String email) {
    return employerRepository.findAllByEmailContainingIgnoreCase(email);
  }

  //WILL NOT USE
  public List<EmployerProfile> findEmployerProfilesByEmail(String email) {
    List<Employer> employers = employerRepository.findAllByEmailContainingIgnoreCase(email);
    return employers.stream()
        .map(Employer::getProfile)  // Assuming getProfile() returns EmployerProfile
        .collect(Collectors.toList());
  }

}
