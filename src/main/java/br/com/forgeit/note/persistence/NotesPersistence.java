package br.com.forgeit.note.persistence;

import br.com.forgeit.note.Note;
import br.com.forgeit.note.NoteStatuses;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class NotesPersistence {

    private final List<Note> notes = new ArrayList<>();

    public Mono<Note> save(String title, String note, String createdBy) {
        final var createdNote = Note.builder()
                .id(UUID.randomUUID().toString())
                .title(title)
                .note(note)
                .status(NoteStatuses.DRAFT)
                .createdAt(LocalDateTime.now())
                .createdBy(createdBy)
                .build();

        notes.add(createdNote);

        return Mono.just(createdNote);
    }

    public Mono<Note> updateExternalId(String id, String externalId) {
        return notes.stream()
                .filter(n -> n.getId().equals(id))
                .findFirst()
                .map(value -> Mono.just(value.toBuilder().externalId(externalId).build()))
                .orElseGet(() -> Mono.error(new RuntimeException("Note not found!")));
    }

}
