package com.example.demo.service;

import com.example.demo.domain.Student;
import com.example.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.*;

/**
 * Created by zm on 2019/8/22.
 */
@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;
    private AtomicInteger num = new AtomicInteger(0);

    public void save(){
        studentRepository.save(new Student("AA", "AA" + num.incrementAndGet()));
    }

    public void saveInner(String name, boolean isThrowException){
        saveInnerTrans(name, isThrowException);
    }

    @Transactional
    public void saveInnerTrans(String name, boolean isThrowException) {
        studentRepository.save(new Student("AA", "AA" + num.incrementAndGet()));
        if(isThrowException){
            throw new RuntimeException("FAAAAAAAAAAAAAA");
        }
    }
}
