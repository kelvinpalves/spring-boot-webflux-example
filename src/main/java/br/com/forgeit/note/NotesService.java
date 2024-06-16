package br.com.forgeit.note;

import br.com.forgeit.note.client.NoteApiResponse;
import br.com.forgeit.note.dto.CreateNoteRequest;
import br.com.forgeit.note.persistence.NotesPersistence;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class NotesService {

    private final NotesPersistence persistence;
    private final WebClient webClient;

    public Mono<Note> create(CreateNoteRequest request, String createdBy) {
        return persistence.save(request.title(), request.note(), createdBy)
                .flatMap(this::integrationWithExternalSystem)
                .flatMap(noteApiResponse -> persistence.updateExternalId(noteApiResponse.getId(), noteApiResponse.getExternalId()));
    }

    public Mono<NoteApiResponse> integrationWithExternalSystem(Note note) {
        return webClient
                .post()
                .uri("/notes")
                .bodyValue(note)
                .retrieve()
                .bodyToMono(NoteApiResponse.class);
    }

}
