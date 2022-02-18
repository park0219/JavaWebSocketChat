package me.park.javawebsocketchat.chat;

import me.park.javawebsocketchat.model.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, Long> {

    List<ChatEntity> findByChatRegdateBetweenOrderByChatRegdateAsc(LocalDateTime start, LocalDateTime end);
}
