package com.marcin.acaisoft.controllers;

import com.marcin.acaisoft.dto.Send;
import com.marcin.acaisoft.models.Message;
import com.marcin.acaisoft.repositories.MessageRepositories;
import com.marcin.acaisoft.services.EmailService;
import com.marcin.acaisoft.services.MessageServices;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = MessageController.class)
@Import(MessageServices.class)
class MessageControllerUnitTest {

    @Mock
    private MessageRepositories messageRepositories;

    @Mock
    private EmailService emailService;

    @MockBean
    private MessageServices messageServices;

    @Captor
    private ArgumentCaptor<Message> messageArgumentCaptor;

    @Captor
    private ArgumentCaptor<Send> sendArgumentCaptor;

    @Autowired
    private WebTestClient webTestClient;


    @Test
    @DisplayName("Should return objects Message.class by email")
    void getByEmail_valid_email_format() {
        //given
        List<Message> responseValues = List.of(
                new Message(UUID.randomUUID(), "marcin@wp.pl", "title1", "content1",  1),
                new Message(UUID.randomUUID(), "marcin@wp.pl", "title2", "content2",  1));

        String email = "marcin@wp.pl";
        int page = 0;
        int size = 10;
        //when
        when(messageServices.getAllByEmail(email, page, size)).thenReturn(Flux.fromStream(responseValues.stream()));
        //then
        webTestClient
                .get()
                .uri("/api/messages/" + email + "?page="+page+"&size="+size)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Message.class);

        verify(messageServices, times(1)).getAllByEmail(email, page, size);

    }


    @Test
    @DisplayName("Should return objects Message.class by email")
    void getByEmail_not_valid_email_format() {
        //given
        String email = "marcin";
        //when
        //then
        webTestClient
                .get()
                .uri("/api/messages/" + email)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }


    @Test
    @DisplayName("Should remove objects witch correct magic number")
    void send_correct_magic_number() {
        //given
        Send send = new Send(1);

        //when
        webTestClient
                .post()
                .uri("/api/send")
                .body(Mono.just(send), Send.class)
                .exchange().expectStatus().isOk();
        verify(messageServices, times(1)).send(sendArgumentCaptor.capture());
        Assertions.assertThat(sendArgumentCaptor.getValue().getMagicNumber()).isEqualTo(send.getMagicNumber());

    }


    @Test
    @DisplayName("Should return bad request status")
    void create_bad_json() {
        //given
        Message newMessage = new Message(null, "marcinpl", "", "content", 1);
        //when
        //then
        webTestClient
                .post()
                .uri("/api/messages")
                .body(Mono.just(newMessage), Message.class)
                .exchange()
                .expectStatus()
                .isBadRequest();

    }

    @Test
    @DisplayName("Should correct add new object")
    void create_correct_json() {
        //given
        Message newMessage = new Message(null, "marcin@wp.pl", "title", "content", 1);
        //when
        when(messageServices.create(newMessage)).thenReturn(Mono.just(newMessage));
        //then
        webTestClient
                .post()
                .uri("/api/messages")
                .body(Mono.just(newMessage), Message.class)
                .exchange()
                .expectStatus()
                .isCreated();

        verify(messageServices, times(1)).create(messageArgumentCaptor.capture());
        Assertions.assertThat(messageArgumentCaptor.getValue().getEmail()).isEqualTo("marcin@wp.pl");
    }
}