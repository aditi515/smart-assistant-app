package Smart.Assistant.Backend.project.controller;

import Smart.Assistant.Backend.project.entity.Note;
import Smart.Assistant.Backend.project.exception.UnauthorizedException;
import Smart.Assistant.Backend.project.payload.NoteRequest;
import Smart.Assistant.Backend.project.repository.NoteRepository;
import Smart.Assistant.Backend.project.repository.UserRepository;
import Smart.Assistant.Backend.project.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {
    private final NoteRepository noteRepository;
    private final JwtUtil jwtUtil;

    public NoteController(NoteRepository noteRepository, JwtUtil jwtUtil) {
        this.noteRepository = noteRepository;
        this.jwtUtil = jwtUtil;
    }
    @GetMapping
    public List<Note> getAllNotes(HttpServletRequest request) {
        String email = extractEmailFromRequest(request);
        return noteRepository.findNotesByUserEmail(email);
    }

    @PostMapping
    public Note createNote(@Valid @RequestBody NoteRequest noteRequest, HttpServletRequest request) {
        String email = extractEmailFromRequest(request);
        Note note = Note.builder()
                .userEmail(email)
                .content(noteRequest.getContent())
                .build();
        return noteRepository.save(note);
    }

    @PutMapping("/{id}")
    public Note updateNote(@PathVariable Long id,
                           @Valid @RequestBody NoteRequest noteRequest,
                           HttpServletRequest request) {
        String email = extractEmailFromRequest(request);
        Note note = noteRepository.findById(id).orElseThrow(() -> new RuntimeException("Note not found"));
        if (!note.getUserEmail().equals(email)) {
            throw new UnauthorizedException("Unauthorized to edit this note");
        }
        note.setContent(noteRequest.getContent());
        return noteRepository.save(note);

    }
    @DeleteMapping("/{id}")
    public String deleteNote(@PathVariable Long id,
                           HttpServletRequest request) {
        String email = extractEmailFromRequest(request);
        Note note = noteRepository.findById(id).orElseThrow(() -> new RuntimeException("Note not found"));
        if (!note.getUserEmail().equals(email)) {
            throw new UnauthorizedException("Unauthorized to edit this note");
        }
        noteRepository.delete(note);
        return "Note deleted successfully";
    }

    private String extractEmailFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return jwtUtil.getEmailFromToken(token);
    }
}
