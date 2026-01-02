package Smart.Assistant.Backend.project.controller;

import Smart.Assistant.Backend.project.entity.ChatHistory;
import Smart.Assistant.Backend.project.payload.ChatRequest;
import Smart.Assistant.Backend.project.payload.ChatResponse;
import Smart.Assistant.Backend.project.repository.ChatHistoryRepository;
import Smart.Assistant.Backend.project.security.JwtUtil;
import Smart.Assistant.Backend.project.service.GeminiService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ask")
public class AIController {
    private final JwtUtil jwtUtil;
    private final GeminiService geminiService;
    private final ChatHistoryRepository chatHistoryRepository;

    public AIController(JwtUtil jwtUtil, GeminiService geminiService, ChatHistoryRepository chatHistoryRepository) {
        this.jwtUtil = jwtUtil;
        this.geminiService = geminiService;
        this.chatHistoryRepository = chatHistoryRepository;

    }

    @PostMapping
    public ChatResponse askAI(@Valid @RequestBody ChatRequest chatRequest, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String email = jwtUtil.getEmailFromToken(token);

        System.out.println("User [" + email + "] asked: " + chatRequest.getQuestion());

        return geminiService.askGemini(chatRequest,email);
    }

    @GetMapping("/history")
    public List<ChatHistory> getChatHistory(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String email = jwtUtil.getEmailFromToken(token);
        return chatHistoryRepository.findHistoryByUserEmail(email);
    }
}
