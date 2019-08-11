package com.example.demo.service;

import com.example.demo.domain.CodeSequence;
import com.example.demo.domain.Student;
import com.example.demo.repository.CodeSequenceRepository;
import com.example.demo.repository.StudentRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zm on 2019/8/10.
 */
@ActiveProfiles("mysql")
@RunWith(SpringRunner.class)
@SpringBootTest
public class CodeSequenceServiceFT {

    @Autowired
    private CodeSequenceService codeSequenceService;
    @Autowired
    CodeSequenceRepository codeSequenceRepository;
    @Autowired
    StudentRepository studentRepository;

    @Test
    public void genCodeIncrease() {
        CodeSequence codeSequence = new CodeSequence();
        codeSequence.setName("AA");
        codeSequence.setSequence(1L);

        codeSequenceRepository.save(codeSequence);

        String result = codeSequenceService.genCode("AA");
        Assert.assertEquals("AA0002", result);
    }

    @Transactional(propagation = Propagation.NEVER)
    @Test
    public void reGenCode() {
        // retry 1. code sequence:no recode; code sequence <codesequnce>1<codesequnce> save success;student: have <codesequnce>1<codesequnce> record throw PassportNumberExistException;
        // retry 2. code sequence:have 1 recode; code sequence save 2 error OptimisticLockingFailureException; student: no <codesequnce>2<codesequnce>  record;
        // retry 3. code sequence:have 2 recode; code sequence save 3 success; student: no 3 record;

        Student student = new Student("ZhangSan", "AA0001");
        studentRepository.save(student);

        String result = codeSequenceService.genCode("AA");

        studentRepository.deleteAll();
        codeSequenceRepository.deleteAll();

        Assert.assertEquals("AA0003", result);
    }

    @Test
    public void for_reGenCode_optimisticException(){
        CodeSequence sequence = codeSequenceRepository.save(new CodeSequence("AA", 2L));

        System.out.println(sequence);
        System.out.println("Finish.");
    }

}