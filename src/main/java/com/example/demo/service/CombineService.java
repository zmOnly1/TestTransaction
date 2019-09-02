package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zm on 2019/8/10.
 */
@Service
public class CombineService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private final PassportNumberService passportNumberService;
    private final CodeSequenceService codeSequenceService;
    private final StudentService studentService;

    public CombineService(CodeSequenceService codeSequenceService, PassportNumberService passportNumberService, StudentService studentService) {
        this.codeSequenceService = codeSequenceService;
        this.passportNumberService = passportNumberService;
        this.studentService = studentService;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public String execute(String name, boolean isThrowException) {
        String passport = codeSequenceService.genCode(name);
        passportNumberService.savePassportWithTrans(passport, isThrowException);
        return passport;
    }

    @Transactional
    public String savePassport(String name, boolean isThrowException) {
        passportNumberService.savePassportWithTrans(name, isThrowException);
        return "OK";
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void executeNoTrans(String name, boolean isThrowException) {
        studentService.save();
        passportNumberService.savePassportWithNoTrans(name, isThrowException);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void executeRetryButNoTrans(String name, boolean isThrowException) {
        studentService.save();
        passportNumberService.savePassportWithRetryButNoTrans(name, isThrowException);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void executeRetryAndTrans(String name, boolean isThrowException) {
        studentService.save();
        passportNumberService.savePassportWithRetryAndTrans(name, isThrowException);
    }
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void executeRetryAndTransNoRollback(String name, boolean isThrowException) {
        studentService.save();
        passportNumberService.savePassportWithRetryAndTransNoRollback(name, isThrowException);
    }
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void executeWithTransThenRetry(String name, boolean isThrowException) {
        studentService.save();
        passportNumberService.savePassportWithTransThenRetry(name, isThrowException);
    }

}