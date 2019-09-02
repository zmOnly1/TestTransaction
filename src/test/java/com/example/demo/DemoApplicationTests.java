package com.example.demo;

import net.javacrumbs.shedlock.core.LockConfiguration;
import net.javacrumbs.shedlock.core.SimpleLock;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

import static java.time.Instant.now;

@ActiveProfiles("mysql")
@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Autowired
    private DataSource dataSource;

    @Test
    public void testLock() throws InterruptedException {
        int nThreads = 1;
        JdbcTemplateLockProvider lockProvider = new JdbcTemplateLockProvider(dataSource);
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        currencyDoTask(nThreads,
            index -> doTask(lockProvider, index,
                lockName -> System.out.println(index + ",I acquire a lock " + lockName + " at " + now())), executorService);
        System.out.println("Finish");
        new Scanner(System.in).next();
    }

    private void currencyDoTask(int nThread, Consumer<Integer> consumer, ExecutorService executorService) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(nThread);
        for (int i = 0; i < nThread; i++) {
            int finalI = i;
            executorService.submit(() -> {
                try {
                    consumer.accept(finalI);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
    }

    private void doTask(JdbcTemplateLockProvider lockProvider, Integer index, Consumer<String> consumer) {
        System.out.println("Begin to acquire lock " + now());
        Instant now = now();
        String lockName = "myLock";
        LockConfiguration lockConfiguration = new LockConfiguration(
            lockName,
            now.plus(Duration.parse("PT10s")),
            now.plus(Duration.parse("PT5s")));
        Optional<SimpleLock> lock = lockProvider.lock(lockConfiguration);
        if (lock.isPresent()) {
            try {
                consumer.accept(lockName);
            } finally {
                lock.get().unlock();
            }
        } else {
            System.out.println(index + " could not acquire lock.");
        }
    }

}
