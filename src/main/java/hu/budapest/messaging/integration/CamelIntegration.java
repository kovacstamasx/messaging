package hu.budapest.messaging.integration;

import hu.budapest.messaging.application.domain.MessageCreated;
import hu.budapest.messaging.application.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.errorhandler.DefaultErrorHandlerDefinition;
import org.springframework.stereotype.Component;

import static hu.budapest.messaging.application.domain.MessageState.DELIVERED;
import static hu.budapest.messaging.application.domain.MessageState.FAILED;
import static hu.budapest.messaging.application.domain.MessageState.IN_PROGRESS;

@Slf4j
@Component
@RequiredArgsConstructor
public class CamelIntegration extends RouteBuilder {

    public static final String DEAD_LETTER = "seda:domain-event-dead-letter";
    public static final String DOMAIN_ROUTE = "direct:domain";

    private final MessageService messageService;

    @Override
    public void configure() throws Exception {
        from(DOMAIN_ROUTE)
                .routeId("domain-event-route")
                .errorHandler(getDeadLetter())
                .choice()
                .when(bodyAs(MessageCreated.class))
                    .process(exchange -> {
                        MessageCreated event = exchange.getIn().getBody(MessageCreated.class);
                        messageService.changeStatus(event.messageId(), IN_PROGRESS);
                        log.info("Set message({}) state to IN_PROGRESS", event.messageId());

                        if (event.messageId() == 1) {
                            throw new RuntimeException("Nem mukodik :(");
                        }

                        log.info("Set message({}) state to DELIVERED", event.messageId());
                        messageService.changeStatus(event.messageId(), DELIVERED);
                    })
                .endChoice();

        from(DEAD_LETTER)
                .routeId("dead-letter-route")
                .process(exchange -> {
                    MessageCreated event = exchange.getIn().getBody(MessageCreated.class);
                    log.warn("Set message({}) state to FAILED", event.messageId());
                    messageService.changeStatus(event.messageId(), FAILED);
                });
    }

    private DefaultErrorHandlerDefinition getDeadLetter() {
        return deadLetterChannel(DEAD_LETTER)
                .useOriginalMessage()
                .maximumRedeliveries(3)  // Retry 3 times
                .redeliveryDelay(2000);
    }
}
