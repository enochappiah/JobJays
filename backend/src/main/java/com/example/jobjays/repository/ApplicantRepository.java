package com.example.jobjays.repository;

import com.example.jobjays.model.Applicant;
import com.example.jobjays.model.JobPost;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ApplicantRepository extends JpaRepository<Applicant, Long> {

  List<Applicant> findAllByUsernameContainingIgnoreCase(String name);

  @Query("select a from Applicant a where upper(a.profile.name) like upper(concat('%', :name, '%'))")
  List<Applicant> findAllByNameContainingIgnoreCase(String name);

  Applicant findByUsernameIsIgnoreCase(String username);

  Applicant findByToken(String token);
  // TODO CREATE A JOB APPLICATION REPOSITORY

  // @Query("select sj from JobPost sj where :applicantID MEMBER of
  // sj.applicants")
  // Set<JobPost> findSavedJobsByApplicantId(Long applicantId);
  // Optional<JobPost> findJobPostByApplicantIdandJobId(Long applicantId, Long
  // jobPostId);
  @Query("select a.profile.savedJobs from Applicant a where a.applicantId = :applicantId")
  Set<JobPost> findSavedJobsByApplicantId(Long applicantId);

  Boolean existsByEmail(String email);
}
