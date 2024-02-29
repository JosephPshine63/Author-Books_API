package dev.pioruocco;

import dev.pioruocco.domain.dto.AuthorDto;
import dev.pioruocco.domain.dto.BookDto;
import dev.pioruocco.domain.entity.AuthorEntity;
import dev.pioruocco.domain.entity.BookEntity;

/*
questa classe test simula i comportamenti della creazione di reference ed oggetti sia Entità che Dto Book e Author
 */
public final class TestDataUtil {
    private TestDataUtil(){
    }

    //primo AuthorEntity
    public static AuthorEntity createTestAuthorEntityA() {
        return AuthorEntity.builder()
                .id(1)
                .name("George Orwell")
                .age(100)
                .description("George Orwell, pseudonimo di Eric Arthur Blair (Motihari, 25 giugno 1903 – Londra, 21" +
                        " gennaio 1950), è stato uno scrittore, giornalista, saggista, attivista e critico letterario" +
                        " britannico.")
                .build();
    }

    //primo AuthorDto
    public static AuthorDto createTestAuthorDtoA() {
        return AuthorDto.builder()
                .id(1)
                .name("George Orwell")
                .age(100)
                .description(
                        "George Orwell, pseudonimo di Eric Arthur Blair (Motihari, 25 giugno 1903 – Londra, 21 gennaio " +
                                "1950), è stato uno scrittore, giornalista, saggista, attivista e critico letterario" +
                                " britannico."
                )
                .build();
    }

    public static AuthorEntity createTestAuthorB() {
        return AuthorEntity.builder()
                .id(2)
                .name("Thomas Cronin")
                .age(44)
                .description("I SUOI LIBRI SONO FANTASTICI")
                .build();
    }

    public static AuthorEntity createTestAuthorC() {
        return AuthorEntity.builder()
                .id(3)
                .name("Jesse A Casey")
                .age(24)
                .build();
    }

    public static BookEntity createTestBookEntityA(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("0124-8964-7552")
                .title("Lettera al padre")
                .authorEntity(authorEntity)
                .build();
    }

    public static BookDto createTestBookDtoA(final AuthorDto authorDto) {
        return BookDto.builder()
                .isbn("0124-8964-7552")
                .title("lettera al padre")
                .author(authorDto)
                .build();
    }

    public static BookDto createTestBookDtoB(final AuthorDto authorDto) {
        return BookDto.builder()
                .isbn("1212-4545-7878")
                .title("Testing in Java")
                .author(authorDto)
                .build();
    }


    public static BookEntity createTestBookB(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("978-1-2345-6789-1")
                .title("Beyond the Horizon")
                .authorEntity(authorEntity)
                .build();
    }

    public static BookEntity createTestBookC(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("978-1-2345-6789-2")
                .title("The Last Ember")
                .authorEntity(authorEntity)
                .build();
    }

    public static BookEntity createTestBookEntityB(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("1213-9528-7924")
                .title("BOOK TESTING")
                .authorEntity(authorEntity)
                .build();
    }
}
