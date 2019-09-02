package com.example.demo.config;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by zm on 2019/9/2.
 */
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT30S")
@Configuration
public class MyLockConfiguration {

    //@Bean
    //public LockProvider lockProvider(DataSource dataSource) {
    //    return new JdbcTemplateLockProvider(dataSource);
    //}
    //
    //@Scheduled(cron = "0/5 * * * * *")
    //@SchedulerLock(name = "TaskScheduler_scheduledTask",
    //    lockAtLeastForString = "PT5M", lockAtMostForString = "PT14M")
    //public void scheduledTask() {
    //    System.out.println("Lock" + new Date());
    //}


}
