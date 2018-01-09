package me;

import me.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping
public class AppController {
    
    @Autowired
    private AppServiceAsync appServiceAsync;

    @GetMapping("{id}")
    public Mono<User> getUser(@PathVariable("id") Long id) {
        return appServiceAsync.getUser(id);
    }
}
