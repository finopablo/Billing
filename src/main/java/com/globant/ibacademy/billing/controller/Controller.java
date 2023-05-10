package com.globant.ibacademy.billing.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.function.Function;

public abstract class Controller<T> {
    Function<List<T>, ResponseEntity<List<T>>> response = (list) -> ( list.isEmpty()  ? ResponseEntity.noContent().build(): ResponseEntity.ok(list));
    Function<Integer, URI> location = (id) -> ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
}
