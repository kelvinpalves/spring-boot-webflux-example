package br.com.forgeit.note.dto;

public record CreateNoteRequest(
        String title,
        String note
) {
}
