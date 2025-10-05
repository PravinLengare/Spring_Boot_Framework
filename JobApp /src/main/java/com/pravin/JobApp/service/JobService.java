package com.pravin.JobApp.service;

import com.pravin.JobApp.model.JobPost;
import com.pravin.JobApp.repo.JobRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;
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


}
