# JavaWebSocketChat
Spring-boot, JPA, WebSocket을 활용하며 만든 채팅 프로그램입니다.

해당 프로그램은 MySQL 데이터베이스를 사용하고 있습니다.\
테이블 생성문
```sql
CREATE DATABASE webSocketChat;

CREATE USER 'webSocketChat'@'%' IDENTIFIED BY '1234';

GRANT ALL PRIVILEGES ON webSocketChat.* TO 'webSocketChat'@'%';

FLUSH PRIVILEGES;

create table chat
(
    chat_seq     int auto_increment comment '채팅 SEQ'
        primary key,
    chat_type    char         null comment '채팅 타입(1: 사용자, 2: 관리자)',
    chat_message text         null comment '채팅 내용',
    nickname     varchar(100) null comment '닉네임',
    ip           varchar(100) null comment 'IP',
    chat_regdate datetime     null comment '작성시간'
);

```
