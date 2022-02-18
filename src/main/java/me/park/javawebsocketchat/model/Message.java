package me.park.javawebsocketchat.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Message {

    String message = "";
    String link = "";

    public Message(String message, String link) {
        this.message = message;
        this.link = link;
    }
}
