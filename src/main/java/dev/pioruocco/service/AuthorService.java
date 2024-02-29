package dev.pioruocco.service;

import dev.pioruocco.domain.entity.AuthorEntity;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    AuthorEntity save(AuthorEntity authorEntity);

    List<AuthorEntity> findAll();

    Optional<AuthorEntity> findOneAuthor(Integer id);

    boolean doesExist(Integer id);

    AuthorEntity partialUpdate(Integer id, AuthorEntity authorEntity);

    void delete(Integer id);
}
