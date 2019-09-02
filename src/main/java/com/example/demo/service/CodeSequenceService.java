package com.example.demo.service;

import com.example.demo.domain.CodeSequence;
import com.example.demo.exception.ExceedMaxRetryException;
import com.example.demo.exception.PassportNumberExistException;
import com.example.demo.repository.CodeSequenceRepository;
import com.example.demo.repository.StudentRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zm on 2019/8/10.
 */
@Transactional
@Service
public class CodeSequenceService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private final CodeSequenceRepository codeSequenceRepository;
    private final StudentRepository studentRepository;

    public CodeSequenceService(CodeSequenceRepository codeSequenceRepository, StudentRepository studentRepository) {
        this.codeSequenceRepository = codeSequenceRepository;
        this.studentRepository = studentRepository;
    }

    @Recover
    public String recover(Exception e, String name) {
        logger.info("Max retry error, " + e.getMessage() + ", Recover**********" + name + "," + e.getClass());
        throw new ExceedMaxRetryException(e.getMessage());
    }

    @Transactional(noRollbackFor = {PassportNumberExistException.class})
    @Retryable(value = {Exception.class},
        maxAttempts = 10, backoff = @Backoff(delay = 200, multiplier = 1))
    public String genCode(String name) {
        CodeSequence nextCodeSequence = getNextCodeSequence(name);

        String nextPassportNumber = getPassportNumber(nextCodeSequence);

        validateIfExist(nextPassportNumber);
        return nextPassportNumber;
    }

    private String getPassportNumber(CodeSequence nextCodeSequence) {
        return nextCodeSequence.getName() + StringUtils.leftPad(nextCodeSequence.getSequence().toString(), 4, "0");
    }

    private void validateIfExist(String passportNumber) {
        studentRepository.findByPassportNumber(passportNumber).ifPresent(student -> {
            throw new PassportNumberExistException("New passport number [" + passportNumber + "] already existed.");
        });
    }

    private CodeSequence getNextCodeSequence(String name) {
        return codeSequenceRepository.findByName(name)
                   .map(this::increaseCode)
                   .orElseGet(() -> initCode(name));
    }

    private CodeSequence initCode(String name) {
        CodeSequence codeSequence = new CodeSequence();
        codeSequence.setName(name);
        codeSequence.setSequence(1L);

        return codeSequenceRepository.save(codeSequence);
    }

    private CodeSequence increaseCode(CodeSequence codeSequence) {
        codeSequence.setSequence(codeSequence.getSequence() + 1);
        return codeSequenceRepository.save(codeSequence);
    }

}
