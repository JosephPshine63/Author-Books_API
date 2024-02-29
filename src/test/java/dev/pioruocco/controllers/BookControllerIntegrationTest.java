package dev.pioruocco.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.pioruocco.TestDataUtil;
import dev.pioruocco.domain.dto.BookDto;
import dev.pioruocco.domain.entity.BookEntity;
import dev.pioruocco.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BookControllerIntegrationTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final BookService bookService;

    //DI con il costruttore
    @Autowired
    public BookControllerIntegrationTest(MockMvc mockMvc, BookService bookService) {
        this.mockMvc = mockMvc;
        this.bookService = bookService;
        this.objectMapper = new ObjectMapper();
    }

    /*
    con questo metodo controlliamo che l'entity Book creata ritorni codice 201(CREATED) e lo
    passiamo poi in formato JSON con l'object mapper, con il mockMVC costruiamo la richiesta PUT
    passando la chiave primaria e aspettandoci il corpo della risposta di tipo JSON e content che gli
    abbiamo passato faceno il parsing
     */
    @Test
    public void testThatCreateBookReturnsHttpStatus201Created() throws Exception {
        BookDto testBookA = TestDataUtil.createTestBookDtoA(null);
        String bookJson = objectMapper.writeValueAsString(testBookA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/0124-8964-7552")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );

        System.out.println("_____________________testThatCreateBookReturnsHttpStatus201Created()____________________________________");
        System.out.println("IL TEST E' ANDATO A BUON FINE");
        System.out.println("--------------------------------------------------------------------------------------------");
    }

    /*
    con questo metodo testiamo che l'Entity Book nel database sia effettivamente quella creata
    (STAVOLTA PASSIAMO DATI MOCK)
    creiamo l'Entity Book con dati mock, simuliamo la richiesta PUT col mockmvc aspettandoci corpo
    della risposta JSON e content il JSON parsato dall'entità e controlliamo il contenuto con il ResultMatchers
     */
    @Test
    public void testThatCreateBookReturnsCreatedBook() throws Exception {
        BookDto testBookB = TestDataUtil.createTestBookDtoB(null);
        String bookJson = objectMapper.writeValueAsString(testBookB);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/1212-4545-7878")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value("1212-4545-7878")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value("Testing in Java")
        );

        System.out.println("_____________________testThatCreateBookReturnsCreatedBook()____________________________________");
        System.out.println("IL TEST E' ANDATO A BUON FINE");
        System.out.println("--------------------------------------------------------------------------------------------");
    }

    /*
    this test case verifies that the listBooks() method returns a status code of 200 (OK)
     when a GET request is made to the /books endpoint.

     usiamo mockMvc per simulare la richiesta GET aspettandoci il corpo della risposta
     di essere di tipo JSON e verifichiamo che lo staus sia OK(200)
     */
    @Test
    public void testThatListBooksReturnsHttpStatus200Ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
        System.out.println("_____________________testThatListBooksReturnsHttpStatus200Ok()____________________________________");
        System.out.println("IL TEST E' ANDATO A BUON FINE");
        System.out.println("--------------------------------------------------------------------------------------------");
    }

    /*
    con questo metodo verifichiamo che effettivamente l'endpoint listBooks() ritorni effettivamente una lista
    creando un oggetto mock e salvandolo, poi con la reference di mockMvc costruiamo la richiesta GET aspettandoci
    il corpo della richiesta sia JSON, e verifichiamo che il contenuto della LISTA (in questo caso il primo
    BookEntity (con offset 0) abbia l'isbn e il titolo come i valori che ci aspettiamo
    quindi controlliamo AuthorEntityTest con il primo AuthorEntity della lista
    */
    @Test
    public void testThatListBooksReturnsBook() throws Exception {
        BookEntity testBookEntityA = TestDataUtil.createTestBookEntityA(null);
        bookService.saveBook(testBookEntityA.getIsbn(), testBookEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].isbn").value("0124-8964-7552")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].title").value("lettera al padre")
        );

        System.out.println("_____________________testThatListBooksReturnsBook()____________________________________");
        System.out.println("IL TEST E' ANDATO A BUON FINE");
        System.out.println("--------------------------------------------------------------------------------------------");

    }

    /*
    controlliamo che l'endpoint getBook() ci restituisca codice 200(OK) quando ritornato
    passandogli dati di mock e controllando il corpo cdella richiesta aspettandoci un json e
    passandogli direttamente l'isbn dell'oggetto rispetto che glielo passiamo noi
     */
    @Test
    public void testThatGetBookReturnsHttpStatus200OkWhenBookExists() throws Exception {
        BookEntity testBookEntityA = TestDataUtil.createTestBookEntityA(null);
        bookService.saveBook(testBookEntityA.getIsbn(), testBookEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/" + testBookEntityA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );

        System.out.println("IL TEST E' ANDATO A BUON FINE");
        System.out.println("--------------------------------------------------------------------------------------------");
    }

    /*
    con questo metodo di testing ci aspettiamo che ci venga ritornato status code 404 in
    caso di Entity non trovato, sempre costruendo la richiesta col mockMVC e passandogli la chiave primaria
    in questo caso per metodo di testing gli passiamo un isbn diverso

    QUESTO METODO E' INTESO A FALLIRE SICCOME GLI PASSIAMO L'ISBN GIUSTO, SE LO ALTERIAMO NON VA!
     */
    @Test
    public void testThatGetBookReturnsHttpStatus404WhenBookDoesntExist() throws Exception {
        BookEntity testBookEntityA = TestDataUtil.createTestBookEntityA(null);
        BookEntity testBookEntityB = TestDataUtil.createTestBookEntityB(null);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/" + testBookEntityA.getIsbn()+"85430")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );

        System.out.println("IL TEST E' ANDATO A BUON FINE");
        System.out.println("--------------------------------------------------------------------------------------------");
    }

    /*
    con questo metodo controlliamo se un AuthorEntity test che aggiorniamo
    venga effettivamente ritornato con i suoi parametri

    creiamo il corrispettivo DTO e gli cambiamo il titolo e gli assegnamo l'id del BookEntity di prima e
    come fatto altre volte costruiamo la richiesta PUT aspettandoci un contenuto di tipo JSON convertendo
    il DTO in un tipo String e controllando che il contenutp del json sia quello aspettatoci

     */
    @Test
    public void testThatUpdateBookReturnsUpdatedBook() throws Exception {
        BookEntity testBookEntityA = TestDataUtil.createTestBookEntityA(null);
        BookEntity savedBookEntity = bookService.saveBook(
                testBookEntityA.getIsbn(), testBookEntityA
        );

        BookDto testBookB = TestDataUtil.createTestBookDtoB(null);
        testBookB.setIsbn(savedBookEntity.getIsbn());
        testBookB.setTitle("UPDATED");
        String bookJson = objectMapper.writeValueAsString(testBookB);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + savedBookEntity.getIsbn() )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value("978-1-2345-6789-0")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value("UPDATED")
        );

        System.out.println("IL TEST E' ANDATO A BUON FINE");
        System.out.println("--------------------------------------------------------------------------------------------");

    }

    /*
    con questo metodo controlliamo che il codice di risposta HTTP sia 200 quando aggiorniamo il book
    creiamo un BookEntity mock e poi lo salviamo nel database con il Service, creiamo il corrispettivo DTO
    settiamo(AGGIORNIAMO) il nome e poi in una variabile String che useremo come json lo convertiamo,
    costruiasmo la richiesta PATCH e controlliamo che il contenuto della risposta JSON sia equivalente al nostro
     */
    @Test
    public void testThatPartialUpdateBookReturnsHttpStatus200Ok() throws Exception {
        BookEntity testBookEntityA = TestDataUtil.createTestBookEntityA(null);
        bookService.saveBook(testBookEntityA.getIsbn(), testBookEntityA);

        BookDto testBookA = TestDataUtil.createTestBookDtoA(null);
        testBookA.setTitle("UPDATED");
        String bookJson = objectMapper.writeValueAsString(testBookA);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/" + testBookEntityA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );

        System.out.println("IL TEST E' ANDATO A BUON FINE");
        System.out.println("--------------------------------------------------------------------------------------------");

    }

    /*
    controlliamo che ci venga restituito il book aggiornato, creiamo l'BookEntity mock, lo salviamo,
    creiamo il DTO e come nel metodo precedente lo convertiamo in una variabile String che useremo come json
    e controlliamo che i valori passati siano quelli aspettatoci
     */
    @Test
    public void testThatPartialUpdateBookReturnsUpdatedBook() throws Exception {
        BookEntity testBookEntityA = TestDataUtil.createTestBookEntityA(null);
        bookService.saveBook(testBookEntityA.getIsbn(), testBookEntityA);

        BookDto testBookA = TestDataUtil.createTestBookDtoA(null);
        testBookA.setTitle("UPDATED");
        String bookJson = objectMapper.writeValueAsString(testBookA);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/" + testBookEntityA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(testBookEntityA.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value("UPDATED")
        );

        System.out.println("IL TEST E' ANDATO A BUON FINE");
        System.out.println("--------------------------------------------------------------------------------------------");

    }

    /*
    Simulates a DELETE request: It sends a DELETE request to /books/kjsbdfjdfsk, using a likely
    non-existent ISBN value as the ID.
    Asserts expected status code: It expects the response status code to be 204 (No Content),
    indicating that the deletion was successful even though the book didn't exist. This ensures
    the endpoint handles non-existent resources gracefully.
     */
    @Test
    public void testThatDeleteNonExistingBookReturnsHttpStatus204NoContent() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/kjsbdfjdfsk")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());

        System.out.println("IL TEST E' ANDATO A BUON FINE");
        System.out.println("--------------------------------------------------------------------------------------------");

    }

    /*
    It creates a test book entity using TestDataUtil.createTestBookEntityA(null).
    It saves the book using bookService.createUpdateBook.
    Simulates DELETE request: It sends a DELETE request to /books/{isbn}, where {isbn} is replaced
    with the saved book's ISBN.
    Asserts expected status code: It expects the response status code to be 204 (No Content) again,
    indicating successful deletion of the existing book.
     */
    @Test
    public void testThatDeleteExistingBookReturnsHttpStatus204NoContent() throws Exception {
        BookEntity testBookEntityA = TestDataUtil.createTestBookEntityA(null);
        bookService.saveBook(testBookEntityA.getIsbn(), testBookEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/" + testBookEntityA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());

        System.out.println("IL TEST E' ANDATO A BUON FINE");
        System.out.println("--------------------------------------------------------------------------------------------");

    }
}
