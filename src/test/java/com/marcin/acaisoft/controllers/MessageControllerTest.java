package com.marcin.acaisoft.controllers;


import com.marcin.acaisoft.dto.Send;
import com.marcin.acaisoft.models.Message;
import com.marcin.acaisoft.services.MessageServices;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;


@SpringBootTest
@AutoConfigureWebTestClient
class MessageControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private MessageServices messageServices;

    @Test
    @DisplayName("Should make POST request to add new message")
    void create_method_test_witch_valid_data() {
        //given
        Message message = new Message(null, "Marcin.Mikolajczyk902@outlook.com", "New Message",
                "New message content", 1);
        //when
        this.webTestClient
                .post()
                .uri("/api/messages")
                .body(Mono.just(message), Message.class)
                .exchange()
                //then
                .expectStatus()
                .isCreated()
                .expectBody(Message.class)
                .value(message1 -> message1.getEmail().equals(message.getEmail()));
    }

    @Test
    @DisplayName("Email value is empty, should throw exception WebExchangeBindException.class")
    void createTest_witch_email_empty() {
        //given
        Message message = new Message(null, "", "New Message",
                "New message content", 1);
        //when
        this.webTestClient
                .post()
                .uri("/api/messages")
                .body(Mono.just(message), Message.class)
                .exchange()
                //then
                .expectStatus()
                .isBadRequest();

    }

    @Test
    @DisplayName("Should return objects Message.class by email")
    void getByEmailTest_witch_valid_email() {
        //given
        messageServices.create(new Message(null, "marcin@wp.pl", "test1", "test1", 1));
        messageServices.create(new Message(null, "marcin@wp.pl", "test2", "test2", 5));
        //when
        this.webTestClient
                .get()
                .uri("/api/messages/marcin@wp.pl")
                .exchange()
                //then
                .expectStatus()
                .isOk();
    }

    @Test
    @DisplayName("Should send message and return ok status")
    void sendTest_witch_valid_magic_number() {
        //given
        messageServices.create(new Message(null, "acaisoft.homework@gmail.com", "test2", "test2", 70));
        Send send = new Send(70);
        //when
        this.webTestClient
                .post()
                .uri("/api/send")
                .body(Mono.just(send), Send.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    @DisplayName("Should return not_found status because, there is no message with the given magic_number")
    void sendTest_witch_not_valid_magic_number() {
        //given
        Send send = new Send(7777);
        //when
        this.webTestClient
                .post()
                .uri("/api/send")
                .body(Mono.just(send), Send.class)
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }
}