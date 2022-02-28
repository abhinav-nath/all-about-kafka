package com.codecafe.kafka.persistence.dao;

import org.springframework.data.repository.CrudRepository;

import com.codecafe.kafka.persistence.entity.LibraryEvent;

public interface LibraryEventsRepository extends CrudRepository<LibraryEvent, Integer> {

}