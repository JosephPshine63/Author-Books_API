package dev.pioruocco.mappers.impl;


import dev.pioruocco.domain.dto.AuthorDto;
import dev.pioruocco.domain.entity.AuthorEntity;
import dev.pioruocco.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/*
questa classe contiene la logica del mapper dall'oggetto di tipo Entità a l'oggetto DTO, quindi la loro
conversione nel tipo rispettivo

AuthoEntity -> AuthorDto
AuthoDto -> AuthorEntity
 */
@Component
public class AuthorMapperImpl implements Mapper<AuthorEntity, AuthorDto> {

    private final ModelMapper modelMapper;

    public AuthorMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    //mappa da AuthorEntity a AuthorDto
    @Override
    public AuthorDto mapTo(AuthorEntity authorEntity) {
        return modelMapper.map(authorEntity, AuthorDto.class);
    }

    //mappa da AutorDto a AuthorEntity
    @Override
    public AuthorEntity mapFrom(AuthorDto authorDto) {
        return modelMapper.map(authorDto, AuthorEntity.class);
    }
}
