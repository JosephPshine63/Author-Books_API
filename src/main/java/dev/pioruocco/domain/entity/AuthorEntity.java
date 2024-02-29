package dev.pioruocco.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
questa classe rappresenta l'entità Author del database mySQL
 */

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "authors")
public class AuthorEntity {

    //Dichiarato il tipo come classe wrapper Integer in modo da poter accedere ai metodi di conversione
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//prima la generationType era sequence, testing senza tabella di join
    private Integer id;
    private String name;
    private Integer age;
    private String description;

}
