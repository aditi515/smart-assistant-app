package Smart.Assistant.Backend.project.repository;

import Smart.Assistant.Backend.project.entity.ChatHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {

    @Query(value = "SELECT * FROM chat_history WHERE user_email = :email ORDER BY created_at DESC", nativeQuery = true)
    List<ChatHistory> findHistoryByUserEmail(String email);
}
