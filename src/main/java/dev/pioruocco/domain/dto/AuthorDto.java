package dev.pioruocco.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
questa classe è fatta per fare in modo che nel modello Presentation non vengano mostrati direttamente
i dati delle Entità, che potrebbe essere pericoloso siccome si rischia di mostrare la logica dell'
applicazione all'utente
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDto {

    private Integer id;
    private String name;
    private int age;
    private String description;

}
