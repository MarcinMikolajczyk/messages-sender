package com.marcin.acaisoft.repositories;

import com.marcin.acaisoft.models.Message;

public interface CustomizedMessageRepository {
    Message save(Message message, Integer ttl);
}
