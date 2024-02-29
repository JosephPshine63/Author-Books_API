package dev.pioruocco.service.impl;

import dev.pioruocco.domain.entity.BookEntity;
import dev.pioruocco.repository.BookRepository;
import dev.pioruocco.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/*
implementazione dell'interfaccia BookService e usando il polimorfismo per l'informatin hiding
 */
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /*
    questo metodo permette la creazione di un oggetto BookEntity passandogli la chiave primaria e l'oggetto stesso
    controlliamo anche che l'isbn assegbato sia quello passato come argomento
     */
    /*VERSIONE PRECEDENTE
    @Override
    public BookEntity createBook(String isbn, BookEntity bookEntity) {
        bookEntity.setIsbn(isbn);//settiamo come isbn il valore passato così da evitare problemi
        return bookRepository.save(bookEntity);
    }
     */

    @Override
    public BookEntity saveBook(String isbn, BookEntity bookEntity) {
        bookEntity.setIsbn(isbn);//settiamo come isbn il valore passato così da evitare problemi
        return bookRepository.save(bookEntity);
    }

    /*
    this code fetches all book data from the database using the repository, converts it into a stream
    for potential processing, and finally returns a list of all book entities.

    usando la stream all'utente mostreremo solo la lista finale già realizzata, evitando di occupare in antticipi aree di  memoria per nulla
     */
    @Override
    public List<BookEntity> findAllPage() {
        return StreamSupport
                .stream(
                        bookRepository.findAll().spliterator(),
                        false
                ).collect(Collectors.toList());
    }

    @Override
    public Optional<BookEntity> findSingleBook(String isbn) {
        return bookRepository.findById(isbn);
    }

    //controllo per verificare se l'entity Book esista
    @Override
    public boolean doesExist(String isbn) {
        return bookRepository.existsById(isbn);
    }

    /*
    questo metodo di business ci permette di aggiornare parzialmente un Entity Book
    settiamo la chiave primaria dell'entità a quella passataci, controlliamo se esista e in caso
    positivo con una variabile locale controllando con Optional.ofNullable() se il cmapo possa esere vuoto.
    in caso sia vuoto allora verrà ignorato, se presente settiamo existingBook con i parametri del AuthorEntity
    e lo salviamo.
    in caso di risposta negativa diciamo che non esiste
     */
    //metodo per l'aggiornamento parziale di un Entity book
    @Override
    public BookEntity partialUpdate(String isbn, BookEntity bookEntity) {
        bookEntity.setIsbn(isbn);

        return bookRepository.findById(isbn).map(existingbook->{

            Optional.ofNullable(bookEntity.getTitle()).ifPresent(existingbook::setTitle);
            Optional.ofNullable(bookEntity.getAuthorEntity()).ifPresent(existingbook::setAuthorEntity);
            return bookRepository.save(existingbook);

        }).orElseThrow(()-> new RuntimeException("Book does not exist"));
    }

    //metodo per eliminazione di una BookEntity

    @Override
    public void delete(String isbn) {
        bookRepository.deleteById(isbn);
    }
}
