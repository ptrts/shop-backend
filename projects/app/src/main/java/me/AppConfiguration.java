package me;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class AppConfiguration {
    
    @Bean
    @Qualifier("request")
    Scheduler requestScheduler() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        return Schedulers.fromExecutorService(executorService);
    }
    
    @Bean
    @Qualifier("response")
    Scheduler responseScheduler() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        return Schedulers.fromExecutorService(executorService);
    }
}
