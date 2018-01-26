package me;

import me.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping
public class ProductController {
    
    @Autowired
    private ProductServiceAsync productServiceAsync;

    @GetMapping("{id}")
    public Mono<Product> getProduct(@PathVariable("id") Long id) {
        return productServiceAsync.getProduct(id);
    }
}
