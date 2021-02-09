package com.marcin.acaisoft.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.marcin.acaisoft.dto.Send;
import com.marcin.acaisoft.jsonviews.Views;
import com.marcin.acaisoft.models.Message;
import com.marcin.acaisoft.services.MessageServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.Email;

@RestController
@RequestMapping("/api")
public class MessageController {

    private final MessageServices messageServices;

    public MessageController(MessageServices messageServices){
        this.messageServices = messageServices;
    }

    @PostMapping("/messages")
    @ResponseStatus(HttpStatus.CREATED)
    @JsonView(Views.Public.class)
    public Mono<Message> create(@Valid @RequestBody Message message){
        return messageServices.create(message);
    }

    @GetMapping(value = "/messages/{email}", produces = MediaType.APPLICATION_NDJSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Views.Public.class)
    public Flux<Message> getByEmail(@Valid @PathVariable("email") @Email String email,
                                    @RequestParam(defaultValue = "0") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size){
        return messageServices.getAllByEmail(email, page, size);
    }

    @PostMapping("/send")
    @ResponseStatus(HttpStatus.OK)
    public void send(@Valid @RequestBody Send send){
        messageServices.send(send);
    }


}
