package com.codecafe.kafka.avro.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "books")
public class BookEntity {

  @Id
  private String isbn;
  private String title;
  private String author;

}