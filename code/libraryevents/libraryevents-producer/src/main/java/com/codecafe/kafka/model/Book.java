package com.codecafe.kafka.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Book {

    @NotNull
    private Integer bookId;

    @NotBlank
    private String bookName;

    private String bookAuthor;

}