package hu.budapest.messaging.application.infrastructure;

public interface EventBus {

    void sendDomainEvent(Object event);
}
