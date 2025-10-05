package org.example.repo_;
import org.example.model.Laptop;
import org.springframework.stereotype.Repository;

@Repository
public class LaptopRepository {

    public void save(Laptop laptop){
        System.out.println("saved successfully !");
    }
}
