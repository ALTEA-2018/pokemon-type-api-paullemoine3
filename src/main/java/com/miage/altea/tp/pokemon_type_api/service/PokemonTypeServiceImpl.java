package com.miage.altea.tp.pokemon_type_api.service;

import com.miage.altea.tp.pokemon_type_api.bo.PokemonType;
import com.miage.altea.tp.pokemon_type_api.repository.PokemonTypeRepository;
import com.miage.altea.tp.pokemon_type_api.repository.TranslationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class PokemonTypeServiceImpl implements PokemonTypeService{

    @Autowired
    public PokemonTypeRepository pokemonTypeRepository;

    @Autowired
    public TranslationRepository translatRepository;


    public PokemonTypeServiceImpl() {
        super();
    }


    @Override
    public PokemonType getPokemonType(int id) {
        if(LocaleContextHolder.getLocale() != Locale.ENGLISH && LocaleContextHolder.getLocale() != Locale.FRENCH) {
            LocaleContextHolder.setLocale(Locale.ENGLISH);
        }
        PokemonType pk = pokemonTypeRepository.findPokemonTypeById(id);
        pk.setName(this.translatRepository.getPokemonName(id, LocaleContextHolder.getLocale()));
        return pk;
    }

    @Override
    public List<PokemonType> getAllPokemonTypes(){
        List<PokemonType> listPk = pokemonTypeRepository.findAllPokemonType();
        List<PokemonType> listPkTrans = new ArrayList<>();
        for(PokemonType pokemon : listPk){
            pokemon.setName(this.translatRepository.getPokemonName(pokemon.getId(), LocaleContextHolder.getLocale()));
            listPkTrans.add(pokemon);
        }
        return listPkTrans;
    }

    @Override
    public PokemonType getPokemonName(String name){
        return pokemonTypeRepository.findPokemonTypeByName(name);
    }

    @Override
    public void setPokemonTypeRepository(PokemonTypeRepository pkRepo){
        this.pokemonTypeRepository = pkRepo;
    }
    @Override
    public void setTranslationRepository(TranslationRepository transRepo){
        this.translatRepository = transRepo;
    }
}