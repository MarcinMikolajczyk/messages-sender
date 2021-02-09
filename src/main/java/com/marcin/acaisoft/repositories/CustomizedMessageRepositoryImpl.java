package com.marcin.acaisoft.repositories;

import com.marcin.acaisoft.models.Message;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.InsertOptions;

public class CustomizedMessageRepositoryImpl implements CustomizedMessageRepository {

    private final CassandraOperations cassandraOperations;

    public CustomizedMessageRepositoryImpl(CassandraOperations cassandraOperations) {
        this.cassandraOperations = cassandraOperations;
    }

    @Override
    public Message save(Message message, Integer ttl) {
        return cassandraOperations.insert(message, InsertOptions.builder().ttl(ttl).build()).getEntity();
    }
}
