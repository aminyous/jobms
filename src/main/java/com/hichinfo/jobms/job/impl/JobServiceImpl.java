package com.hichinfo.jobms.job.impl;


import com.hichinfo.jobms.job.Job;
import com.hichinfo.jobms.job.JobRepository;
import com.hichinfo.jobms.job.JobService;
import com.hichinfo.jobms.job.dto.JobWithCompanyDTO;
import com.hichinfo.jobms.job.external.Company;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {

    JobRepository jobRepository;

    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }



    @Override
    public List<JobWithCompanyDTO> findAll() {
        List<Job> jobs = jobRepository.findAll();
        List<JobWithCompanyDTO> jobWithCompanyDTOS = new ArrayList<>();


        return jobs.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private JobWithCompanyDTO convertToDto(Job job){

            RestTemplate restTemplate = new RestTemplate();
            JobWithCompanyDTO jobWithCompanyDTO = new JobWithCompanyDTO();

            jobWithCompanyDTO.setJob(job);

            jobWithCompanyDTO.setCompany(
                    restTemplate.getForObject("http://localhost:8086/companies/" + job.getCompanyId(), Company.class)
            );

        return jobWithCompanyDTO;
    }

    @Override
    public void createJob(Job job) {

        jobRepository.save(job);
    }

    @Override
    public Job findById(Long id) {
       return jobRepository.findById(id).orElse(null);
    }

    @Override
    public boolean deleteById(Long id) {

        try{
            jobRepository.deleteById(id);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean updateJob(Long id, Job updatedJob) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if(jobOptional.isPresent()){
            Job job = jobOptional.get();
            job.setTitle(updatedJob.getTitle());
            job.setDescription(updatedJob.getDescription());
            job.setLocation(updatedJob.getLocation());
            job.setMaxSalary(updatedJob.getMaxSalary());
            job.setMinSalary(updatedJob.getMinSalary());
            jobRepository.save(job);
            return true;
            }
        return false;
        }



}
