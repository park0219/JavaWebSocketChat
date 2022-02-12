package me.park.javawebsocketchat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "chat")
public class ChatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_seq")
    private Long chatSeq;

    @Column(name = "chat_type")
    private char chatType;

    @Column(columnDefinition = "TEXT", name = "chat_message")
    private String chatMessage;

    @Column(length = 100)
    private String nickname;

    @Column(length = 100)
    private String ip;

    @Column(columnDefinition = "DATETIME", name = "chat_regdate")
    private LocalDateTime chatRegdate;

}
