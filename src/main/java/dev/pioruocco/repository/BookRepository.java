package dev.pioruocco.repository;

import dev.pioruocco.domain.entity.BookEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


/*
Questa interfaccia verrà usata per sfruttare i metodi già scritti di CrudRepository<> in modo da facilitarci la creazione
dei metodi di cui abbiamo bisogno per le operazioni col database
 */
/*
AGGIORNAMENTO PER REINGENERING
la repository è stata estesa con la  PagingAndSortingRepository<> per poter accedere al sorting e al Paging
ovvero durante un 'operazione di Read ritorniamo tutte le entità nel database ma a pezzi, ad esemepio
una pagina da 5, un altra pagina da 5 e via dicendo
 */
@Repository
public interface BookRepository extends
        CrudRepository<BookEntity, String>,
        PagingAndSortingRepository<BookEntity, String>
{
}
