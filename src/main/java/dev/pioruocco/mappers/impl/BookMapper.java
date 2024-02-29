package dev.pioruocco.mappers.impl;

import dev.pioruocco.domain.dto.BookDto;
import dev.pioruocco.domain.entity.BookEntity;
import dev.pioruocco.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/*
questa classe contiene la logica del mapper dall'oggetto di tipo Entità a l'oggetto DTO, quindi la loro
conversione nel tipo rispettivo

BookEntity -> BookDto
BookDto -> BookEntity
 */
@Component
public class BookMapper implements Mapper<BookEntity, BookDto> {

    private final ModelMapper modelMapper;

    public BookMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    //mappa da BookEntity a BookDto
    @Override
    public BookDto mapTo(BookEntity bookEntity) {
        return modelMapper.map(bookEntity, BookDto.class);
    }

    //mappa da BookDto a BookEntity
    @Override
    public BookEntity mapFrom(BookDto bookDto) {
        return modelMapper.map(bookDto, BookEntity.class);
    }
}
