package com.example.demo.repository;

import com.example.demo.domain.Student;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by zm on 2019/8/10.
 */
@ActiveProfiles("mysql")
@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void save(){
        for (int i = 1; i <= 500; i++) {
            int random = RandomUtils.nextInt(2, 5);
            if (i % random == 0){
                insert(i);
            }
        }
    }

    private void insert(int i) {
        String passportNumber = "AA" + StringUtils.leftPad(String.valueOf(i), 4, "0");
        Student student = new Student("ZhangSan" + i, passportNumber);
        studentRepository.save(student);
    }

}
