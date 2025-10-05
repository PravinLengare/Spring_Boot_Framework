package org.example.service;

import org.example.model.Laptop;
import org.example.repo_.LaptopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LaptopService {
//    @Autowired
    private LaptopRepository repo;
    public LaptopRepository getRepo() {
        return repo;
    }
    @Autowired
    public void setRepo(LaptopRepository repo) {
        this.repo = repo;
    }

    public void addLaptop(Laptop laptop) {
        repo.save(laptop);
        System.out.println("added laptop !");
    }
    public boolean isGood(Laptop laptop){
        return true;
    }
}
