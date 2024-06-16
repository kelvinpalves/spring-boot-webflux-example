package br.com.forgeit.note.api;

import br.com.forgeit.note.NotesService;
import br.com.forgeit.note.dto.CreateNoteRequest;
import br.com.forgeit.note.dto.NoteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NotesController {

    private final NotesService service;

    @PostMapping
    public Mono<ResponseEntity<NoteResponse>> create(@RequestBody CreateNoteRequest request,
                                     @RequestHeader(value = "user") String user) {
        return service.create(request, user)
                .flatMap(note -> Mono.just(new NoteResponse(note.getId(),
                        note.getStatus().name(),
                        note.getCreatedBy(),
                        note.getExternalId())))
                .map(ResponseEntity::ok)
                .onErrorResume(_ -> Mono.just(ResponseEntity.badRequest().build()));
    }

}
