package com.example.demo.service;

import com.example.demo.domain.PassportNumber;
import com.example.demo.repository.PassportNumberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zm on 2019/8/11.
 */
@Service
public class PassportNumberService {

    private final PassportNumberRepository passportNumberRepository;

    public PassportNumberService(PassportNumberRepository passportNumberRepository) {
        this.passportNumberRepository = passportNumberRepository;
    }

    @Transactional
    public void savePassport(String passportNumber) {
        passportNumberRepository.save(new PassportNumber(passportNumber));
        //int i = 1/0;
    }

}
