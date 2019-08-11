package com.example.demo.service;

import com.example.demo.domain.CodeSequence;
import com.example.demo.repository.CodeSequenceRepository;
import com.example.demo.repository.StudentRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by zm on 2019/8/10.
 */
public class CodeSequenceServiceTest {

    @Mock
    CodeSequenceRepository codeSequenceRepository;
    @Mock
    StudentRepository studentRepository;
    @InjectMocks
    CodeSequenceService codeSequenceService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGenCodeInit() throws Exception {
        CodeSequence codeSequence = new CodeSequence();
        codeSequence.setName("AA");
        codeSequence.setSequence(1L);

        when(codeSequenceRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(codeSequenceRepository.save(any(CodeSequence.class))).thenReturn(codeSequence);
        when(studentRepository.findByPassportNumber(anyString())).thenReturn(Optional.empty());

        String result = codeSequenceService.genCode("AA");
        Assert.assertEquals("AA0001", result);
    }

    @Test
    public void testGenCodeIncrease() throws Exception {
        CodeSequence codeSequence = new CodeSequence();
        codeSequence.setName("AA");
        codeSequence.setSequence(1L);

        when(codeSequenceRepository.findByName(anyString())).thenReturn(Optional.of(codeSequence));

        when(codeSequenceRepository.save(any(CodeSequence.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(studentRepository.findByPassportNumber(anyString())).thenReturn(Optional.empty());

        String result = codeSequenceService.genCode("name");
        Assert.assertEquals("AA0002", result);

        ArgumentCaptor<CodeSequence> argument = ArgumentCaptor.forClass(CodeSequence.class);
        verify(codeSequenceRepository).save(argument.capture());
        Assert.assertEquals(2, argument.getValue().getSequence().longValue());
    }
}
