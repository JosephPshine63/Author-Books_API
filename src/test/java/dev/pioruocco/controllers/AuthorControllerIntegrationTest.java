package dev.pioruocco.controllers;

/*
questa classe verrà usata per simulare il comportamento dell'applicazione
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.pioruocco.TestDataUtil;
import dev.pioruocco.domain.dto.AuthorDto;
import dev.pioruocco.domain.entity.AuthorEntity;
import dev.pioruocco.service.AuthorService;
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

/*
@SpringbootTest: Avvia automaticamente un'applicazione Spring Boot per eseguire il test.
@ExtendWith(SpringExtension.class): L'estensione Spring fornisce varie funzionalità utili per i test Spring, come l'iniezione di dipendenze.
@DirtiesContext: Indica a Spring che il test modifica lo stato dell'applicazione, quindi Spring deve ripristinare il contesto dell'applicazione prima di eseguire il test successivo.
@AutoConfigureMockMvc: Questa annotazione configura automaticamente un oggetto MockMvc per poter testare i controller web della tua applicazione Spring Boot.
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AuthorControllerIntegrationTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final AuthorService authorService;

    @Autowired
    public AuthorControllerIntegrationTest(MockMvc mockMvc, ObjectMapper objectMapper, AuthorService authorService) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.authorService=authorService;
    }

    /*
    questo metodo controlla se il codice di risposta durante la creazione di un autore sia 201(CREATED)

    passiamo l'oggetto AuthorEntity e settiamo ID a null sapendo che viene autogenerato, poi con l'objectMapper
    facciamo in modo che il contenuto dell'oggetto AuthorEntity venga scritto come valore String in authorJson

    poi con mockMvc(reference che simula una richiesta http) e il metodo perfom avvia, creaiamo una richiesta di tipo POST
    passando il dominio ("/authors") e aspettandoci il corpo della rochiesta di tipo JSON e impostiamo il contenuto di tale
    richiesta tramite authorJson.
    con .andExcept() controlliamo un valore che ci sia passato, in questo caso codice 201(CREATED)
     */
    @Test
    public void testThatCreateAuthorSuccessfullyReturnsHttp201Created() throws Exception {
        AuthorEntity testAuthorA = TestDataUtil.createTestAuthorB();
        testAuthorA.setId(null);
        String authorJson = objectMapper.writeValueAsString(testAuthorA);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
        System.out.println("____________________________________________________________________________________________");
        System.out.println("IL TEST E' ANDATO A BUON FINE");
        System.out.println("--------------------------------------------------------------------------------------------");
    }

    /*
    Il codice simula una richiesta POST per creare un nuovo autore e verifica che la risposta JSON contenga i dati dell'autore creato,
    con i valori specificati per i campi id, name e age. Questo test assicura che l'endpoint /authors funzioni correttamente per la
    creazione di autori.

    Controllliamo il valore dell'oggetto JSON tramite il .jsonPath() dove vi passiamo il campo e isNumber() o value per verificare il valore
     */
    @Test
    public void testThatCreateAuthorSuccessfullyReturnsSavedAuthor() throws Exception {
        AuthorDto testAuthorA = TestDataUtil.createTestAuthorDtoA();
        testAuthorA.setId(null);
        String authorJson = objectMapper.writeValueAsString(testAuthorA);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Abigail Rose")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(80)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.description").value("GRANDISSIMA SCRITTRICE!!!")
        );

        System.out.println("____________________________________________________________________________________________");
        System.out.println("IL TEST E' ANDATO A BUON FINE");
        System.out.println("--------------------------------------------------------------------------------------------");
    }

    /*
    this test case verifies that the listAuthors method returns a status code of 200 (OK)
     when a GET request is made to the /authors endpoint.

     usiamo mockMvc per simulare la richiesta GET aspettandoci il corpo della risposta
     di essere di tipo JSON e verifichiamo che lo staus sia OK(200)
     */
    @Test
    public void testThatListAuthorsReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
        System.out.println("__________testThatListAuthorsReturnsHttpStatus200()______________________________");
        System.out.println("IL TEST E' ANDATO A BUON FINE");
        System.out.println("--------------------------------------------------------------------------------------------");
    }

    /*
    con questo metodo verifichiamo che effettivamente l'endpoint listAuthors() ritorni effettivamente una lista
    creando un oggetto mock e salvandolo, poi con la reference di mockMvc costruiamo la richiesta GET aspettandoci
    il corpo della richiesta sia JSON, e verifichiamo che il contenuto della LISTA (in questo caso il primo
    AuthorEntity (con offset 0) abbia l'id come valore numerico, e i valori di age, description e name
    corrispondano a quelli passati
    quindi controlliamo AuthorEntityTest con il primo AuthorEntity della lista
    */
    @Test
    public void testThatListAuthorsReturnsListOfAuthors() throws Exception {
        AuthorEntity testAuthorEntityA = TestDataUtil.createTestAuthorEntityA();
        authorService.save(testAuthorEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value("george orwell")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].age").value(98)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].description").value("kjfvuifufiuuiufufuif")
        );

        System.out.println("_____________________testThatListAuthorsReturnsListOfAuthors()____________________________________");
        System.out.println("IL TEST E' ANDATO A BUON FINE");
        System.out.println("--------------------------------------------------------------------------------------------");
    }

    /*
    creiamo un entity test e col mockMvc costruiamo la richiesta GET ASPETTANDOCI un json e verificando
    che lo status code sia 200
     */
    @Test
    public void testThatGetAuthorReturnsHttpStatus200WhenAuthorExist() throws Exception {
        AuthorEntity testAuthorEntityA = TestDataUtil.createTestAuthorEntityA();
        authorService.save(testAuthorEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());

        System.out.println("IL TEST E' ANDATO A BUON FINE");
        System.out.println("--------------------------------------------------------------------------------------------");
    }

    /*
    controlliamo che l'endpoint getAuthor() ritorni effettivamente l'author che cerchiamo con
    corpo della risposta json e controllando i para,etri che ci aspettiamo siano corrispondenti
     */
    @Test
    public void testThatGetAuthorReturnsAuthorWhenAuthorExist() throws Exception {
        AuthorEntity testAuthorEntityA = TestDataUtil.createTestAuthorEntityA();
        authorService.save(testAuthorEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(1)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("George Orwell")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(100)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.description").value("George Orwell, pseudonimo di Eric Arthur Blair (Motihari, 25 giugno 1903 – Londra, 21 gennaio 1950), è stato uno scrittore, giornalista, saggista, attivista e critico letterario britannico.")
        );

        System.out.println("IL TEST E' ANDATO A BUON FINE");
        System.out.println("--------------------------------------------------------------------------------------------");
    }

    /*
    controlliamo che l'Optional<> dell'endpoint getAuthor ritorni 404 quando non vi
     sia effettivamente un autore trovato
     */
    @Test
    public void testThatGetAuthorReturnsHttpStatus404WhenNoAuthorExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/9999999")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());

        System.out.println("IL TEST E' ANDATO A BUON FINE");
        System.out.println("--------------------------------------------------------------------------------------------");
    }

    /*
    facciamo un controllo sull'endpoint PUT fullupdateAuthor che debba ritornare 404 nel
    caso l'autore che vogliamo aggiornare non venga ritrovato o non esista proprio

    creiamo loggetto DTO, lo passiamo a String in un json, e controlliamo il corpo della risposta
    aspettandoci un NotFound
     */
    @Test
    public void testThatFullUpdateAuthorReturnsHttpStatus404WhenNoAuthorExists() throws Exception {
        AuthorDto testAuthorDtoA = TestDataUtil.createTestAuthorDtoA();
        String authorDtoJson = objectMapper.writeValueAsString(testAuthorDtoA);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());

        System.out.println("IL TEST E' ANDATO A BUON FINE");
        System.out.println("--------------------------------------------------------------------------------------------");

    }

    /*
    controlliamo che l'autore aggiornato ci ritorni un codice 200 nel caso esista

    creiamo l'Entity, la salviamo, successivamente creiamo anche il DTO e lo convertiamo a String
    per salvarlo in un JSON, costruiamo la richiesta PUT e controlliamo se il nostro DTO CORRISPONDA
    all'Entity salvata nel database
     */
    @Test
    public void testThatFullUpdateAuthorReturnsHttpStatus200WhenAuthorExists() throws Exception {
        AuthorEntity testAuthorEntityA = TestDataUtil.createTestAuthorEntityA();
        AuthorEntity savedAuthor = authorService.save(testAuthorEntityA);

        AuthorDto testAuthorDtoA = TestDataUtil.createTestAuthorDtoA();
        String authorDtoJson = objectMapper.writeValueAsString(testAuthorDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());

        System.out.println("IL TEST E' ANDATO A BUON FINE");
        System.out.println("--------------------------------------------------------------------------------------------");

    }

    /*
    creiamo una Entity test che verrà salvata nel database con un altra entity, poi creaiamo un
    DTO e settiamo il suo ID a quello dell'entità precedentemente salvata e lo convertiamo in una
    variabile json.
    costruiamo la rochiesta PUT e vi aspettiamo che il DTO ABBIA l'id dell'entità salvata e
    i suoi parametri originali
     */
    @Test
    public void testThatFullUpdateUpdatesExistingAuthor() throws Exception {
        AuthorEntity testAuthorEntityA = TestDataUtil.createTestAuthorEntityA();
        AuthorEntity savedAuthor = authorService.save(testAuthorEntityA);

        AuthorEntity authorDto = TestDataUtil.createTestAuthorB();
        authorDto.setId(savedAuthor.getId());

        String authorDtoUpdateJson = objectMapper.writeValueAsString(authorDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoUpdateJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedAuthor.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(authorDto.getAge())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.description").value(authorDto.getDescription())
        );

        System.out.println("IL TEST E' ANDATO A BUON FINE");
        System.out.println("--------------------------------------------------------------------------------------------");

    }

    /*
    con questo metodo controlliamo che il codice di risposta HTTP sia 200 quando aggiorniamo l'autore
    creiamo un AuthorEntity mock e poi lo salviamo nel database con il Service, creiamo il corrispettivo DTO
    settiamo(AGGIORNIAMO) il nome e poi in una variabile String che useremo come json lo convertiamo,
    costruiasmo la richiesta PATCH e controlliamo che il contenuto della risposta JSON sia equivalente al nostro
    json e il codice sia 200(OK)
     */
    @Test
    public void testThatPartialUpdateExistingAuthorReturnsHttpStatus20Ok() throws Exception {
        AuthorEntity testAuthorEntityA = TestDataUtil.createTestAuthorEntityA();
        AuthorEntity savedAuthor = authorService.save(testAuthorEntityA);

        AuthorDto testAuthorDtoA = TestDataUtil.createTestAuthorDtoA();
        testAuthorDtoA.setName("UPDATED");
        String authorDtoJson = objectMapper.writeValueAsString(testAuthorDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());

        System.out.println("IL TEST E' ANDATO A BUON FINE");
        System.out.println("--------------------------------------------------------------------------------------------");

    }

    /*
    controlliamo che ci venga restituito l'autore aggiornato, creiamo l'AuthorEntity mock, lo salviamo,
    creiamo il DTO e come nel metodo precedente lo convertismo in una variabile String che useremo come json
    e controlliamo che i valori passati siano quelli aspettatoci
     */
    @Test
    public void testThatPartialUpdateExistingAuthorReturnsUpdatedAuthor() throws Exception {
        AuthorEntity testAuthorEntityA = TestDataUtil.createTestAuthorEntityA();
        AuthorEntity savedAuthor = authorService.save(testAuthorEntityA);

        AuthorDto testAuthorDtoA = TestDataUtil.createTestAuthorDtoA();
        testAuthorDtoA.setName("UPDATED");
        String authorDtoJson = objectMapper.writeValueAsString(testAuthorDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedAuthor.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("UPDATED")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(testAuthorDtoA.getAge())
        );

        System.out.println("IL TEST E' ANDATO A BUON FINE");
        System.out.println("--------------------------------------------------------------------------------------------");

    }

    /*
    This test simulates a DELETE request to /authors/999.
    It asserts that the expected status code is 204 (No Content). This is appropriate because the endpoint
    doesn't return any content in the response, even if the author doesn't exist.
    This test case verifies if the controller handles non-existent resources gracefully by returning the
    correct status code and not throwing an error.
     */
    @Test
    public void testThatDeleteAuthorReturnsHttpStatus204ForNonExistingAuthor() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/999")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());

        System.out.println("IL TEST E' ANDATO A BUON FINE");
        System.out.println("--------------------------------------------------------------------------------------------");

    }

    /*
    This test sets up a scenario with an existing author:
    It creates a test author entity (testAuthorEntityA).
    It saves the author using authorService.save(testAuthorEntityA).
    It then simulates a DELETE request to the endpoint /authors/{id}, where {id} is replaced with the saved author's ID.
    Similar to the previous test, it asserts that the expected status code is 204 (No Content). This is because the
    endpoint doesn't return any content after successfully deleting the author.
    This test case verifies if the controller can successfully delete an existing author and return
    the appropriate status code.
     */
    @Test
    public void testThatDeleteAuthorReturnsHttpStatus204ForExistingAuthor() throws Exception {
        AuthorEntity testAuthorEntityA = TestDataUtil.createTestAuthorEntityA();
        AuthorEntity savedAuthor = authorService.save(testAuthorEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());

        System.out.println("IL TEST E' ANDATO A BUON FINE");
        System.out.println("--------------------------------------------------------------------------------------------");

    }
}
