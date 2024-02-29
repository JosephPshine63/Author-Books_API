package dev.pioruocco.service.impl;

import dev.pioruocco.domain.entity.AuthorEntity;
import dev.pioruocco.repository.AuthorRepository;
import dev.pioruocco.service.AuthorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/*
implementazione dell'interfaccia AuthorService e usando il polimorfismo per l'informatin hiding
 */
@Service
public class AuthorServiceImpl implements AuthorService {

    //richiamniamo la reference per usare i metodi
    private final AuthorRepository authorRepository;

    //ignezione di dipendenza dal costruttore
    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    //metodo per creare e aggiornare un Oggetto di tipo AuthorEntity
    @Override
    public AuthorEntity save(AuthorEntity authorEntity) {
        return authorRepository.save(authorEntity);
    }


    //metodo che ritorna tutte le Entity di tipo Author
    /*
    this code fetches all author data from the database using the repository, converts it into a stream
    for potential processing, and finally returns a list of all author entities.
     */
    @Override
    public List<AuthorEntity> findAll() {
        return StreamSupport.stream(
                authorRepository.findAll().spliterator(), false
        ).collect(Collectors.toList());
    }

    //metodo per il ritorno di un singolo autore
    /*
    passiamo l'optional per vedere se un valore sia presente oppure no e sfruttiamo la repository
    con il metodo built-in findById()
     */
    @Override
    public Optional<AuthorEntity> findOneAuthor(Integer id) {
        return authorRepository.findById(id);
    }

    //controllo se l'autore esista o meno
    @Override
    public boolean doesExist(Integer id) {
        return authorRepository.existsById(id);
    }

    /*
    questo metodo per l'aggiornamento parziale di un AuthorEntity setta l'id della reference passata
    all'id di quello passato nella firma del metodo,lo cerchiamo col metodo built-in findById():

        1. nel primo scenario dove l'autore non dovrebbe essere ritornato lanceremo una exception dove
        dire cheesso non esiste
        2. nel secondo scenario in cui l'autore esista facciamo in modo che vengano aggiornati solo i
        campi non passati a null tramite ifPresent() e Optional.ofNullable() per proprietà che potrebbero
        essere vuote, in questo caso non le considera
     */
    //aggiornamento parziale di un autore
    @Override
    public AuthorEntity partialUpdate(Integer id, AuthorEntity authorEntity) {
        authorEntity.setId(id);

        return authorRepository.findById(id).map(existingAuthor ->{

            Optional.ofNullable(authorEntity.getName()).ifPresent(existingAuthor::setName);
            Optional.ofNullable(authorEntity.getAge()).ifPresent((existingAuthor::setAge));
            Optional.ofNullable(authorEntity.getDescription()).ifPresent(existingAuthor::setDescription);

            return authorRepository.save(existingAuthor);

        }).orElseThrow(()-> new RuntimeException("Author does not exist"));
    }

    //metodo per l'eliminazione di un entità

    @Override
    public void delete(Integer id) {
        authorRepository.deleteById(id);
    }
}
