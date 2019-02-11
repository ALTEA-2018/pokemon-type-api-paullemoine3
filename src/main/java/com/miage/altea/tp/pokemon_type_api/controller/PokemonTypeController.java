package com.miage.altea.tp.pokemon_type_api.controller;

import com.miage.altea.tp.pokemon_type_api.bo.PokemonType;
import com.miage.altea.tp.pokemon_type_api.service.PokemonTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping(value="/pokemon-types")
public class PokemonTypeController {

    PokemonTypeService pokemonTypeService;

    @Autowired
    public PokemonTypeController(PokemonTypeService pokeTypeServ) {
        pokemonTypeService = pokeTypeServ;
    }

    @GetMapping("/{id}")
    public PokemonType getPokemonTypeFromId(@PathVariable  int id){
        return pokemonTypeService.getPokemonType(id);
    }
    @GetMapping("/")
    public List<PokemonType> getAllPokemonTypes() {
        return pokemonTypeService.getAllPokemonTypes();
    }

    @GetMapping(value ="/", params="name")
    public PokemonType getPokemonTypeFromName(String name){
        return pokemonTypeService.getPokemonName(name);
    }


}
