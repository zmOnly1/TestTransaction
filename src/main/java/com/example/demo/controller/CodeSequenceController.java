package com.example.demo.controller;

import com.example.demo.service.CodeSequenceService;
import com.example.demo.service.CombineService;
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

    @GetMapping("/gen")
    public String genCode(@RequestParam String name){
        try {
            return combineService.execute(name);
        } catch (Exception e) {
            logger.error("Gen code fail**************" + e.getMessage() + "," + e.getClass());
            return e.getMessage() + ",,," + e.getClass();
        }
    }

    @GetMapping("/gen2")
    public String genCode2(@RequestParam String name){
        try {
            return codeSequenceService.genCode(name);
        } catch (Exception e) {
            logger.error("Gen code fail**************" + e.getMessage() + "," + e.getClass());
            return e.getMessage() + ",,," + e.getClass();
        }
    }

    @GetMapping("/pass")
    public String savePassport(@RequestParam String name){
        try {
            return combineService.savePassport(name);
        } catch (Exception e) {
            logger.error("Gen code fail**************" + e.getMessage() + "," + e.getClass());
            return e.getMessage() + ",,," + e.getClass();
        }
    }

}
