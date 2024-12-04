package hu.budapest.messaging.application.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Message {

    @Id
    @GeneratedValue
    private Long id;
    private String content;
    private MessageState state;

    public Message updateStatus(MessageState newState) {
        state = newState;
        return this;
    }
}
