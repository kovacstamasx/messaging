package hu.budapest.messaging.application.web;

import hu.budapest.messaging.application.domain.Message;
import hu.budapest.messaging.application.domain.MessageState;
import hu.budapest.messaging.application.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageResource {

    private final MessageService messageService;

    @PostMapping
    public void addMessage(@RequestBody String content) {
        messageService.add(content);
    }

    @GetMapping
    public List<Message> getMessages() {
        return messageService.getAll();
    }

    @PatchMapping("/{id}")
    public void changeMessageStatus(@PathVariable Long id, @RequestParam MessageState newState) {
        messageService.changeStatus(id, newState);
    }
}
