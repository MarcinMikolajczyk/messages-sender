package com.marcin.acaisoft.repositories;

import com.marcin.acaisoft.models.Message;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepositories extends CassandraRepository<Message, UUID>, CustomizedMessageRepository {
    @AllowFiltering
    Slice<Message> findByEmail(String email, Pageable pageRequest);
    @AllowFiltering
    List<Message> findAllByMagicNumber(Integer magic_number);
}
