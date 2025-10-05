package org.example;

import org.example.model.Alien;
import org.example.model.Laptop;
import org.example.service.LaptopService;
import org.springframework.context.ApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootApplicationDemo {
    public static void main( String[] args ) {
        ApplicationContext context = SpringApplication.run(SpringBootApplicationDemo.class,args);
//        Alien obj = context.getBean(Alien.class);
//        obj.code();

        LaptopService service = context.getBean(LaptopService.class);

        Laptop laptop = context.getBean(Laptop.class);
        service.addLaptop(laptop);

    }
}
