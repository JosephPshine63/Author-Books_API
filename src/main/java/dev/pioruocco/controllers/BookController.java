package dev.pioruocco.controllers;

import dev.pioruocco.domain.dto.BookDto;
import dev.pioruocco.domain.entity.BookEntity;
import dev.pioruocco.mappers.Mapper;
import dev.pioruocco.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
classe che presenta i dati al presentation layer
 */
@RestController
public class BookController {

    private final Mapper<BookEntity, BookDto> bookMapper;
    private final BookService bookService;

    public BookController(Mapper<BookEntity, BookDto> bookMapper, BookService bookService) {
        this.bookMapper = bookMapper;
        this.bookService = bookService;
    }


    /*
    questa richiesta PUT crea nel database un entità di tipo Book passando l'oggetto BookDto e l'isbn(la chiave primaria)
    mappiamo da BookDto a BookEntity e poi restituiamo l'oggetto Dto con risposta 201(CRTEATED)
     */
   /*VERSIONE PRECEDENTE CHE PERMETTEVA SOLO LA CREAZIONE

    @PutMapping("/books/{isbn}")
    public ResponseEntity<BookDto> createBook(@PathVariable("isbn") String isbn, @RequestBody BookDto bookDto){
        BookEntity bookEntity = bookMapper.mapFrom(bookDto);
        BookEntity saveBookEntity = bookService.createBook(isbn, bookEntity);
        BookDto savedBookDto = bookMapper.mapTo(saveBookEntity);

        return new ResponseEntity<>(savedBookDto, HttpStatus.CREATED);
    }
    */
    //metodo per creazione e aggiornamento di EntityBook
    @PutMapping("/books/{isbn}")
    public ResponseEntity<BookDto> saveBook(@PathVariable("isbn") String isbn, @RequestBody BookDto bookDto){
        BookEntity bookEntity = bookMapper.mapFrom(bookDto);
        boolean doesBookExist = bookService.doesExist(isbn);

        BookEntity savedBookEntity = bookService.saveBook(isbn, bookEntity);
        BookDto savedOrUpdatedBookDto = bookMapper.mapTo(savedBookEntity);

        if(doesBookExist)
            return new ResponseEntity<>(savedOrUpdatedBookDto, HttpStatus.OK);
        else
            return new ResponseEntity<>(savedOrUpdatedBookDto, HttpStatus.CREATED);
    }

    /*
    questo endpoint GET restituisce tutti le Entity Author in una lista ma sottoforma di DTO.

    creiamo una lista BoookEntity che col metodo findAllPage() dell'BookService restituirà tutti gli autori,
    questa lista verrà poi resa una Stream e mappata da BookEntity a BookDto così e poi convertita in
    una Lista da poterla mostrare all'utente
     */
    //endpoint che ritorna tutti gli autori
    @GetMapping(path = "/books")
    public List<BookDto> listBooks(){
        List<BookEntity> bookEntityList = bookService.findAllPage();
        return bookEntityList.stream()
                .map(bookMapper::mapTo)
                .collect(Collectors.toList());
    }

    //ritrovo di un singolo libro
    @GetMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> getBook(@PathVariable("isbn") String isbn){
        Optional<BookEntity> foundBook = bookService.findSingleBook(isbn);

        return foundBook.map(bookEntity -> {
            BookDto bookDto = bookMapper.mapTo(bookEntity);
            return new ResponseEntity<>(bookDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> partialUpdateBook(@PathVariable("isbn") String isbn, @RequestBody BookDto bookDto){
        if(!bookService.doesExist(isbn)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        BookEntity bookEntity = bookMapper.mapFrom(bookDto);
        BookEntity updatedBookEntity = bookService.partialUpdate(isbn, bookEntity);

        return new ResponseEntity<>(bookMapper.mapTo(updatedBookEntity), HttpStatus.OK);
    }

    @DeleteMapping(path = "/books/{isbn}")
    public ResponseEntity deleBook(@PathVariable("isbn") String isbn){
        bookService.delete(isbn);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
