package com.hichinfo.jobms.job;

import com.hichinfo.jobms.job.dto.JobWithCompanyDTO;

import java.util.List;

public interface JobService {

    List<JobWithCompanyDTO> findAll();
    void createJob(Job job);
    Job findById(Long id);
    boolean deleteById(Long id);

    boolean updateJob(Long id, Job updatedJob);
}
