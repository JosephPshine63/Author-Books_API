package dev.pioruocco.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
questa è una classe di configurazione per creare il Bean modelMapper()
 */
@Configuration
public class MapperConfig {

    /*VECCHIA IMPLEMENTAZIONE
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
     */

    //implementazione per il cascade
    /*
    la nuova implementazione ci permette di fare il mapping di due oggetti da fonte a destinazione
    ci permette che nel caso di oggetti annidati durante la creazione di un Entità con relazione OneToMany
    al passaggio dell'oggetto annidato gestiamo due casi:
        1. se l'autore dovesse essere nuovo, esso verrà creato nel database per il cascade settato con JPA
        2. se l'autore dovesse esistere e viene modificato un suo paremtro, corrispettivo autore e libri
            a lui relazionati verranno aggiornati
     */
    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper;
    }
}
