package com.hichinfo.jobms.job.impl;


import com.hichinfo.jobms.job.Job;
import com.hichinfo.jobms.job.JobRepository;
import com.hichinfo.jobms.job.JobService;
import com.hichinfo.jobms.job.clients.CompanyClient;
import com.hichinfo.jobms.job.clients.ReviewClient;
import com.hichinfo.jobms.job.dto.JobDTO;
import com.hichinfo.jobms.job.external.Company;
import com.hichinfo.jobms.job.external.Review;
import com.hichinfo.jobms.job.mapper.JobMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {

    JobRepository jobRepository;
    @Autowired
    RestTemplate restTemplate;

    private CompanyClient companyClient;
    private ReviewClient reviewClient;

    public JobServiceImpl(JobRepository jobRepository,
                          CompanyClient companyClient,
                          ReviewClient reviewClient) {

        this.jobRepository = jobRepository;
        this.companyClient = companyClient;
        this.reviewClient = reviewClient;
    }


    @Override
    public List<JobDTO> findAll() {
        List<Job> jobs = jobRepository.findAll();
        List<JobDTO> jobDTOS = new ArrayList<>();


        return jobs.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private JobDTO convertToDto(Job job) {

        //RestTemplate restTemplate = new RestTemplate();

        // This one is using rest template
        // Company company = restTemplate.getForObject("http://COMPANY-SERVICE:8086/companies/" + job.getCompanyId(), Company.class);

        // This one is using Open Feign
        Company company = companyClient.getCompany(job.getCompanyId());

        // This one is using rest template
//        ResponseEntity<List<Review>> reviewResponse = restTemplate.exchange("http://REVIEW-SERVICE:8087/reviews?companyId=" + job.getCompanyId(),
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<List<Review>>() {
//                });

        // List<Review> reviews = reviewResponse.getBody();
         // This one is using Open Feign
        List<Review> reviews = reviewClient.getReviews(job.getCompanyId());



        JobDTO jobDTO = JobMapper.mapToJobCompanyDto(
                job, company, reviews
        );

        // jobWithCompanyDTO.setCompany(company);

        return jobDTO;
    }

    @Override
    public void createJob(Job job) {

        jobRepository.save(job);
    }

    @Override
    public JobDTO findById(Long id) {

        Job job = jobRepository.findById(id).orElse(null);
        return convertToDto(job);
    }

    @Override
    public boolean deleteById(Long id) {

        try {
            jobRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateJob(Long id, Job updatedJob) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (jobOptional.isPresent()) {
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
