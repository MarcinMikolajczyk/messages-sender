package com.marcin.acaisoft.services;

import com.marcin.acaisoft.constant.Constant;
import com.marcin.acaisoft.dto.Send;
import com.marcin.acaisoft.exeptions.MessageNotFoundExeption;
import com.marcin.acaisoft.models.Message;
import com.marcin.acaisoft.repositories.MessageRepositories;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;


@ExtendWith(MockitoExtension.class)
class MessageServicesTest {

    @Mock
    private MessageRepositories messageRepositories;
    @Mock
    private EmailService emailService;

    private MessageServices messageServices;

    @Captor
    private ArgumentCaptor<Message> messageArgumentCaptor;

    @BeforeEach
    public void init(){
        messageServices = new MessageServices(messageRepositories, emailService);
    }

    @Test
    @DisplayName("Should Save And Return Message")
    void create() {
        //given
        Message message = new Message(null, "Marcin.Mikolajczyk902@outlook.com", "New Message",
                "New message content", 1);
        //when
        Mockito.when(messageRepositories.save(message, Constant.TTL)).thenReturn(message);
        Mono<Message> savedMessage = messageServices.create(message);
        //then
        Assertions.assertThat(savedMessage.block().getId()).isNotNull();
    }

    @Test
    @DisplayName("Size of returned list schould be equal to 1")
    void getAllByEmail() {
        //given
        String email = "marcin@wp.pl";
        Integer page = 0;
        Integer size = 10;

        Slice<Message> messageSlice = new SliceImpl<>(
                List.of(new Message(null, "marcin@wp.pl", null, null, null)));

        Mockito.when(messageRepositories.findByEmail(email, CassandraPageRequest.of(page, size)))
                .thenReturn(messageSlice);
        //when
        Flux<Message> messageFlux = messageServices.getAllByEmail(email, page, size);
        //then
        Assertions.assertThat(messageFlux.toStream().count()).isEqualTo(1L);


    }


    @Test
    @DisplayName("Should throw exception because, there is no message with the given magic_number")
    void sendTest_not_valid_magic_number() {
        //given
        Send send = new Send(34);
        //when
        Mockito.when(messageRepositories.findAllByMagicNumber(send.getMagicNumber()))
                .thenReturn(Collections.emptyList());
        //then
        Assertions.assertThatThrownBy(() -> messageServices.send(send)
        ).isInstanceOf(MessageNotFoundExeption.class);

    }

    @Test
    @DisplayName("Should send message")
    void sendTest_valid_magic_number() {
        //given
        Send send = new Send(34);
        List<Message> messages = List.of(
                new Message(null, "marcin@wp.pl", "title", "content", 34));

        //when
        Mockito.when(messageRepositories.findAllByMagicNumber(send.getMagicNumber()))
                .thenReturn(messages);

        messageServices.send(send);
        Mockito.verify(emailService, Mockito.times(1)).send(messageArgumentCaptor.capture());
        //then
        Assertions.assertThat(messageArgumentCaptor.getValue().getEmail()).isEqualTo("marcin@wp.pl");

    }


}