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

    public CombineService(CodeSequenceService codeSequenceService, PassportNumberService passportNumberService) {
        this.codeSequenceService = codeSequenceService;
        this.passportNumberService = passportNumberService;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public String execute(String name) {
        String passport = codeSequenceService.genCode(name);
        //passportNumberService.savePassport(passport);
        return passport;
    }

    @Transactional
    public String savePassport(String name) {
        passportNumberService.savePassport(name);
        return "OK";
    }
}