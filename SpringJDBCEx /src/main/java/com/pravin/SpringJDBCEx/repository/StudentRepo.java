package com.pravin.SpringJDBCEx.repository;

import com.pravin.SpringJDBCEx.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StudentRepo {
    private JdbcTemplate jdbc;


    public JdbcTemplate getJdbc() {
        return jdbc;
    }
    @Autowired
    public void setJdbc(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }



    public void save(Student s) {
        String sql = "Insert INTO student(rollNO,name,marks) Values(?,?,?)";
        int rows = jdbc.update(sql,s.getRollNO(),s.getName(),s.getMarks());
        System.out.println("Rows affected "+rows);

    }

    public List<Student> findAll() {
         String sql = "select * from student";

        RowMapper<Student> mapper = new RowMapper<Student>() {
            @Override
            public Student mapRow(ResultSet rs, int rowNum) throws SQLException {

                Student s = new Student();
                s.setRollNO(rs.getInt("rollNO"));
                s.setName(rs.getString("name"));
                s.setMarks(rs.getInt("marks"));

                return s;

            }
        };

         return jdbc.query(sql,mapper);
    }
}
