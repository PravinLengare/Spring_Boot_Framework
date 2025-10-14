package com.pravin.spring_data_rest.repo;


import com.pravin.spring_data_rest.model.JobPost;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;

@Repository
public interface JobRepo extends JpaRepository<JobPost,Integer> {

    //List<JobPost>findByPostProfileContainingOrPostDescContaining(String postProfile,String postDesc);


}
