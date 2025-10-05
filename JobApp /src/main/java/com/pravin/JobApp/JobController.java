package com.pravin.JobApp;

import com.pravin.JobApp.model.JobPost;
import com.pravin.JobApp.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class JobController {

    @Autowired
    private JobService service;


    @GetMapping({"/","home"})
    public String home(){

        return "home";
    }
    @GetMapping("addjob")
    public String addJob(){

        return "addjob";
    }
    @GetMapping("success")
    public String success(){

        return "success";
    }
    @PostMapping("handleForm")
    public String handleForm(JobPost jobPost){
        service.addJob(jobPost);
        return "success";
    }


    @GetMapping("viewalljobs")
    public String viewAllJobs(Model model) {
        List<JobPost> jobs = service.getAllJobs();
        model.addAttribute("jobPosts",jobs);
        return "viewalljobs";
    }


}
