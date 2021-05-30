package com.codecafe.kafka.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LibraryEvent {

    private Integer libraryEventId;
    private LibraryEventType libraryEventType;

    @NotNull
    @Valid
    private Book book;

}