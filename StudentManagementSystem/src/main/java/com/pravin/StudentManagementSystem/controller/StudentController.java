package com.pravin.StudentManagementSystem.controller;

import com.pravin.StudentManagementSystem.model.Student;
import com.pravin.StudentManagementSystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/students")
    public String getStudent(Model model){
        model.addAttribute("students",studentService.getAllStudents());
        return "students";
    }

    @GetMapping("/students/new")
    public String createStudentForm(Model model) {

        // create student object to hold student form data
        Student student = new Student();
        model.addAttribute("student", student);
        return "create_student";

    }

    @PostMapping("/students")
    public String saveStudent(@ModelAttribute("student") Student student) {
        studentService.saveStudent(student);
        return "redirect:/students";
    }

    @GetMapping("/students/edit/{user_id}")
    public String editStudentsForm(@PathVariable Long user_id, Model model) {

    // 1. Get the Student from the service and handle the case if it's not found

        Student student = studentService.getStudentById(user_id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + user_id));

        // 2. Add the actual Student object (not the Optional) to the model
        model.addAttribute("student", student);

        return "edit_student";
    }

    @PostMapping("/students/edit/{id}")
    public String updateStudent(@PathVariable Long user_id, @ModelAttribute("student") Student student,Model model){

        Optional<Student> existingStudent = studentService.getStudentById(user_id);

        existingStudent.get().setFirstName(student.getFirstName());
        existingStudent.get().setLastName(student.getLastName());
        existingStudent.get().setEmail(student.getEmail());

        // save updated student object
        studentService.updateStudent(existingStudent.orElse(null));
        return "redirect:/students";

    }

    @GetMapping("/students/delete/{user_id}")
    public String deleteStudent(@PathVariable Long user_id){
        studentService.delete(user_id);
        return "redirect:/students";
    }


}
