package dev.pioruocco.repository;

import dev.pioruocco.domain.entity.AuthorEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/*
Questa interfaccia verrà usata per sfruttare i metodi già scritti di CrudRepository<> in modo da facilitarci la creazione
dei metodi di cui abbiamo bisogno per le operazioni col database
 */
@Repository
public interface AuthorRepository extends CrudRepository<AuthorEntity, Integer> {
}
