package com.pravin.spring_data_jpa;

import com.pravin.spring_data_jpa.model.Student;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Optional;

@SpringBootApplication
public class SpringDataJpaApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(SpringDataJpaApplication.class, args);

		StudentRepo repo = context.getBean(StudentRepo.class);

		Student s1 = context.getBean(Student.class);
		Student s2 = context.getBean(Student.class);
		Student s3 = context.getBean(Student.class);

//		s1.setRollNO(20);
//		s1.setName("Pravin");
//		s1.setMarks(100);


		s2.setRollNO(10);
		s2.setName("Suraj");
		s2.setMarks(97);


//		s3.setRollNO(21);
//		s3.setName("Karan");
//		s3.setMarks(77);


		// for insert data
		/*
		repo.save(s1);
		repo.save(s3);

		 */


		// ----------------------------------------------------------------------------



		// for getting it

		// System.out.println(repo.findAll());


		// for getting by id
		/*
		Optional<Student> s = repo.findById(20);
		System.out.println(s.orElse(new Student()));


		// ----------------------------------------------------------------------------


		 */
		// for crud operation

		/*
		System.out.println(repo.findByName("Pravin"));
		System.out.println(repo.findByMarks(99));
		System.out.println(repo.findByMarksGreaterThan(99));

		 */


		// ----------------------------------------------------------------------------



		// update the values and delete also
		/*

		repo.save(s2);

		repo.delete(s2);


		 */

	}

}
