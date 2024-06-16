package br.com.forgeit.note.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NoteApiResponse {
    @JsonProperty("_id")
    String externalId;
    String id;
}
