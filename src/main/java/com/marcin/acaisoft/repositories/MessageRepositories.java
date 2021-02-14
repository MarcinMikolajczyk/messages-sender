package com.marcin.acaisoft.repositories;

import com.marcin.acaisoft.models.Message;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.UUID;

@EnableCassandraRepositories
public interface MessageRepositories extends CassandraRepository<Message, UUID>, CustomizedMessageRepository {
    Slice<Message> findByEmail(String email, Pageable pageRequest);
    List<Message> findAllByMagicNumber(Integer magic_number);
}
