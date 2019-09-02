package com.example.demo.service;

import com.example.demo.domain.PassportNumber;
import com.example.demo.repository.PassportNumberRepository;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.*;

/**
 * Created by zm on 2019/8/11.
 */
@Service
public class PassportNumberService {

    private final PassportNumberRepository passportNumberRepository;

    public PassportNumberService(PassportNumberRepository passportNumberRepository) {
        this.passportNumberRepository = passportNumberRepository;
    }

    //@Transactional
    public void savePassportWithTrans(String passportNumber, boolean isThrowException) {
        passportNumberRepository.save(new PassportNumber(passportNumber));
        if (isThrowException) {
            throw new RuntimeException("For test throw exception");
        }
    }

    public void savePassportWithNoTrans(String passportNumber, boolean isThrowException) {
        passportNumberRepository.save(new PassportNumber(passportNumber));
        if (isThrowException) {
            throw new RuntimeException("For test throw exception");
        }
    }

    private AtomicInteger num = new AtomicInteger(0);

    @Retryable(value = {Exception.class},
        maxAttempts = 3, backoff = @Backoff(delay = 200, multiplier = 1))
    public void savePassportWithRetryButNoTrans(String passportNumber, boolean isThrowException) {
        int i = num.incrementAndGet();
        passportNumberRepository.save(new PassportNumber(passportNumber + ":::" + i));
        if (isThrowException && i < 2) {
            throw new RuntimeException("For test throw exception");
        }
        num.set(0);
    }

    @Transactional
    @Retryable(value = {Exception.class},
        maxAttempts = 3, backoff = @Backoff(delay = 200, multiplier = 1))
    public void savePassportWithRetryAndTrans(String passportNumber, boolean isThrowException) {
        int i = num.incrementAndGet();
        passportNumberRepository.save(new PassportNumber(passportNumber + ":::" + i));
        if (isThrowException && i < 2) {
            throw new RuntimeException("For test throw exception");
        }
        num.set(0);
    }

    @Transactional(noRollbackFor = RuntimeException.class)
    @Retryable(value = {Exception.class},
        maxAttempts = 3, backoff = @Backoff(delay = 200, multiplier = 1))
    public void savePassportWithRetryAndTransNoRollback(String passportNumber, boolean isThrowException) {
        int i = num.incrementAndGet();
        passportNumberRepository.save(new PassportNumber(passportNumber + ":::" + i));
        if (isThrowException && i < 2) {
            throw new RuntimeException("For test throw exception");
        }
        num.set(0);
    }

    @Transactional
    public void savePassportWithTransThenRetry(String passportNumber, boolean isThrowException){
        savePassportWithRetry(passportNumber, isThrowException);
    }

    @Retryable(value = {Exception.class},
        maxAttempts = 3, backoff = @Backoff(delay = 200, multiplier = 1))
    public void savePassportWithRetry(String passportNumber, boolean isThrowException) {
        int i = num.incrementAndGet();
        passportNumberRepository.save(new PassportNumber(passportNumber + ":::" + i));
        if (isThrowException && i < 2) {
            throw new RuntimeException("For test throw exception");
        }
        num.set(0);
    }

}
