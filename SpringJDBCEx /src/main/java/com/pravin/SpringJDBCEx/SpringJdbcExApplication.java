package com.pravin.SpringJDBCEx;

import com.pravin.SpringJDBCEx.model.Student;
import com.pravin.SpringJDBCEx.repository.StudentRepo;
import com.pravin.SpringJDBCEx.service.StudentService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;

@SpringBootApplication
public class SpringJdbcExApplication {

	public static void main(String[] args) {

		ApplicationContext context = SpringApplication.run(SpringJdbcExApplication.class, args);

		Student s = context.getBean(Student.class);
//		s.setMarks(98);
//		s.setRollNO(19);
//		s.setName("Amol");


//		System.out.println(s.getRollNO());
//		System.out.println(s.getName());
//		System.out.println(s.getMarks());

		StudentService service = context.getBean(StudentService.class);
		service.addStudent(s);


		List<Student> students = service.getStudents();
		System.out.println(students);
	}

}
