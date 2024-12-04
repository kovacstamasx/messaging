package hu.budapest.messaging.application.service;

import hu.budapest.messaging.application.domain.Message;
import hu.budapest.messaging.application.domain.MessageState;

import java.util.List;

public interface MessageService {
    void add(String content);

    List<Message> getAll();

    void changeStatus(Long id, MessageState newState);
}
