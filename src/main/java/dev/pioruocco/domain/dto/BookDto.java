package dev.pioruocco.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
/*
questa classe è fatta per fare in modo che nel modello Presentation non vengano mostrati direttamente
i dati delle Entità Book, che potrebbe essere pericoloso siccome si rischia di mostrare la logica dell'
applicazione all'utente
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto {

    private String isbn;
    private String title;
    private AuthorDto author;

}
