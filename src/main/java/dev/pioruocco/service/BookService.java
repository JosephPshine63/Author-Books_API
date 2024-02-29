package dev.pioruocco.service;

import dev.pioruocco.domain.entity.BookEntity;

import java.util.List;
import java.util.Optional;

public interface BookService {

    //firma createBook
    BookEntity saveBook(String isbn, BookEntity bookEntity);

    //firma metodo restituizione dei libri
    List<BookEntity> findAllPage();

    Optional<BookEntity> findSingleBook(String isbn);

    boolean doesExist(String isbn);

    BookEntity partialUpdate(String isbn, BookEntity bookEntity);

    void delete(String isbn);
}
