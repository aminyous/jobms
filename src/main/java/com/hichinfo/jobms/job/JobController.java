package com.hichinfo.jobms.job;

import com.hichinfo.jobms.job.dto.JobWithCompanyDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/jobs")
public class JobController {


    private JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }


    @GetMapping
    public ResponseEntity<List<JobWithCompanyDTO>> findAll(){
        return new ResponseEntity<>(jobService.findAll(), HttpStatus.OK) ;
    }


    @PostMapping
    public ResponseEntity<String> createJob(@RequestBody Job job){

        jobService.createJob(job);

        return new ResponseEntity<>("Added Successfully", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id){
        Job job = jobService.findById(id);
        if (job != null){
            return  new ResponseEntity<>(jobService.findById(id), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJobById(@PathVariable Long id){

        boolean deleted = jobService.deleteById(id);

        if (deleted){
            return new ResponseEntity<>("Deleted Successfully.", HttpStatus.OK);
        }

        return new ResponseEntity<>("Record not found", HttpStatus.NOT_FOUND);


    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateJob(
                    @PathVariable Long id,
                    @RequestBody Job updatedJob
    ){

        boolean updated = jobService.updateJob(id, updatedJob);

        if (updated){
            return new ResponseEntity<>("Updated Successfully.", HttpStatus.OK);
        }

        return new ResponseEntity<>("Record not found", HttpStatus.NOT_FOUND);


    }
}
