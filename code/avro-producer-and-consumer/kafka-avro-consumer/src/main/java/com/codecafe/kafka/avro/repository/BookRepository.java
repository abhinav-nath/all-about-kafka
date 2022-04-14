package com.codecafe.kafka.avro.repository;

import com.codecafe.kafka.avro.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, String> {
}