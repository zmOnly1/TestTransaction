package com.example.demo.controller;

import com.example.demo.service.CodeSequenceService;
import com.example.demo.service.CombineService;
import com.example.demo.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zm on 2019/8/10.
 */
@RestController
public class CodeSequenceController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CombineService combineService;
    @Autowired
    private CodeSequenceService codeSequenceService;
    @Autowired
    private StudentService studentService;

    @GetMapping("/genco")
    public String genCode(@RequestParam String name, @RequestParam(required = false) boolean isThrowException) {
        try {
            return combineService.execute(name, isThrowException);
        } catch (Exception e) {
            logger.error("Gen code fail**************" + e.getMessage() + "," + e.getClass());
            return e.getMessage() + ",,," + e.getClass();
        }
    }

    @GetMapping("/gendi")
    public String genCode2(@RequestParam String name) {
        try {
            return codeSequenceService.genCode(name);
        } catch (Exception e) {
            logger.error("Gen code fail**************" + e.getMessage() + "," + e.getClass());
            return e.getMessage() + ",,," + e.getClass();
        }
    }

    @GetMapping("/pass")
    public String savePassport(@RequestParam String name, @RequestParam(required = false) boolean isThrowException) {
        try {
            return combineService.savePassport(name, isThrowException);
        } catch (Exception e) {
            logger.error("Gen code fail**************" + e.getMessage() + "," + e.getClass());
            return e.getMessage() + ",,," + e.getClass();
        }
    }

    @GetMapping("/genconotrans")
    public String genconotrans(@RequestParam String name, @RequestParam(required = false) boolean isThrowException) {
        try {
            combineService.executeNoTrans(name, isThrowException);
        } catch (Exception e) {
            logger.error("Gen code fail**************" + e.getMessage() + "," + e.getClass());
            return e.getMessage() + ",,," + e.getClass();
        }
        return "OK";
    }

    @GetMapping("/retrybutnotrans")
    public String executeRetryButNoTrans(@RequestParam String name, @RequestParam(required = false) boolean isThrowException) {
        try {
            combineService.executeRetryButNoTrans(name, isThrowException);
        } catch (Exception e) {
            logger.error("Gen code fail**************" + e.getMessage() + "," + e.getClass());
            return e.getMessage() + ",,," + e.getClass();
        }
        return "OK";
    }

    @GetMapping("/retryandtrans")
    public String executeRetryAndTrans(@RequestParam String name, @RequestParam(required = false) boolean isThrowException) {
        try {
            combineService.executeRetryAndTrans(name, isThrowException);
        } catch (Exception e) {
            logger.error("Gen code fail**************" + e.getMessage() + "," + e.getClass());
            return e.getMessage() + ",,," + e.getClass();
        }
        return "OK";
    }
    @GetMapping("/retryandtransnorollback")
    public String executeRetryAndTransNoRollback(@RequestParam String name, @RequestParam(required = false) boolean isThrowException) {
        try {
            combineService.executeRetryAndTransNoRollback(name, isThrowException);
        } catch (Exception e) {
            logger.error("Gen code fail**************" + e.getMessage() + "," + e.getClass());
            return e.getMessage() + ",,," + e.getClass();
        }
        return "OK";
    }
    @GetMapping("/executewithtransthenretry")
    public String executeWithTransThenRetry(@RequestParam String name, @RequestParam(required = false) boolean isThrowException) {
        try {
            combineService.executeWithTransThenRetry(name, isThrowException);
        } catch (Exception e) {
            logger.error("Gen code fail**************" + e.getMessage() + "," + e.getClass());
            return e.getMessage() + ",,," + e.getClass();
        }
        return "OK";

    }

    @GetMapping("/saveinner")
    public String saveInner(@RequestParam String name, @RequestParam(required = false) boolean isThrowException) {
        try {
            studentService.saveInner(name, isThrowException);
        } catch (Exception e) {
            logger.error("Gen code fail**************" + e.getMessage() + "," + e.getClass());
            return e.getMessage() + ",,," + e.getClass();
        }
        return "OK";
    }

}
