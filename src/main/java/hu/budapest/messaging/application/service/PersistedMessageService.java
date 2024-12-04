package hu.budapest.messaging.application.service;

import hu.budapest.messaging.application.domain.Message;
import hu.budapest.messaging.application.domain.MessageCreated;
import hu.budapest.messaging.application.domain.MessageRepository;
import hu.budapest.messaging.application.domain.MessageState;
import hu.budapest.messaging.application.infrastructure.EventBus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static hu.budapest.messaging.application.domain.MessageState.NEW;

@Service
@RequiredArgsConstructor
public class PersistedMessageService implements MessageService {

    private static final Logger log = LoggerFactory.getLogger(PersistedMessageService.class);

    private final EventBus eventBus;
    private final MessageRepository messageRepository;

    @Override
    public void add(String content) {
        var message = new Message();

        message.setContent(content);
        message.setState(NEW);

        var persistedMessage = messageRepository.save(message);

        eventBus.sendDomainEvent(new MessageCreated(persistedMessage.getId()));
    }

    @Override
    public List<Message> getAll() {
        return messageRepository.findAll();
    }

    @Override
    public void changeStatus(Long id, MessageState newState) {
        messageRepository.findById(id)
                .map(message -> message.updateStatus(newState))
                .ifPresent(message -> {
                    messageRepository.save(message);
                    log.info("Message({}) status updated to {}", message.getId(), message.getState());

                    if (NEW == newState) {
                        eventBus.sendDomainEvent(new MessageCreated(id));
                    }
                });
    }
}
