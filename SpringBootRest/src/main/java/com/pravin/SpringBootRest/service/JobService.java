package com.pravin.SpringBootRest.service;


import com.pravin.SpringBootRest.model.JobPost;
import com.pravin.SpringBootRest.repo.JobRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {
    @Autowired
    public JobRepo repo;

    public void addJob(JobPost jobPost){
        repo.addJob(jobPost);

    }

    public List<JobPost> getAllJobs(){

        return repo.getAllJobs();
    }


    public JobPost getJob(int postID) {
        return repo.getJob(postID);
    }

    public void updateJob(JobPost jobPost) {
         repo.updateJob(jobPost);
    }

    public void deleteJob(int postId) {
        repo.deleteJob(postId);
    }
}
