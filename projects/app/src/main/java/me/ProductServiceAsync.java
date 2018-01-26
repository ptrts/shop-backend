package me;

import me.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@Component
public class ProductServiceAsync {
    
    @Autowired
    private ProductServiceSync productServiceSync;

    @Autowired
    @Qualifier("request")
    private Scheduler requestScheduler;
    
    @Autowired
    @Qualifier("response")
    private Scheduler responseScheduler;
    
    public Mono<Product> getProduct(Long id) {
        
        return 
                Mono.fromSupplier(
                        // При подписке на эту нашу мону, нужно:
                        //      откуда-то взять какой-нибудь Scheduler
                        //      в этот Scheduler передать задачу, которая:
                        //          выполнит указанный ниже Supplier
                        //          получит возвращенное им значение
                        //          оповестит подписчика о поступлении нового значения
                        () -> productServiceSync.getProduct(id)
                )
                .subscribeOn(requestScheduler)
                .publishOn(responseScheduler);
    }
}
