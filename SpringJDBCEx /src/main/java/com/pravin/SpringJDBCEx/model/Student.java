package com.pravin.SpringJDBCEx.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class Student {
    private int rollNO;
    @Value("Pravin")
    private String name;
    private int marks;

    public int getRollNO() {
        return rollNO;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRollNO(int rollNO) {
        this.rollNO = rollNO;
    }

    @Override
    public String toString() {
        return "Student{" +
                "rollNO=" + rollNO +
                ", name='" + name + '\'' +
                ", marks=" + marks +
                '}';
    }


}
