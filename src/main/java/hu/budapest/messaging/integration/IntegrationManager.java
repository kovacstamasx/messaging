package hu.budapest.messaging.integration;

import hu.budapest.messaging.application.domain.MessageCreated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class IntegrationManager {

    @ApplicationModuleListener
    void on(MessageCreated event) {
        log.info("Message created event received, messageId: {}", event.messageId());
    }
}
