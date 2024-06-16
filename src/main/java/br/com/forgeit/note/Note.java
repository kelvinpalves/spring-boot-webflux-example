package br.com.forgeit.note;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
public class Note {
    String id;
    NoteStatuses status;
    String title;
    String note;
    String createdBy;
    String externalId;
    LocalDateTime createdAt;
}
