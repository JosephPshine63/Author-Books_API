package dev.pioruocco.mappers;

/*
in questa interfaccia è contenuta del'applicazione riguardante il mapping di oggetti
in modo che possa essere rutilizzata
 */
public interface Mapper<Classe1, Classe2>{


    //questo metodo mapperà la seconda classe alla prima
    Classe2 mapTo(Classe1 classe1);

    //questo metodo mapperà la prima classe alla seconda
    Classe1 mapFrom(Classe2 classe2);

}
