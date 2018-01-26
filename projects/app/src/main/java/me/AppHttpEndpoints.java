package me;

import me.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.NettyContext;
import reactor.ipc.netty.http.server.HttpServer;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
public class AppHttpEndpoints extends AbstractSmartLifeCycle {

    private static final Logger log = LoggerFactory.getLogger(AppHttpEndpoints.class);

    private static final Mono<ServerResponse> NOT_FOUND_SERVER_RESPONSE_MONO = ServerResponse.notFound().build();

    private NettyContext nettyContext = null;
    
    @Autowired
    private AppServiceAsync appServiceAsync;

    private Mono<ServerResponse> getPerson(ServerRequest request) {

        String userIdStr = request.pathVariable("id");
        
        Long userId = Long.valueOf(userIdStr);

        Mono<User> userMono = appServiceAsync.getUser(userId);

        return userMono
                
                // Если пользователь найден, то сделать из него мону преобразования этого 
                // пользователя в ServerResponse
                .flatMap(

                        user -> {

                            Mono<ServerResponse> serverResponseMono =
                                    ServerResponse
                                            .ok()
                                            .contentType(APPLICATION_JSON)
                                            .syncBody(user);

                            return serverResponseMono;
                        }
                )
                
                // Если пользователь не найден, то мы не делаем мону создания 
                // успешного ServerResponse, содержащего 0 пользователей. 
                // Вместо этого мы делаем мону создания ServerResponse, который содержит ошибку
                .switchIfEmpty(
                        NOT_FOUND_SERVER_RESPONSE_MONO
                );
    }
    
    @Override
    protected void onStart() {
        
        RouterFunction<ServerResponse> route =
                route(
                        GET("/person/{id}")
                                .and(
                                        accept(APPLICATION_JSON)
                                )
                        ,
                        this::getPerson
                );

        HttpHandler httpHandler = RouterFunctions.toHttpHandler(route);

        ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(httpHandler);

        HttpServer server = HttpServer.create("localhost", 8080);

        Mono<? extends NettyContext> serverHandlerMono;

        serverHandlerMono = server.newHandler(adapter);

        nettyContext = serverHandlerMono.block();

        log.info("=== nettyContext = {}", nettyContext);
    }

    @Override
    protected void onStop() {
        log.info("=== onStop");
        if (nettyContext != null) {
            log.info("=== dispose");
            nettyContext.dispose();
        }
    }
}
