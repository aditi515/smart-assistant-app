package Smart.Assistant.Backend.project.repository;

import Smart.Assistant.Backend.project.entity.Note;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NoteRepository extends CrudRepository<Note, Long> {

    @Query(value = "select * from notes where user_email = :email ", nativeQuery = true)
    List<Note> findNotesByUserEmail(String email);

}
