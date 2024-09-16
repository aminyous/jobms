package com.hichinfo.jobms.job;

import com.hichinfo.jobms.job.dto.JobDTO;

import java.util.List;

public interface JobService {

    List<JobDTO> findAll();
    void createJob(Job job);
    JobDTO findById(Long id);
    boolean deleteById(Long id);

    boolean updateJob(Long id, Job updatedJob);
}
