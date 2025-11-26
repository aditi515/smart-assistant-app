package Smart.Assistant.Backend.project.service;

import Smart.Assistant.Backend.project.entity.ChatHistory;
import Smart.Assistant.Backend.project.payload.ChatRequest;
import Smart.Assistant.Backend.project.payload.ChatResponse;
import Smart.Assistant.Backend.project.repository.ChatHistoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    private final WebClient webClient;
    private final ChatHistoryRepository chatHistoryRepository;

    public GeminiService(WebClient.Builder builder, ChatHistoryRepository chatHistoryRepository) {
        this.webClient = builder.build();
        this.chatHistoryRepository = chatHistoryRepository;
    }

    public ChatResponse askGemini(ChatRequest request,String userEmail) {
        // Request body
        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(Map.of("text", request.getQuestion())))
                )
        );

        System.out.println("Sending to Gemini API: " + request.getQuestion());

        Map<String, Object> response = webClient.post()
                .uri(geminiApiUrl + "?key=" + geminiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        String answer = extractAnswer(response);

        // Save chat history
        ChatHistory history = ChatHistory.builder()
                .userEmail(userEmail)
                .question(request.getQuestion())
                .answer(answer)
                .createdAt(Timestamp.from(Instant.now()))
                .build();

        chatHistoryRepository.save(history);
        // Extract answer
        return new ChatResponse(extractAnswer(response));
    }

    private String extractAnswer(Map<String, Object> response) {
        try {
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
            Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
            return (String) parts.get(0).get("text");
        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing Gemini response.";
        }
    }
}
