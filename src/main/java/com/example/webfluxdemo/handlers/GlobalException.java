package com.example.webfluxdemo.handlers;

import com.example.webfluxdemo.models.exception.ItemNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

public class GlobalException {

    @ExceptionHandler({ItemNotFoundException.class})
    public Mono<ResponseEntity<String>> handlerNotFoundException(ItemNotFoundException ex){
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage()));
    }


}

