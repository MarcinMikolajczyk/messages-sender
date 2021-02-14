package com.marcin.acaisoft.services;

import com.marcin.acaisoft.constant.Constant;
import com.marcin.acaisoft.dto.Send;
import com.marcin.acaisoft.exeptions.MessageNotFoundExeption;
import com.marcin.acaisoft.models.Message;
import com.marcin.acaisoft.repositories.MessageRepositories;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
public class MessageServices {

    private final MessageRepositories messageRepositories;
    private final EmailService emailService;

    public MessageServices(MessageRepositories messageRepositories, EmailService emailService){
        this.messageRepositories = messageRepositories;
        this.emailService = emailService;
    }

    public Mono<Message> create(Message message){
        message.setId(UUID.randomUUID());
        return Mono.just(messageRepositories.save(message, Constant.TTL));
    }

    public Flux<Message> getAllByEmail(String email, Integer page, Integer size){
        Integer currentPage = 0;

        Slice<Message> messages = messageRepositories.findByEmail(email, CassandraPageRequest.of(currentPage, size));
        while (!currentPage.equals(page) && messages.hasNext()){
            messages = messageRepositories.findByEmail(email, messages.nextPageable());
            currentPage++;
        }

        return Flux.fromStream(messages.stream());
    }

    public void send(Send send){
        List<Message> messageList = messageRepositories.findAllByMagicNumber(send.getMagicNumber());
        if (messageList.isEmpty()) throw new MessageNotFoundExeption("magic_number", String.valueOf(send.getMagicNumber()));
        messageList.stream().forEach(emailService::send);
        messageRepositories.deleteAll(messageList);
    }
}
