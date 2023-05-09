package com.m2t.hachikodictionary.dto;

import com.m2t.hachikodictionary.model.Word;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class WordDtoConverter {

    public Word dtoWordConverter(WordDto from) {
        return new Word(from.getId(), from.getTitle(), from.getKind(), from.getDescriptions(),
                from.getSynonyms(), from.getAntonyms(), from.getSentences());
    }
    public WordDto wordDtoConverter(Word from) {
        return new WordDto(from.getId(), from.getTitle(), from.getKind(),
                from.getDescriptions(), from.getSynonyms(),
                from.getAntonyms(), from.getSentences());
    }
}
