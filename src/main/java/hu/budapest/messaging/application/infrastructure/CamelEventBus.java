package hu.budapest.messaging.application.infrastructure;

import lombok.RequiredArgsConstructor;
import org.apache.camel.ProducerTemplate;
import org.springframework.stereotype.Service;

import static hu.budapest.messaging.integration.CamelIntegration.DOMAIN_ROUTE;

@Service
@RequiredArgsConstructor
public class CamelEventBus implements EventBus {

    private final ProducerTemplate producer;

    @Override
    public void sendDomainEvent(Object event) {
        producer.asyncSendBody(DOMAIN_ROUTE, event);
    }
}
