package com.example.demo.service;

import com.example.demo.domain.CodeSequence;
import com.example.demo.domain.Student;
import com.example.demo.repository.CodeSequenceRepository;
import com.example.demo.repository.StudentRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by zm on 2019/8/10.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CodeSequenceServiceCT {

    @Autowired
    private CodeSequenceService codeSequenceService;
    @MockBean
    CodeSequenceRepository codeSequenceRepository;
    @MockBean
    StudentRepository studentRepository;

    @Test
    public void genCodeIncrease() {
        CodeSequence codeSequence = new CodeSequence();
        codeSequence.setName("AA");
        codeSequence.setSequence(1L);

        when(codeSequenceRepository.findByName(anyString())).thenReturn(Optional.of(codeSequence));

        when(codeSequenceRepository.save(any(CodeSequence.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(studentRepository.findByPassportNumber(anyString())).thenReturn(Optional.empty());

        String result = codeSequenceService.genCode("AA");
        Assert.assertEquals("AA0002", result);

        ArgumentCaptor<CodeSequence> argument = ArgumentCaptor.forClass(CodeSequence.class);
        verify(codeSequenceRepository).save(argument.capture());
        Assert.assertEquals(2, argument.getValue().getSequence().longValue());
    }

    @Transactional(propagation = Propagation.NEVER)
    @Test
    public void reGenCode3times() {
        // retry 1. code sequence:no recode; code sequence <codesequnce>1<codesequnce> save success;student: have <codesequnce>1<codesequnce> record throw PassportNumberExistException;
        // retry 2. code sequence:have 1 recode; code sequence save 2 error OptimisticLockingFailureException; student: no <codesequnce>2<codesequnce>  record;
        // retry 3. code sequence:have 2 recode; code sequence save 3 success; student: no 3 record;

        when(codeSequenceRepository.findByName(anyString())).thenAnswer(new Answer<Optional<CodeSequence>>() {
            private int count = 0;

            @Override
            public Optional<CodeSequence> answer(InvocationOnMock invocation) throws Throwable {
                count++;
                if (count == 1) {
                    return Optional.empty();
                } else if(count ==  2){
                    return Optional.of(new CodeSequence("AA", 1L));
                }
                return Optional.of(new CodeSequence("AA", 2L));
            }
        });
        //init
        when(codeSequenceRepository.save(any(CodeSequence.class))).thenAnswer(new Answer<CodeSequence>() {
            private int count = 0;
            @Override
            public CodeSequence answer(InvocationOnMock invocation) throws Throwable {
                count++;
                if (count == 1) {
                    return invocation.getArgument(0);
                } else if(count ==  2){
                    throw new OptimisticLockingFailureException("Duplicate save error ******");
                }
                return invocation.getArgument(0);

            }
        });

        when(studentRepository.findByPassportNumber(anyString())).thenAnswer(new Answer<Optional<Student>>() {
            private int count = 0;

            @Override
            public Optional<Student> answer(InvocationOnMock invocation) throws Throwable {
                count++;
                if (count == 1) {
                    return Optional.of(new Student());
                }
                return Optional.empty();
            }
        });

        String result = codeSequenceService.genCode("AA");
        Assert.assertEquals("AA0003", result);

        ArgumentCaptor<CodeSequence> argument = ArgumentCaptor.forClass(CodeSequence.class);
        verify(codeSequenceRepository, times(3)).save(argument.capture());
        Assert.assertEquals(3, argument.getAllValues().size());
        Assert.assertEquals(3, argument.getValue().getSequence().longValue());
    }

}