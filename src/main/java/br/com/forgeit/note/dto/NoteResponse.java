package br.com.forgeit.note.dto;

public record NoteResponse(
        String id,
        String status,
        String createdBy,
        String externalId
) {
}
