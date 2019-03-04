package com.miage.altea.tp.pokemon_type_api.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miage.altea.tp.pokemon_type_api.bo.Translation;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;

@Repository
public class TranslationRepositoryImpl implements TranslationRepository {

    private Map<Locale, List<Translation>> translations;

    private List<Translation> defaultTranslations;

    public TranslationRepositoryImpl() {
        try {
            var objectMapper = new ObjectMapper();

            var frenchTranslationStream = new ClassPathResource("translations-fr.json").getInputStream();
            var frenchTranslationsArray = objectMapper.readValue(frenchTranslationStream, Translation[].class);

            var englishTranslationStream = new ClassPathResource("translations-en.json").getInputStream();
            var englishTranslationsArray = objectMapper.readValue(englishTranslationStream, Translation[].class);

            this.translations = Map.of(
                    Locale.FRANCE, List.of(frenchTranslationsArray),
                    Locale.UK, List.of(englishTranslationsArray)
            );

            this.defaultTranslations = List.of(englishTranslationsArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getPokemonName(int id, Locale locale) {
        Translation find = new Translation();
        List<Translation> listT = this.translations.get(locale) == null ? this.translations.get(this.translations.keySet().stream().filter(x -> x.getLanguage().contains(locale.getLanguage())).findFirst().get()): this.translations.get(locale);

        for(Translation t : listT){
            if(t.getId() == id){
                find = t;
            }
        }
        return find.getName();
     //   Optional<Translation> optTrans = this.translations.get(locale).stream().filter(trans -> trans.getId() == id).findFirst();
     //   return optTrans.isPresent() ? optTrans.get().getName()+"-"+locale.getLanguage() : null;
    }
}