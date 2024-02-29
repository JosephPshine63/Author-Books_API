package dev.pioruocco.controllers;

import dev.pioruocco.domain.dto.AuthorDto;
import dev.pioruocco.domain.entity.AuthorEntity;
import dev.pioruocco.mappers.Mapper;
import dev.pioruocco.service.AuthorService;
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
public class AuthorController {


    private final AuthorService authorService;
    private final Mapper<AuthorEntity, AuthorDto> authorMapper;

    public AuthorController(AuthorService authorService, Mapper<AuthorEntity, AuthorDto> authorMapper) {
        this.authorService = authorService;
        this.authorMapper= authorMapper;
    }


    //creazione di una EnityAuthor
    /*
    questo metodo permette di creare un Autore nel database con i seguenit passaggi:
        - passa un oggetto DTO nella @RequestBBody
        - creaiamo un AuthorEntity mappando i parametri di AuthorDto grazie a ModelMapper
        - salviamo l'oggetto nel database tramite il metodo save() del AuthorService

    grazie alla ResponseEntity controlliamo anche il corpo della risposta
     */@PostMapping(path = "/authors")
    public ResponseEntity<AuthorDto> createAuthor(@RequestBody AuthorDto author) {
        AuthorEntity authorEntity = authorMapper.mapFrom(author);
        AuthorEntity savedAuthorEntity = authorService.save(authorEntity);

        return new ResponseEntity<>(authorMapper.mapTo(savedAuthorEntity), HttpStatus.CREATED);
    }

    //ricerca di tutte le AuthorEntity nel databse
    /*
    questo endpoint GET restituisce tutti le Entity Author in una lista ma sottoforma di DTO.

    creiamo una lista AuthorEntity che col metodo findAllPage() dell'AuthorService restituirà tutti gli autori,
    questa lista verrà poi resa una Stream e mappata da AuthorEntity a AuthorDto così e poi convertita in
    una Lista da poterla mostrare all'utente
     */
    @GetMapping(path = "/authors")
    public List<AuthorDto> listAuthors(){
        List<AuthorEntity> authorEntityList = authorService.findAll();
        return authorEntityList.stream()
                .map((authorMapper::mapTo)).collect(Collectors.toList());
    }

    //ricerca di un singolo AuthorEntity tramite id
    /*
    questo endpoint GET ci permette di ritrovare un singolo autore tramite il suo ID

    Usiamo l'Optional<> per gestire i due possibili scenari:
        1. l'autore è stato effettivamente ritrovato viene evocato trammite il metodo map
           e viene poi convertito da un Entity a un oggetto DTO
        2. l'Optional è vuoto e allora ritorniamo status 404
     */
    @GetMapping(path = "/authors/{id}")
    public  ResponseEntity<AuthorDto> getAuthor(@PathVariable("id") Integer id){
        Optional<AuthorEntity> foundAuthor = authorService.findOneAuthor(id);

        return foundAuthor.map(authorEntity -> {
            AuthorDto authorDto = authorMapper.mapTo(authorEntity);
            return new ResponseEntity<>(authorDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    //update di tutti i parametri di un author
    /*
    per questo endpoint fullupdateAuthor() controlliamo se l'autore che vogliamo aggiornare esista tramite
    il suo di con il metodo doesExist(), in caso non esista ritorniamo un codice 404
    se esiste settiamo l'id del DTO con l'id passato nella firma del metodo, lo convertiamo e lo salviamo nel DB
     */
    @PutMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDto> fullUpdateAuthor(@PathVariable("id") Integer id, @RequestBody AuthorDto authorDto){
        if(!authorService.doesExist(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        authorDto.setId(id);
        AuthorEntity authorEntity = authorMapper.mapFrom(authorDto);
        AuthorEntity savedAuthorEntity = authorService.save(authorEntity);

        return new ResponseEntity<>(authorMapper.mapTo(savedAuthorEntity), HttpStatus.OK);
    }

    /*

     */
    //metodo per l'update parziale di un Author
    @PatchMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDto> partialUpdateAuthor(@PathVariable("id") Integer id, @RequestBody AuthorDto authorDto){
        if(!authorService.doesExist(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        AuthorEntity authorEntity = authorMapper.mapFrom(authorDto);
        AuthorEntity updatedEntity = authorService.partialUpdate(id, authorEntity);

        return new ResponseEntity<>(authorMapper.mapTo(updatedEntity), HttpStatus.OK);
    }

    /*
    eliminiamo l'autore e ritorniamo un codice di risposta NO_CONTENT
     */
    //metodo per l'eliminazione di un AuthorEntity
    @DeleteMapping(path = "/authors/{id}")
    public ResponseEntity deleteAuthor(@PathVariable("id") Integer id){
        authorService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
