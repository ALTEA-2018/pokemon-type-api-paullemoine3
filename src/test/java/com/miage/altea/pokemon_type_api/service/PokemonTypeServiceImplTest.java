package com.miage.altea.pokemon_type_api.service;

import com.miage.altea.tp.pokemon_type_api.bo.PokemonType;
import com.miage.altea.tp.pokemon_type_api.repository.PokemonTypeRepository;
import com.miage.altea.tp.pokemon_type_api.repository.PokemonTypeRepositoryImpl;
import com.miage.altea.tp.pokemon_type_api.repository.TranslationRepository;
import com.miage.altea.tp.pokemon_type_api.service.PokemonTypeService;
import com.miage.altea.tp.pokemon_type_api.service.PokemonTypeServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PokemonTypeServiceImplTest {

    @Test
    void applicationContext_shouldLoadPokemonTypeService(){
        var context = new AnnotationConfigApplicationContext(PokemonTypeServiceImpl.class, PokemonTypeRepositoryImpl.class);
        var serviceByName = context.getBean("pokemonTypeServiceImpl");
        var serviceByClass = context.getBean(PokemonTypeService.class);

        assertEquals(serviceByName, serviceByClass);
        assertNotNull(serviceByName);
        assertNotNull(serviceByClass);
    }

    @Test
    void pokemonTypeRepository_shouldBeAutowired_withSpring(){
        var context = new AnnotationConfigApplicationContext(PokemonTypeServiceImpl.class, PokemonTypeRepositoryImpl.class);
        var service = context.getBean(PokemonTypeServiceImpl.class);
        assertNotNull(service.pokemonTypeRepository);
    }

    @Test
    void pokemonTypeRepository_shouldBeCalled_whenFindById(){
        var pokemonTypeRepository = mock(PokemonTypeRepository.class);
        var pokemonTypeService = new PokemonTypeServiceImpl();
        var translationRepository = mock(TranslationRepository.class);
        var pokemonType = new PokemonType();
        pokemonType.setId(25);



        pokemonTypeService.setTranslationRepository(translationRepository);

        pokemonTypeService.setPokemonTypeRepository(pokemonTypeRepository);
        when(pokemonTypeRepository.findPokemonTypeById(Mockito.anyInt())).thenReturn(pokemonType);
        pokemonType.setName("Pikachu");
        //when(pokemonTypeService.getPokemonType(Mockito.anyInt())).thenReturn(pokemonType);

        verify(pokemonTypeRepository).findPokemonTypeById(25);
    }

    @Test
    void pokemonTypeRepository_shouldBeCalled_whenFindAll(){
        var pokemonTypeRepository = mock(PokemonTypeRepository.class);
        var pokemonTypeService = new PokemonTypeServiceImpl();
        pokemonTypeService.setPokemonTypeRepository(pokemonTypeRepository);

        pokemonTypeService.getAllPokemonTypes();

        verify(pokemonTypeRepository).findAllPokemonType();
    }

    @Test
    void pokemonNames_shouldBeTranslated_usingLocaleResolver(){
        var pokemonTypeService = new PokemonTypeServiceImpl();

        LocaleContextHolder.setLocale(Locale.FRENCH);

        var pokemonTypeRepository = mock(PokemonTypeRepository.class);
        pokemonTypeService.setPokemonTypeRepository(pokemonTypeRepository);
        when(pokemonTypeRepository.findPokemonTypeById(25)).thenReturn(new PokemonType());

        var translationRepository = mock(TranslationRepository.class);
        pokemonTypeService.setTranslationRepository(translationRepository);
        when(translationRepository.getPokemonName(Mockito.anyInt(), Mockito.any(Locale.class))).thenReturn("Pikachu-fr");

        var pikachu = pokemonTypeService.getPokemonType(25);

        assertEquals("Pikachu-fr", pikachu.getName());
        verify(translationRepository).getPokemonName(25, Locale.FRENCH);
    }

    @Test
    void allPokemonNames_shouldBeTranslated_usingLocaleResolver(){
        var pokemonTypeService = new PokemonTypeServiceImpl();

        var pokemonTypeRepository = mock(PokemonTypeRepository.class);
        pokemonTypeService.setPokemonTypeRepository(pokemonTypeRepository);

        var pikachu = new PokemonType();
        pikachu.setId(25);
        var raichu = new PokemonType();
        raichu.setId(26);
        when(pokemonTypeRepository.findAllPokemonType()).thenReturn(List.of(pikachu, raichu));

        var translationRepository = mock(TranslationRepository.class);
        pokemonTypeService.setTranslationRepository(translationRepository);
        when(translationRepository.getPokemonName(25, Locale.FRENCH)).thenReturn("Pikachu-fr");
        when(translationRepository.getPokemonName(26, Locale.FRENCH)).thenReturn("Raichu-fr");

        LocaleContextHolder.setLocale(Locale.FRENCH);

        var pokemonTypes = pokemonTypeService.getAllPokemonTypes();

        assertEquals("Pikachu-fr", pokemonTypes.get(0).getName());
        assertEquals("Raichu-fr", pokemonTypes.get(1).getName());
        verify(translationRepository).getPokemonName(25, Locale.FRENCH);
        verify(translationRepository).getPokemonName(26, Locale.FRENCH);
    }
}
